package com.whx.ott.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.bean.CoursesBean;
import com.whx.ott.bean.ParseLogin;
import com.whx.ott.conn.Conn;
import com.whx.ott.conn.JsonAddTownInfo;
import com.whx.ott.conn.TownNumToString;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.util.X5WebView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by oleky on 2017/9/27.
 */

public class WkWebActivity extends Activity {

    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;
    private static final int MAX_LENGTH = 14;
    private boolean mNeedTestPage = false;
    private ValueCallback<Uri> uploadFile;
    private int model_id;

    private CoursesBean coursesBean;  //基础课类
    private String macAdress;
    private String user_id;
    private String user_name;
    ParseLogin data;

    private boolean hasPayed;
    private String videoPath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }

        setContentView(R.layout.activity_wkweb);

        mViewParent = (ViewGroup) findViewById(R.id.webView1);
        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
        coursesBean = (CoursesBean) getIntent().getSerializableExtra("courseBean");
        model_id = getIntent().getIntExtra("model_id", 1);
        videoPath = getIntent().getStringExtra("videoPath");
        hasPayed = false;

        user_id = (String) SharedpreferenceUtil.getData(this, "user_id", "");
        user_name = (String) SharedpreferenceUtil.getData(this, "user_name", "");
        macAdress = (String) SharedpreferenceUtil.getData(this, "dev_id", "");

        new JsonAddTownInfo(this, coursesBean).addTownPlay();
    }



    private void init() {
        mWebView = new X5WebView(this, null);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
                }
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {


            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;
            CustomViewCallback callback;

            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
                View fileview = LayoutInflater.from(WkWebActivity.this).inflate(R.layout.filechooser_layout, null);
                FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
//                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                ViewGroup viewGroup = (ViewGroup) fileview;
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                if (arg2.contains("课程")) {
                    addCountryPay();
                }
                arg3.confirm();
                return true;
            }
        });


        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
         webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setNeedInitialFocus(false);
        webSetting.setSupportZoom(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);


        long time = System.currentTimeMillis();
        mWebView.loadUrl(videoPath);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();

    }

    @Override
    protected void onDestroy() {
        if (mTestHandler != null)
            mTestHandler.removeCallbacksAndMessages(null);
        if (mWebView != null){
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.removeAllViews();
            mWebView.setVisibility(View.GONE);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }



    public static final int MSG_OPEN_TEST_URL = 0;
    public static final int MSG_INIT_UI = 1;
    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_TEST_URL:
                    if (!mNeedTestPage) {
                        return;
                    }
                    String testUrl = "http://studycenter.vko.cn/thdGoods/play?videoId=";
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }

                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    init();
                    break;
                case 1001:
                    hasPayed = true;
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 乡镇基础课扣费
     */

    public void addCountryPay() {
        OkHttpUtils.get()
                .url(Conn.BASEURL + Conn.COUNTRY_PAY)
                .addParams("devid", macAdress)
                .addParams("user_id", user_id)
                .addParams("user_name", user_name)
                .addParams("model_id", coursesBean.getModel_id() + "")
                .addParams("model_name", "乡镇云教室基础课程")
                .addParams("course_id", coursesBean.getId() + "")
                .addParams("course_name", coursesBean.getCourse_name())
                .addParams("teacher_id", coursesBean.getTeacher_id() + "")
                .addParams("teacher_name", TownNumToString.searchTeacher(coursesBean.getTeacher_id()))
                .addParams("deduct_time", coursesBean.getCourse_length() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        data = ParseLogin.getUserInfo(response);
                        if (data.getCode() != null && data.getCode().equals("0")) {
                            double time = Double.parseDouble(data.getData().get(0).getTown_money());
                            if (time >= 1) {
                                Log.e("Time", "---剩余时长" + time);
                                SharedpreferenceUtil.saveData(WkWebActivity.this, "country_user_money", data.getData().get(0).getTown_money());
                                Toast.makeText(WkWebActivity.this, "扣费成功", Toast.LENGTH_SHORT).show();
                                mTestHandler.sendEmptyMessage(1001);
                            } else {
                                Toast.makeText(WkWebActivity.this, "试看时间已过，请及时充值", Toast.LENGTH_SHORT).show();
                                WkWebActivity.this.finish();
                            }
                        } else {
                            Toast.makeText(WkWebActivity.this, "试看时间已过，请及时充值", Toast.LENGTH_SHORT).show();
                            WkWebActivity.this.finish();
                        }
                    }
                });
    }
}