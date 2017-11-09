package com.whx.ott.bean;

import java.io.Serializable;

/**
 * Created by oleky on 2016/9/1.
 */
public class SubjectsBean implements Serializable{
    private int id;
    private String subject_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public SubjectsBean(int id, String subject_name) {
        this.id = id;
        this.subject_name = subject_name;
    }

    public SubjectsBean() {
    }
}
