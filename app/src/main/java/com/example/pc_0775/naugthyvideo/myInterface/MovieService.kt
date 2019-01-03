package com.example.pc_0775.naugthyvideo.myInterface

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by PC-0775 on 2019/1/3.
 */
interface MovieService {
    @GET("in_theaters?")
    fun getMovie(@QueryMap params:Map<String, String>): Call<ResponseBody>
}