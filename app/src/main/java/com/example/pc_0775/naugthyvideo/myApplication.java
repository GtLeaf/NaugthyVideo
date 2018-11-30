package com.example.pc_0775.naugthyvideo;

import android.app.Activity;
import android.app.Application;

import com.example.pc_0775.naugthyvideo.LogCrash.CrashExceptionHandler;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PC-0775 on 2018/5/29.
 */

public class myApplication extends Application {
    private List<Activity> oList;//用于存放所有启动的Activity的集合

    public void onCreate() {
        super.onCreate();
        oList = new ArrayList<Activity>();
        CrashReport.initCrashReport(getApplicationContext(), "f93662b4ee", false);
        Thread.setDefaultUncaughtExceptionHandler(CrashExceptionHandler.Companion.getInstance(this));
    }

    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
        // 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
        //判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }
}
