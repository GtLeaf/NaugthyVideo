package com.example.pc_0775.naugthyvideo.model.bean.UIMessage;


import cn.jpush.im.android.api.model.Message;

public class UIMessage {

    private String uuid;
    private String msgId;
    private MsgType msgType;
    private MsgBody body;
    private MsgSendStatus sentStatus;
    private String senderId;
    private String targetId;
    private long sentTime;
    private Message serverMessage;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public MsgBody getBody() {
        return body;
    }

    public void setBody(MsgBody body) {
        this.body = body;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public MsgSendStatus getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(MsgSendStatus sentStatus) {
        this.sentStatus = sentStatus;
    }


    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public Message getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(Message serverMessage) {
        this.serverMessage = serverMessage;
    }
}
