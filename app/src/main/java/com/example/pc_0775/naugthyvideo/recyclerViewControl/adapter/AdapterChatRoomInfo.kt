package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter

import cn.jpush.im.android.api.model.ChatRoomInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.pc_0775.naugthyvideo.R

/**
 * Created by PC-0775 on 2019/2/27.
 */
class AdapterChatRoomInfo(layoutResId: Int, data:MutableList<ChatRoomInfo>):BaseQuickAdapter<ChatRoomInfo, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: ChatRoomInfo?) {
        helper!!.setText(R.id.tv_chat_room_title, item!!.name)
                .setText(R.id.tv_chat_room_describe, item.description)
    }
}