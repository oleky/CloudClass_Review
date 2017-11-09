package com.whx.ott.beanfeature;

import com.google.gson.Gson;

/**
 * Created by HelloWorld on 2016/9/23.
 */
public class ParseMineModification {

    private String code;
    private String meg;

    public ParseMineModification(String code, String meg) {
        this.code = code;
        this.meg = meg;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static ParseMineModification getModifyData(String json) {
        return new Gson().fromJson(json, ParseMineModification.class);
    }
}
