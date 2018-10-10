package com.example.pc_0775.naugthyvideo.CardSwipeControl;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;

import java.util.List;

/**
 * Created by PC-0775 on 2018/10/9.
 */

public class CardShowInfoBean {
    private GlideDrawable glideDrawable;
    private List<String> categories;
    private VideoInfo videoInfo;

    public CardShowInfoBean() {
    }

    public CardShowInfoBean(GlideDrawable glideDrawable, VideoInfo videoInfo) {
        this.glideDrawable = glideDrawable;
        this.videoInfo = videoInfo;
    }

    public GlideDrawable getGlideDrawable() {
        return glideDrawable;
    }

    public void setGlideDrawable(GlideDrawable glideDrawable) {
        this.glideDrawable = glideDrawable;
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
}
