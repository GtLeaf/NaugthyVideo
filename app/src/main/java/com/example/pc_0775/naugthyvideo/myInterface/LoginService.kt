package com.example.pc_0775.naugthyvideo.myInterface

import com.example.pc_0775.naugthyvideo.bean.BaseResult
import com.example.pc_0775.naugthyvideo.bean.UserBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by PC-0775 on 2019/1/11.
 */
interface LoginService {

    @FormUrlEncoded
    @POST
    fun userLogin(@Url url:String, @Field("phone_number")phoneNumber:String, @Field("password")password:String):Observable<BaseResult<UserBean>>
}