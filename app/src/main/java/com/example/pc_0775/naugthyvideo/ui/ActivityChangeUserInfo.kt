package com.example.pc_0775.naugthyvideo.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivityKotlin
import kotlinx.android.synthetic.main.activity_change_user_info.*

class ActivityChangeUserInfo : BaseActivityKotlin() {

    override fun initParams(params: Bundle?) {
    }

    override fun bindLayout(): Int {
        return R.layout.activity_change_user_info
    }

    override fun initView(view: View) {
    }

    override fun setListener() {
        tb_change_user_info.setNavigationOnClickListener { v ->  finish() }
    }

    override fun doBusiness(mContext: Context) {
    }

    override fun widgetClick(v: View) {
    }
}
