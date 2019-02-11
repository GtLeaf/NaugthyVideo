package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.bean.NormalItem

/**
 * Created by PC-0775 on 2019/2/11.
 */
class AdapterNormal(layoutResId: Int, data: MutableList<NormalItem>?) : BaseQuickAdapter<NormalItem, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: NormalItem?) {
        helper!!.setText(R.id.tv_normal_title, item!!.title)
        helper!!.setText(R.id.tv_normal_describe, item!!.describe)
        Glide.with(mContext).load(item!!.imgUrl).crossFade().into(helper!!.getView<ImageView>(R.id.iv_chat_room_image))
    }

}