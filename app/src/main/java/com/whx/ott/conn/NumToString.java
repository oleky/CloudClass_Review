package com.whx.ott.conn;

import com.whx.ott.application.YunApplication;
import com.whx.ott.bean.BaseInfo;
import com.whx.ott.bean.GradesBean;
import com.whx.ott.bean.Soulplates;
import com.whx.ott.bean.SubjectsBean;
import com.whx.ott.bean.TeachersBean;
import com.whx.ott.bean.TermsBean;
import com.whx.ott.bean.YearsBean;
import com.whx.ott.util.SharedpreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleky on 2016/9/9.
 */
public class NumToString {

    public static BaseInfo baseInfo = (BaseInfo) SharedpreferenceUtil.queryObj2Sp(YunApplication.getInstance().getApplicationContext(), "base_info");
    private static List<YearsBean> yearList = new ArrayList<>();
    private static List<TermsBean> termList = new ArrayList<>();
    private static List<GradesBean> gradeList = new ArrayList<>();
    private static List<SubjectsBean> subjectList = new ArrayList<>();
    private static List<TeachersBean> teacherList = new ArrayList<>();
    private static List<Soulplates> soulplateList = new ArrayList<>();


    // TODO: 2017/8/12 新增两种type存储，默认为高中，如果true等于1为小初

    public static String searchYears(int id) {
        yearList = baseInfo.getYears();
        String yearName = "";
        yearName = yearList.get(id-1).getYear_name();
        return yearName;
    }

    public static String searchTerms(int id) {
        termList = baseInfo.getTerms();
        String termName = "";
        termName = termList.get(id-1).getTerm_name();
        return termName;
    }

    public static String searchGrades(int id) {
        String gradeName = "";
        gradeList = baseInfo.getGrades();
        gradeName = gradeList.get(id-1).getGrade_name();

        return gradeName;
    }

    public static String searchSubject(int id) {
        String subjectName = "";
        subjectList = baseInfo.getSubjects();
        subjectName = subjectList.get(id-1).getSubject_name();

        return subjectName;
    }

    public static String searchTeacher(int id) {
        String teacherName = "";
        teacherList = baseInfo.getTeachers();
        teacherName = teacherList.get(id-1).getTeacher_name();

        return teacherName;
    }
    public static String searSoulplate(int id) {
        String souplatename = "";
        soulplateList = baseInfo.getSoulplates();
        souplatename = soulplateList.get(id-1).getSoulplate_name();

        return souplatename;
    }

}
