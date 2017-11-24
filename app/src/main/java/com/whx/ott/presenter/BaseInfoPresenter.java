package com.whx.ott.presenter;

import android.content.Context;

import com.whx.ott.bean.BaseInfo;
import com.whx.ott.bean.GradesBean;
import com.whx.ott.bean.ParseLogin;
import com.whx.ott.bean.Soulplates;
import com.whx.ott.bean.SubjectsBean;
import com.whx.ott.bean.TeachersBean;
import com.whx.ott.bean.TermsBean;
import com.whx.ott.bean.YearsBean;
import com.whx.ott.conn.ApiService;
import com.whx.ott.conn.RetrofitClient;
import com.whx.ott.db.DBManager;
import com.whx.ott.db.TownDBManager;
import com.whx.ott.presenter.viewinface.BaseInfoView;
import com.whx.ott.util.RxUtil;
import com.whx.ott.util.SharedpreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by oleky on 2017/9/10.
 */

public class BaseInfoPresenter extends Presenter {

    private BaseInfoView mInfoView;
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

    public BaseInfoPresenter(BaseInfoView infoView, Context context) {
        mInfoView = infoView;
        mContext = context;
        mService = new RetrofitClient().createApiClient();
        manager = new DBManager(context);
        mTownDBManager = new TownDBManager(context);
    }

    public void getBaseInfo(String userid) {
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
                        if (null != mInfoView) {
                            mInfoView.getFailed("请检查网络");
                        }
                    }
                });
    }

    public void getTownBaseInfo(String userid) {
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

    public void getAddress(String userid) {
        mService.userPos(userid)
                .compose(RxUtil.rxScheduleHelper())
                .subscribe(new Consumer<ParseLogin>() {
                    @Override
                    public void accept(ParseLogin parseLogin) throws Exception {
                        if (null != mInfoView) {
                            String code = parseLogin.getCode();
                            if ("0".equals(code)) {
                                mInfoView.userAddress(parseLogin.getData().get(0).getAddress_name());
                                SharedpreferenceUtil.saveData(mContext, "user_money", parseLogin.getData().get(0).getUser_money());
                                SharedpreferenceUtil.saveData(mContext, "user_validetime", parseLogin.getData().get(0).getValide_time());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    public void uploadAddress(String userid, String username, final String loaction, String mac) {
        mService.uploadPosition(userid,username,loaction,mac)
                .compose(RxUtil.rxScheduleHelper())
                .subscribe(new Consumer<ParseLogin>() {
                    @Override
                    public void accept(ParseLogin parseLogin) throws Exception {
                        SharedpreferenceUtil.saveData(mContext, "localposition", loaction);
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
        if (null != mInfoView) {
            mInfoView = null;
        }
    }
}
