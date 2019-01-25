package com.example.pc_0775.naugthyvideo.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.pc_0775.naugthyvideo.Constants.Constants
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivityKotlin
import kotlinx.android.synthetic.main.activity_user_info.*

class ActivityUserInfo : BaseActivityKotlin() {
    override fun initParams(params: Bundle?) {
    }

    override fun bindLayout(): Int {
        return R.layout.activity_user_info
    }

    override fun initView(view: View) {
        tv_user_name.text = Constants.user.nick_name
        tv_user_phone.text = Constants.user.phone_number
        tv_user_sex.text = Constants.user.sex
    }

    override fun setListener() {
    }

    override fun doBusiness(mContext: Context) {
    }

}
