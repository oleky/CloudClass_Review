package com.whx.ott.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by oleky on 2016/9/5.
 * 解析基础课信息列表
 */
public class ParseJichu implements Serializable{
    private String code; //0为成功
    private List<CoursesBean>courses; //视频列表

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CoursesBean> getCourses() {
        return courses;
    }

    public void setCourses(List<CoursesBean> courses) {
        this.courses = courses;
    }

    public ParseJichu() {
    }

    public ParseJichu(String code, List<CoursesBean> courses) {
        this.code = code;
        this.courses = courses;
    }

    public static ParseJichu getJichuData(String json) {
        return new Gson().fromJson(json, ParseJichu.class);
    }
}
