package com.example.pc_0775.naugthyvideo.retrofit

import com.example.pc_0775.naugthyvideo.bean.BaseResult
import com.example.pc_0775.naugthyvideo.bean.UserBean
import com.example.pc_0775.naugthyvideo.myInterface.LoginService
import io.reactivex.Observable

/**
 * Created by PC-0775 on 2019/1/12.
 */
class UserLoginLoader:ObjectLoader() {
    private var mLoginService: LoginService? = null

    init {
        mLoginService = RetrofitServiceManager.create(LoginService::class.java)
    }
    fun postUserLogin(url:String, phoneNumber:String, password:String): Observable<BaseResult<UserBean>> {
        return observe(mLoginService!!.userLogin(url, phoneNumber, password))
    }

}