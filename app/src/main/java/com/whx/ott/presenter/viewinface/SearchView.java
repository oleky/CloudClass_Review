package com.whx.ott.presenter.viewinface;

import com.whx.ott.bean.CoursesBean;

import java.util.List;

/**
 * Created by oleky on 2017/9/10.
 */

public interface SearchView {
    void searchSucc(List<CoursesBean> clist);

    void searchFailed(String message);

    void getUrl(String url);
}
