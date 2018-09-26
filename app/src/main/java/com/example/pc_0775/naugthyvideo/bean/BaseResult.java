package com.example.pc_0775.naugthyvideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PC-0775 on 2018/8/20.
 */

public class BaseResult<T> implements Serializable{
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
