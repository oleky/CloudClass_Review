package com.whx.ott.bean;

import java.io.Serializable;

/**
 * Created by Oleky on 2016/9/9.
 */
public class SoulInfo implements Serializable {
    private String user_id;
    private String model_id;
    private String soulplate_id;
    private String soulcourse;
    private int play_count; //播放总次数
    private int over_viewnum; //已经播放次数

    public String getSoulcourse() {
        return soulcourse;
    }

    public void setSoulcourse(String soulcourse) {
        this.soulcourse = soulcourse;
    }

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

    public SoulInfo(String user_id, String model_id, String soulplate_id, int play_count, int over_viewnum) {
        this.user_id = user_id;
        this.model_id = model_id;
        this.soulplate_id = soulplate_id;
        this.play_count = play_count;
        this.over_viewnum = over_viewnum;
    }

    public SoulInfo() {
    }
}
