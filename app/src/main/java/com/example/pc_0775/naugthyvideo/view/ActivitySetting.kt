package com.example.pc_0775.naugthyvideo.view

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import com.example.pc_0775.naugthyvideo.Constants.Constants
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_setting.*

class ActivitySetting : BaseActivity() {



    override fun initParams(params: Bundle?) {

    }

    override fun bindLayout(): Int {
        return R.layout.activity_setting;
    }

    override fun bindView(): View? {
        return null;
    }

    override fun initView(view: View?) {
        setSupportActionBar(tb_setting);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("设置")

        if (1 == Constants.PLAY_MODE){
            switch_play_mode.isChecked = true
        }

        if (Constants.DOWNLOAD_AT_THE_SAME_TIME){
            switch_download_at_the_same_time.isChecked = true
        }
    }

    override fun setListener() {
        switch_play_mode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener{
            buttonView, isChecked ->

            Constants.PLAY_MODE = 0
            tv_setting_play_mode.text = "播放：网页模式"
            if (isChecked){
                Constants.PLAY_MODE = 1
                tv_setting_play_mode.text = "播放：播放器模式"
            }
        })

        switch_download_at_the_same_time.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener{
            buttonView, isChecked ->

            Constants.DOWNLOAD_AT_THE_SAME_TIME = false
            if (isChecked){
                Constants.DOWNLOAD_AT_THE_SAME_TIME = true
            }
        })
    }

    override fun widgetClick(v: View?) {
    }

    override fun doBusiness(mContext: Context?) {
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {}
        }

        return super.onOptionsItemSelected(item)
    }
}
