package com.example.pc_0775.naugthyvideo.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardConfig;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardItemTouchHelperCallback;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardShowInfoBean;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.OnCardSwipeListener;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.adapter.AdapterCardSwipeLive;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.adapter.AdapterCardSwipeRight;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.myLayoutManager.CardLayoutManager;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.bean.MessageEvent;
import com.example.pc_0775.naugthyvideo.bean.liveBean.LivePlatform;
import com.example.pc_0775.naugthyvideo.bean.liveBean.LiveRoomInfo;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.AdapterCardSwipeCollection;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class ActivityLiveCardSilde extends BaseActivity {

    //view
    @ViewInject(R.id.dl_card_slide)
    private DrawerLayout dl_cardSlide;
    @ViewInject(R.id.rv_card_slide)
    private RecyclerView rv_cardSlide;
    @ViewInject(R.id.rv_card_slide_right_list)
    private RecyclerView rv_cardSlideRightList;
    @ViewInject(R.id.rv_live_collection)
    private RecyclerView rv_liveCollection;

    //
    private static final String COLLECTION_LIST = "collectionList";

    //adapter
    private AdapterCardSwipeLive adapterCardSwipeLive;
    private AdapterCardSwipeRight adapterCardSwipeRight;
    private AdapterCardSwipeCollection adapterCardSwipeCollection;
    //other
    /**
     * 滑动ItemTouchHelper的回调函数
     */
    private CardItemTouchHelperCallback cardCallback;

    private ActionBarDrawerToggle drawerbar;

    //sharePreferences
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    //data
    private List videoInfoDataList = new ArrayList();
    private List livePlatformList = new ArrayList();
    /**
     * 用户收藏
     */
    private List<LiveRoomInfo> userCollectionList;

    private Uri uri;

    //handler
    private MyHandler myHandler = new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<ActivityLiveCardSilde> weakReference;

        public MyHandler(ActivityLiveCardSilde activityCardSilde){
            weakReference = new WeakReference<ActivityLiveCardSilde>(activityCardSilde);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final ActivityLiveCardSilde activity = weakReference.get();
            if(null == msg.obj){
                Toast.makeText(activity, "获取数据失败，请检查网络", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (msg.what){
                case Constants.LIVE_PLATFORM_REQUEST:
                    activity.livePlatformList.clear();
                    activity.livePlatformList.addAll(NetWorkUtil.parseJsonArray(msg.obj.toString(), LivePlatform.class));
                    activity.adapterCardSwipeRight.notifyDataSetChanged();
                    break;
                case Constants.LIVE_ROOM_REQUEST:
                    activity.videoInfoDataList.clear();
                    activity.videoInfoDataList.addAll(NetWorkUtil.parseJsonArray(msg.obj.toString(), LiveRoomInfo.class));
                    activity.adapterCardSwipeLive = new AdapterCardSwipeLive(activity.getApplicationContext(),
                            activity.videoInfoDataList, null);
                    activity.rv_cardSlide.setAdapter(activity.adapterCardSwipeLive);
                    activity.cardCallback.setAdapterCardSwipeLive(activity.adapterCardSwipeLive);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void initParams(Bundle params) {
        NetWorkUtil.sendRequestWithOkHttp(Constants.LIVE_PLATFORM_URL, Constants.LIVE_PLATFORM_REQUEST, myHandler);
        NetWorkUtil.sendRequestWithOkHttp(Constants.LIVE_ROOM_URL+"?url=jsonchunban.txt", Constants.LIVE_ROOM_REQUEST, myHandler);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_live_card_silde;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(final View view) {

        //本地化存储
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        userCollectionList = getDataList(COLLECTION_LIST);
        showToast("收藏数量"+userCollectionList.size()+":"+userCollectionList.get(0).toString());
        //注册eventBus
        EventBus.getDefault().register(this);

        adapterCardSwipeLive = new AdapterCardSwipeLive(this, videoInfoDataList, uri);
        rv_cardSlide.setItemAnimator(new DefaultItemAnimator());//设置动画
        rv_cardSlide.setAdapter(adapterCardSwipeLive);
        cardCallback = new CardItemTouchHelperCallback(adapterCardSwipeLive);

        //ItemTouchHelper的用法
        ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        CardLayoutManager cardLayoutManager = new CardLayoutManager(rv_cardSlide, touchHelper);
        rv_cardSlide.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(rv_cardSlide);
        initDrawer();

        //左侧抽屉布局
        adapterCardSwipeRight = new AdapterCardSwipeRight(livePlatformList, myHandler);
        rv_cardSlideRightList.setAdapter(adapterCardSwipeRight);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_cardSlideRightList.setLayoutManager(layoutManager);

        //右侧抽屉布局
        adapterCardSwipeCollection = new AdapterCardSwipeCollection(userCollectionList);
        rv_liveCollection.setAdapter(adapterCardSwipeCollection);
        RecyclerView.LayoutManager collectionManager = new LinearLayoutManager(this);
        rv_liveCollection.setLayoutManager(collectionManager);

//        this.registerForContextMenu(rv_liveCollection);

    }

    @Override
    public void setListener() {
        //swipe滑动监听事件
        cardCallback.setOnCardSwipeListener(new OnCardSwipeListener<CardShowInfoBean>() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                AdapterCardSwipeLive.ViewHolder holder = (AdapterCardSwipeLive.ViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
                if (CardConfig.SWIPING_LEFT == direction) {
                    holder.iv_dislike.setAlpha(Math.abs(ratio));
                } else if (CardConfig.SWIPING_RIGHT == direction) {
                    holder.iv_like.setAlpha(Math.abs(ratio));
                }else {
                    holder.iv_dislike.setAlpha(0f);
                    holder.iv_like.setAlpha(0f);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, CardShowInfoBean cardShowInfoBean, int direction) {
                AdapterCardSwipeLive.ViewHolder holder = (AdapterCardSwipeLive.ViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                holder.iv_dislike.setAlpha(0f);
                holder.iv_like.setAlpha(0f);
                if (ItemTouchHelper.RIGHT == direction) {
                    LiveRoomInfo mLiveRoomInfo = new LiveRoomInfo();
                    mLiveRoomInfo.setAddress(cardShowInfoBean.getVideoInfo().getUrl());
                    mLiveRoomInfo.setTitle(cardShowInfoBean.getVideoInfo().getTitle());

                    if (0 == userCollectionList.size()) {
                        userCollectionList.add(mLiveRoomInfo);
                        saveList(COLLECTION_LIST, userCollectionList);
                        adapterCardSwipeCollection.notifyDataSetChanged();
                    }else {
                        //用for(:)会报错ConcurrentModificationException
                        for (int i = 0; i<userCollectionList.size(); i++){
                            //只有地址不同才加入
                            if (userCollectionList.get(i).getAddress().equals(mLiveRoomInfo.getAddress())) {
                                return;
                            }
                        }
                        userCollectionList.add(mLiveRoomInfo);
                        saveList(COLLECTION_LIST, userCollectionList);
                        adapterCardSwipeCollection.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(ActivityLiveCardSilde.this, "已经没有了，去看看其他频道吧", Toast.LENGTH_SHORT).show();
                //这是什么函数：延迟3秒执行线程
                /*rv_cardSlide.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        rv_cardSlide.getAdapter().notifyDataSetChanged();
                    }
                }, 3000L);*/
            }
        });

    }

    @Override
//    @OnClick({R.id.btn_card_swipe_left_video, R.id.btn_card_swipe_left_live})//暂时没用
    public void widgetClick(View v) throws Exception {
        switch (v.getId()){
            default:
                break;
        }
    }

    @Override
    public void doBusiness(Context mContext) {
        int count = rv_cardSlide.getAdapter().getItemCount();
        Log.d(TAG, "initView: "+count);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.live_swipe_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int positon = adapterCardSwipeCollection.getPosition();
        switch (item.getItemId()){
            case 1:
                LiveRoomInfo liveRoomInfo = userCollectionList.remove(positon);
                saveList(COLLECTION_LIST, userCollectionList);
                adapterCardSwipeCollection.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void initDrawer(){
        drawerbar = new ActionBarDrawerToggle(this, dl_cardSlide, R.drawable.nav_icon, R.string.dl_open, R.string.dl_close){
            //菜单打开
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                showToast("抽屉打开");
            }

            //菜单关闭
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                showToast("抽屉关闭");
            }
        };
        dl_cardSlide.setDrawerListener(drawerbar);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent){
    }

    /**
     * 保存list
     * @param tag
     * @param list
     * @param <T>
     */
    public <T> void saveList(String tag, List<T> list){
        if (null == list || list.size() <= 0) {
            return;
        }
        Gson gson = new Gson();
        //转换成json数据，在保存
        String strJson = gson.toJson(list);
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 获取collectionList
     * @param tag
     * @return
     */
    public List<LiveRoomInfo> getDataList(String tag){
        List<LiveRoomInfo> list = new ArrayList<>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return list;
        }
        Gson gson = new Gson();
        list = gson.fromJson(strJson, new TypeToken<List<LiveRoomInfo>>(){
        }.getType());

        return list;
    }
}
