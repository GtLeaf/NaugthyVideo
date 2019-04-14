package com.example.pc_0775.naugthyvideo.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import com.example.pc_0775.naugthyvideo.Constants.Constants
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.ui.base.BaseActivity
import com.example.pc_0775.naugthyvideo.other.media.AndroidMediaController
import kotlinx.android.synthetic.main.activity_ijk_live_play.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer


class ActivityIjkLivePlay : BaseActivity() {
    //后续增加代码
    private val SIZE_DEFAULT = 0
    private val SIZE_4_3 = 1
    private val SIZE_16_9 = 2
    private var currentSize = SIZE_16_9
//    private val video: IjkVideoView? = null
    private var seekBar: SeekBar? = null
    var screenWidth:Int = 0
    var screenHeight:Int = 0

    var path:String = ""
    var mMediaContronller:AndroidMediaController? = null
    //用户是否按下返回
    private var isBackPressed = false

    override fun initParams(params: Bundle?) {
        path = params!!.getString(Constants.INTENT_VIDEO_URL)

    }

    override fun bindLayout(): Int {
        return R.layout.activity_ijk_live_play;
    }

    override fun bindView(): View? {
        return null;
    }

    override fun initView(view: View?) {
        /*IjkVV_live_play.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
        IjkVV_live_play.setVideoPath(path)
        IjkVV_live_play.start()*/
        //初始化播放器
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        //初始化ijkVideoView
        seekBar = SeekBar(this)
        mMediaContronller = AndroidMediaController(this, false)
        IjkVV_live_play.setMediaController(mMediaContronller)
        IjkVV_live_play.setMediaController(mMediaContronller)
        IjkVV_live_play.setVideoPath(path)
        IjkVV_live_play.start()

    }

    override fun setListener() {
        seekBar!!.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                IjkVV_live_play.seekTo(seekBar!!.progress*IjkVV_live_play.duration/1000)
            }
        })
        btn_ijk_change_ratio.setOnClickListener { IjkVV_live_play.toggleAspectRatio() }
    }

    override fun widgetClick(v: View?) {
    }

    override fun doBusiness(mContext: Context?) {
    }

    override fun onStop() {
        super.onStop()

        if(isBackPressed || IjkVV_live_play.isBackgroundPlayEnabled){
            IjkVV_live_play.stopPlayback()
            IjkVV_live_play.release(true)
            IjkVV_live_play.stopBackgroundPlay()
        }else{
            IjkVV_live_play.enterBackground()
        }
        IjkMediaPlayer.native_profileEnd()

    }



    override fun onBackPressed() {
        isBackPressed = true
        super.onBackPressed()
    }

    //获取屏幕高度
    fun initScreenInfo(){
        var metric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)
        screenWidth = metric.widthPixels
        screenHeight = metric.heightPixels
    }

    //设置视频播放比例
    fun setScreenRate(rate: Int) {
        initScreenInfo()
        var width = 0
        var height = 0
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {//横屏
            if (rate == SIZE_DEFAULT) {
                width = IjkVV_live_play.getmVideoWidth()
                height = IjkVV_live_play.getmVideoHeight()
            }else if (rate == SIZE_4_3) {
                width = screenHeight / 3 * 4
                height = screenHeight
            }else if (rate == SIZE_16_9) {
                width = screenHeight / 9 * 16
                height = screenHeight
            }
        }else{//竖屏
            if (rate == SIZE_DEFAULT) {
                width = IjkVV_live_play.getmVideoWidth()
                height = IjkVV_live_play.getmVideoHeight()
            }else if (rate == SIZE_4_3) {
                width = screenWidth / 3 * 4
                height = screenWidth
            }else if (rate == SIZE_16_9) {
                width = screenWidth / 9 * 16
                height = screenWidth
            }
        }
        if (width>0 && height>0){
            val lp = IjkVV_live_play.getmRenderView().view.layoutParams as FrameLayout.LayoutParams
            lp.width = width
            lp.height = height
            IjkVV_live_play.getmRenderView().view.layoutParams = lp
        }
    }

    //屏幕方向切换
    fun fullChangeScreen(){
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){//切换为竖屏
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    //全屏播放
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        //重新获取屏幕宽高
        /*initScreenInfo();
        if (newConfig!!.orientation == Configuration.ORIENTATION_LANDSCAPE){//切换为横屏
            var lp = IjkVV_live_play.layoutAnimation as LinearLayout.LayoutParams
            lp.height = screenHeight
            lp.width = screenWidth
            IjkVV_live_play.layoutParams = lp
        }else{
            var lp = IjkVV_live_play.layoutAnimation as LinearLayout.LayoutParams
            lp.height = screenWidth * 9/16
            lp.width = screenWidth
            IjkVV_live_play.layoutParams = lp
        }*/
        //setScreenRate(currentSize)
    }

    //播放进度 视频开始播放时使用handle.sendMessageDelayed更新时间显示
    private fun refreshTime(){
        var totalSeconds = IjkVV_live_play.currentPosition/1000
        var seconds = totalSeconds%60
        var minutes = (totalSeconds/60)%60
        var hours = totalSeconds/3600
        val ti = if (hours > 0) String.format("%02d:%02d:%02d", hours, minutes, seconds) else String.format("%02d:%02d", minutes, seconds)
    }

    companion object {
        fun actionStart(context: Context, viedoUrl: String) {
            val intent = Intent(context, ActivityIjkLivePlay::class.java)
            val bundle = Bundle()
            bundle.putString(Constants.INTENT_VIDEO_URL, viedoUrl)
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

}
