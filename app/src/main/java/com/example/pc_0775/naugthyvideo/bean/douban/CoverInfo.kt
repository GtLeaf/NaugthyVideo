package com.example.pc_0775.naugthyvideo.bean.douban

import com.bumptech.glide.load.resource.drawable.GlideDrawable

/**
 * Created by PC-0775 on 2018/12/18.
 */
data class CoverInfo(
        var img:GlideDrawable,
        var name:String,
        var direct:List<DoubanMovie.SubjectsBean.DirectorsBean>,
        var average:Double,
        var id:String)