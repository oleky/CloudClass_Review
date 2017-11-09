package com.whx.ott.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by oleky on 2016/9/16.
 */
public class ParseVersion {
    private int code;
    private String meg;
    private List<VersionBean>data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }

    public List<VersionBean> getData() {
        return data;
    }

    public void setData(List<VersionBean> data) {
        this.data = data;
    }

    public ParseVersion(int code, String meg, List<VersionBean> data) {
        this.code = code;
        this.meg = meg;
        this.data = data;
    }

    public ParseVersion() {
    }

    public static ParseVersion getVersion(String json) {
        return new Gson().fromJson(json, ParseVersion.class);
    }
}
