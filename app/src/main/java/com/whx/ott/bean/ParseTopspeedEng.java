package com.whx.ott.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by HelloWorld on 2017/4/12.
 */

public class ParseTopspeedEng {
    private ResultBean result;


    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static ResultBean parser(String json) {
        return new Gson().fromJson(json, ParseTopspeedEng.class).getResult();
    }
}
