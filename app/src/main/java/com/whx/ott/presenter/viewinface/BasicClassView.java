package com.whx.ott.presenter.viewinface;

import com.whx.ott.bean.CoursesBean;

import java.util.List;

/**
 * Created by oleky on 2017/9/21.
 */

public interface BasicClassView {
    void getDate(List<CoursesBean> cList);

    void geturl(String url);
}
