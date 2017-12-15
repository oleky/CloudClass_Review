package com.whx.ott.util;


import android.content.Context;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by oleky on 2017/5/31.
 */

public class FindIPUtils {

    private IPListener mIPListener;

    public void taobaoIP(IPListener listener) {
        this.mIPListener = listener;
        OkHttpUtils.get()
                .url("https://www.taobao.com/help/getip.php")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mIPListener.findIP(response);
                    }
                });
    }

    public interface IPListener {
        void findIP(String ip);
    }

}
