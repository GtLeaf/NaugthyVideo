package com.example.pc_0775.naugthyvideo.retrofit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by PC-0775 on 2019/1/14.
 */

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
    protected fun <T> observe(observable: Observable<T>): Observable<T> {
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}