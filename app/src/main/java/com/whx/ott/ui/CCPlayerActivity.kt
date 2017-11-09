package com.whx.ott.ui

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.pili.pldroid.player.AVOptions
import com.pili.pldroid.player.PLMediaPlayer
import com.pili.pldroid.player.widget.PLVideoTextureView
import com.whx.ott.R
import com.whx.ott.bean.CoursesBean
import com.whx.ott.beanfeature.SoulcoursesBean
import com.whx.ott.extentions.DelegatesExt
import com.whx.ott.extentions.dp2sp
import com.whx.ott.extentions.generateTime
import com.whx.ott.extentions.showToast
import com.whx.ott.media.MediaController
import com.whx.ott.presenter.LivePresenter
import com.whx.ott.presenter.viewinface.LiveView
import java.util.*

/**
 * Created by oleky on 2017/11/2.
 */
class CCPlayerActivity : Activity(),GestureDetector.OnGestureListener,
        View.OnTouchListener,LiveView {

    private lateinit var mVideoView: PLVideoTextureView
    private lateinit var gesture_volume: RelativeLayout
    private lateinit var gesture_progress: RelativeLayout
    private lateinit var iv_gesture_volume: ImageView
    private lateinit var iv_gesture_progress: ImageView
    private lateinit var tv_gesture_progress_time: TextView
    private lateinit var tv_gesture_volume_percentage: TextView
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
    private var curr_pos:Long = 0

    private var maxVolume: Int = 0
    private var currentVolume: Int = 0
    private var playingTime: Long = 0L
    private var videoTotalTime: Long = 0L
    private var videoPath: String? = ""
    private var videoTitle: String? = ""
    private var coursesBean: CoursesBean? = null
    private var soulcoursesBean: SoulcoursesBean? = null
    private var model_id: Int? = 1
    private var type: String? = ""



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
        coursesBean = intent.getSerializableExtra("courseBean") as CoursesBean
        soulcoursesBean = intent.getSerializableExtra("soulcoursesBean") as SoulcoursesBean
        model_id = intent.getIntExtra("model_id", 1)
        type = intent.getStringExtra("type") as String

        val codec = intent.getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE)
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
            with(mVideoView) {
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

        videoTotalTime = mVideoView.duration
        mGestureDetector = GestureDetector(this, this)
        mGestureDetector?.setIsLongpressEnabled(true)
        audiomanager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        maxVolume = audiomanager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        currentVolume = audiomanager!!.getStreamVolume(AudioManager.STREAM_MUSIC)

        val mediaController = MediaController(this, !isLiveStreaming, isLiveStreaming)
        mediaController.setOnClickSpeedAdjustListener(mOnClickSpeedAdjustListener)
        mediaController.setBackListener { this.finish() }
        mediaController.setControllerTitle(videoTitle)
        mVideoView.setMediaController(mediaController)
        mTimer = Timer()
        mVideoTimerTask = VideoTimerTask()
        mTimer?.schedule(mVideoTimerTask, 1000, 1000)

        when (model_id) {
            1 -> {
                if ("town" == type) {
                    mLivePresenter?.addTownPlayInfo(coursesBean!!)
                } else {
                    mLivePresenter?.addHighBasePlayInfo(coursesBean!!)
                }
            }
            2 ->{
                mLivePresenter?.addHighSoulPlayInfo(soulcoursesBean!!)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mVideoView.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoView.stopPlayback()
        mVideoTimerTask?.cancel()
        mVideoTimerTask = null
    }


    private fun initView() {
        mVideoView = findViewById(R.id.VideoView)
        val loadingView: View = findViewById(R.id.LoadingView)
        loadingView.visibility = View.VISIBLE
        mVideoView.setBufferingIndicator(loadingView)
        val mCoverView: View = findViewById(R.id.CoverView)
        mVideoView.setCoverView(mCoverView)
        gesture_progress = findViewById(R.id.gesture_progress_layout)
        gesture_volume = findViewById(R.id.gesture_volume_layout)
        iv_gesture_progress = findViewById(R.id.gesture_iv_progress)
        iv_gesture_volume = findViewById(R.id.gesture_iv_player_volume)
        tv_gesture_progress_time = findViewById(R.id.gesture_tv_progress_time)
        tv_gesture_volume_percentage = findViewById(R.id.gesture_tv_volume_percentage)
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
        Log.i(TAG, "OnInfo, what = $what, extra = $extra")
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
            PLMediaPlayer.MEDIA_INFO_METADATA -> Log.i(TAG, mVideoView.metadata.toString())
            PLMediaPlayer.MEDIA_INFO_VIDEO_BITRATE, PLMediaPlayer.MEDIA_INFO_VIDEO_FPS -> {
            }
            PLMediaPlayer.MEDIA_INFO_CONNECTED -> {
                Log.e(TAG, "ReConnected !")
                mVideoView.seekTo(curr_pos)
            }
            else -> {
            }
        }//                    updateStatInfo();
        true
    }

    private val mOnErrorListener:PLMediaPlayer.OnErrorListener =
            PLMediaPlayer.OnErrorListener { _, errorCode ->
        Log.e(TAG, "Error happened, errorCode = $errorCode")
        when (errorCode) {
            PLMediaPlayer.ERROR_CODE_IO_ERROR -> {
                //SDK will do reconnecting automatically
                Log.e(TAG, "IO Error")
                false
            }
            PLMediaPlayer.ERROR_CODE_OPEN_FAILED ->{
                showToast("无法打开当前视频，请检查网络重试")
                finish()
            }
            PLMediaPlayer.ERROR_CODE_SEEK_FAILED ->{
                Log.e(TAG, "failed to seek")
            }
            else -> { }
        }
        true
    }

    private val mOnCompletionListener:PLMediaPlayer.OnCompletionListener =
            PLMediaPlayer.OnCompletionListener {
        showToast("播放完成")
        finish()
    }

    private var payed = false
    private val mOnBufferingUpdateListener:PLMediaPlayer.OnBufferingUpdateListener =
            PLMediaPlayer.OnBufferingUpdateListener{
        _, percent ->
        if (percent >= 40 && !payed) {
            payed = true
            when (type) {
                "town" ->mLivePresenter?.townPay(coursesBean!!)
                else ->{
                    if (model_id == 1) {
                        mLivePresenter?.highBasePay(coursesBean!!)
                    } else {
                        mLivePresenter?.highSoulPay(soulcoursesBean!!)
                    }
                }
            }
        }
    }

    private val mOnClickSpeedAdjustListener:MediaController.OnClickSpeedAdjustListener =
            object : MediaController.OnClickSpeedAdjustListener {
        override fun onClickNormal() {
            mVideoView.setPlaySpeed(0X00010001)
        }

        override fun onClickFaster() {
            mVideoView.setPlaySpeed(0X00020001)
        }

        override fun onClickSlower() {
            mVideoView.setPlaySpeed(0X00010002)
        }
    }

   var mHandler:Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                200 -> curr_pos = msg.obj as Long
                300 -> mVideoView.seekTo(curr_pos)
            }
        }
    }

    /**
     * 计时器
     */
    private inner class VideoTimerTask : TimerTask() {
        override fun run() {
                val currentPos = mVideoView.currentPosition
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
        playingTime = mVideoView.currentPosition
        videoTotalTime = mVideoView.duration
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        if (firstScroll) {
            if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                GESTURE_FLAG = GESTURE_MODIFY_PROGRESS
                gesture_progress.visibility = View.VISIBLE
                gesture_volume.visibility = View.GONE
            } else {
                GESTURE_FLAG = GESTURE_MODIFY_VOLUME
                gesture_progress.visibility = View.GONE
                gesture_volume.visibility = View.VISIBLE
            }
        }

        if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
            if (distanceX >= dp2sp(STEP_PROGRESS)) { //快退
                iv_gesture_progress.setImageResource(R.drawable.souhu_player_backward)
                if (playingTime > 3000) {
                    playingTime -= 3000
                } else {
                    playingTime = 0
                }
            } else if (distanceX <= -dp2sp(STEP_PROGRESS)) {
                iv_gesture_progress.setImageResource(R.drawable.souhu_player_forward)
                if (playingTime < videoTotalTime - 16000) {
                    playingTime += 3000
                } else {
                    playingTime = videoTotalTime - 10000
                }
            }
            if (playingTime < 0) {
                playingTime = 0
            }
            mVideoView.seekTo(playingTime)
            tv_gesture_progress_time.text = generateTime(playingTime) + "/" + generateTime(videoTotalTime)
        } else {
            currentVolume = audiomanager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                if (distanceY > dp2sp(STEP_VOLUME)) { //调大音量
                    if (currentVolume < maxVolume) {
                        currentVolume++
                    }
                    iv_gesture_volume.setImageResource(R.drawable.souhu_player_volume)
                }else if (distanceY <= -dp2sp(STEP_VOLUME)) {
                    if (currentVolume > 0) {
                        currentVolume--
                        if (currentVolume == 0) {
                            iv_gesture_volume.setImageResource(R.drawable.souhu_player_silence)
                        }
                    }
                }
                var percentage = (currentVolume * 100) / maxVolume
                tv_gesture_volume_percentage.text = "$percentage %"
                audiomanager?.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
            }
        }
        firstScroll = false
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        if (motionEvent?.action == MotionEvent.ACTION_UP) {
            GESTURE_FLAG = 0
            gesture_volume.visibility = View.GONE
            gesture_progress.visibility = View.GONE
        }
        return mGestureDetector!!.onTouchEvent(motionEvent)
    }
}