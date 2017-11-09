package com.whx.ott.presenter;



import com.whx.ott.bean.CoursesBean;
import com.whx.ott.bean.ParseSearch;
import com.whx.ott.bean.TestUrl;
import com.whx.ott.conn.ApiService;
import com.whx.ott.conn.RetrofitClient;
import com.whx.ott.presenter.viewinface.SearchView;
import com.whx.ott.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by oleky on 2017/9/10.
 */

public class SearchPresenter extends Presenter{

    private SearchView mSearchView;
    private List<CoursesBean> cList = new ArrayList<>();
    private ApiService mService;

    public SearchPresenter(SearchView searchView) {
        mSearchView = searchView;
        mService = new RetrofitClient().createApiClient();
    }

    public void searchCourse(String courseName, String mac,int start, int limit) {
        mService.seach(courseName,mac,start+"",start+limit+"")
                .compose(RxUtil.<ParseSearch>rxScheduleHelper())
                .subscribe(new Consumer<ParseSearch>() {
                    @Override
                    public void accept(ParseSearch parseSearch) throws Exception {
                        String code = parseSearch.getCode();
                        if (null != mSearchView) {
                            if ("0".equals(code)) {
                                if (parseSearch.getBasecourses() != null && parseSearch.getBasecourses().size() > 0) {
                                    cList = parseSearch.getBasecourses();
                                    mSearchView.searchSucc(cList);
                                }
                            } else {
                                mSearchView.searchFailed("搜索结果为空");
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (null != mSearchView) {
                            mSearchView.searchFailed("请检查网络");
                        }
                    }
                });
    }

    public void geturl(String filename, String devid) {
        mService.liveUrl(filename,devid)
                .compose(RxUtil.<TestUrl>rxScheduleHelper())
                .subscribe(new Consumer<TestUrl>() {
                    @Override
                    public void accept(TestUrl testUrl) throws Exception {
                        if (mSearchView != null) {
                            String url = testUrl.getUrl();
                            mSearchView.getUrl(url);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (mSearchView != null) {
                            mSearchView.getUrl("");
                        }
                    }
                });
    }

    @Override
    public void onDestory() {
        if (null != mSearchView) {
            mSearchView = null;
        }
    }
}
