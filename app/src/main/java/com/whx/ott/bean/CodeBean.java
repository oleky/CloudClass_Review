package com.whx.ott.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Oleky on 2016/9/9.
 */
public class CodeBean implements Serializable{
    private String code;
    private String meg;

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

    public CodeBean(String code, String meg) {
        this.code = code;
        this.meg = meg;
    }

    public CodeBean() {
    }

    public static CodeBean getCode(String json) {
        return new Gson().fromJson(json, CodeBean.class);
    }
}
