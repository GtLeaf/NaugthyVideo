package com.example.pc_0775.naugthyvideo.view.ActitivtyIM.activity

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivityKotlin
import com.example.pc_0775.naugthyvideo.view.ActitivtyIM.adapter.ChatAdapter
import com.example.pc_0775.naugthyvideo.view.ActitivtyIM.bean.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

/**
 * Created by PC-0775 on 2019/2/12.
 */
class ChatActvity:BaseActivityKotlin(), SwipeRefreshLayout.OnRefreshListener {

    val mSenderId = "right"
    val mTargetId = "left"
    val REQUEST_CODE_IMAGE = 1000
    val REQUEST_CODE_VEDIO = 2000
    val REQUEST_CODE_FILE = 3000

    private var ivAudio:ImageView? = null
    private var mAdapter:ChatAdapter? = null


    override fun initParams(params: Bundle?) {
    }

    override fun bindLayout(): Int {
        return R.layout.activity_chat
    }

    override fun initView(view: View) {
        mAdapter = ChatAdapter(this, ArrayList<Message>())
        rv_chat_list.layoutManager = LinearLayoutManager(this)
        rv_chat_list.adapter = mAdapter
        swipe_chat.setOnRefreshListener(this)
    }

    override fun setListener() {
    }

    override fun doBusiness(mContext: Context) {
    }

    override fun widgetClick(v: View) {
    }

    override fun onRefresh() {
        //下拉刷新模拟获取历史消息
        var mReceiveMsgList = ArrayList<Message>()
        //构建文本消息
        var mMessageText = getBaseReceiveMessage(MsgType.TEXT)
        var mTextMsgBody = TextMsgBody()
        mTextMsgBody.message = "收到的消息"
        mMessageText.body = mTextMsgBody
        mReceiveMsgList.add(mMessageText)
        //构建图片消息
        var mMessageImage = getBaseReceiveMessage(MsgType.IMAGE)
        var mImageMsgBody = ImageMsgBody()
        mImageMsgBody.thumbUrl = "http://pic19.nipic.com/20120323/9248108_173720311160_2.jpg"
        mMessageImage.body = mImageMsgBody
        mReceiveMsgList.add(mMessageImage)
    }

    private fun getBaseReceiveMessage(msgType: MsgType):Message{
        var mMessage = Message()
        mMessage.uuid = UUID.randomUUID().toString()
        mMessage.senderId = mTargetId
        mMessage.targetId = mSenderId
        mMessage.sentTime = System.currentTimeMillis()
        mMessage.sentStatus = MsgSendStatus.SENDING
        mMessage.msgType = msgType
        return mMessage
    }
}