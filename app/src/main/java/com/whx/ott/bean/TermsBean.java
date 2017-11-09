package com.whx.ott.bean;

import java.io.Serializable;

/**
 * Created by oleky on 2016/9/1.
 */
public class TermsBean implements Serializable{
    private int id;
    private String term_name;
    private String grade_ids;

    public String getGrade_ids() {
        return grade_ids;
    }

    public void setGrade_ids(String grade_ids) {
        this.grade_ids = grade_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerm_name() {
        return term_name;
    }

    public void setTerm_name(String term_name) {
        this.term_name = term_name;
    }

    public TermsBean(int id, String term_name) {
        this.id = id;
        this.term_name = term_name;
    }

    public TermsBean() {
    }

    public TermsBean(int id, String term_name, String grade_ids) {
        this.id = id;
        this.term_name = term_name;
        this.grade_ids = grade_ids;
    }

    @Override
    public String toString() {
        return id+"；"+term_name+ "---" + grade_ids;
    }
}
