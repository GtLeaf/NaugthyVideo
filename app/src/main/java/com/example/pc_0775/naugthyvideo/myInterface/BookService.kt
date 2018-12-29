package com.example.pc_0775.naugthyvideo.myInterface

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by PC-0775 on 2018/12/29.
 */
interface BookService {
    @GET("book/{id}")
    abstract fun getBook(@Path("id") id: Int): Call<ResponseBody>
}