package com.example.pc_0775.naugthyvideo.other.retrofit

import com.example.pc_0775.naugthyvideo.model.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.model.bean.douban.DoubanMovieDetail
import com.example.pc_0775.naugthyvideo.model.bean.douban.DoubanMovieEntry
import com.example.pc_0775.naugthyvideo.model.remote.DoubanMovieService
import io.reactivex.Observable

/**
 * Created by PC-0775 on 2019/1/8.
 */
class MovieLoader : ObjectLoader(){
    private var mDoubanMovieService: DoubanMovieService? = null

    init {
        mDoubanMovieService = RetrofitServiceManager.create(DoubanMovieService::class.java)
    }

    fun getMovieTop250(start:Int, count:Int):Observable<List<DoubanMovie.SubjectsBean>>{
        return observe(mDoubanMovieService!!.getTop250(start, count)).map { t -> t.subjects }
    }

    fun getLatestMovie(start:Int, count:Int):Observable<List<DoubanMovie.SubjectsBean>>{
        return observe(mDoubanMovieService!!.getLatestMovie(start, count)).map { t -> t.subjects }
    }

    fun getMovieDetail(movieId:String):Observable<DoubanMovieDetail>{
        return observe(mDoubanMovieService!!.getMovieDetail(movieId))
    }

    fun getMovieEntry(movieId:String, key:String):Observable<DoubanMovieEntry>{
        return observe(mDoubanMovieService!!.getMovieEntry(movieId, key))
    }
}