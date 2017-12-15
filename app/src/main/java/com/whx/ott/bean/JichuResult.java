package com.whx.ott.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by oleky on 2016/9/5.
 * 解析基础课信息列表
 */
public class JichuResult implements Serializable{
    private String code; //0为成功
    private String meg;
    private List<CoursesBean>courses; //视频列表

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

    public List<CoursesBean> getCourses() {
        return courses;
    }

    public void setCourses(List<CoursesBean> courses) {
        this.courses = courses;
    }

    public JichuResult() {
    }

    public JichuResult(String code, List<CoursesBean> courses) {
        this.code = code;
        this.courses = courses;
    }
}
