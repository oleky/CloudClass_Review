package com.whx.ott.beanfeature;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by HelloWorld on 2016/9/8.
 */
public class ParseMineBasised {
    private int code;
    private String meg;
    private List<BasisedBean> data;

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

    public List<BasisedBean> getData() {
        return data;
    }

    public void setData(List<BasisedBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ParseMineBasised{" +
                "code='" + code + '\'' +
                ", meg='" + meg + '\'' +
                ", data=" + data +
                '}';
    }

    public static ParseMineBasised getBasisData(String json){
        return new Gson().fromJson(json,ParseMineBasised.class);
    }
}
