package com.whx.ott.presenter;

import android.util.Log;

import com.whx.ott.bean.JichuResult;
import com.whx.ott.bean.TownTeseResult;
import com.whx.ott.bean.VideoPathBean;
import com.whx.ott.conn.ApiService;
import com.whx.ott.conn.RetrofitClient;
import com.whx.ott.presenter.viewinface.BasicClassView;
import com.whx.ott.presenter.viewinface.TeseClassView;
import com.whx.ott.util.RxUtil;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by oleky on 2017/9/21.
 */

public class HighClassPresenter extends Presenter {

    private BasicClassView mClassView;
    private ApiService mService;
    private TeseClassView mTeseClassView;

    public HighClassPresenter(BasicClassView classView) {
        mClassView = classView;
        mService = new RetrofitClient().createApiClient();
    }

    public HighClassPresenter(TeseClassView classView) {
        mTeseClassView = classView;
        mService = new RetrofitClient().createApiClient();
    }

    /**
     * 高中基础课筛选后课程列表
     * */
    public void getBasicClassList(Map<String,String> map) {
        mService.basicCourses(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JichuResult>() {
                    @Override
                    public void accept(JichuResult jichuResult) throws Exception {
                        if (mClassView != null) {
                            mClassView.getDate(jichuResult.getCourses());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        if (null != mClassView) {
                            mClassView.getDate(null);
                        }
                    }
                });
    }

    /**
     * 小初基础课筛选后课程列表
     * */
    public void getTownClassList(Map<String,String> map) {
        mService.townCourses(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JichuResult>() {
                    @Override
                    public void accept(JichuResult jichuResult) throws Exception {
                        if (mClassView != null) {
                            mClassView.getDate(jichuResult.getCourses());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        if (null != mClassView) {
                            mClassView.getDate(null);
                        }
                    }
                });


    }
    public void loadmoreTownList(Map<String, String> map) {
        mService.townCourses(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jichuResult -> {
                    if (mClassView != null) {
                        mClassView.moreDate(jichuResult.getCourses());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (null != mClassView) {
                        mClassView.moreDate(null);
                    }
                });
    }

    /**
     * 小初基础课筛选后课程列表
     * */
    public void getTownSoulplateList(Map<String,String> map) {
        mService.townTeseCourses(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TownTeseResult>() {
                    @Override
                    public void accept(TownTeseResult teseResult) throws Exception {
                        if (mTeseClassView != null) {
                            mTeseClassView.getDate(teseResult.getXcsoulcourses());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        if (null != mTeseClassView) {
                            mTeseClassView.getDate(null);
                        }
                    }
                });


    }


    public void loadmoreTownSoulList(Map<String, String> map) {
        mService.townTeseCourses(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jichuResult -> {
                    if (mTeseClassView != null) {
                        mTeseClassView.moreDate(jichuResult.getXcsoulcourses());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (null != mTeseClassView) {
                        mTeseClassView.moreDate(null);
                    }
                });
    }

    //新系统获取播放地址
    public void geturl(String filename,String devid, String bancode,String modeCode,String yearCode) {

        mService.videoUrl(filename,devid,bancode,modeCode,yearCode)
                .compose(RxUtil.rxScheduleHelper())
                .subscribe(videoPathBean -> {
                    if (mClassView != null) {
                        String url = videoPathBean.getUrl();
                        Log.e("VideoUrl", "视频地址" + url);
                        mClassView.geturl(url);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (mClassView != null) {
                        mClassView.geturl("");
                    }
                });

    }

    public void geturl(String filename, String devid) {
        mService.liveUrl(filename,devid)
                .compose(RxUtil.rxScheduleHelper())
                .subscribe(new Consumer<VideoPathBean>() {
                    @Override
                    public void accept(VideoPathBean videoPathBean) throws Exception {
                        if (mClassView != null) {
                            String url = videoPathBean.getUrl();
                            mClassView.geturl(url);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (mClassView != null) {
                            mClassView.geturl("");
                        }
                    }
                });


    }


    @Override
    public void onDestory() {
        if (mClassView != null) {
            mClassView = null;
        }
    }
}
