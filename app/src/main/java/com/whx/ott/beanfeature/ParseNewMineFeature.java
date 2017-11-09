package com.whx.ott.beanfeature;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HelloWorld on 2017/2/21.
 */

public class ParseNewMineFeature implements Serializable {
    private String code;//  "0",
    private String meg;//  "该用户的播放权限信息",
    private String rule;//  "1#2;2#4;3#4",
    private List<NewMineFeatureBean> data;
    private String playtimes;

    public String getPlaytimes() {
        return playtimes;
    }

    public void setPlaytimes(String playtimes) {
        this.playtimes = playtimes;
    }

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

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public List<NewMineFeatureBean> getData() {
        return data;
    }

    public void setData(List<NewMineFeatureBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ParseNewMineFeature{" +
                "code='" + code + '\'' +
                ", meg='" + meg + '\'' +
                ", rule='" + rule + '\'' +
                ", data=" + data +
                ", playtimes='" + playtimes + '\'' +
                '}';
    }

    public static ParseNewMineFeature parseNewMineFeature(String json) {
        return new Gson().fromJson(json, ParseNewMineFeature.class);
    }
}
