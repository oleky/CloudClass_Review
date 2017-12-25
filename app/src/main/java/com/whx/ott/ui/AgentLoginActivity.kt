package com.whx.ott.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.whx.ott.R
import com.whx.ott.conn.Conn
import com.whx.ott.conn.RetrofitClient
import com.whx.ott.extentions.DelegatesExt
import com.whx.ott.extentions.showToast
import com.whx.ott.presenter.AgentPresenter
import com.whx.ott.presenter.viewinface.AgentView
import com.whx.ott.util.Base64
import com.whx.ott.util.Const
import com.whx.ott.util.FindIPUtils
import com.whx.ott.util.UpdateAppManager
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_agentlogin.*
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by oleky on 2017/12/6.
 */
class AgentLoginActivity : Activity(),View.OnClickListener,AgentView {
    private val QR_WIDTH = 200
    private val QR_HEIGHT = 200
    private var all: View? = null
    private var vto: ViewTreeObserver? = null
    private var mLoginPresenter: AgentPresenter? = null
    private var myIP: String? = ""
    private var result: String = ""
    private var agentID: String = ""
    private var agentName: String = ""
    private var devId: String = ""
    //测试用
    private val internalurl = Conn.BASEURL+Conn.SCANFLAG

    private val mHandler = @SuppressLint("HandlerLeak")
    object: Handler(){
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                1 -> {
                    createQrImage(result, iv_qrcode)
                    internalApi(internalurl)
                }
                2 ->{
                    tv_result.text = "验证完成，您可以登录学生账号了！"
                    showToast("验证成功，您可以登录学生账号了")
                    rl_stulogin.visibility = View.VISIBLE
                    ln_agentlogin.visibility = View.GONE

                }
                3 ->{
                    tv_result.visibility = View.VISIBLE
                    btn_fresh.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agentlogin)
        mLoginPresenter = AgentPresenter(this,this)
        initView()
        getIP()
    }

    private fun initView() {
        all = findViewById(R.id.rl_top)
        vto = all?.viewTreeObserver as ViewTreeObserver
        login_btn.setOnClickListener(this)
        btn_fresh.setOnClickListener(this)

        vto?.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            newFocus?.bringToFront() //防止被压下面
            val scale = 1.05f
            id_mainUpView_login.setFocusView(newFocus, all, scale)
            all = newFocus
        }

        UpdateAppManager(this).getUpdateMsg()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_btn ->{
                val str_user = username_et.text.toString()
                val str_password = userpassword_et.text.toString()
                when {
                    TextUtils.isEmpty(str_user) -> username_et.error = "用户名不能为空"
                    TextUtils.isEmpty(str_password) -> userpassword_et.error = "密码不能为空"
                    else -> mLoginPresenter?.stuLogin(agentID,str_user, str_password)
                }
            }

