package com.example.pc_0775.naugthyvideo.model.repository

import com.example.pc_0775.naugthyvideo.model.local.dao.DoubanMovieDao
import com.example.pc_0775.naugthyvideo.model.remote.DoubanMovieService

/**
 * Created by PC-0775 on 2019/4/19.
 */
class DoubanMovieRepo(private val remote:DoubanMovieService, private val local:DoubanMovieDao){
    //首先查看本地数据库是否有数据
    fun getMovieInfo() = local.getDoubanMoive()
//            .onErrorResumeNext{ //本地数据库不存在，进行网络获取，成功后保存在数据库 }
}