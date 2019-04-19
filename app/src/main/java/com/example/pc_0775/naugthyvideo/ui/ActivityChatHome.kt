package com.example.pc_0775.naugthyvideo.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.ui.base.BaseActivityKotlin
import com.example.pc_0775.naugthyvideo.ui.fragment.FragmentChatRoomList
import com.example.pc_0775.naugthyvideo.ui.fragment.FragmentFriendList
import kotlinx.android.synthetic.main.activity_chat_home.*

class ActivityChatHome : BaseActivityKotlin() {

    //fragmentFriendList
    private var fragmentChatRoomList: FragmentChatRoomList? = null
    private var fragmentFriendList: FragmentFriendList? = null
    private var mFragmentManager = fragmentManager

    override fun initParams(params: Bundle?) {
    }

    override fun bindLayout(): Int {
        return R.layout.activity_chat_home
    }

    override fun initView(view: View) {
        fragmentFriendList = FragmentFriendList()
        mFragmentManager.beginTransaction().add(R.id.fl_chat_home, fragmentFriendList).commit()


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
                if (null == fragmentFriendList){
                    fragmentFriendList = FragmentFriendList()
                    mFragmentManager.beginTransaction().add(R.id.fl_chat_home, fragmentFriendList).commit()
                }else{
                    mFragmentManager.beginTransaction().show(fragmentFriendList).commit()
                }
            }
            R.id.tv_chat_room -> {
                if (null == fragmentChatRoomList){
                    fragmentChatRoomList = FragmentChatRoomList()
                    mFragmentManager.beginTransaction().add(R.id.fl_chat_home, fragmentChatRoomList).commit()
                }else{
                    mFragmentManager.beginTransaction().show(fragmentChatRoomList).commit()
                }
            }
            else -> {}
        }
    }

    private fun reset(){

        //防止fragment重影
        var transation = mFragmentManager.beginTransaction()
        if (null != fragmentChatRoomList){
            transation.hide(fragmentChatRoomList)
        }
        if(null != fragmentFriendList){
            transation.hide(fragmentFriendList)
        }

        transation.commit()
    }

}
