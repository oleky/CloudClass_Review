package com.whx.ott.beanfeature;

import java.io.Serializable;

/**
 * Created by HelloWorld on 2017/2/21.
 */

public class NewMineFeatureBean implements Serializable {
    private String user_id; // "102",
    private String model_id; // "2",
    private String soulplate_id; // "2",
    private String soulplate_name; // "大年班",
    private int subject_id; // "1",
    private String subject_name; // "语文",
    private String soulcourse_id; // "77",
    private String soulcourse_name; // "【2017大年班语文】一、小说",
    private String play_count; // "2",
    private String over_viewnum; // "1",
    private String create_time; // "2017-02-21 17:15:54"

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getSoulplate_name() {
        return soulplate_name;
    }

    public void setSoulplate_name(String soulplate_name) {
        this.soulplate_name = soulplate_name;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSoulcourse_id() {
        return soulcourse_id;
    }

    public void setSoulcourse_id(String soulcourse_id) {
        this.soulcourse_id = soulcourse_id;
    }

    public String getSoulcourse_name() {
        return soulcourse_name;
    }

    public void setSoulcourse_name(String soulcourse_name) {
        this.soulcourse_name = soulcourse_name;
    }

    public String getPlay_count() {
        return play_count;
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }

    public String getOver_viewnum() {
        return over_viewnum;
    }

    public void setOver_viewnum(String over_viewnum) {
        this.over_viewnum = over_viewnum;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "NewMineFeatureBean{" +
                "user_id='" + user_id + '\'' +
                ", model_id='" + model_id + '\'' +
                ", soulplate_id='" + soulplate_id + '\'' +
                ", soulplate_name='" + soulplate_name + '\'' +
                ", subject_id='" + subject_id + '\'' +
                ", subject_name='" + subject_name + '\'' +
                ", soulcourse_id='" + soulcourse_id + '\'' +
                ", soulcourse_name='" + soulcourse_name + '\'' +
                ", play_count='" + play_count + '\'' +
                ", over_viewnum='" + over_viewnum + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
