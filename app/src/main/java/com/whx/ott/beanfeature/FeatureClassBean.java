package com.whx.ott.beanfeature;

import java.io.Serializable;

/**
 * Created by HelloWorld on 2017/12/16.
 */

public class FeatureClassBean implements Serializable {
    private String id; //: ; //1; //,
    private String user_id; //: ; //214; //,
    private String student_id; //: ; //10; //,
    private String stu_name; //: ; //15917101519; //,
    private String stu_realname; //: ; //闫清悦; //,
    private String type_id; //: ; //1; //,
    private String type_name; //: ; //雄博士; //,
    private String year_id; //: ; //1; //,
    private String year_name; //: ; //精越; //,
    private String model_id; //: ; //1; //,
    private String model_name; //: ; //大年班; //,
    private String stage_id; //: ; //1; //,
    private String stage_name; //: ; //暑期班; //,
    private String grade_id; //: ; //1; //,
    private String grade_name; //: ; //一年级; //,
    private String subject_id; //: ; //1; //,
    private String subject_name; //: ; //语文; //,
    private String teacher_id; //: ; //1; //,
    private String teacher_name; //: ; //语文老师; //,
    private String course_id; //: ; //1; //,
    private String course_name; //: ; //语文特色课; //,
    private String code_num; //: null,
    private String create_time; //: ; //2017-11-29 14:34:40; //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getStu_realname() {
        return stu_realname;
    }

    public void setStu_realname(String stu_realname) {
        this.stu_realname = stu_realname;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getYear_id() {
        return year_id;
    }

    public void setYear_id(String year_id) {
        this.year_id = year_id;
    }

    public String getYear_name() {
        return year_name;
    }

    public void setYear_name(String year_name) {
        this.year_name = year_name;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getStage_id() {
        return stage_id;
    }

    public void setStage_id(String stage_id) {
        this.stage_id = stage_id;
    }

    public String getStage_name() {
        return stage_name;
    }

    public void setStage_name(String stage_name) {
        this.stage_name = stage_name;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(String grade_id) {
        this.grade_id = grade_id;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCode_num() {
        return code_num;
    }

    public void setCode_num(String code_num) {
        this.code_num = code_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "FeatureClassBean{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", stu_name='" + stu_name + '\'' +
                ", stu_realname='" + stu_realname + '\'' +
                ", type_id='" + type_id + '\'' +
                ", type_name='" + type_name + '\'' +
                ", year_id='" + year_id + '\'' +
                ", year_name='" + year_name + '\'' +
                ", model_id='" + model_id + '\'' +
                ", model_name='" + model_name + '\'' +
                ", stage_id='" + stage_id + '\'' +
                ", stage_name='" + stage_name + '\'' +
                ", grade_id='" + grade_id + '\'' +
                ", grade_name='" + grade_name + '\'' +
                ", subject_id='" + subject_id + '\'' +
                ", subject_name='" + subject_name + '\'' +
                ", teacher_id='" + teacher_id + '\'' +
                ", teacher_name='" + teacher_name + '\'' +
                ", course_id='" + course_id + '\'' +
                ", course_name='" + course_name + '\'' +
                ", code_num='" + code_num + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
