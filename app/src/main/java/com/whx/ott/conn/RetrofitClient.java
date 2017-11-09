package com.whx.ott.conn;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by oleky on 2017/7/8.
 */

public class RetrofitClient {
    protected Retrofit getRetrofit() {

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .connectTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new LoggerInterceptor("HttpLog"))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Conn.BASEURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public ApiService createApiClient() {
        return getRetrofit().create(ApiService.class);
    }
}
