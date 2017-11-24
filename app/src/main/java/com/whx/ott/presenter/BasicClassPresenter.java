package com.whx.ott.presenter;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.whx.ott.bean.ParseJichu;
import com.whx.ott.bean.TestUrl;
import com.whx.ott.conn.ApiService;
import com.whx.ott.conn.RetrofitClient;
import com.whx.ott.presenter.viewinface.BasicClassView;
import com.whx.ott.util.RxUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * Created by oleky on 2017/9/21.
 */

public class BasicClassPresenter extends Presenter {

    private BasicClassView mClassView;
    private ApiService mService;

    public BasicClassPresenter(BasicClassView classView) {
        mClassView = classView;
        mService = new RetrofitClient().createApiClient();
    }

    public void getBasicClassList(Map<String,String> map) {
        mService.basicCourses(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ParseJichu>() {
                    @Override
                    public void accept(ParseJichu parseJichu) throws Exception {
                        if (mClassView != null) {
                            mClassView.getDate(parseJichu.getCourses());
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
                .subscribe(new Consumer<ParseJichu>() {
                    @Override
                    public void accept(ParseJichu parseJichu) throws Exception {
                        if (mClassView != null) {
                            mClassView.getDate(parseJichu.getCourses());
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
                .subscribe(new Consumer<ParseJichu>() {
                    @Override
                    public void accept(ParseJichu parseJichu) throws Exception {
                        if (mClassView != null) {
                            mClassView.moreDate(parseJichu.getCourses());
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
                .subscribe(new Consumer<TestUrl>() {
                    @Override
                    public void accept(TestUrl testUrl) throws Exception {
                        if (mClassView != null) {
                            String url = testUrl.getUrl();
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
