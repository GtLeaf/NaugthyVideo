package com.example.pc_0775.naugthyvideo.model.bean.UIMessage;



public class AudioMsgBody extends FileMsgBody{
    //语音消息长度,单位：秒。
    private long duration;


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


}
