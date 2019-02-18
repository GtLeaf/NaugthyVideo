package com.example.pc_0775.naugthyvideo.view.ActitivtyIM.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Picture
import android.graphics.drawable.AnimationDrawable
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivityKotlin
import com.example.pc_0775.naugthyvideo.view.ActitivtyIM.adapter.ChatAdapter
import com.example.pc_0775.naugthyvideo.view.ActitivtyIM.bean.*
import com.example.pc_0775.naugthyvideo.view.ActitivtyIM.util.ChatUiHelper
import com.example.pc_0775.naugthyvideo.view.ActitivtyIM.util.LogUtil
import com.example.pc_0775.naugthyvideo.view.ActitivtyIM.util.PictureFileUtil
import com.example.pc_0775.naugthyvideo.view.ActitivtyIM.widget.MediaManager
import android.media.MediaPlayer
import android.os.Environment
import com.example.pc_0775.naugthyvideo.view.ActitivtyIM.util.FileUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_chat.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Created by PC-0775 on 2019/2/12.
 */
class ChatActivity :BaseActivityKotlin(), SwipeRefreshLayout.OnRefreshListener {



    private var ivAudio:ImageView? = null
    private var mAdapter:ChatAdapter? = null


    companion object{
        val mSenderId = "right"
        val mTargetId = "left"
        val REQUEST_CODE_IMAGE = 1000
        val REQUEST_CODE_VEDIO = 2000
        val REQUEST_CODE_FILE = 3000
    }

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
        initChatUi()
        mAdapter!!.setOnItemChildClickListener{ adapter, view, position ->
            if (ivAudio != null){
                ivAudio!!.setBackgroundResource(R.mipmap.audio_animation_list_right_3)
                ivAudio = null
                MediaManager.reset()
            }else{
                //是哪个xml中的iv_audio??
                ivAudio = view.findViewById(R.id.iv_audio)
                MediaManager.reset()
                ivAudio!!.setBackgroundResource(R.drawable.audio_animation_right_list)
                var drawable = ivAudio!!.background as AnimationDrawable
                drawable.start()
                MediaManager.playSound(this@ChatActivity, (mAdapter!!.data.get(position).body as AudioMsgBody).localPath) {
                    LogUtil.d("播放结束")
                    ivAudio!!.setBackgroundResource(R.mipmap.audio_animation_list_right_3)
                    MediaManager.release()
                }
            }

        }
    }

    override fun setListener() {
    }

    override fun doBusiness(mContext: Context) {
    }

    override fun widgetClick(v: View) {
        when(v.id){
            R.id.btn_send -> {
                sendTextMessage(et_content.text.toString())
                et_content.setText("")
            }
            R.id.rl_photo -> {
                PictureFileUtil.openGalleryPic(this, REQUEST_CODE_IMAGE)
            }
            R.id.rl_video -> {
                PictureFileUtil.openGalleryAudio(this, REQUEST_CODE_VEDIO)
            }
            R.id.rl_file -> {
                PictureFileUtil.openFile(this, REQUEST_CODE_FILE)
            }
            R.id.rl_location -> {}
            else ->{}
        }
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
        //构建文件消息
        var mMessageFile = getBaseReceiveMessage(MsgType.FILE)
        var mFileMsgBody = FileMsgBody()
        mFileMsgBody.displayName = "收到的文件"
        mFileMsgBody.size = 12
        mMessageFile.body = mFileMsgBody
        mReceiveMsgList.add(mMessageFile)
        mAdapter!!.addData(mMessageFile)
        swipe_chat.isRefreshing = false

    }

    private fun initChatUi(){
        //mBtnAudio
        val mUiHelper = ChatUiHelper.with(this)
        mUiHelper.bindContentLayout(ll_content)
                .bindttToSendButton(btn_send)
                .bindEditText(et_content)
                .bindBottomLayout(rl_bottom_layout)
                .bindEmojiLayout(ll_emotion as LinearLayout)
                .bindAddLayout(llAdd as LinearLayout)
                .bindToAddButton(iv_add)
                .bindToEmojiButton(iv_emoji)
                .bindAudioBtn(btn_audio)
                .bindAudioIv(iv_audio)
                .bindEmojiData()
        //底部布局弹出,聊天列表上滑
        rv_chat_list.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom){
                rv_chat_list.post(object :Runnable{ //为什么要开线程？
                    override fun run() {
                        if (mAdapter!!.itemCount > 0){
                            rv_chat_list.smoothScrollToPosition(mAdapter!!.itemCount - 1)
                        }
                    }

                })
            }
        }
        //点击空白区域关闭键盘
        rv_chat_list.setOnTouchListener { v, event ->
            mUiHelper.hideBottomLayout(false)
            mUiHelper.hideSoftInput()
            et_content.clearFocus()
            iv_emoji.setImageResource(R.mipmap.ic_emoji)
            false
        }
        //
        btn_audio.setOnFinishedRecordListener{ audioPath, time ->  
            LogUtil.d("录音结束回调")
            var file = File(audioPath)
            if (file.exists()){
                sendAudioMessage(audioPath, time)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_FILE -> {
                    //文件选择结果回调
                    var filePath = data!!.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                    LogUtil.d("获取到的文件路径:" + filePath)
                    sendFileMessage(mSenderId, mTargetId, filePath)
                }
                REQUEST_CODE_IMAGE -> {
                    // 图片选择结果回调
                    var selectListPic = PictureSelector.obtainMultipleResult(data)
                    /*for ( media in selectListPic){
                        LogUtil.d("获取图片路径成功:" + media.path)
                        sendImageMessage(media)
                    }*/
                    selectListPic.forEach {
                        LogUtil.d("获取图片路径成功:" + it.path)
                        sendImageMessage(it)
                    }

                }
                REQUEST_CODE_VEDIO -> {
                    // 视频选择结果回调
                    var selectListVideo = PictureSelector.obtainMultipleResult(data)
                    selectListVideo.forEach {
                        LogUtil.d("获取视频路径成功:" + it.getPath())
                        sendVedioMessage(it)
                    }
                }
                else -> {}
            }
        }
    }

    //发送文本消息
    private fun sendTextMessage(text:String){
        var mMessage = getBaseSendMessage(MsgType.TEXT)
        var mTextMsgBody = TextMsgBody()
        mTextMsgBody.message = text
        //开始发送
        mAdapter!!.addData(mMessage)
        //模拟两秒后发送成功
        updateMsg(mMessage)
    }

    //发送音频文件
    private fun sendAudioMessage(path:String, time:Int){
        val mMessage = getBaseSendMessage(MsgType.AUDIO)
        var mFileMsgBody = AudioMsgBody()
        mFileMsgBody.localPath = path
        mFileMsgBody.duration = time.toLong()
        mMessage.body = mFileMsgBody
        //开始发送
        mAdapter!!.addData(mMessage)
        //模拟两秒后发送成功
        updateMsg(mMessage)
    }

    //发送文件消息
    private fun sendFileMessage(from:String, to:String, path: String){
        val mMessage = getBaseSendMessage(MsgType.FILE)
        val mFileMsgBody = FileMsgBody()
        mFileMsgBody.localPath = path
        mFileMsgBody.displayName = FileUtils.getFileName(path)
        mFileMsgBody.size = FileUtils.getFileLength(path)
        mMessage.body = mFileMsgBody
        mAdapter!!.addData(mMessage)
        updateMsg(mMessage)
    }

    //图片消息
    private fun sendImageMessage(media:LocalMedia){
        val mMessage = getBaseSendMessage(MsgType.IMAGE)
        val mImageMsgBody = ImageMsgBody()
        mImageMsgBody.thumbUrl = media.compressPath
        mMessage.body = mImageMsgBody
        //开始发送
        mAdapter!!.addData(mMessage)
        //模拟两秒后发送成功
        updateMsg(mMessage)
    }

    //视频消息
    private fun sendVedioMessage(media:LocalMedia){
        val mMessage = getBaseSendMessage(MsgType.VIDEO)
        //生成缩略图路径
        var vedioPath = media.path
        var mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(vedioPath)
        var bitmap = mediaMetadataRetriever.frameAtTime
        var imgName = System.currentTimeMillis().toString() + ".jpg"
        //路径拼接??
        var urlPath = Environment.getExternalStorageDirectory().path + "/" + imgName
        var f = File(urlPath)
        try {
            if (f.exists()){
                f.delete()
            }
            var out = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
        }catch (e:Exception){
            LogUtil.d("视频缩略图路径获取失败：" + e.toString())
            e.printStackTrace()
        }
        var mVideoMsgBody = VideoMsgBody()
        mVideoMsgBody.extra = urlPath
        mMessage.body = mVideoMsgBody
        //开始发送
        mAdapter!!.addData(mMessage)
        //模拟两秒后发送成功
        updateMsg(mMessage)
    }

    private fun getBaseSendMessage(msgType: MsgType):Message{
        var mMessage = Message()
        mMessage.uuid = UUID.randomUUID().toString()
        mMessage.senderId = mSenderId
        mMessage.targetId = mTargetId
        mMessage.sentTime = System.currentTimeMillis()
        mMessage.sentStatus = MsgSendStatus.SENDING
        mMessage.msgType = msgType
        return mMessage
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

    private fun updateMsg(mMessage:Message){
        rv_chat_list.scrollToPosition(mAdapter!!.itemCount - 1)
        //模拟2秒后发送成
        Handler().postDelayed({
            var position = 0
            mMessage.sentStatus = MsgSendStatus.SENT
            //更新单个子条目
            for (i in 0 until mAdapter!!.data.size) {
                val mAdapterMessage = mAdapter!!.data[i]
                if (mMessage.uuid == mAdapterMessage.uuid) {
                    position = i
                }
            }
            mAdapter!!.notifyItemChanged(position)

        }, 2000)
    }
}