package com.whx.ott.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.whx.ott.R;
import com.whx.ott.ui.AgentLoginActivity;
import com.whx.ott.ui.HomeActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Created by oleky on 2016/8/17.
 */
public class YunApplication extends Application {
    private static YunApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(client);

        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        Beta.autoInit = true;

        Beta.largeIconId = R.mipmap.ic_launcher;
        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        Beta.smallIconId = R.mipmap.ic_launcher;

        Beta.canShowUpgradeActs.add(AgentLoginActivity.class);
        Beta.canShowUpgradeActs.add(HomeActivity.class);

        //      when test app,turn the third paramater into true
        Bugly.init(this, "7ac2329e73", false);


    }

    public static YunApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }
}
