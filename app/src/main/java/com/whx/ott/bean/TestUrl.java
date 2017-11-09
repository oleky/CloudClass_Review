package com.whx.ott.bean;

import com.google.gson.Gson;

/**
 * Created by oleky on 2016/9/7.
 */
public class TestUrl {
    private String code;
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TestUrl(String code, String url) {
        this.code = code;
        this.url = url;
    }

    public TestUrl() {
    }

    public static String getVideoPath(String json) {
        return new Gson().fromJson(json,TestUrl.class).getUrl();
    }

    public static TestUrl getdata(String json) {
        return new Gson().fromJson(json, TestUrl.class);
    }
}
