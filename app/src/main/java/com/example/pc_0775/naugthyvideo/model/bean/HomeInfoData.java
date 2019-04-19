package com.example.pc_0775.naugthyvideo.model.bean;

/**
 * Created by PC-0775 on 2018/9/22.
 */

public class HomeInfoData {
    private String title;
    private String describe;
    private int type;

    public HomeInfoData(String title, String describe, int type) {
        this.title = title;
        this.describe = describe;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
