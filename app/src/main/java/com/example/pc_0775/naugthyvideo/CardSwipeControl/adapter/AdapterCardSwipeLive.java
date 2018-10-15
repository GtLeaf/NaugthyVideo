package com.example.pc_0775.naugthyvideo.CardSwipeControl.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.bean.MessageEvent;
import com.example.pc_0775.naugthyvideo.view.ActivityVideoPlay;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by PC-0775 on 2018/10/15.
 */

public class AdapterCardSwipeLive extends AdapterCardSwipe{
    public AdapterCardSwipeLive(Context context, List videoInfoDataList, Uri uri) {
        super(context, videoInfoDataList, uri);
    }

}
