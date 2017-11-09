package com.whx.ott.util;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by oleky on 2017/5/31.
 */

public class FindIPUtils {

    private IPListener mIPListener;

    public void catchIP(IPListener listener) {
        this.mIPListener = listener;
        OkHttpUtils.get()
                .url("http://www.3322.org/dyndns/getip")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            mIPListener.findIP(response);
                        }
                    }
                });
    }

    public interface IPListener {
        void findIP(String ip);
    }

}
