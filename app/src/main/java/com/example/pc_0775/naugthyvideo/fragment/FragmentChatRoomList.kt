package com.example.pc_0775.naugthyvideo.fragment

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jpush.im.android.api.ChatRoomManager
import cn.jpush.im.android.api.callback.RequestCallback
import cn.jpush.im.android.api.model.ChatRoomInfo
import com.example.pc_0775.naugthyvideo.R
import io.vov.vitamio.utils.Log

/**
 * Created by PC-0775 on 2019/2/8.
 */
class FragmentChatRoomList : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_room_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ChatRoomManager.getChatRoomListByApp(0, 10, object :RequestCallback<List<ChatRoomInfo>>(){
            override fun gotResult(p0: Int, p1: String?, p2: List<ChatRoomInfo>?) {
                Log.d("room","p2")
            }
        })
    }
}