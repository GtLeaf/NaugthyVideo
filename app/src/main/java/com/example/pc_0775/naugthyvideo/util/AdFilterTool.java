package com.example.pc_0775.naugthyvideo.util;

import android.content.Context;
import android.content.res.Resources;

import com.example.pc_0775.naugthyvideo.R;


/**
 * Created by PC-0775 on 2018/8/22.
 */

public class AdFilterTool {
    public static boolean isAd(Context context, String url){
        Resources res = context.getResources();
        String[] filterUrls = res.getStringArray(R.array.adUrls);
        for (String adUrl : filterUrls){
            if(url.contains(adUrl)){
                return true;
            }
        }
        return false;
    }
}