            R.id.btn_fresh ->{
                btn_fresh.visibility = View.GONE
                mHandler.sendEmptyMessageDelayed(1, 200)
            }
        }
    }

    override fun agentLoginSucc(agent: String,agentname:String,macAddress:String, lastip: String,lastAddress:String) {
        agentID = agent
        agentName = agentname
        devId = macAddress
        if (TextUtils.isEmpty(lastip) || lastip != myIP||TextUtils.isEmpty(lastAddress)) {
            //获取到ip为空或与当前不相同，进行二维码安全校验
            rl_stulogin.visibility = View.GONE
            ln_agentlogin.visibility = View.VISIBLE
            ln_safevarify.visibility = View.VISIBLE
            iv_qrcode.visibility = View.VISIBLE
            tv_result.text = "检测到您是首次使用新系统登录\n请在2分钟内使用手机app，在掌上云教室app中扫描上方二维码进行身份验证"
            if (!TextUtils.isEmpty(lastip) && lastip != myIP) {
                tv_result.text = "检测到您的网络发生了变化\n请在2分钟内使用手机app，在掌上云教室app中扫描上方二维码进行安全验证"
                result = "$agentname&$agentID&$devId&$myIP&falsel"
            } else {
                result = "$agentname&$agentID&$devId&$myIP&true"
            }
            Log.e("myIP",myIP)

            val encodestr = "whx"+Base64.encode(result.toByteArray(charset("utf-8")))
            Log.e("Base64", encodestr)

            result = encodestr

            //TODO 对二维码信息:agentID,uuid
            if (ln_agentlogin.visibility == View.VISIBLE) {
                mHandler.sendEmptyMessageDelayed(1, 200)
            }


        } else {
            rl_stulogin.visibility = View.VISIBLE
            ln_agentlogin.visibility = View.GONE
            ln_safevarify.visibility = View.GONE
        }

    }

    override fun agentLoginFailed(error: String?) {
        rl_stulogin.visibility = View.GONE
        ln_agentlogin.visibility = View.VISIBLE
        iv_qrcode.visibility = View.GONE
        ln_safevarify.visibility = View.INVISIBLE
        tv_result.text = error
    }

    override fun stuLoginSucc() {
        showToast("登录成功")
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun stuLoginFailed(error: String) {
        showToast(error)
        tv_result.text = error
    }



    /**
     * 创建QrCode
     * */
    private fun createQrImage(url: String?, pic: ImageView) {
        try {
            if (TextUtils.isEmpty(url)) {
                return
            }
            val hints: Hashtable<EncodeHintType, String> = Hashtable()
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8")
            //图像数据转换，使用矩阵转换
            val bitMatrix: BitMatrix = QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints)
            val pixels = IntArray(QR_WIDTH * QR_HEIGHT)
            //按照二维码算法，逐个生成二维码图片
            for (y in 0 until QR_HEIGHT) {
                for (x in 0 until QR_WIDTH) {
                    if (bitMatrix[x, y]) {
                        pixels[y * QR_WIDTH + x] = 0xff000000.toInt()
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff.toInt()
                    }
                }
            }
            //生成二维码图片，ARGB——8888
            val bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT)
            pic.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }


    private var mDisposable: Disposable? = null


    private fun internalApi(interurl:String) {
        mDisposable =  Observable.interval(1, TimeUnit.SECONDS)
                .filter {
                    aLong ->
                    Log.e("interval","$aLong")
                    if (aLong < 60) {
                        true
                    } else {
                        mDisposable?.dispose()
                        mHandler.sendEmptyMessageDelayed(3, 300)
                        false
                    }
                }.flatMap(Function<Long, ObservableSource<Int>> {
            Observable.create { emmiter ->
                OkHttpUtils.post()
                        .url(interurl)
                        .addParams("user_id", agentID)
                        .addParams("dev_mac",devId)
                        .build()
                        .execute(object : StringCallback() {
                            override fun onError(call: Call, e: Exception, id: Int) {
                                e.printStackTrace()
                                emmiter.onError(e)
                            }

                            override fun onResponse(response: String, id: Int) {
                                try {
                                    val jsonObject = JSONObject(response)
                                    val code = jsonObject.optInt("code")
                                    val data = jsonObject.getJSONArray("data")
                                    if (data.length() > 0) {
                                        val obj:JSONObject = data.get(0) as JSONObject
                                        val flag = obj.optInt("flag")

                                        if (flag == 1) {
                                            emmiter.onNext(code)
                                        }
                                    }

                                } catch (e1: JSONException) {
                                    e1.printStackTrace()
                                    emmiter.onError(e1)
                                }

                            }
                        })
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    interger ->
                    Log.e("Integer","$interger")
                    mDisposable?.dispose()
                    mHandler.sendEmptyMessageDelayed(2, 300)
                },{
                    e ->e.printStackTrace()
                    showToast("网络状况差")
                })

    }


    private fun getIP() {
        FindIPUtils().taobaoIP {
            ip ->
            if (!TextUtils.isEmpty(ip)) {
                myIP = ip.substring(16, ip.length - 3)
            }
            mLoginPresenter?.varifyAgent()
        }
    }

    override fun onPause() {
        super.onPause()
        mDisposable?.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != mDisposable) {
            mDisposable?.dispose()
            mDisposable = null
        }
        mHandler.removeCallbacksAndMessages(1)
        mHandler.removeCallbacksAndMessages(2)
        mHandler.removeCallbacksAndMessages(3)
    }
}