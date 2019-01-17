package com.example.pc_0775.naugthyvideo.myInterface

import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovieDetail
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by PC-0775 on 2019/1/3.
 */
interface MovieService {

    @GET("in_theaters")
    fun getLatestMovie(@Query("start") start:Int, @Query("count") count:Int): Observable<DoubanMovie>

    @GET("subject/{movieId}")
    fun getMovieItem(@Path("movieId") movieId:String ): Observable<DoubanMovieDetail>


    @GET("top250")
    fun getTop250(@Query("start") start:Int, @Query("count") count:Int): Observable<DoubanMovie>
    companion object {
        val baseUrl = "https://api.douban.com/v2/movie/"
    }
}