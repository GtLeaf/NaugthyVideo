package com.example.pc_0775.naugthyvideo.CardSwipeControl;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.bean.mmBean.LiveRoomMiMi;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-0775 on 2018/10/14.
 * 用于帮助adapetr进行网络请求，被观察者
 */

public class ListObservable {

    private Context context;
    /**
     * 发送网络请求的地址
     */
    private String url;

    /**
     * 所有已注册的观察者对象
     */
    private List<ListObserver> observerList = new ArrayList<>();

    /**
     * 请求得到的数据
     */
    private List dataList;
    private LiveRoomMiMi liveRoomMiMi;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler{
        WeakReference<ListObservable> weakReference;

        public MyHandler(ListObservable listObservable){
            weakReference = new WeakReference<ListObservable>(listObservable);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ListObservable listObservable = weakReference.get();
            if(null == msg.obj){
                Toast.makeText(listObservable.context, "获取数据失败，请检查网络", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (msg.what){
                case Constants.CARTOON_VIDEO_REQUEST:
                    listObservable.dataList = NetWorkUtil.parseJsonArray(msg.obj.toString(), VideoInfo.class);
                    if(listObservable.dataList.size() == 0){
                        Toast.makeText(listObservable.context, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listObservable.onChange();
                    break;
                case Constants.LIVE_PLATFORM_MIMI_REQUEST:
                    listObservable.liveRoomMiMi = NetWorkUtil.parseJsonWithGson(msg.obj.toString(), LiveRoomMiMi.class);
                    if(listObservable.liveRoomMiMi == null){
                        Toast.makeText(listObservable.context, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listObservable.onChange();
                    break;
                default:
                    break;
            }

        }
    }

    public ListObservable(Context context) {
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        requestDataList(url);
    }

    private void onChange(){
        for ( ListObserver observer : observerList){
            if (null == observer) {
                continue;
            }
            if (null == liveRoomMiMi) {
                return;
            }
            observer.onUpdate(liveRoomMiMi);
        }
    }

    public void registerObserver(ListObserver observer){
        observerList.add(observer);
    }

    public void unregisterObserver(ListObserver observer){
        observerList.remove(observer);
    }

    public void unregisterAll(){
        observerList.clear();
    }

    /**
     * 发送网络请求
     */
    public void requestDataList(String url){
        NetWorkUtil.sendRequestWithOkHttp(url, Constants.LIVE_PLATFORM_MIMI_REQUEST, myHandler );
    }
}
