package com.example.pc_0775.naugthyvideo.CardSwipeControl;

import android.graphics.drawable.Drawable;

import com.example.pc_0775.naugthyvideo.bean.VideoInfo;

import java.util.List;

/**
 * Created by PC-0775 on 2018/10/9.
 */

public class CardShowInfoBean {
    private Drawable drawable;
    private List<String> categories;
    private int type;
    private VideoInfo videoInfo;

    public CardShowInfoBean() {
    }

    public CardShowInfoBean(Drawable drawable, VideoInfo videoInfo) {
        this.drawable = drawable;
        this.videoInfo = videoInfo;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
