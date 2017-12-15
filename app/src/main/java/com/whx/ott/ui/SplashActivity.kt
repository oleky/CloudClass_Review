package com.whx.ott.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

import com.whx.ott.R
import com.whx.ott.util.SharedpreferenceUtil

/**
 * Created by oleky on 2016/9/16.
 */
class SplashActivity : Activity() {

    private var hasLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        Handler(Handler.Callback {
            getMsg()
            false
        }).sendEmptyMessageDelayed(0, 1200)
    }


    // 读取数据
    fun getMsg() {

        hasLogin = SharedpreferenceUtil.getData(this@SplashActivity, "hasLogin", false) as Boolean
            val intent = Intent(this@SplashActivity, AgentLoginActivity::class.java)
            startActivity(intent)
            finish()
    }

    override fun onPause() {
        super.onPause()
        this@SplashActivity.finish()
    }


}