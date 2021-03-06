package com.whx.ott.db;

import android.content.Context;

import com.whx.ott.bean.GradesBean;
import com.whx.ott.bean.Soulplates;
import com.whx.ott.bean.SubjectsBean;
import com.whx.ott.bean.TeachersBean;
import com.whx.ott.bean.TermsBean;
import com.whx.ott.bean.YearsBean;
import com.whx.ott.conn.TownNumToString;
import com.whx.ott.util.SharedpreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleky on 2016/9/5.
 * 数据库操作类
 */
public class TownDBManager {
    private Context context;

    public TownDBManager(Context context) {
        this.context = context;
    }

    /**
     * 将学年存入sp表中
     */
    public void insertYear(List<YearsBean> yearsList) {
        SharedpreferenceUtil.saveObj2Sp(context, "town_yearlist", yearsList);
    }

    /**
     * 获取学年表list
     */
    public List<YearsBean> getYearList() {
        List<YearsBean> list = new ArrayList<>();
       list = (List<YearsBean>) SharedpreferenceUtil.queryObj2Sp(context, "town_yearlist");
        return list;
    }


    /**
     * 将学期表存入sp中
     */
    public void insertTerm(List<TermsBean> termsList) {
        SharedpreferenceUtil.saveObj2Sp(context, "town_termlist", termsList);
    }

    /**
     * 获取学期表list
     */
    public List<TermsBean> getTermList() {
        List<TermsBean> list = new ArrayList<>();
       list = (List<TermsBean>) SharedpreferenceUtil.queryObj2Sp(context, "town_termlist");
        return list;
    }

    /**
     * 通过gradeids数组获取List<GradesBean>
     *
     * @param array String数组
     */
    public List<GradesBean> getGradeList(String[] array) {
        List<GradesBean> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            int gradeid = Integer.parseInt(array[i]);
         String gradename = TownNumToString.searchGrades(gradeid);
            list.add(new GradesBean(gradeid, gradename));
        }
        return list;
    }


    /**
     * 将年级表插入sp
     */
    public void insertGrade(List<GradesBean> gradesBeanList) {
        SharedpreferenceUtil.saveObj2Sp(context, "town_gradelist", gradesBeanList);
    }

    /**
     * 将学科表存入sp
     */
    public void insertSubject(List<SubjectsBean> subjectsBeenList) {
        SharedpreferenceUtil.saveObj2Sp(context, "town_subjectlist", subjectsBeenList);
    }

    /**
     * 获取学科list
     */
    public List<SubjectsBean> getSubjectList() {
        List<SubjectsBean> list = new ArrayList<>();
       list = (List<SubjectsBean>) SharedpreferenceUtil.queryObj2Sp(context, "town_subjectlist");
        return list;
    }



    /**
     * 将教师表插入sp
     */
    public void insertDataToTeacher(List<TeachersBean> teacherList) {
        SharedpreferenceUtil.saveObj2Sp(context, "town_teacherlist", teacherList);
    }

    /**
     * 获取教师表中subjectid的数据字符串
     */
    public List<TeachersBean> getTeacherList(int subjectid) {
        List<TeachersBean> list = new ArrayList<>();
        List<TeachersBean> mylist = new ArrayList<>();
        mylist = (List<TeachersBean>) SharedpreferenceUtil.queryObj2Sp(context, "town_teacherlist");
        if (mylist != null && mylist.size() > 0) {
            for (int i = 0; i < mylist.size(); i++) {
                if (subjectid == mylist.get(i).getSubject_id()) {
                    list.add(new TeachersBean(mylist.get(i).getId(), subjectid, mylist.get(i).getTeacher_name()));
                }
            }
        }

        return list;
    }

    public void insertDataToSoulplate(List<Soulplates> soulplateList) {
        SharedpreferenceUtil.saveObj2Sp(context, "town_soulplatelist", soulplateList);
    }

    /**
     * 获取小初阶段表list
     */
    public List<Soulplates> getSoulplateList() {
        List<Soulplates> list = new ArrayList<>();
        list = (List<Soulplates>) SharedpreferenceUtil.queryObj2Sp(context, "town_soulplatelist");
        return list;
    }

}
