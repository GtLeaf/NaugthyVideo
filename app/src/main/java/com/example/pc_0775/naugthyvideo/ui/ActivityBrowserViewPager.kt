package com.example.pc_0775.naugthyvideo.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cn.jpush.im.android.api.callback.DownloadCompletionCallback
import cn.jpush.im.android.api.callback.ProgressUpdateCallback
import cn.jpush.im.android.api.content.ImageContent
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.Message
import com.bumptech.glide.Glide
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivityKotlin
import com.example.pc_0775.naugthyvideo.util.BitmapLoader
import com.example.pc_0775.naugthyvideo.util.LogUtil
import com.example.pc_0775.naugthyvideo.util.NativeImageLoader
import com.example.pc_0775.naugthyvideo.view.PhotoView
import kotlinx.android.synthetic.main.activity_browser_view_pager.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.lang.ref.WeakReference
import java.util.ArrayList

class ActivityBrowserViewPager : BaseActivityKotlin() {

    private lateinit var photoView:PhotoView
    private var mWidth = 0
    private var mHeight = 0
    private var msgID:Long = 0
    private var currentItem:Int = 0
    private lateinit var mMsg:Message

    //存放所有图片的路径
    private var mPathList: MutableList<String> = ArrayList()
    private var mImgMsgList = ArrayList<Message>()

    companion object {
        val BROWSER_AVATAR = "browserAvatar"
        val FROM_CHAT_ACTIVITY = "fromChatActivity"
        private val IMG_MSG_LIST = "imgMsgList"
        private val MESSAGE_ID = "messageID"

        fun actionStart(context: Context, msgID:Long) {
            val intent = Intent(context, ActivityBrowserViewPager::class.java)
            val bundle = Bundle()
            bundle.putLong(MESSAGE_ID, msgID)
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun initParams(params: Bundle?) {
        msgID = intent.extras.getLong(MESSAGE_ID)
    }

    override fun bindLayout(): Int {
        return R.layout.activity_browser_view_pager
    }

    override fun initView(view: View) {
        //注册evenBus
        EventBus.getDefault().register(this)

    }

    override fun setListener() {
        img_browser_viewpager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                LogUtil.d("ViewPager", "滑动中")
            }

            override fun onPageSelected(position: Int) {
                LogUtil.d("ViewPager", "滑动后")
//                downloadImage(mImgMsgList[position])
            }
        })
    }

    override fun doBusiness(mContext: Context) {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        mWidth = dm.widthPixels
        mHeight = dm.heightPixels
    }

    override fun widgetClick(v: View) {
    }

    private var pagerAdapter = object :PagerAdapter(){
        override fun getCount(): Int {
            return mPathList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        /**
         * 点击某张图片预览时，系统自动调用此方法加载这张图片左右视图（如果有的话）
         */
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            photoView = PhotoView(true, container.context)
            photoView.tag = position
            var msg = mImgMsgList[position]
            var path:String? = mPathList[position]
            if (path != null){
                val file = File(path)
                if (file.exists()){
                    val bitmap = BitmapLoader.getBitmapFromFile(path, mWidth, mHeight)
                    if (bitmap != null) {
                        photoView.maxScale = 9.toFloat()
                        photoView.setImageBitmap(bitmap)
                    } else {
                        photoView.setImageResource(R.mipmap.default_img_failed)
                    }
                }else{
                    val bitmap = NativeImageLoader.getInstance().getBitmapFromMemCache(path)
                    if (bitmap != null) {
                        photoView.maxScale = 9.toFloat()
                        photoView.setImageBitmap(bitmap)
                    } else {
                        photoView.setImageResource(R.mipmap.default_img_failed)
                    }
                }
            }else{
                photoView.setImageResource(R.mipmap.default_img_failed)
            }
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            /*Thread{
                Thread.sleep(3000)
                LogUtil.i("photoView","开始更换图片")
                runOnUiThread{
                    if (position == img_browser_viewpager.currentItem)
                    {
                        photoView.setImageResource(R.mipmap.default_img_failed)
                    }

                }
                LogUtil.i("photoView","更换结束")
            }.start()*/
            //图片长按保存到手机
            onImageViewFound(photoView, path!!)
            return photoView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    private fun onImageViewFound(photoView: PhotoView, path:String){
        photoView.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                //保存图片或者转发
                savePicture(path)


                return false
            }
        })
    }

    private fun savePicture(path:String){

    }

    /**
     * 初始化会话中的所有图片路径
     */
    private fun initImgPathList(){
        mImgMsgList.map { it.content as ImageContent }
                .forEach {
                    if (!TextUtils.isEmpty(it.localPath)){
                        mPathList.add(it.localPath)
                    }else{
                        mPathList.add(it.localThumbnailPath)
                    }
                }
    }

    private fun initCurrentItem(){
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            Toast.makeText(this, this.getString(R.string.jmui_local_picture_not_found_toast), Toast.LENGTH_SHORT).show()
        }
        val ic = mMsg.content as ImageContent
        //如果点击的是第一张图片并且图片未下载过，则显示大图//&& currentItem == mImgMsgList.size-1
        if (null == ic.localPath ){
//            downloadImage(mMsg)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun event(imgMsgList: ArrayList<Message>){
        mImgMsgList = imgMsgList
        initImgPathList()
        img_browser_viewpager.adapter = pagerAdapter
        mMsg = mImgMsgList.find { it.serverMessageId == msgID }!!
        currentItem = mImgMsgList.indexOf(mMsg)
        img_browser_viewpager.currentItem = currentItem
        initCurrentItem()
    }

    private fun downloadImage(mMsg:Message){
        val imgContent = mMsg.content as ImageContent
        //如果不存在进度条Callback，重新注册
        if (!mMsg.isContentDownloadProgressCallbackExists){
            //显示下载进度条
            mMsg.setOnContentDownloadProgressCallback(object :ProgressUpdateCallback(){
                override fun onProgressUpdate(p0: Double) {
                    if (p0<1.0){
                        photoView.setPer(p0.toFloat())
                    }else{
                        photoView.finish()
                    }
                }
            })

            imgContent.downloadOriginImage(mMsg, object :DownloadCompletionCallback(){
                override fun onComplete(p0: Int, p1: String?, p2: File?) {
                    //此处处理查看原图按钮的消失
                    if (0 == p0){
                        mPathList[currentItem] = p2!!.absolutePath
                        img_browser_viewpager.adapter!!.notifyDataSetChanged()
                        photoView.finish()
                    }
                }
            })
        }
    }
}
