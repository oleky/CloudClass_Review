package com.whx.ott.bean;

import com.whx.ott.beanfeature.SoulcoursesBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by oleky on 2017/12/26.
 */

public class TownTeseResult implements Serializable{
    private String code; //0为成功
    private String meg;
    private List<SoulcoursesBean> xcsoulcourses; //视频列表

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

    public List<SoulcoursesBean> getXcsoulcourses() {
        return xcsoulcourses;
    }

    public void setXcsoulcourses(List<SoulcoursesBean> xcsoulcourses) {
        this.xcsoulcourses = xcsoulcourses;
    }
}
