package com.example.pc_0775.naugthyvideo.retrofit

import com.example.pc_0775.naugthyvideo.bean.BaseResult
import com.example.pc_0775.naugthyvideo.bean.UserBean
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.myInterface.LoginService
import com.example.pc_0775.naugthyvideo.myInterface.MovieService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Created by PC-0775 on 2019/1/8.
 */
class MovieLoader : ObjectLoader(){
    private var mMovieService:MovieService? = null
    private var mLoginService:LoginService? = null

    init {
        mMovieService = RetrofitServiceManager.create(MovieService::class.java)
        mLoginService = RetrofitServiceManager.create(LoginService::class.java)
    }

    fun getMovieTop250(start:Int, count:Int):Observable<List<DoubanMovie.SubjectsBean>>{
        return observe(mMovieService!!.getTop250(start, count)).map { t -> t.subjects }
    }

    fun getLatestMovie(start:Int, count:Int):Observable<List<DoubanMovie.SubjectsBean>>{
        return observe(mMovieService!!.getLatestMovie(start, count)).map { t -> t.subjects }
    }

    fun postUserLogin(url:String, map:Map<String, String>):Observable<BaseResult<UserBean>>{
        return observe(mLoginService!!.userLogin(url, map))
    }
}