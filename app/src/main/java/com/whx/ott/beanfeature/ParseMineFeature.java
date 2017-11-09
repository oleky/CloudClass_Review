package com.whx.ott.beanfeature;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by HelloWorld on 2016/9/8.
 */
public class ParseMineFeature {
    private int code;
    private String meg;
    private List<MineFeatureBean> data;

    public ParseMineFeature(int code, String meg, List<MineFeatureBean> data) {
        this.code = code;
        this.meg = meg;
        this.data = data;
    }

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

    public List<MineFeatureBean> getData() {
        return data;
    }

    public void setData(List<MineFeatureBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ParseMineFeature{" +
                "code=" + code +
                ", meg='" + meg + '\'' +
                ", data=" + data +
                '}';
    }

    public static ParseMineFeature getParseData(String json) {
        return new Gson().fromJson(json, ParseMineFeature.class);
    }
}
