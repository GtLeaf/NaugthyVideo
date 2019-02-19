package com.example.pc_0775.naugthyvideo.fragment

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jpush.im.android.api.ChatRoomManager
import cn.jpush.im.android.api.callback.RequestCallback
import cn.jpush.im.android.api.model.ChatRoomInfo
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.bean.NormalItem
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.AdapterNormal
import io.vov.vitamio.utils.Log
import kotlinx.android.synthetic.main.fragment_chat_room_list.*

/**
 * Created by PC-0775 on 2019/2/8.
 */
class FragmentChatRoomList : Fragment(){

    private var roomAdapter:AdapterNormal? = null
    private var roomList = ArrayList<NormalItem>()
    private var mActivity:Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_room_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = activity
        init()
    }

    private fun init(){
        ChatRoomManager.getChatRoomListByApp(0, 10, object :RequestCallback<List<ChatRoomInfo>>(){
            override fun gotResult(p0: Int, p1: String?, p2: List<ChatRoomInfo>?) {
                Log.d("room","p2")
                p2!!.forEach{
                    roomList.add(NormalItem(it.roomID, "null", it.name, it.description))
                    if (roomAdapter != null){
                        roomAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        })
        roomAdapter = AdapterNormal(R.layout.item_normal, roomList)
        rv_chat_room_list.adapter = roomAdapter
        rv_chat_room_list.layoutManager = LinearLayoutManager(mActivity)
    }
}