package com.whx.ott.bean;

/**
 * Created by HelloWorld on 2017/12/5.
 */

public class StuJurisdictionBean {
    private String id; //: ; //3; //,
    private String stu_id; //: ; //11; //,
    private String rule_code; //: ; //;xiaochu_jichuke_all //,
    private int have_num; //: ; //12; //,
    private int over_num; //: ; //0; //,
    private String create_time; //: ; //2017-11-28 17:05:03; //


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getRule_code() {
        return rule_code;
    }

    public void setRule_code(String rule_code) {
        this.rule_code = rule_code;
    }

    public int getHave_num() {
        return have_num;
    }

    public void setHave_num(int have_num) {
        this.have_num = have_num;
    }

    public int getOver_num() {
        return over_num;
    }

    public void setOver_num(int over_num) {
        this.over_num = over_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
