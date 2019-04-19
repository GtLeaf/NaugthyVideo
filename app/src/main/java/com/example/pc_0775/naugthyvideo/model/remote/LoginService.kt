package com.example.pc_0775.naugthyvideo.model.remote

import com.example.pc_0775.naugthyvideo.model.bean.BaseResult
import com.example.pc_0775.naugthyvideo.model.bean.UserBean
import io.reactivex.Observable
import retrofit2.http.*
import java.util.*

/**
 * Created by PC-0775 on 2019/1/11.
 */
interface LoginService {

    @FormUrlEncoded
    @POST
    fun userLogin(@Url url:String, @Field("phone_number")phoneNumber:String, @Field("password")password:String):Observable<BaseResult<UserBean>>

    @FormUrlEncoded
    @POST
    fun userRegister(@Url url:String, @Field("phone_number")phoneNumber:String, @Field("nick_name")nickName:String, @Field("password")password:String, @Field("sex")sex:String):Observable<BaseResult<Objects>>
}