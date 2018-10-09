package com.example.pc_0775.naugthyvideo.CardSwipeControl;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;

/**
 * Created by PC-0775 on 2018/10/9.
 */

public class CardInfoBean {
    private GlideDrawable glideDrawable;
    private VideoInfo videoInfo;

    public CardInfoBean() {
    }

    public CardInfoBean(GlideDrawable glideDrawable, VideoInfo videoInfo) {
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
}
