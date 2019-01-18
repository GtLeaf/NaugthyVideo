package com.example.pc_0775.naugthyvideo.retrofit

import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovieDetail
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovieEntry
import com.example.pc_0775.naugthyvideo.myInterface.MovieService
import io.reactivex.Observable

/**
 * Created by PC-0775 on 2019/1/8.
 */
class MovieLoader : ObjectLoader(){
    private var mMovieService:MovieService? = null

    init {
        mMovieService = RetrofitServiceManager.create(MovieService::class.java)
    }

    fun getMovieTop250(start:Int, count:Int):Observable<List<DoubanMovie.SubjectsBean>>{
        return observe(mMovieService!!.getTop250(start, count)).map { t -> t.subjects }
    }

    fun getLatestMovie(start:Int, count:Int):Observable<List<DoubanMovie.SubjectsBean>>{
        return observe(mMovieService!!.getLatestMovie(start, count)).map { t -> t.subjects }
    }

    fun getMovieDetail(movieId:String):Observable<DoubanMovieDetail>{
        return observe(mMovieService!!.getMovieDetail(movieId))
    }

    fun getMovieEntry(movieId:String, key:String):Observable<DoubanMovieEntry>{
        return observe(mMovieService!!.getMovieEntry(movieId, key))
    }
}