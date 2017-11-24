package com.whx.ott.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

import com.whx.ott.R
import com.whx.ott.extentions.showToast
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_qrlogin.*
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by oleky on 2017/11/20.
 */

class QrCodeLoginActivity : Activity() {

    private val QR_WIDTH = 200
    private val QR_HEIGHT = 200
    private val apiurl = "http://api.121drhero.com/public/index.php/interfaces/two/scanlogin/addteacheruuid"
    private val internalurl = "http://api.121drhero.com/public/index.php/interfaces/two/scanlogin/scanteachermessage"
    private var result: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrlogin)
        getuuid()
    }

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

    private val mHandler = @SuppressLint("HandlerLeak")
    object:Handler(){
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                1 -> {
                    result = msg.obj as String
                    createQrImage(result, iv_qrcode)
                    internalApi(result)
                }
            }
        }
    }
    //获取uuid
    private fun getuuid() {
      OkHttpUtils.get()
                .url(apiurl)
                .build()
                .execute(object : StringCallback() {
                    override fun onResponse(response: String, id: Int) {
                        Log.e("uuid", response)
                        val jsonobject = JSONObject(response)
                        val code = jsonobject.optString("code")
                        if ("200" == code) {
                            val uuid = jsonobject.optString("uuid")
                            val message = Message.obtain()
                            message.obj = uuid
                            message.what = 1
                            mHandler.sendMessage(message)
                        }
                    }

                    override fun onError(call: Call?, e: Exception, id: Int) {
                        e.printStackTrace()
                    }
                })
    }

    private var mDisposable: Disposable? = null

    private fun internalApi(uuid: String) {
     mDisposable =  Observable.interval(1, TimeUnit.SECONDS)
                .filter {
                    aLong ->
                    Log.e("interval","$aLong")
                    if (aLong < 60) {
                        true
                    } else {
                        mDisposable?.dispose()
                        false
                    }
                }.flatMap(Function<Long, ObservableSource<Int>> {
            Observable.create { emmiter ->
                OkHttpUtils.post()
                        .url(internalurl)
                        .addParams("uuid", uuid)
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
                                    if (code == 200) {
                                        emmiter.onNext(code)
                                        Log.e("qrcode", response)
                                        mDisposable?.dispose()
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
                .subscribe { integer ->
                    tv_content.text = "扫码完成，您已经成功登录！"
                    showToast("登录成功")
                 }
    }

    override fun onPause() {
        super.onPause()
        mDisposable?.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != mDisposable) {
            mDisposable = null
        }
    }
}
