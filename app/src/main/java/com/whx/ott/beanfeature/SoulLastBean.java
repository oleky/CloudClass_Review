package com.whx.ott.beanfeature;

/**
 * Created by oleky on 2016/9/18.
 */
public class SoulLastBean {
    private int user_id;
    private int model_id;
    private int soulplate_id;
    private String soulcourse_name;
    private int soulcourse_id;
    private int play_count;
    private int over_viewnum;
    private int subject_id;
    private String subject_name;

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public int getSoulplate_id() {
        return soulplate_id;
    }

    public void setSoulplate_id(int soulplate_id) {
        this.soulplate_id = soulplate_id;
    }

    public String getSoulcourse_name() {
        return soulcourse_name;
    }

    public void setSoulcourse_name(String soulcourse_name) {
        this.soulcourse_name = soulcourse_name;
    }

    public int getSoulcourse_id() {
        return soulcourse_id;
    }

    public void setSoulcourse_id(int soulcourse_id) {
        this.soulcourse_id = soulcourse_id;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
    }

    public int getOver_viewnum() {
        return over_viewnum;
    }

    public void setOver_viewnum(int over_viewnum) {
        this.over_viewnum = over_viewnum;
    }

    public SoulLastBean(int user_id, int model_id, int soulplate_id, String soulcourse_name, int soulcourse_id, int play_count, int over_viewnum) {
        this.user_id = user_id;
        this.model_id = model_id;
        this.soulplate_id = soulplate_id;
        this.soulcourse_name = soulcourse_name;
        this.soulcourse_id = soulcourse_id;
        this.play_count = play_count;
        this.over_viewnum = over_viewnum;
    }

    @Override
    public String toString() {
        return "SoulLastBean{" +
                "user_id=" + user_id +
                ", model_id=" + model_id +
                ", soulplate_id=" + soulplate_id +
                ", soulcourse_name='" + soulcourse_name + '\'' +
                ", soulcourse_id=" + soulcourse_id +
                ", play_count=" + play_count +
                ", over_viewnum=" + over_viewnum +
                ", subject_id=" + subject_id +
                ", subject_name='" + subject_name + '\'' +
                '}';
    }

    public SoulLastBean() {
    }
}
