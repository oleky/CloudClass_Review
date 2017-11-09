package com.whx.ott.conn;

import android.content.Context;
import android.util.Log;

import com.whx.ott.util.SharedpreferenceUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by oleky on 2017/2/10.
 * 当位置与用户默认位置不同，向服务器提交
 */

public class JsonPostPosition {
    Context mContext;
    String user_id;
    String user_name;
    String dev_mac;
    String location_name;

    public JsonPostPosition(Context context, String user_id, String user_name, String dev_mac, String location_name) {
        mContext = context;
        this.user_id = user_id;
        this.user_name = user_name;
        this.dev_mac = dev_mac;
        this.location_name = location_name;
    }

    public void commitPos() {
        OkHttpUtils.post()
                .url(Conn.BASEURL+Conn.POST_POSITION)
                .addParams("user_id",user_id)
                .addParams("user_name",user_name)
                .addParams("location_name",location_name)
                .addParams("dev_mac",dev_mac)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("commitPos", "CommitSuccess");
                        SharedpreferenceUtil.saveData(mContext, "localposition", location_name);
                    }
                });
    }
}
