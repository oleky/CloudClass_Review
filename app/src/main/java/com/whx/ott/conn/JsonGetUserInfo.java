package com.whx.ott.conn;

import com.whx.ott.bean.ParseLogin;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by oleky on 2017/2/10.
 * 获取用户位置信息以及当前剩余时长
 */

public class JsonGetUserInfo {

    public static void getUserAddress(String user_id, final LocListener listener) {

        OkHttpUtils.post()
                .url(Conn.BASEURL + Conn.USER_POS)
                .addParams("user_id", user_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ParseLogin parse = ParseLogin.getUserInfo(response);
                        String code = parse.getCode();
                        if ("0".equals(code) && code != null) {
                            listener.getLoc(parse.getData().get(0).getAddress_name());
                            listener.getMoney(parse.getData().get(0).getUser_money());
                            listener.getTownMoney(parse.getData().get(0).getTown_money());
                            listener.getTownTime(parse.getData().get(0).getTown_time());
                        }
                    }
                });
    }

    public interface LocListener {
        void getLoc(String pos);

        void getMoney(String money);

        void getTownMoney(String money);

        void getTownTime(String time);
    }
}
