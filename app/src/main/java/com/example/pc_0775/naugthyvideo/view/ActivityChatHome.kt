package com.example.pc_0775.naugthyvideo.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivityKotlin
import com.example.pc_0775.naugthyvideo.fragment.FragmentChatRoom
import com.example.pc_0775.naugthyvideo.fragment.FragmentFriendMessage
import io.vov.vitamio.utils.Log
import kotlinx.android.synthetic.main.activity_chat_home.*

class ActivityChatHome : BaseActivityKotlin() {

    //fragmentFriendMessage
    private var fragmentChatRoom:FragmentChatRoom? = null
    private var fragmentFriendMessage:FragmentFriendMessage? = null
    private var mFragmentManager = fragmentManager

    override fun initParams(params: Bundle?) {
    }

    override fun bindLayout(): Int {
        return R.layout.activity_chat_home
    }

    override fun initView(view: View) {
        fragmentFriendMessage = FragmentFriendMessage()
        mFragmentManager.beginTransaction().add(R.id.fl_chat_home, fragmentFriendMessage).commit()


    }

    override fun setListener() {
        tv_chat_room.setOnClickListener(this)
        tv_friend_msg.setOnClickListener(this)
    }

    override fun doBusiness(mContext: Context) {
    }

    override fun widgetClick(v: View) {
        reset()

        when(v.id){
            R.id.tv_friend_msg -> {
                if (null == fragmentFriendMessage){
                    fragmentFriendMessage = FragmentFriendMessage()
                    mFragmentManager.beginTransaction().add(R.id.fl_chat_home, fragmentFriendMessage).commit()
                }else{
                    mFragmentManager.beginTransaction().show(fragmentFriendMessage).commit()
                }
            }
            R.id.tv_chat_room -> {
                if (null == fragmentChatRoom){
                    fragmentChatRoom = FragmentChatRoom()
                    mFragmentManager.beginTransaction().add(R.id.fl_chat_home, fragmentChatRoom).commit()
                }else{
                    mFragmentManager.beginTransaction().show(fragmentChatRoom).commit()
                }
            }
            else -> {}
        }

    }

    private fun reset(){

        //防止fragment重影
        var transation = mFragmentManager.beginTransaction()
        if (null != fragmentChatRoom){
            transation.hide(fragmentChatRoom)
        }
        if(null != fragmentFriendMessage){
            transation.hide(fragmentFriendMessage)
        }

        transation.commit()
    }

}
