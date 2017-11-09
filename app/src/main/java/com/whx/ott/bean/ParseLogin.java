package com.whx.ott.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by oleky on 2016/9/8.
 */
public class ParseLogin implements Serializable{
    private String code;
    private String meg;
    private List<UserData>data;

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

    public List<UserData> getData() {
        return data;
    }

    public void setData(List<UserData> data) {
        this.data = data;
    }

    public ParseLogin(String code, String meg, List<UserData> data) {
        this.code = code;
        this.meg = meg;
        this.data = data;
    }

    public ParseLogin() {
    }

    public static ParseLogin getUserInfo(String json) {
        return new Gson().fromJson(json, ParseLogin.class);
    }
}
