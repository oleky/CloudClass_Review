package com.whx.ott.beanfeature;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by HelloWorld on 2017/12/16.
 */

public class ParseMineBaseClassed {
    private int code;
    private String meg;
    private List<BaseClassBean> data;

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

    public List<BaseClassBean> getData() {
        return data;
    }

    public void setData(List<BaseClassBean> data) {
        this.data = data;
    }

    public static ParseMineBaseClassed parse(String json) {
        return new Gson().fromJson(json, ParseMineBaseClassed.class);
    }
}
