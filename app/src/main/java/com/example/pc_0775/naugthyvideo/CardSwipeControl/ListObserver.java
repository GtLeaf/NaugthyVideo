package com.example.pc_0775.naugthyvideo.CardSwipeControl;


/**
 * Created by PC-0775 on 2018/10/14.
 */

//需要用泛型封装
public interface ListObserver<T> {
    public void onUpdate(T t);
}
