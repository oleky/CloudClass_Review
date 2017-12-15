package com.whx.ott.presenter;

import android.content.Context;
import android.widget.Toast;

import com.whx.ott.bean.BaseInfo;
import com.whx.ott.bean.GradesBean;
import com.whx.ott.bean.Soulplates;
import com.whx.ott.bean.SubjectsBean;
import com.whx.ott.bean.TeachersBean;
import com.whx.ott.bean.TermsBean;
import com.whx.ott.bean.YearsBean;
import com.whx.ott.conn.ApiService;
import com.whx.ott.conn.RetrofitClient;
import com.whx.ott.db.DBManager;
import com.whx.ott.db.TownDBManager;
import com.whx.ott.util.RxUtil;
import com.whx.ott.util.SharedpreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by oleky on 2017/9/10.
 */

public class BaseInfoPresenter extends Presenter {

    private Context mContext;
    private ApiService mService;

    private DBManager manager;
    private TownDBManager mTownDBManager;
    private List<YearsBean> yearList = new ArrayList<>();
    private List<TermsBean> termList = new ArrayList<>();
    private List<GradesBean> gradeList = new ArrayList<>();
    private List<SubjectsBean> subjectList = new ArrayList<>();
    private List<TeachersBean> teacherList = new ArrayList<>();
    private List<Soulplates> soulplateList = new ArrayList<>();

    public BaseInfoPresenter( Context context) {
        mContext = context;
        mService = new RetrofitClient().createApiClient();
        manager = new DBManager(context);
        mTownDBManager = new TownDBManager(context);
    }

    public void getBaseInfo() {
        mService.highBaseInfo()
                .compose(RxUtil.rxScheduleHelper())
                .subscribe(new Consumer<BaseInfo>() {
                    @Override
                    public void accept(BaseInfo info) throws Exception {
                        String code = info.getCode();
                        if ("0".equals(code)) {
                            SharedpreferenceUtil.saveObj2Sp(mContext, "base_info", info);
                            insertData(info);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getTownBaseInfo() {
        mService.townBaseInfo()
                .compose(RxUtil.rxScheduleHelper())
                .subscribe(new Consumer<BaseInfo>() {
                    @Override
                    public void accept(BaseInfo info) throws Exception {
                        String code = info.getCode();
                        if ("0".equals(code)) {
                            SharedpreferenceUtil.saveObj2Sp(mContext, "town_base_info", info);
                            insertTownData(info);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }


    /**
     * 插入sp
     *
     * */
    private void insertData(BaseInfo baseinfo) {
        yearList = baseinfo.getYears();
        termList = baseinfo.getTerms();
        gradeList = baseinfo.getGrades();
        subjectList = baseinfo.getSubjects();
        teacherList = baseinfo.getTeachers();
        soulplateList = baseinfo.getSoulplates();
        manager.insertYear(yearList);
        manager.insertTerm(termList);
        manager.insertGrade(gradeList);
        manager.insertSubject(subjectList);
        manager.insertDataToTeacher(teacherList);
        manager.insertDataToSoulplate(soulplateList);

    }

    private void insertTownData(BaseInfo baseinfo) {
        yearList.clear();
        termList.clear();
        gradeList.clear();
        subjectList.clear();
        teacherList.clear();
        soulplateList.clear();

        yearList = baseinfo.getYears();
        termList = baseinfo.getTerms();
        gradeList = baseinfo.getGrades();
        subjectList = baseinfo.getSubjects();
        teacherList = baseinfo.getTeachers();
        soulplateList = baseinfo.getSoulplates();

        mTownDBManager.insertYear(yearList);
        mTownDBManager.insertTerm(termList);
        mTownDBManager.insertGrade(gradeList);
        mTownDBManager.insertSubject(subjectList);
        mTownDBManager.insertDataToTeacher(teacherList);
        mTownDBManager.insertDataToSoulplate(soulplateList);

    }

    @Override
    public void onDestory() {
    }
}
