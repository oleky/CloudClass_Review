package com.whx.ott.bean;

import com.google.gson.Gson;
import com.hjoleky.stucloud.bean.DictionaryBean;

import java.util.List;

/**
 * Created by HelloWorld on 2017/12/5.
 */

public class ParseDictionary {
    private int code;
    private String meg;
    private List<DictionaryBean> dicts;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }

    public List<DictionaryBean> getDicts() {
        return dicts;
    }

    public void setDicts(List<DictionaryBean> dicts) {
        this.dicts = dicts;
    }

    public static ParseDictionary parseDictionary(String json) {
        return new Gson().fromJson(json, ParseDictionary.class);
    }
}
