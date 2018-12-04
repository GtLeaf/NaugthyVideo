package com.example.pc_0775.naugthyvideo.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.pc_0775.naugthyvideo.Constants.Constants
import com.example.pc_0775.naugthyvideo.MyApplication
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivity
import com.example.pc_0775.naugthyvideo.media.IRenderView
import kotlinx.android.synthetic.main.activity_ijk_live_play.*


class ActivityIjkLivePlay : BaseActivity() {

    var path:String = ""

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
        IjkVV_live_play.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
        IjkVV_live_play.setVideoPath(path)
        IjkVV_live_play.start()
    }

    override fun setListener() {
    }

    override fun widgetClick(v: View?) {
    }

    override fun doBusiness(mContext: Context?) {
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
