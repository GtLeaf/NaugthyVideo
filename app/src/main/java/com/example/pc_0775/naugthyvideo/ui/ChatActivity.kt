package com.example.pc_0775.naugthyvideo.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import com.example.pc_0775.naugthyvideo.ui.base.BaseActivityKotlin
import com.example.pc_0775.naugthyvideo.ui.ActitivtyIM.adapter.ChatAdapter
import com.example.pc_0775.naugthyvideo.ui.ActitivtyIM.util.ChatUiHelper
import com.example.pc_0775.naugthyvideo.ui.ActitivtyIM.util.LogUtil
import com.example.pc_0775.naugthyvideo.ui.ActitivtyIM.util.PictureFileUtil
import com.example.pc_0775.naugthyvideo.ui.ActitivtyIM.widget.MediaManager
import android.os.Environment
import cn.jpush.im.android.api.ChatRoomManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.event.ChatRoomMessageEvent
import cn.jpush.im.android.api.model.ChatRoomInfo
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.Message
import cn.jpush.im.api.BasicCallback
import com.example.pc_0775.naugthyvideo.Constants.Constants
import com.example.pc_0775.naugthyvideo.viewModel.ChatMsgModel
import com.example.pc_0775.naugthyvideo.model.bean.UIMessage.*
import com.example.pc_0775.naugthyvideo.ui.ActitivtyIM.util.FileUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.common_titlebar.*
import kotlinx.android.synthetic.main.include_add_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by PC-0775 on 2019/2/12.
 */
class ChatActivity : BaseActivityKotlin(), SwipeRefreshLayout.OnRefreshListener {

    private var ivAudio: ImageView? = null
    private var mAdapter: ChatAdapter? = null
    private var currentRoom: ChatRoomInfo? = null
    private var imgMsgList = ArrayList<Message>()
    private var handler = ChatActivityHandler(this)
    private var chatMsgModel = ChatMsgModel()

    companion object {
        val mSenderId = "right"
        val mTargetId = "left"
        val REQUEST_CODE_IMAGE = 1000
        val REQUEST_CODE_VEDIO = 2000
        val REQUEST_CODE_FILE = 3000
        val DOWNLOAD_VIDEO_IMAGE = 4000

        fun actionStart(context: Context, roomID:Long){
            val intent = Intent(context, ChatActivity::class.java)
            val bundle = Bundle()
            bundle.putLong("roomID" ,roomID)
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        class ChatActivityHandler(activity: ChatActivity) : Handler(){
            var activity = WeakReference<ChatActivity>(activity)

            override fun handleMessage(msg: android.os.Message?) {
                when(msg!!.what){
                    DOWNLOAD_VIDEO_IMAGE->{

                    }
                    else ->{
                    }
                }
            }

        }
    }

    override fun initParams(params: Bundle?) {

    }

    override fun bindLayout(): Int {
        return R.layout.activity_chat
    }

    override fun initView(view: View) {
        //注册evenBus
        EventBus.getDefault().register(this)

        //注册极光消息接收
        mAdapter = ChatAdapter(this, ArrayList<UIMessage>())
        rv_chat_list.layoutManager = LinearLayoutManager(this)
        rv_chat_list.adapter = mAdapter


    }

    override fun setListener() {
        btn_send.setOnClickListener(this)
        iv_common_title_bar_back.setOnClickListener(this)
        rl_photo.setOnClickListener(this)
        rl_video.setOnClickListener(this)
        rl_file.setOnClickListener(this)
        rl_location.setOnClickListener(this)
        swipe_chat.setOnRefreshListener(this)
        initChatUi()
        mAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            (adapter.getItem(position) as UIMessage).let {
                when(it.msgType){
                    MsgType.TEXT -> {}
                    MsgType.IMAGE -> {
                        if (it.sentStatus != MsgSendStatus.SENT) return@setOnItemChildClickListener
                        EventBus.getDefault().postSticky(chatMsgModel.getAllImgMsgList())
                        ActivityBrowserViewPager.actionStart(this@ChatActivity, it.msgId.toLong())
                    }
                    MsgType.AUDIO -> {
                        if (ivAudio != null) {  //如果正在播放中点击，停止播放音频
                            ivAudio!!.setBackgroundResource(R.mipmap.audio_animation_list_right_3)
                            ivAudio = null
                            MediaManager.reset()
                        } else {
                            //是哪个xml中的iv_audio??
                            ivAudio = view.findViewById(R.id.iv_audio)
                            MediaManager.reset()
                            ivAudio!!.setBackgroundResource(R.drawable.audio_animation_right_list)
                            val drawable = ivAudio!!.background as AnimationDrawable
                            drawable.start()
                            MediaManager.playSound(this@ChatActivity, (mAdapter!!.data.get(position).body as AudioMsgBody).localPath) {
                                LogUtil.d("播放结束")
                                ivAudio!!.setBackgroundResource(R.mipmap.audio_animation_list_right_3)
                                MediaManager.release()
                            }
                        }
                    }
                    MsgType.VIDEO -> {
                        ActivityLivePlay.actionStart(this, (it.body as VideoMsgBody).localPath)
                    }
                    MsgType.FILE -> {}
                    MsgType.LOCATION -> {}
                    else -> {}
                }
            }
        }
    }

