package com.example.pc_0775.naugthyvideo.util

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by PC-0775 on 2019/1/5.
 */
/*
 * 拦截器
 *
 * 向请求头里添加公共参数
 */
class HttpCommonInterceptor:Interceptor {
    private var mHeaderParamsMap = HashMap<String, String>()

    override fun intercept(chain: Interceptor.Chain?): Response {
        Log.d("HttpCommonInterceptor","add common params");
        var oldRequest = chain!!.request()
        // 添加新的参数，添加到url 中
        /*HttpUrl.Builder authorizedUrlBuilder = oldRequest.url().newBuilder()
        .scheme(oldRequest.url().scheme())
        .host(oldRequest.url().host());*/

        // 新的请求
        var requestBuilder = oldRequest.newBuilder()
        requestBuilder.method(oldRequest.method(), oldRequest.body())

        //添加公共参数,添加到header中
        if (mHeaderParamsMap.size > 0){
            for(params in mHeaderParamsMap.entries){
                requestBuilder.header(params.key, params.value)
            }
        }

        var newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }

    class Builder{
        var mHttpCommonInterceptor:HttpCommonInterceptor? = null

        init {
            mHttpCommonInterceptor = HttpCommonInterceptor()
        }

        fun addHeaderParams(key:String, value:String):Builder{
            mHttpCommonInterceptor!!.mHeaderParamsMap.put(key, value)
            return this
        }

        fun addHeaderParams(key:String, value:Int):Builder{
            mHttpCommonInterceptor!!.mHeaderParamsMap.put(key, value.toString())
            return this
        }

        fun addHeaderParams(key:String, value:Float):Builder{
            mHttpCommonInterceptor!!.mHeaderParamsMap.put(key, value.toString())
            return this
        }

        fun addHeaderParams(key:String, value:Long):Builder{
            mHttpCommonInterceptor!!.mHeaderParamsMap.put(key, value.toString())
            return this
        }

        fun addHeaderParams(key:String, value:Double):Builder{
            mHttpCommonInterceptor!!.mHeaderParamsMap.put(key, value.toString())
            return this
        }

        fun build():HttpCommonInterceptor{
            return mHttpCommonInterceptor!!
        }
    }
}