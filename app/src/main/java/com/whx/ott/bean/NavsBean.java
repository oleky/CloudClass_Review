package com.whx.ott.bean;

import java.io.Serializable;

/**
 * Created by oleky on 2016/9/1.
 * 基础课程or特色课
 */
public class NavsBean implements Serializable{
    private int id;
    private String name; //基础课程

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NavsBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public NavsBean() {
    }
}
