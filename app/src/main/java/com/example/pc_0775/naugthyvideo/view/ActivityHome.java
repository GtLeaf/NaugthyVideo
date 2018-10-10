package com.example.pc_0775.naugthyvideo.view;

import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.homeAdapter.AdapterHomeInfo;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.bean.HomeInfoData;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityHome extends BaseActivity {

    //view
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer_layout;
    @ViewInject(R.id.nav_header_view)
    private NavigationView nav_headerView;
    @ViewInject(R.id.rv_home_list)
    private RecyclerView rv_homeList;

    //adapter
    private AdapterHomeInfo adapterHomeInfo;

    //flag
    /**
     * 是否允许启动ActivityCardSilde页面，当用户点击后为真
     */
    private boolean isStartActivityCardSilde = false;

    //data
    /**
     * rv_homeList的数据源
     */
    private List<HomeInfoData> homeInfoDataList = new ArrayList(){};
    private List<VideoInfo> videoInfoList;
    private List dataList;

    //handler
    private ActivityHome.MyHandler handler = new ActivityHome.MyHandler(this);
    private static class MyHandler<T> extends Handler {
        WeakReference<ActivityHome> weakReference;

        public MyHandler(ActivityHome activityHome){
            weakReference = new WeakReference<ActivityHome>(activityHome);
        }

        @Override
        public void handleMessage(Message msg) {
            ActivityHome activity = weakReference.get();
            if(null == msg.obj){
                Toast.makeText(activity, "获取数据失败，请检查网络", Toast.LENGTH_SHORT).show();
                return;
            }
             activity.dataList = NetWorkUtil.parseJsonArray(msg.obj.toString(), VideoInfo.class);
            if(activity.dataList.size() == 0){
                Toast.makeText(activity, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (msg.what){
                case Constants.CLASS_TWO_REQUEST:
                    if (activity.isStartActivityCardSilde){
                        activity.startActivityCardSilde();
                    }
                    break;
                case Constants.EUROPE_VIDEO_REQUEST:
                    if (activity.isStartActivityCardSilde){
                        activity.startActivityCardSilde();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initParams(Bundle params) {
        initData();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_home;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {

        //设置标题栏
        Toolbar toolbar = $(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (null != actionBar){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //配置rv_homeList
        adapterHomeInfo = new AdapterHomeInfo(homeInfoDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_homeList.setLayoutManager(layoutManager);
        rv_homeList.setAdapter(adapterHomeInfo);

        nav_headerView.setCheckedItem(R.id.nav_home);
        requestVideoInfoData(handler);
    }

    @Override
    public void setListener() {
        nav_headerView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer_layout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_home:
                        break;
                    case R.id.nav_function:
                        ActivityFunction.actionStart(ActivityHome.this);
                        break;
                    case R.id.nav_part_slide:
                        startActivity(ActivityPartSlide.class);
                        break;
                    case R.id.nav_card_slide:
                        isStartActivityCardSilde = true;
                        if (null != videoInfoList && 0 != videoInfoList.size()) {
                            startActivityCardSilde();
                        }else {
                            requestVideoInfoData(handler);
                        }
//                        startActivityCardSilde();
                        break;
                    case R.id.nav_function_5:
                        Toast.makeText(ActivityHome.this, "功能5未开放", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void widgetClick(View v) throws Exception {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backup:
                Toast.makeText(this, "You click Backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "You click delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You click settings", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    private void initData(){
        for (int i = 0; i<20; i++){
            int type = Constants.ITEM_TYPE.ITEM_TYPE_INFO.ordinal();
            if(2 == i || 3 == i){
                type = Constants.ITEM_TYPE.ITEM_TYPE_LIST.ordinal();
            }
            HomeInfoData homeInfoData = new HomeInfoData("title:"+i, "describe:"+i, type);
            homeInfoDataList.add(homeInfoData);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
//        Toast.makeText(this, "用户自定义拦截方法", Toast.LENGTH_SHORT).show();
    }

    private void requestVideoInfoData(Handler myHandler){
        HashMap classTowparameters = new HashMap();
        classTowparameters.put("yeshu", "1");
        Uri classTowUri = NetWorkUtil.createUri(Constants.EUROPE_VIDEO_URL, classTowparameters);
        NetWorkUtil.sendRequestWithOkHttp(classTowUri.toString(), Constants.EUROPE_VIDEO_REQUEST, myHandler );
    }

    private void startActivityCardSilde(){
        HashMap classTowparameters = new HashMap();
        classTowparameters.put("yeshu", "1");
        Uri classTowUri = NetWorkUtil.createUri(Constants.EUROPE_VIDEO_URL, classTowparameters);
        Bundle bundle = new Bundle();
        bundle.putString("uri", classTowUri.toString());
        bundle.putSerializable("resultList", (Serializable)dataList);
        startActivity(ActivityCardSilde.class, bundle);
    }
}
