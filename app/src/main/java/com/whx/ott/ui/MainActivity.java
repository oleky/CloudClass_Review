package com.whx.ott.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.whx.ott.R;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.util.TimeUtils;

/**
 * Created by oleky on 2016/9/16.
 */
public class MainActivity extends Activity {

    boolean hasLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//从共享参数获取数据
//        getMsg();

        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                getMsg();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 1200);
    }


    // 读取数据
    public void getMsg() {

        hasLogin = (boolean) SharedpreferenceUtil.getData(MainActivity.this, "hasLogin", false);

        long currentTime = TimeUtils.getNowTimeMills();
        String last = (String) SharedpreferenceUtil.getData(MainActivity.this, "lasttime", "0");
        long lastTime = Long.parseLong(last);

//

        long duration = currentTime-lastTime;  //7天=604800000毫秒
        if (duration <= 604800000||lastTime == 0) {
            //使用间隔小于等于7天或新更新用户
            if (hasLogin) {
                Intent intent = new Intent(MainActivity.this, NewHomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.this.finish();
    }


}