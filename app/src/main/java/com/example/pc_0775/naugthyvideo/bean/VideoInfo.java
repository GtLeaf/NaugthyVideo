package com.example.pc_0775.naugthyvideo.bean;

import java.io.Serializable;

/**
 * Created by PC-0775 on 2018/8/20.
 */

public class VideoInfo implements Serializable{
    /**
     * title : 第55集
     * url : https://www.12qihu.com/html/12/61976.html
     * imgUrl : https://pppp.642p.com/91/2018/07/VMVkLhXw.gif
     * info : 最新
     */

    private String title;
    private String url;
    private String img;
    private String info;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public VideoInfo(){}

    public VideoInfo(String title, String url, String img, String info) {
        this.title = title;
        this.url = url;
        this.img = img;
        this.info = info;
    }
}
