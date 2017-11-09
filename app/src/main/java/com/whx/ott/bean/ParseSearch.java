package com.whx.ott.bean;

import com.google.gson.Gson;
import com.whx.ott.beanfeature.SoulcoursesBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by oleky on 2016/9/5.
 * 解析基础课信息列表
 */
public class ParseSearch implements Serializable{
    private String code; //0为成功
    private List<CoursesBean>basecourses; //基础课视频列表
    private List<SoulcoursesBean>soulcourses;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CoursesBean> getBasecourses() {
        return basecourses;
    }

    public void setBasecourses(List<CoursesBean> basecourses) {
        this.basecourses = basecourses;
    }

    public List<SoulcoursesBean> getSoulcourses() {
        return soulcourses;
    }

    public void setSoulcourses(List<SoulcoursesBean> soulcourses) {
        this.soulcourses = soulcourses;
    }

    public ParseSearch(String code, List<CoursesBean> basecourses, List<SoulcoursesBean> soulcourses) {
        this.code = code;
        this.basecourses = basecourses;
        this.soulcourses = soulcourses;
    }

    public ParseSearch() {
    }

    public static ParseSearch getSearchData(String json) {
        return new Gson().fromJson(json, ParseSearch.class);
    }
}
