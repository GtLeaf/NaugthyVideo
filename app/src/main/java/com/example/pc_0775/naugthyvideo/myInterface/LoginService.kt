package com.example.pc_0775.naugthyvideo.myInterface

import com.example.pc_0775.naugthyvideo.bean.BaseResult
import com.example.pc_0775.naugthyvideo.bean.UserBean
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * Created by PC-0775 on 2019/1/11.
 */
interface LoginService {

    @POST
    fun userLogin(@Url url:String, @QueryMap map: Map<String, String>):Observable<BaseResult<UserBean>>
}