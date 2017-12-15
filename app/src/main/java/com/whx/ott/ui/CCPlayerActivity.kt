package com.whx.ott.ui

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.pili.pldroid.player.AVOptions
import com.pili.pldroid.player.PLMediaPlayer
import com.pili.pldroid.player.widget.PLVideoTextureView
import com.whx.ott.R
import com.whx.ott.bean.CoursesBean
import com.whx.ott.beanfeature.SoulcoursesBean
import com.whx.ott.extentions.dp2sp
import com.whx.ott.extentions.generateTime
import com.whx.ott.extentions.showToast
import com.whx.ott.media.MediaController
import com.whx.ott.presenter.LivePresenter
import com.whx.ott.presenter.viewinface.LiveView
import kotlinx.android.synthetic.main.activity_pl_video_view.*
import java.lang.Exception
import java.util.*

/**
 * Created by oleky on 2017/11/2.
 */
class CCPlayerActivity : AppCompatActivity(), GestureDetector.OnGestureListener,
        View.OnTouchListener, LiveView {

    companion object {
        const val TAG = "WhxPlay"
        const val STEP_VOLUME = 2f
        const val STEP_PROGRESS = 2f
        const val GESTURE_MODIFY_PROGRESS = 1
        const val GESTURE_MODIFY_VOLUME = 2
    }

    private var firstScroll = false //每次触摸屏幕之后，第一次归零
    private var GESTURE_FLAG = 0
    private var mDisplayAspectRatio: Int = PLVideoTextureView.ASPECT_RATIO_16_9
    private var curr_pos: Long = 0

    private var maxVolume: Int = 0
    private var currentVolume: Int = 0
    private var playingTime: Long = 0L
    private var videoTotalTime: Long = 0L
    private var videoPath: String? = ""
    private var videoTitle: String? = ""
    private var coursesBean: CoursesBean? = null
    private var soulcoursesBean: SoulcoursesBean? = null
    private var model_id: Int? = 1
    private var type_id: Int = 0
    private var type_name: String = "云教室"

    private var mGestureDetector: GestureDetector? = null
    private var audiomanager: AudioManager? = null
    private var mTimer: Timer? = null
    private var mVideoTimerTask: VideoTimerTask? = null
    private var mLivePresenter: LivePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pl_video_view)
        initView()

        videoPath = intent.getStringExtra("videoPath")
        coursesBean = intent.getSerializableExtra("courseBean") as? CoursesBean
        soulcoursesBean = intent.getSerializableExtra("soulcoursesBean") as? SoulcoursesBean
        model_id = intent.getIntExtra("model_id", 1)
        type_id = intent.getIntExtra("type_id", 1)
        type_name = intent.getStringExtra("type_name")

        val codec =  AVOptions.MEDIA_CODEC_SW_DECODE //软解
        val isLiveStreaming = false
        mLivePresenter = LivePresenter(this, this)
        videoTitle = if (model_id == 1) {
            coursesBean?.course_name
        } else {
            soulcoursesBean?.soulcourse_name
        }
        if (TextUtils.isEmpty(videoPath)) {
            finish()
        }
        val options = AVOptions()
        options.apply {
            setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000)
            setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 3000)
            setInteger(AVOptions.KEY_MEDIACODEC, codec)
        }.let {
            with(VideoView) {
                setAVOptions(it)
                setOnInfoListener(mOnInfoListener)
                setOnBufferingUpdateListener(mOnBufferingUpdateListener)
                setOnCompletionListener(mOnCompletionListener)
                setOnErrorListener(mOnErrorListener)
                setVideoPath(videoPath)
                setOnTouchListener(this@CCPlayerActivity)
                displayAspectRatio = mDisplayAspectRatio
                isLooping = false
                start()
            }
        }

