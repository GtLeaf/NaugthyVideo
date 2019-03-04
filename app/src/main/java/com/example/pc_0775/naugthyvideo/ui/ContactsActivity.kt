package com.example.pc_0775.naugthyvideo.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.transition.Slide
import android.view.Gravity
import android.view.Window

/**
 * Created by PC-0775 on 2018/12/15.
 */
class ContactsActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            setExitTransition()
        }

        super.onCreate(savedInstanceState)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun setExitTransition(){
        /*
        *1、打开FEATURE_CONTENT_TRANSITIONS开关(可选)，这个开关默认是打开的
        * */
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        /*
        *2、设置除ShareElement外其它View的退出方式(左边滑出)
        * */
        window.exitTransition = Slide(Gravity.LEFT)
    }

    companion object {
        val KEY_CONTACTS = "1"
    }

}