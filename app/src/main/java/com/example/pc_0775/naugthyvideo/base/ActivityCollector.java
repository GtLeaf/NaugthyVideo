package com.example.pc_0775.naugthyvideo.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-0775 on 2018/5/29.
 */

public class ActivityCollector {

    /**
     * Activity集合
     **/
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