    override fun doBusiness(mContext: Context) {

    }

    override fun onDestroy() {
        JMessageClient.exitConversation()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    override fun widgetClick(v: View) {
        when (v.id) {
            R.id.btn_send -> {
                sendTextMessage(et_content.text.toString())
                et_content.setText("")
                rv_chat_list.scrollToPosition(mAdapter!!.itemCount - 1)
            }
            R.id.rl_photo -> {
                PictureFileUtil.openGalleryPic(this, REQUEST_CODE_IMAGE)
                rv_chat_list.scrollToPosition(mAdapter!!.itemCount - 1)
            }
            R.id.rl_video -> {
                PictureFileUtil.openGalleryAudio(this, REQUEST_CODE_VEDIO)
                rv_chat_list.scrollToPosition(mAdapter!!.itemCount - 1)
            }
            R.id.rl_file -> {
                PictureFileUtil.openFile(this, REQUEST_CODE_FILE)
                rv_chat_list.scrollToPosition(mAdapter!!.itemCount - 1)
            }
            R.id.rl_location -> {
            }
            R.id.iv_common_title_bar_back -> {
                onBackPressed()
            }
            else -> {
            }
        }
    }

    override fun onRefresh() {
        //下拉刷新模拟获取历史消息
        val mReceiveMsgList = ArrayList<UIMessage>()
        //构建文本消息
        val mMessageText = getBaseReceiveMessage(MsgType.TEXT)
        val mTextMsgBody = TextMsgBody()
        mTextMsgBody.message = "收到的消息"
        mMessageText.body = mTextMsgBody
        mReceiveMsgList.add(mMessageText)
        //构建图片消息
        val mMessageImage = getBaseReceiveMessage(MsgType.IMAGE)
        val mImageMsgBody = ImageMsgBody()
        mImageMsgBody.thumbUrl = "http://pic19.nipic.com/20120323/9248108_173720311160_2.jpg"
        mMessageImage.body = mImageMsgBody
        mReceiveMsgList.add(mMessageImage)
        //构建文件消息
        val mMessageFile = getBaseReceiveMessage(MsgType.FILE)
        val mFileMsgBody = FileMsgBody()
        mFileMsgBody.displayName = "收到的文件"
        mFileMsgBody.size = 12
        mMessageFile.body = mFileMsgBody
        mReceiveMsgList.add(mMessageFile)
        mAdapter!!.addData(0, mReceiveMsgList)
        swipe_chat.isRefreshing = false

    }

    private fun initChatUi() {
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
            if (bottom < oldBottom) {
                rv_chat_list.post {
                    if (mAdapter!!.itemCount > 0) {
                        rv_chat_list.smoothScrollToPosition(mAdapter!!.itemCount - 1)
                    }
                }
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
        btn_audio.setOnFinishedRecordListener { audioPath, time ->
            LogUtil.d("录音结束回调")
            val file = File(audioPath)
            if (file.exists()) {
                sendAudioMessage(audioPath, time)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_FILE -> {
                    //文件选择结果回调
                    val filePath = data!!.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                    LogUtil.d("获取到的文件路径:" + filePath)
                    sendFileMessage(mSenderId, mTargetId, filePath)
                }
                REQUEST_CODE_IMAGE -> {
                    // 图片选择结果回调
                    val selectListPic = PictureSelector.obtainMultipleResult(data)
                    selectListPic.forEach {
                        LogUtil.d("获取图片路径成功:" + it.path)
                        sendImageMessage(it)
                    }

                }
                REQUEST_CODE_VEDIO -> {
                    // 视频选择结果回调
                    val selectListVideo = PictureSelector.obtainMultipleResult(data)
                    selectListVideo.forEach {
                        LogUtil.d("获取视频路径成功:" + it.getPath())
                        sendVideoMessage(it)
                    }
                }
                else -> {
                }
            }
        }
    }

    //发送文本消息
    private fun sendTextMessage(text: String) {
        var mMessage = getBaseSendMessage(MsgType.TEXT)
        var mTextMsgBody = TextMsgBody()
        mTextMsgBody.message = text
        mMessage.body = mTextMsgBody
        //开始发送
        mAdapter!!.addData(mMessage)

        //将消息发上服务器
        var conv = JMessageClient.getChatRoomConversation(currentRoom!!.roomID)
        if (null == conv) {
            conv = Conversation.createChatRoomConversation(currentRoom!!.roomID)
        }
        val msg = conv.createSendTextMessage(text)
        msg.setOnSendCompleteCallback(object : BasicCallback() {
            override fun gotResult(p0: Int, p1: String?) {
                if (0 == p0) {
                    mMessage.sentStatus = MsgSendStatus.SENT

                } else {
                    showToast("消息发送失败")
                    mMessage.sentStatus = MsgSendStatus.FAILED
                }
                msgCompleteUpdate(mMessage)
            }
        })
        JMessageClient.sendMessage(msg)


        //模拟两秒后发送成功

//        updateMsg(mMessage)
    }

    //发送音频文件
    private fun sendAudioMessage(path: String, time: Int) {
        val mMessage = getBaseSendMessage(MsgType.AUDIO)
        val mFileMsgBody = AudioMsgBody()
        mFileMsgBody.localPath = path
        mFileMsgBody.duration = time.toLong()
        mMessage.body = mFileMsgBody
        //开始发送
        mAdapter!!.addData(mMessage)
        //极光发送音频到服务器
        try {
            val msg = JMessageClient.createGroupVoiceMessage(currentRoom!!.roomID, File(path),time)
            msg.setOnSendCompleteCallback(object :BasicCallback(){
                override fun gotResult(p0: Int, p1: String?) {
                    if (0 == p0) {
                        mMessage.sentStatus = MsgSendStatus.SENT
                    } else {
                        showToast("消息发送失败")
                        mMessage.sentStatus = MsgSendStatus.FAILED
                    }
                    msgCompleteUpdate(mMessage)
                }
            })
            JMessageClient.sendMessage(msg)
        }catch (e:FileNotFoundException){
            showToast("发送失败")
            return
        }
    }

    //发送文件消息
    private fun sendFileMessage(from: String, to: String, path: String) {
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
    private fun sendImageMessage(media: LocalMedia) {
        val mMessage = getBaseSendMessage(MsgType.IMAGE)
        val mImageMsgBody = ImageMsgBody()
        mImageMsgBody.thumbUrl = media.compressPath
        mMessage.body = mImageMsgBody
        //开始发送
        mAdapter!!.addData(mMessage)

        //极光发送图片到服务器
        try {
            val msg = JMessageClient.createGroupImageMessage(currentRoom!!.roomID, File(media.path))
            msg.setOnSendCompleteCallback(object :BasicCallback(){
                override fun gotResult(p0: Int, p1: String?) {
                    if (0 == p0) {
                        mMessage.sentStatus = MsgSendStatus.SENT

                    } else {
                        showToast("消息发送失败")
                        mMessage.sentStatus = MsgSendStatus.FAILED
                    }
                    msgCompleteUpdate(mMessage)
                }
            })
            JMessageClient.sendMessage(msg)
        }catch (e:Exception){
            showToast("发送失败，图片不存在")
            return
        }


    }

    //视频消息
    private fun sendVideoMessage(media: LocalMedia) {
        val mMessage = getBaseSendMessage(MsgType.VIDEO)
        //生成缩略图路径
        val videoPath = media.path
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(videoPath)
        val bitmap = mediaMetadataRetriever.frameAtTime
        val imgName = System.currentTimeMillis().toString() + ".jpg"
        //路径拼接
        val urlPath = Environment.getExternalStorageDirectory().path + "/" + imgName
        val f = File(urlPath)
        try {
            if (f.exists()) {
                f.delete()
            }
            val out = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            LogUtil.d("视频缩略图路径获取失败：" + e.toString())
            e.printStackTrace()
        }
        val mVideoMsgBody = VideoMsgBody()
        mVideoMsgBody.extra = urlPath
        mVideoMsgBody.localPath = videoPath
        mMessage.body = mVideoMsgBody

        val during = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        var JPmsg:Message
        try {
            //问题在JMessageClient
            JPmsg = JMessageClient.createGroupVideoMessage(currentRoom!!.roomID, bitmap, System.currentTimeMillis().toString(), File(videoPath), null,10)
            JPmsg.setOnSendCompleteCallback(object :BasicCallback(){
                override fun gotResult(p0: Int, p1: String?) {
                    if (0 == p0) {
                        mMessage.sentStatus = MsgSendStatus.SENT

                    } else {
                        showToast("消息发送失败")
                        mMessage.sentStatus = MsgSendStatus.FAILED
                    }
                    msgCompleteUpdate(mMessage)
                }
            })
            //开始发送
            JMessageClient.sendMessage(JPmsg)
            mAdapter!!.addData(mMessage)
        }catch (e:IOException){
            e.printStackTrace()
            showToast("消息发送失败,文件不存在")
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun getBaseSendMessage(msgType: MsgType): UIMessage {
        var mMessage = UIMessage()
        mMessage.uuid = UUID.randomUUID().toString()
        mMessage.senderId = Constants.userInfo.userID.toString()
        mMessage.targetId = mTargetId
        mMessage.sentTime = System.currentTimeMillis()
        mMessage.sentStatus = MsgSendStatus.SENDING
        mMessage.msgType = msgType
        return mMessage
    }

    private fun getBaseReceiveMessage(msgType: MsgType): UIMessage {
        var mMessage = UIMessage()
        mMessage.uuid = UUID.randomUUID().toString()
        mMessage.senderId = mTargetId
        mMessage.targetId = mSenderId
        mMessage.sentTime = System.currentTimeMillis()
        mMessage.sentStatus = MsgSendStatus.SENDING
        mMessage.msgType = msgType
        return mMessage
    }

    private fun updateMsg(mUIMessage: UIMessage) {
        rv_chat_list.scrollToPosition(mAdapter!!.itemCount - 1)
        //模拟2秒后发送成
        Handler().postDelayed({
            var position = 0
            mUIMessage.sentStatus = MsgSendStatus.SENT
            //更新单个子条目
            for (i in 0 until mAdapter!!.data.size) {
                val mAdapterMessage = mAdapter!!.data[i]
                if (mUIMessage.uuid == mAdapterMessage.uuid) {
                    position = i
                }
            }
            mAdapter!!.notifyItemChanged(position)

        }, 2000)
    }

    private fun msgCompleteUpdate(mUIMessage: UIMessage) {
        var position = 0
        //更新单个子条目
        for (i in 0 until mAdapter!!.data.size) {
            val mAdapterMessage = mAdapter!!.data[i]
            if (mUIMessage.uuid == mAdapterMessage.uuid) {
                position = i
            }
        }
        mAdapter!!.notifyItemChanged(position)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun event(chatRoomInfo: ChatRoomInfo) {
        currentRoom = chatRoomInfo
    }


    fun onEventMainThread(event: ChatRoomMessageEvent) {
        chatMsgModel.addChatRoomMsg(event)
        val mMessageText = getBaseReceiveMessage(MsgType.TEXT)

        mAdapter!!.addData( chatMsgModel.getMsgList())
        mAdapter!!.notifyDataSetChanged()
        rv_chat_list.scrollToPosition(mAdapter!!.itemCount - 1)



    }

    private fun onMyBackPressed() {
        //如果已经进入房间，就退出房间，否则直接结束activity
        if (null != currentRoom) {
            ChatRoomManager.leaveChatRoom(currentRoom!!.roomID, object : BasicCallback() {
                override fun gotResult(p0: Int, p1: String?) {

                }
            })
        }

    }

    override fun onBackPressed() {
        onMyBackPressed()
        super.onBackPressed()
    }

    fun mySetOnSendCompleteCallback(msg: Message){

    }
}