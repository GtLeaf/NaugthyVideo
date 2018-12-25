package com.example.pc_0775.naugthyvideo.app

import android.app.Activity

/**
 * Created by PC-0775 on 2018/12/25.
 */
class ActivityCollector {
    companion object {
        var activities = ArrayList<Activity>()

        fun addActivity(activity: Activity){
            activities.add(activity)
        }

        fun removeActivity(activity: Activity){
            activities.remove(activity)
        }

        fun finishAll(){
            for(activity in activities){
                if (!activity.isFinishing){
                    activity.finish()
                }
            }
        }
    }
}