package com.example.pc_0775.naugthyvideo.app;

import android.app.Activity;
import android.app.Application;

import com.example.pc_0775.naugthyvideo.app.ActivityCollector;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import okhttp3.internal.Util;


/**
 * Created by PC-0775 on 2018/5/29.
 */

public class MyApplication extends Application {

    public static Application mApplication;

    public void onCreate() {
        super.onCreate();

        mApplication=this;

        //注册消息监听
        CrashReport.initCrashReport(getApplicationContext(), "f93662b4ee", false);
//        Thread.setDefaultUncaughtExceptionHandler(CrashExceptionHandler.Companion.getInstance(this));
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //极光IM
        JMessageClient.init(this, true);
        //LeakCanary
        LeakCanary.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
