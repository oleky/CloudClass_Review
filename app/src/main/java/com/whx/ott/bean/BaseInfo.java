package com.whx.ott.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by oleky on 2016/9/1.
 */
public class BaseInfo implements Serializable{
    private String code; //"0"成功
    private String meg; //返回信息
    private List<YearsBean> years;
    private List<TermsBean> terms;
    private List<GradesBean>grades;
    private List<NavsBean>navs;
    private List<Soulplates>soulplates;
    private List<SubjectsBean>subjects;
    private List<TeachersBean>teachers;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }

    public List<YearsBean> getYears() {
        return years;
    }

    public void setYears(List<YearsBean> years) {
        this.years = years;
    }

    public List<TermsBean> getTerms() {
        return terms;
    }

    public void setTerms(List<TermsBean> terms) {
        this.terms = terms;
    }

    public List<GradesBean> getGrades() {
        return grades;
    }

    public void setGrades(List<GradesBean> grades) {
        this.grades = grades;
    }

    public List<NavsBean> getNavs() {
        return navs;
    }

    public void setNavs(List<NavsBean> navs) {
        this.navs = navs;
    }

    public List<Soulplates> getSoulplates() {
        return soulplates;
    }

    public void setSoulplates(List<Soulplates> soulplates) {
        this.soulplates = soulplates;
    }

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }

    public List<TeachersBean> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeachersBean> teachers) {
        this.teachers = teachers;
    }

    public BaseInfo(String code, String meg, List<YearsBean> years, List<TermsBean> terms, List<GradesBean> grades, List<NavsBean> navs, List<Soulplates> soulplates, List<SubjectsBean> subjects, List<TeachersBean> teachers) {
        this.code = code;
        this.meg = meg;
        this.years = years;
        this.terms = terms;
        this.grades = grades;
        this.navs = navs;
        this.soulplates = soulplates;
        this.subjects = subjects;
        this.teachers = teachers;
    }

    public BaseInfo() {
    }

    public static BaseInfo getBaseInfo(String json) {
        return new Gson().fromJson(json, BaseInfo.class);
    }
}
