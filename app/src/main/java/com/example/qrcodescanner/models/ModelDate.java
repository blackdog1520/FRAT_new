package com.example.qrcodescanner.models;

public class ModelDate {
    String remark,date;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ModelDate(String remark, String date) {
        this.remark = remark;
        this.date = date;
    }

    public ModelDate() {
    }
}
