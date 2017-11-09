package com.whx.ott.bean;

import java.io.Serializable;

/**
 * Created by oleky on 2016/9/1.
 */
public class GradesBean implements Serializable{
    private int id;
    private String grade_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public GradesBean(int id, String grade_name) {
        this.id = id;
        this.grade_name = grade_name;
    }

    public GradesBean() {
    }
}
