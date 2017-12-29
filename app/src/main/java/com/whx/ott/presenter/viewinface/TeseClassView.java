package com.whx.ott.presenter.viewinface;

import com.whx.ott.beanfeature.SoulcoursesBean;

import java.util.List;

/**
 * Created by oleky on 2017/9/21.
 */

public interface TeseClassView {
    void getDate(List<SoulcoursesBean> cList);

    void geturl(String url);

    void moreDate(List<SoulcoursesBean> mList);
}
