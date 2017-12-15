package com.whx.ott.presenter.viewinface;

/**
 * Created by oleky on 2017/12/4.
 */

public interface DownloadView {
    void downloadProgress(float progress);

    void downloadSuccess();

    void downloadError(String error);
}
