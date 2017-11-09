package com.whx.ott.beanfeature;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by oleky on 2016/9/18.
 */
public class ParseSearchSoul {
    private String code;
    private String meg;
    private List<SoulLastBean>data;

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

    public List<SoulLastBean> getData() {
        return data;
    }

    public void setData(List<SoulLastBean> data) {
        this.data = data;
    }

    public ParseSearchSoul(String code, String meg, List<SoulLastBean> data) {
        this.code = code;
        this.meg = meg;
        this.data = data;
    }

    public ParseSearchSoul() {
    }

    @Override
    public String toString() {
        return "ParseSearchSoul{" +
                "code='" + code + '\'' +
                ", meg='" + meg + '\'' +
                ", data=" + data +
                '}';
    }

    public static ParseSearchSoul getSoul(String json) {
        return new Gson().fromJson(json, ParseSearchSoul.class);
    }
}
