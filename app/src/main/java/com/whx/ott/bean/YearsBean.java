package com.whx.ott.bean;

import java.io.Serializable;

/**
 * Created by oleky on 2016/9/1.
 */
public class YearsBean implements Serializable{
    private int id;
    private String year_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear_name() {
        return year_name;
    }

    public void setYear_name(String year_name) {
        this.year_name = year_name;
    }

    public YearsBean(int id, String year_name) {
        this.id = id;
        this.year_name = year_name;
    }

    public YearsBean() {
    }
}
