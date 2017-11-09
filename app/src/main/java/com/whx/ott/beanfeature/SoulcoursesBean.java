package com.whx.ott.beanfeature;

import java.io.Serializable;

/**
 * Created by HelloWorld on 2016/9/6.
 */
public class SoulcoursesBean implements Serializable {
    private String id;// "1",
    private String soulcourse_name;// "大年班语文",
    private String soulcourse_url;// null,
    private String model_id;// "2",
    private String soulplate_id;// "3",
    private String subject_id;// "1",
    private String status;// "0",
    private String soulcourse_detail;// "测试",
    private String create_time;// "2016-08-25 17:59:42",
    private String create_user;// "0",
    private String soulcourse_length;// "90"
    private String file_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSoulcourse_name() {
        return soulcourse_name;
    }

    public void setSoulcourse_name(String soulcourse_name) {
        this.soulcourse_name = soulcourse_name;
    }

    public String getSoulcourse_url() {
        return soulcourse_url;
    }

    public void setSoulcourse_url(String soulcourse_url) {
        this.soulcourse_url = soulcourse_url;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getSoulplate_id() {
        return soulplate_id;
    }

    public void setSoulplate_id(String soulplate_id) {
        this.soulplate_id = soulplate_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSoulcourse_detail() {
        return soulcourse_detail;
    }

    public void setSoulcourse_detail(String soulcourse_detail) {
        this.soulcourse_detail = soulcourse_detail;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getSoulcourse_length() {
        return soulcourse_length;
    }

    public void setSoulcourse_length(String soulcourse_length) {
        this.soulcourse_length = soulcourse_length;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    @Override
    public String toString() {
        return "SoulcoursesBean{" +
                "id='" + id + '\'' +
                ", soulcourse_name='" + soulcourse_name + '\'' +
                ", soulcourse_url='" + soulcourse_url + '\'' +
                ", model_id='" + model_id + '\'' +
                ", soulplate_id='" + soulplate_id + '\'' +
                ", subject_id='" + subject_id + '\'' +
                ", status='" + status + '\'' +
                ", soulcourse_detail='" + soulcourse_detail + '\'' +
                ", create_time='" + create_time + '\'' +
                ", create_user='" + create_user + '\'' +
                ", soulcourse_length='" + soulcourse_length + '\'' +
                '}';
    }
}
