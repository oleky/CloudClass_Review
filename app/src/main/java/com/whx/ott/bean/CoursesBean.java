package com.whx.ott.bean;

import java.io.Serializable;

/**
 * Created by oleky on 2016/9/5.
 * 视频信息列表
 */
public class CoursesBean implements Serializable{
    private int id;  //视频id
    private String course_name; //视频名
    private String course_url; //视频url
    private int year_id; //学年id
    private int term_id; // 学期id
    private int grade_id; //年级id
    private int subject_id; //学科id
    private int teacher_id; //教师id
    private int course_length; //课程时长
    private String course_detail; //课程简介
    private int model_id; //所属模块id 基础课or特色课
    private String file_name; //视频文件名

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_url() {
        return course_url;
    }

    public void setCourse_url(String course_url) {
        this.course_url = course_url;
    }

    public int getYear_id() {
        return year_id;
    }

    public void setYear_id(int year_id) {
        this.year_id = year_id;
    }

    public int getTerm_id() {
        return term_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getCourse_length() {
        return course_length;
    }

    public void setCourse_length(int course_length) {
        this.course_length = course_length;
    }

    public String getCourse_detail() {
        return course_detail;
    }

    public void setCourse_detail(String course_detail) {
        this.course_detail = course_detail;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public CoursesBean(int id, String course_name, String course_url, int year_id, int term_id, int grade_id, int subject_id, int teacher_id, int course_length, String course_detail, int model_id, String file_name) {
        this.id = id;
        this.course_name = course_name;
        this.course_url = course_url;
        this.year_id = year_id;
        this.term_id = term_id;
        this.grade_id = grade_id;
        this.subject_id = subject_id;
        this.teacher_id = teacher_id;
        this.course_length = course_length;
        this.course_detail = course_detail;
        this.model_id = model_id;
        this.file_name = file_name;
    }

    public CoursesBean() {
    }

}
