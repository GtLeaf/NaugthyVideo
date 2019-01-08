package com.example.pc_0775.naugthyvideo.retrofit

import com.example.pc_0775.naugthyvideo.myInterface.MovieService
import com.example.pc_0775.naugthyvideo.util.HttpCommonInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by PC-0775 on 2019/1/5.
 */
/*
class RetrofitServiceManager private constructor(){
    private var r:Retrofit? = null

    init {

    }

    companion object {
        private val DEFAULT_TIME_OUT = 5//超时时间5s
        private val DEFAULT_READ_TIME_OUT = 10
        private var instance:RetrofitServiceManager? = null
            get(){
                if (field == null){
                    field = RetrofitServiceManager()
                }
                return field
            }

        fun get(): RetrofitServiceManager{
            return instance!!
        }
    }
}*/

object RetrofitServiceManager{
    private val DEFAULT_TIME_OUT = 5//超时时间5s
    private val DEFAULT_READ_TIME_OUT = 10
    private var mRetrofit: Retrofit? = null

    init {
        //创建OkhttpClient
        var builder = OkHttpClient.Builder()
        builder.connectTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS)//连接超时时间
        builder.writeTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS)//读操作超时时间

        // 添加公共参数拦截器
        var commonInterceptor = HttpCommonInterceptor.Builder()
//                .addHeaderParams("", "")
                .build()
        builder.addInterceptor(commonInterceptor)

        //创建retrofit
        mRetrofit = Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MovieService.baseUrl)
                .build()
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    fun <T> create(service:Class<T>):T{
        return mRetrofit!!.create(service)
    }
}
