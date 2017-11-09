package com.whx.ott.bean;

import java.io.Serializable;

/**
 * Created by oleky on 2016/9/1.
 */
public class TeachersBean implements Serializable{
    private int id;
    private int subject_id;
    private String teacher_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public TeachersBean(int id, int subject_id, String teacher_name) {
        this.id = id;
        this.subject_id = subject_id;
        this.teacher_name = teacher_name;
    }

    public TeachersBean() {
    }
}
