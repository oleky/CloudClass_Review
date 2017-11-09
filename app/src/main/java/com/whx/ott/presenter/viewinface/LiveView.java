package com.whx.ott.presenter.viewinface;

/**
 * Created by oleky on 2017/9/11.
 */

public interface LiveView {
    void paySucc();

    void payFailed(String errorMsg);

    void getUrl(String url);
}
