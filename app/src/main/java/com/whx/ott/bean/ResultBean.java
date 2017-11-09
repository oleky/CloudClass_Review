package com.whx.ott.bean;


import java.util.List;

/**
 * Created by HelloWorld on 2017/4/12.
 */

public class ResultBean {

    private String total; //0,
    private String message; //"请求成功！",
    private String pageCount; //0,
    private List datas;
    private String token; //"",
    private String status; //"success",
    private String pageSize; //0,
    private String currentPageNo; //0,
    private String forwardUrl; //"http://api.video.capitalcloud.net/renditions/download/952133646760943220"

    public String getTotal() {
        return total;
    }

    public List getDatas() {
        return datas;
    }

    public void setDatas(List datas) {
        this.datas = datas;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }


    public String getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(String currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public String getForwardUrl() {
        return forwardUrl;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "total='" + total + '\'' +
                ", message='" + message + '\'' +
                ", pageCount='" + pageCount + '\'' +
                ", token='" + token + '\'' +
                ", status='" + status + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", currentPageNo='" + currentPageNo + '\'' +
                ", forwardUrl='" + forwardUrl + '\'' +
                '}';
    }
}
