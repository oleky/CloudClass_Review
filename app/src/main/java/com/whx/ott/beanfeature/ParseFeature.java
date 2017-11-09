package com.whx.ott.beanfeature;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HelloWorld on 2016/9/6.
 */
public class ParseFeature implements Serializable{
    private int code;
    private String meg;
    private List<SoulcoursesBean>soulcourses;

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

    public List<SoulcoursesBean> getSoulcourses() {
        return soulcourses;
    }

    public void setSoulcourses(List<SoulcoursesBean> soulcourses) {
        this.soulcourses = soulcourses;
    }
    public static ParseFeature getFeatureData(String json){
        return new Gson().fromJson(json,ParseFeature.class);
    }
}
