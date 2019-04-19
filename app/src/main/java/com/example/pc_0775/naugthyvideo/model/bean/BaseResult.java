package com.example.pc_0775.naugthyvideo.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PC-0775 on 2018/8/20.
 */

public class BaseResult<T> implements Serializable{
    /**
     * err_code : 0
     * result : [{"_id":"5c0242149a5369af2ce8dcae","phone_number":"15662360528","nick_name":"Candy","password":"cen3799","sex":"1","deadline":"2019-12-05 24:00:00","isVIP":true}]
     * message : success
     * affectedRows : 1
     */

    private int err_code;
    private String message;
    private int affectedRows;
    private List<T> result;

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
