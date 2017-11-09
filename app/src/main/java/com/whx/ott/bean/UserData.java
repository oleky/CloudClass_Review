package com.whx.ott.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by oleky on 2016/9/8.
 */
public class UserData implements Serializable {
    private String user_id;
    private String user_name;
    private String user_money; //基础课剩余课时
    private String valide_time; //有效期
    private String address_name; //用户地址
    private String town_money; //": "0.00",
    private String town_time; //": "0000-00-00 00:00:00"

    public String getTown_money() {
        return town_money;
    }

    public void setTown_money(String town_money) {
        this.town_money = town_money;
    }

    public String getTown_time() {
        return town_time;
    }

    public void setTown_time(String town_time) {
        this.town_time = town_time;
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

    public String getUser_money() {
        return user_money;
    }

    public void setUser_money(String user_money) {
        this.user_money = user_money;
    }

    public String getValide_time() {
        return valide_time;
    }

    public void setValide_time(String valide_time) {
        this.valide_time = valide_time;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public UserData(String user_id, String user_name, String user_money, String valide_time) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_money = user_money;
        this.valide_time = valide_time;
    }

    public UserData(String user_id, String user_name, String user_money, String valide_time, String address_name) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_money = user_money;
        this.valide_time = valide_time;
        this.address_name = address_name;
    }

    public UserData() {
    }

    public static UserData getUserInfo(String json) {
        return new Gson().fromJson(json, UserData.class);
    }
}
