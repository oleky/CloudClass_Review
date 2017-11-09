package com.whx.ott.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Oleky on 2016/9/9.
 */
public class ParseAddSoulInfo implements Serializable{
    private String code;
    private String meg;
    private List<SoulInfo>data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }

    public List<SoulInfo> getData() {
        return data;
    }

    public void setData(List<SoulInfo> data) {
        this.data = data;
    }

    public ParseAddSoulInfo(String code, String meg, List<SoulInfo> data) {
        this.code = code;
        this.meg = meg;
        this.data = data;
    }

    public ParseAddSoulInfo() {
    }

    @Override
    public String toString() {
        return "ParseAddSoulInfo{" +
                "code='" + code + '\'' +
                ", meg='" + meg + '\'' +
                ", data=" + data +
                '}';
    }

    public static ParseAddSoulInfo getSoulInfo(String json) {
        return new Gson().fromJson(json, ParseAddSoulInfo.class);
    }
}
