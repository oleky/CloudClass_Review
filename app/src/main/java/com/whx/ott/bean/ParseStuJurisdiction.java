package com.whx.ott.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HelloWorld on 2017/12/5.
 */

public class ParseStuJurisdiction implements Serializable{
    private int code;
    private String meg;
    private List<StuJurisdictionBean> data;

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

    public List<StuJurisdictionBean> getData() {
        return data;
    }

    public void setData(List<StuJurisdictionBean> data) {
        this.data = data;
    }

    public static ParseStuJurisdiction parseStuJurisdiction(String json) {
        return new Gson().fromJson(json, ParseStuJurisdiction.class);
    }
}
