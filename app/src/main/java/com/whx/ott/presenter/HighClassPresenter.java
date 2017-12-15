package com.whx.ott.presenter;

import com.whx.ott.bean.JichuResult;
import com.whx.ott.bean.VideoPathBean;
import com.whx.ott.conn.ApiService;
import com.whx.ott.conn.RetrofitClient;
import com.whx.ott.presenter.viewinface.BasicClassView;
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

    public HighClassPresenter(BasicClassView classView) {
        mClassView = classView;
        mService = new RetrofitClient().createApiClient();
    }

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
                .subscribe(new Consumer<JichuResult>() {
                    @Override
                    public void accept(JichuResult jichuResult) throws Exception {
                        if (mClassView != null) {
                            mClassView.moreDate(jichuResult.getCourses());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        if (null != mClassView) {
                            mClassView.moreDate(null);
                        }
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
