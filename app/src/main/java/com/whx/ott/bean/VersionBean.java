package com.whx.ott.bean;

/**
 * Created by oleky on 2016/9/16.
 */
public class VersionBean {
    private int app_version;
    private String app_url;
    private String app_message;
    private String is_force;

    public String getApp_message() {
        return app_message;
    }

    public void setApp_message(String app_message) {
        this.app_message = app_message;
    }

    public int getApp_version() {
        return app_version;
    }

    public String getIs_force() {
        return is_force;
    }

    public void setIs_force(String is_force) {
        this.is_force = is_force;
    }

    public void setApp_version(int app_version) {
        this.app_version = app_version;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public VersionBean(int app_version, String app_url) {
        this.app_version = app_version;
        this.app_url = app_url;
    }

    public VersionBean() {
    }

    public VersionBean(int app_version, String app_url, String app_message) {
        this.app_version = app_version;
        this.app_url = app_url;
        this.app_message = app_message;
    }
}
