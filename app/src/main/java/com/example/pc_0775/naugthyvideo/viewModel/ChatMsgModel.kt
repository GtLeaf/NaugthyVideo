package com.example.pc_0775.naugthyvideo.viewModel

import cn.jpush.im.android.api.content.ImageContent
import cn.jpush.im.android.api.content.TextContent
import cn.jpush.im.android.api.content.VoiceContent
import cn.jpush.im.android.api.model.Message
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.event.ChatRoomMessageEvent
import com.example.pc_0775.naugthyvideo.model.bean.UIMessage.*
import com.example.pc_0775.naugthyvideo.ui.ChatActivity
import com.example.pc_0775.naugthyvideo.util.LogUtil
import kotlin.collections.ArrayList

/**
 * Created by PC-0775 on 2019/3/24.
 */
class ChatMsgModel {
    private val allMsg = ArrayList<UIMessage>()
    private val newMsgList = ArrayList<UIMessage>()
    private val allImgMsgList = ArrayList<Message>()
    private val newImgMsgList = ArrayList<Message>()


    fun addChatRoomMsg(event: ChatRoomMessageEvent){
        newMsgList.clear()
        newImgMsgList.clear()
        val msgs = event.messages
        //处理每一条信息
        msgs.forEach {
            when(it.contentType){
                //文本消息
                ContentType.text -> {
                    val mUIMessage = getBaseMessage(MsgType.TEXT, it)
                    val mTextMsgBody = TextMsgBody()
                    mTextMsgBody.message = (it.content as TextContent).text
                    mUIMessage.body = mTextMsgBody
                    newMsgList.add(mUIMessage)
                }
                //图片消息
                ContentType.image -> {
                    val mUIMessage = getBaseMessage(MsgType.IMAGE, it)
                    val mImageMsgBody = ImageMsgBody()
                    mImageMsgBody.thumbPath = (it.content as ImageContent).localThumbnailPath
                    mImageMsgBody.localPath = (it.content as ImageContent).localPath
                    mUIMessage.body = mImageMsgBody
                    newMsgList.add(mUIMessage)
                    newImgMsgList.add(it)
                    allImgMsgList.add(it)
                }
                //视频消息
                ContentType.video -> {
                    /*val mUIMessage = getBaseMessage(MsgType.VIDEO, it)
                    val mVideoMsgBody = VideoMsgBody()
                    mVideoMsgBody.extra = (it.content as VideoContent).thumbLocalPath
                    mVideoMsgBody.localPath = (it.content as VideoContent).videoLocalPath
                    mVideoMsgBody.duration = (it.content as VideoContent).duration.toLong()
                    mUIMessage.body = mVideoMsgBody
                    newMsgList.add(mUIMessage)*/
                }
                //音频消息
                ContentType.voice -> {
                    val mUIMessage = getBaseMessage(MsgType.AUDIO, it)
                    val mAudioMsgBody = AudioMsgBody()
                    mAudioMsgBody.localPath = (it.content as VoiceContent).localPath
                    mAudioMsgBody.duration = (it.content as VoiceContent).duration.toLong()
                    mUIMessage.body = mAudioMsgBody
                    newMsgList.add(mUIMessage)

                }
                //文件消息
                ContentType.file -> {}
                //位置消息
                ContentType.location -> {}
                else -> {
                    LogUtil.d("location","aa")
                }
            }
        }
    }

    fun getMsgList():ArrayList<UIMessage>{
        return newMsgList
    }

    fun getAllMsgList():ArrayList<UIMessage>{
        return allMsg
    }

    fun getAllImgMsgList():ArrayList<Message>{
        return allImgMsgList
    }


    /*private fun getBaseReceiveMessage(msgType: MsgType, serverMsg:Message ): UIMessage {
        var mMessage = UIMessage()
        mMessage.uuid = serverMsg.serverMessageId.toString()
        mMessage.senderId = serverMsg.fromUser.userID.toString()
        mMessage.targetId = ChatActivity.mSenderId
        mMessage.sentTime = System.currentTimeMillis()
        mMessage.sentStatus = MsgSendStatus.SENDING
        mMessage.msgType = msgType
        mMessage.serverMessage = serverMsg
        return mMessage
    }

    private fun getBaseSendMessage(msgType: MsgType): UIMessage {
        var mMessage = UIMessage()
        mMessage.uuid = UUID.randomUUID().toString()
        mMessage.senderId = ChatActivity.mSenderId
        mMessage.targetId = ChatActivity.mTargetId
        mMessage.sentTime = System.currentTimeMillis()
        mMessage.sentStatus = MsgSendStatus.SENDING
        mMessage.msgType = msgType
        return mMessage
    }*/

    private fun getBaseMessage(msgType: MsgType, serverMsg:Message ): UIMessage {
        var mMessage = UIMessage()
        mMessage.uuid = serverMsg.serverMessageId.toString()
        mMessage.msgId = serverMsg.serverMessageId.toString()
        mMessage.senderId = serverMsg.fromUser.userID.toString()
        mMessage.targetId = ChatActivity.mSenderId
        mMessage.sentTime = System.currentTimeMillis()
        mMessage.sentStatus = MsgSendStatus.SENT
        mMessage.msgType = msgType
        mMessage.serverMessage = serverMsg
        return mMessage
    }

}
//处理每一条信息
/*var mUIMessage:UIMessage? = null
msgs.forEach {
    when(it.contentType){
        //文本消息
        ContentType.text -> {

        }
        //图片消息
        ContentType.image -> {
            mUIMessage = if (it.fromUser.userID == Constants.userInfo.userID) {
                getBaseSendMessage(MsgType.IMAGE)
            } else {
                getBaseReceiveMessage(MsgType.IMAGE)
            }
            val mImageMsgBody = ImageMsgBody()//"http://pic19.nipic.com/20120323/9248108_173720311160_2.jpg"
            mImageMsgBody.thumbPath = (it.content as ImageContent).localThumbnailPath
            mImageMsgBody.localPath = (it.content as ImageContent).localPath
            mUIMessage!!.body = mImageMsgBody
            imgMsgList.add(it)
        }
        //视频消息
        ContentType.video -> {
            mUIMessage = if (it.fromUser.userID == Constants.userInfo.userID){
                getBaseSendMessage(MsgType.VIDEO)
            } else {
                getBaseReceiveMessage(MsgType.VIDEO)
            }
            val mVideoMsgBody = VideoMsgBody()
            mVideoMsgBody.extra = (it.content as VideoContent).thumbLocalPath
            mVideoMsgBody.localPath = (it.content as VideoContent).videoLocalPath
            mVideoMsgBody.duration = (it.content as VideoContent).duration.toLong()
            (it.content as VideoContent).downloadThumbImage(it, object : DownloadCompletionCallback(){
                override fun onComplete(p0: Int, p1: String?, p2: File?) {
                    val msg = handler.obtainMessage()
                    val arrayMap = HashMap<String, String>()
                    msg.what = DOWNLOAD_VIDEO_IMAGE
                }
            })
            mUIMessage!!.body = mVideoMsgBody
        }
        //音频消息
        ContentType.voice -> {

        }
        //文件消息
        ContentType.file -> {}
        //位置消息
        ContentType.location -> {}
        else -> {
            LogUtil.d("aa")
        }
    }
}*/