//        headerGif()


        videoTotalTime = VideoView.duration
        mGestureDetector = GestureDetector(this, this)
        mGestureDetector?.setIsLongpressEnabled(true)
        audiomanager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        maxVolume = audiomanager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        currentVolume = audiomanager!!.getStreamVolume(AudioManager.STREAM_MUSIC)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val mediaController = MediaController(this, !isLiveStreaming, isLiveStreaming)
        mediaController.setSupportActionBar(actionbar)
        mediaController.setOnClickSpeedAdjustListener(mOnClickSpeedAdjustListener)

        VideoView.setMediaController(mediaController)
        mTimer = Timer()
        mVideoTimerTask = VideoTimerTask()
        mTimer?.schedule(mVideoTimerTask, 1000, 1000)

        if (TextUtils.isEmpty(videoTitle)) {
            actionbar?.title = "云教室"
        } else {
            actionbar?.title = videoTitle
        }

        when (model_id) {
            1 -> coursesBean?.let { mLivePresenter?.addJichuInfo(it, "$type_id", type_name) }

            2 -> {
                soulcoursesBean?.let { mLivePresenter?.addTesePlayInfo(it, "$type_id", type_name) }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        VideoView.stopPlayback()
        mVideoTimerTask?.cancel()
        mVideoTimerTask = null
        mHandler.removeMessages(200)
        mHandler.removeMessages(300)
    }


    private fun initView() {
        loadingView.visibility = View.VISIBLE
        VideoView.setBufferingIndicator(loadingView)
        val mCoverView: View = findViewById(R.id.CoverView)
        VideoView.setCoverView(mCoverView)
    }

    override fun paySucc() {
        showToast("扣费成功")
    }

    override fun payFailed(errorMsg: String?) {
        if (errorMsg != null) {
            showToast(errorMsg)
        }
        finish()
    }

    override fun getUrl(url: String?) {
    }

    private val mOnInfoListener = PLMediaPlayer.OnInfoListener { _, what, extra ->
        when (what) {
            PLMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
            }
            PLMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
            }
            PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> showToast("first video render time: $extra ms")
            PLMediaPlayer.MEDIA_INFO_VIDEO_FRAME_RENDERING -> Log.i(TAG, "video frame rendering, ts = $extra")
            PLMediaPlayer.MEDIA_INFO_AUDIO_FRAME_RENDERING -> Log.i(TAG, "audio frame rendering, ts = $extra")
            PLMediaPlayer.MEDIA_INFO_VIDEO_GOP_TIME -> Log.i(TAG, "Gop Time: $extra")
            PLMediaPlayer.MEDIA_INFO_SWITCHING_SW_DECODE -> Log.i(TAG, "Hardware decoding failure, switching software decoding!")
            PLMediaPlayer.MEDIA_INFO_METADATA -> Log.i(TAG, VideoView.metadata.toString())
            PLMediaPlayer.MEDIA_INFO_VIDEO_BITRATE, PLMediaPlayer.MEDIA_INFO_VIDEO_FPS -> {
            }
            PLMediaPlayer.MEDIA_INFO_CONNECTED -> {
                Log.e(TAG, "ReConnected !")
                VideoView.seekTo(curr_pos)
            }
            else -> {
            }
        }//                    updateStatInfo();
        true
    }

    private val mOnErrorListener: PLMediaPlayer.OnErrorListener =
            PLMediaPlayer.OnErrorListener { _, errorCode ->
                Log.e(TAG, "Error happened, errorCode = $errorCode")
                when (errorCode) {
                    PLMediaPlayer.ERROR_CODE_IO_ERROR -> {
                        //SDK will do reconnecting automatically
                        Log.e(TAG, "IO Error")
                        false
                    }
                    PLMediaPlayer.ERROR_CODE_OPEN_FAILED -> {
                        showToast("无法打开当前视频，请检查网络重试")
                        finish()
                    }
                    PLMediaPlayer.ERROR_CODE_SEEK_FAILED -> {
                        Log.e(TAG, "failed to seek")
                    }
                    else -> {
                    }
                }
                true
            }

    private val mOnCompletionListener: PLMediaPlayer.OnCompletionListener =
            PLMediaPlayer.OnCompletionListener {
                showToast("播放完成")
                finish()
            }

    private var payed = false
    private val mOnBufferingUpdateListener: PLMediaPlayer.OnBufferingUpdateListener =
            PLMediaPlayer.OnBufferingUpdateListener { _, percent ->
                if (percent >= 20 && !payed) {
                    payed = true
                    when (model_id) {
                        1 -> coursesBean?.let { mLivePresenter?.jichuPay(it, "$type_id", type_name) }
                        2 -> soulcoursesBean?.let { mLivePresenter?.tesePay(it, "$type_id", type_name) }
                    }
                }
            }

    private val mOnClickSpeedAdjustListener: MediaController.OnClickSpeedAdjustListener =
            object : MediaController.OnClickSpeedAdjustListener {
                override fun onClickNormal() {
                    VideoView.setPlaySpeed(0X00010001)
                }

                override fun onClickFaster() {
                    VideoView.setPlaySpeed(0X00020001)
                }

                override fun onClickSlower() {
                    VideoView.setPlaySpeed(0X00010002)
                }
            }

    var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                200 -> curr_pos = msg.obj as Long
                300 -> VideoView.seekTo(curr_pos)
                1 -> {
                    VideoView.start()
                    ad_image.visibility = View.GONE
                }
            }
        }
    }

    //加载gif广告
    private var duration = 0

