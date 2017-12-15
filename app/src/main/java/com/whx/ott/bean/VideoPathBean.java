package com.whx.ott.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by oleky on 2016/9/7.
 */
public class VideoPathBean implements Serializable{
    private String code;
    private String url;
    private String meg;

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }

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

    public VideoPathBean(String code, String url) {
        this.code = code;
        this.url = url;
    }

    public VideoPathBean() {
    }

    public static String getVideoPath(String json) {
        return new Gson().fromJson(json,VideoPathBean.class).getUrl();
    }

    public static VideoPathBean getdata(String json) {
        return new Gson().fromJson(json, VideoPathBean.class);
    }
}
