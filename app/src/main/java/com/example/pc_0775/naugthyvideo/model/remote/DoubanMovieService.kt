package com.example.pc_0775.naugthyvideo.model.remote

import com.example.pc_0775.naugthyvideo.model.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.model.bean.douban.DoubanMovieDetail
import com.example.pc_0775.naugthyvideo.model.bean.douban.DoubanMovieEntry
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by PC-0775 on 2019/1/3.
 */
interface DoubanMovieService {

    @GET("in_theaters")
    fun getLatestMovie(@Query("start") start:Int, @Query("count") count:Int): Observable<DoubanMovie>

    @GET("subject/{movieId}")
    fun getMovieDetail(@Path("movieId") movieId:String ): Observable<DoubanMovieDetail>

    @GET("subject/{movieId}")
    fun getMovieEntry(@Path("movieId") movieId:String, @Query("apikey")key:String): Observable<DoubanMovieEntry>

    @GET("subject/{movieId}")
    fun getMovieEntry2(@Path("movieId") movieId:String, @Query("apikey")apikey:String): Call<ResponseBody>

    @GET("top250")
    fun getTop250(@Query("start") start:Int, @Query("count") count:Int): Observable<DoubanMovie>
    companion object {
        val baseUrl = "https://api.douban.com/v2/movie/"
    }
}