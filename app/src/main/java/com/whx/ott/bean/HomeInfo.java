package com.whx.ott.bean;

/**
 * Created by oleky on 2016/10/19.
 */

public class HomeInfo {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String filmName;
    private String filmImageLink;
    private String fileDetailUrl;
    private int icons;
    private int rs;

    public int getIcons() {
        return icons;
    }

    public void setIcons(int icons) {
        this.icons = icons;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getFilmImageLink() {
        return filmImageLink;
    }

    public void setFilmImageLink(String filmImageLink) {
        this.filmImageLink = filmImageLink;
    }

    public String getFileDetailUrl() {
        return fileDetailUrl;
    }

    public void setFileDetailUrl(String fileDetailUrl) {
        this.fileDetailUrl = fileDetailUrl;
    }

    public int getRs() {
        return rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public HomeInfo(String filmName, String filmImageLink, String fileDetailUrl, int rs) {
        this.filmName = filmName;
        this.filmImageLink = filmImageLink;
        this.fileDetailUrl = fileDetailUrl;
        this.rs = rs;
    }

    public HomeInfo(String filmName, String filmImageLink, String fileDetailUrl, int icons, int rs) {
        this.filmName = filmName;
        this.filmImageLink = filmImageLink;
        this.fileDetailUrl = fileDetailUrl;
        this.icons = icons;
        this.rs = rs;
    }

    public HomeInfo() {
    }
}
