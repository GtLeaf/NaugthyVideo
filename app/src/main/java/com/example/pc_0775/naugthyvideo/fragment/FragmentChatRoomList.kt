package com.example.pc_0775.naugthyvideo.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jpush.im.android.api.ChatRoomManager
import cn.jpush.im.android.api.callback.RequestCallback
import cn.jpush.im.android.api.model.ChatRoomInfo
import cn.jpush.im.android.api.model.Conversation
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.ui.base.BaseFragment
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.AdapterChatRoomInfo
import com.example.pc_0775.naugthyvideo.ui.ChatActivity
import io.vov.vitamio.utils.Log
import kotlinx.android.synthetic.main.fragment_chat_room_list.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by PC-0775 on 2019/2/8.
 * Kotlin中直接引用控件名，发生空指针错误，可能是布局还没加载好就调用。
 */
class FragmentChatRoomList : BaseFragment(){

    private var roomAdapter:AdapterChatRoomInfo? = null
    private var chatRoomInfoList = ArrayList<ChatRoomInfo>()
    private var mActivity:Activity? = null

    override fun initParams(bundle: Bundle?) {
    }

    override fun bindView(inflater: LayoutInflater?, container: ViewGroup?): View {
        return inflater!!.inflate(R.layout.fragment_chat_room_list, container, false)
    }

    override fun initView(view: View?) {
        roomAdapter = AdapterChatRoomInfo(R.layout.item_chat_room, chatRoomInfoList)


    }

    override fun setListener() {
        roomAdapter!!.setOnItemClickListener { adapter, view, position ->
            //进入聊天室
            ChatRoomManager.leaveChatRoom(chatRoomInfoList[position].roomID, null)
            ChatRoomManager.enterChatRoom(chatRoomInfoList[position].roomID, object : RequestCallback<Conversation>() {
                override fun gotResult(p0: Int, p1: String?, p2: Conversation?) {
                    var responseCode = p0
                    var responseMessage = p1
                    //弹框进入成功前阻止所有操作
                    //进入失败直接退出
                    if (0 != p0) {
                        showToast("进入聊天室失败:"+p1)
                        return
                    }
                    EventBus.getDefault().postSticky(chatRoomInfoList[position])
                    startActivity(ChatActivity::class.java)
                }
            })

        }
    }

    override fun widgetClick(v: View?) {
    }

    override fun doBusiness() {
        rv_chat_room_list.adapter = roomAdapter
        rv_chat_room_list.layoutManager = LinearLayoutManager(mActivity)
        ChatRoomManager.getChatRoomListByApp(0, 10, object :RequestCallback<List<ChatRoomInfo>>(){
            override fun gotResult(p0: Int, p1: String?, p2: List<ChatRoomInfo>?) {
                Log.d("room","p2")
                if (null == p2){
                    return
                }
                chatRoomInfoList.addAll(p2)
                if (roomAdapter != null){
                    roomAdapter!!.notifyDataSetChanged()
                }
            }
        })
    }
}