package com.example.pc_0775.naugthyvideo.retrofit

import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
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

    init {
        mMovieService = RetrofitServiceManager.create(MovieService::class.java)
    }

    fun getMovieTop250(start:Int, count:Int):Observable<List<DoubanMovie.SubjectsBean>>{
        return observe(mMovieService!!.getTop250(start, count)).map { t -> t.subjects }
    }
}

/**
 * 将一些重复的操作提出来，放到父类以免Loader 里每个接口都有重复代码
 */
open class ObjectLoader{
    /**
     *
     * @param observable
     * @param <T>
     * @return
     */
    protected fun <T> observe(observable: Observable<T>):Observable<T>{
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}