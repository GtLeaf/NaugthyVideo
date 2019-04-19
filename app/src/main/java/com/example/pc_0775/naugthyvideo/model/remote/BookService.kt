package com.example.pc_0775.naugthyvideo.model.remote

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by PC-0775 on 2018/12/29.
 */
interface BookService {
    @GET("book/{id}")
    abstract fun getBook(@Path("id") id: Int): Call<ResponseBody>

    @GET("movie/subject/{movieId}")
    fun getMovieEntry2(@Path("movieId") movieId:String, @Query("apikey")apikey:String): Call<ResponseBody>

    @GET("movie/subject/{movieId}")
    fun getMovieEntry2(@Path("movieId") movieId:String): Call<ResponseBody>

    @GET("movie/subject/{movieId}")
    fun getMovieDetail(@Path("movieId") movieId:String ): Call<ResponseBody>
}