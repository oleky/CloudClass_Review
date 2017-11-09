package com.whx.ott.bean;

import java.io.Serializable;

/**
 * Created by oleky on 2016/9/1.
 */
public class Soulplates implements Serializable{
    private int id;
    private String soulplate_name;  //大年半or绝密预测

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSoulplate_name() {
        return soulplate_name;
    }

    public void setSoulplate_name(String soulplate_name) {
        this.soulplate_name = soulplate_name;
    }

    public Soulplates(int id, String soulplate_name) {
        this.id = id;
        this.soulplate_name = soulplate_name;
    }

    public Soulplates() {
    }
}