//    private fun headerGif() {
//        Glide.with(this)
//                .load(R.mipmap.cat_anim)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .error(R.drawable.ic_bg_menu)
//                .dontAnimate()
//                .listener(object : RequestListener<Int, GlideDrawable> {
//                    override fun onException(e: Exception?, model: Int?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
//                        return false
//                    }
//
//                    override fun onResourceReady(resource: GlideDrawable?, model: Int?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
//                        //计算动画时常
//                        val drawable: GifDrawable = resource as GifDrawable
//                        val decoder = drawable.decoder
//                        for (i in 0 until drawable.frameCount) {
//                            duration += decoder.getDelay(i)
//                        }
//                        mHandler.sendEmptyMessageDelayed(1, duration.toLong()*5)
//                        return false
//                    }
//
//                })
//                .into(GlideDrawableImageViewTarget(ad_image, 5))
//
//    }

    /**
     * 计时器
     */
    private inner class VideoTimerTask : TimerTask() {
        override fun run() {
            val currentPos = VideoView.currentPosition
            val message = Message.obtain()
            message.obj = currentPos
            message.what = 200
            mHandler.sendMessage(message)
        }
    }


    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        firstScroll = true
        playingTime = VideoView.currentPosition
        videoTotalTime = VideoView.duration
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        if (firstScroll) {
            if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                GESTURE_FLAG = GESTURE_MODIFY_PROGRESS
                gesture_progress_layout.visibility = View.VISIBLE
                gesture_volume_layout.visibility = View.GONE
            } else {
                GESTURE_FLAG = GESTURE_MODIFY_VOLUME
                gesture_progress_layout.visibility = View.GONE
                gesture_volume_layout.visibility = View.VISIBLE
            }
        }

        if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
            if (distanceX >= dp2sp(STEP_PROGRESS)) { //快退
                gesture_iv_progress.setImageResource(R.drawable.souhu_player_backward)
                if (playingTime > 3000) {
                    playingTime -= 3000
                } else {
                    playingTime = 0
                }
            } else if (distanceX <= -dp2sp(STEP_PROGRESS)) {
                gesture_iv_progress.setImageResource(R.drawable.souhu_player_forward)
                if (playingTime < videoTotalTime - 16000) {
                    playingTime += 3000
                } else {
                    playingTime = videoTotalTime - 10000
                }
            }
            if (playingTime < 0) {
                playingTime = 0
            }
            VideoView.seekTo(playingTime)
            gesture_tv_progress_time.text = generateTime(playingTime) + "/" + generateTime(videoTotalTime)
        } else {
            currentVolume = audiomanager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                if (distanceY > dp2sp(STEP_VOLUME)) { //调大音量
                    if (currentVolume < maxVolume) {
                        currentVolume++
                    }
                    gesture_iv_player_volume.setImageResource(R.drawable.souhu_player_volume)
                } else if (distanceY <= -dp2sp(STEP_VOLUME)) {
                    if (currentVolume > 0) {
                        currentVolume--
                        if (currentVolume == 0) {
                            gesture_iv_player_volume.setImageResource(R.drawable.souhu_player_silence)
                        }
                    }
                }
                var percentage = (currentVolume * 100) / maxVolume
                gesture_tv_volume_percentage.text = "$percentage %"
                audiomanager?.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
            }
        }
        firstScroll = false
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        if (motionEvent?.action == MotionEvent.ACTION_UP) {
            GESTURE_FLAG = 0
            gesture_volume_layout.visibility = View.GONE
            gesture_progress_layout.visibility = View.GONE
        }
        return mGestureDetector!!.onTouchEvent(motionEvent)
    }
}