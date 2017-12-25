package com.whx.ott.bean;

/**
 * Created by HelloWorld on 2017/12/5.
 */

public class AllClassBean {
    private String banben;
    private String nianfen;
    private String bankuai;
    private String xueke;
    private int have_num;
    private int over_num;

    public String getBanben() {
        return banben;
    }

    public void setBanben(String banben) {
        this.banben = banben;
    }

    public String getNianfen() {
        return nianfen;
    }

    public void setNianfen(String nianfen) {
        this.nianfen = nianfen;
    }

    public String getBankuai() {
        return bankuai;
    }

    public void setBankuai(String bankuai) {
        this.bankuai = bankuai;
    }

    public String getXueke() {
        return xueke;
    }

    public void setXueke(String xueke) {
        this.xueke = xueke;
    }

    public int getHave_num() {
        return have_num;
    }

    public void setHave_num(int have_num) {
        this.have_num = have_num;
    }

    public int getOver_num() {
        return over_num;
    }

    public void setOver_num(int over_num) {
        this.over_num = over_num;
    }

    @Override
    public String toString() {
        return "AllClassBean{" +
                "banben='" + banben + '\'' +
                ", nianfen='" + nianfen + '\'' +
                ", bankuai='" + bankuai + '\'' +
                ", xueke='" + xueke + '\'' +
                '}';
    }
}
