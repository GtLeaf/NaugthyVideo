package com.example.pc_0775.naugthyvideo.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import com.example.pc_0775.naugthyvideo.Constants.Constants
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivity
import com.example.pc_0775.naugthyvideo.util.SPUtils
import kotlinx.android.synthetic.main.activity_setting.*

class ActivitySetting : BaseActivity() {

    override fun initParams(params: Bundle?) {

    }

    override fun bindLayout(): Int {
        return R.layout.activity_setting;
    }

    override fun bindView(): View? {
        return null
    }

    override fun initView(view: View?) {
        setSupportActionBar(tb_setting);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "设置"

        if (1 == Constants.PLAY_MODE){
            switch_play_mode.isChecked = true
            setPlayMode(true)
        }else{
            switch_play_mode.isChecked = false
            setPlayMode(false)
        }

        if (1 == Constants.PLAYER_SELECT){
            switch_player_select.isChecked = true
            setPlayer(true)
        }else{
            switch_player_select.isChecked = false
            setPlayer(false)
        }

        switch_auto_login.isChecked = SPUtils.get(this, "isAutoLogin", false) as Boolean
        if (null == Constants.user){
            ll_setting_auto_login.visibility = View.GONE
        }
    }

    override fun setListener() {
        switch_play_mode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener{
            buttonView, isChecked ->

            setPlayMode(isChecked);
        })

        switch_player_select.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener{
            buttonView, isChecked ->

            setPlayer(isChecked)
        })

        switch_auto_login.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener{
            buttonView, isChecked ->

            setAutoLogin(isChecked)
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

    private fun setPlayMode(isChecked:Boolean){
        if (isChecked){
            Constants.PLAY_MODE = 1
            tv_setting_play_mode.text = "播放：播放器模式"
            view_setting_player_line.visibility = View.VISIBLE
            ll_setting_player.visibility = View.VISIBLE
        }else{
            Constants.PLAY_MODE = 0
            tv_setting_play_mode.text = "播放：网页模式"
            view_setting_player_line.visibility = View.GONE
            ll_setting_player.visibility = View.GONE
        }
    }

    private fun setPlayer(isChecked: Boolean){
        if (isChecked){
            Constants.PLAYER_SELECT = 1
            tv_setting_player_name.text = getString(R.string.ijk_player)
        }else{
            Constants.PLAYER_SELECT = 0
            tv_setting_player_name.text = getString(R.string.vitamio_player)
        }
    }

    private fun setAutoLogin(isAutoLogin:Boolean){
        SPUtils.put(this, "isAutoLogin", isAutoLogin)
    }

    /*private fun initPreferences(){
        if (null == preferences){
            preferences = PreferenceManager.getDefaultSharedPreferences(this)
            editor = preferences!!.edit()
        }
        if (null == editor){
            if (null == preferences) {
                return //获取不到
            }
            editor = preferences!!.edit()
        }
    }*/

    private fun savePreferencesData(){

    }

    private fun takePreferencesData(){

    }
}
