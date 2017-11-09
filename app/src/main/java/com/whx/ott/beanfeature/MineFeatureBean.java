package com.whx.ott.beanfeature;

/**
 * Created by HelloWorld on 2016/9/8.
 */
public class MineFeatureBean {
    private String id; // :  ; // 1 ; // ,
    private String user_id; // :  ; // 7 ; // ,
    private String user_name; // :  ; // testname ; // ,
    private String model_id; // :  ; // 2 ; // ,
    private String model_name; // :  ; // 特色课 ; // ,
    private String soulplate_id; // :  ; // 1 ; // ,
    private String soulplate_name; // :  ; // 大年班 ; // ,
    private String course_id; // :  ; // 1 ; // ,
    private String course_name; // :  ; // 语文名称 ; // ,
    private String create_time; // :  ; // 2016-09-06 09:31:42 ; //
    private String subject_id;
    private String subject_name;

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getSoulplate_id() {
        return soulplate_id;
    }

    public void setSoulplate_id(String soulplate_id) {
        this.soulplate_id = soulplate_id;
    }

    public String getSoulplate_name() {
        return soulplate_name;
    }

    public void setSoulplate_name(String soulplate_name) {
        this.soulplate_name = soulplate_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "MineFeatureBean{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", model_id='" + model_id + '\'' +
                ", model_name='" + model_name + '\'' +
                ", soulplate_id='" + soulplate_id + '\'' +
                ", soulplate_name='" + soulplate_name + '\'' +
                ", course_id='" + course_id + '\'' +
                ", course_name='" + course_name + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
