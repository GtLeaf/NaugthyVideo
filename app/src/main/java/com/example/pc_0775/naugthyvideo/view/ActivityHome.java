package com.example.pc_0775.naugthyvideo.view;

import android.app.ActionBar;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PositionalDataSource;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie;
import com.example.pc_0775.naugthyvideo.bean.liveBean.LiveRoomInfo;
import com.example.pc_0775.naugthyvideo.bean.mmBean.LiveRoomMiMi;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.homeAdapter.AdapterHomeInfo;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.bean.HomeInfoData;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cdc.sed.yff.nm.cm.ErrorCode;
import cdc.sed.yff.nm.sp.SpotListener;
import cdc.sed.yff.nm.sp.SpotManager;

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
    private boolean isAllowStartActivityCardSilde = false;


    //data
    /**
     * rv_homeList的数据源
     */
    private List<HomeInfoData> homeInfoDataList = new ArrayList(){};
    private List<LiveRoomMiMi> liveInfoList;
    private List dataList;


    //豆瓣电影top250 data
    private DoubanMovie doubanMovie;
    /**
     * 判断doubanMovie是否为新的数据
     */
    private boolean isDataFresh = false;
    private boolean isInitPaging = false;
    private int start = 0;
    private int count = 10;
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
            //网络状况不好时，返回obj为null
            if(null == msg.obj){
                Toast.makeText(activity, "获取数据失败，请检查网络", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (msg.what){

                case Constants.CARTOON_VIDEO_REQUEST:
                    activity.dataList = NetWorkUtil.parseJsonArray(msg.obj.toString(), VideoInfo.class);
                    if (activity.isAllowStartActivityCardSilde){
                        activity.startActivityCardSilde();
                    }
                    break;

                case Constants.LIVE_ROOM_REQUEST:
                    List<LiveRoomInfo> liveRoomInfos = NetWorkUtil.parseJsonArray(msg.obj.toString(), LiveRoomInfo.class);

                    break;
                case Constants.DOUBAN_MOVIE_REQUEST:
                    activity.doubanMovie = NetWorkUtil.parseJsonWithGson(msg.obj.toString(), DoubanMovie.class);
                    activity.isDataFresh = true;
                    if (!activity.isInitPaging) {
                        activity.initPaging();
                        activity.isInitPaging = true;
                    }
                    break;

                default:
                    break;
            }

        }
    }

    @Override
    public void initParams(Bundle params) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_home_my;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {

//        panel_home_left_menu.setOpen(true, true);

        //设置标题栏
        Toolbar toolbar = $(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (null != actionBar){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 设置插屏广告
        setupSpotAd();

        //配置rv_homeList
        adapterHomeInfo = new AdapterHomeInfo();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_homeList.setLayoutManager(layoutManager);
        rv_homeList.setAdapter(adapterHomeInfo);

        nav_headerView.setCheckedItem(R.id.nav_home);
        requestMovieListData(handler);
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
                        isAllowStartActivityCardSilde = true;
                        if (null != liveInfoList && 0 != liveInfoList.size()) {
                            startActivityCardSilde();
                        }else {
                            requestMovieListData(handler);
                        }
                        break;
                    case R.id.nav_live_card_slide:
                        startActivity(ActivityLiveCardSilde.class);
                        break;
                    case R.id.nav_settings:
                        startActivity(ActivitySetting.class);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        // 展示插屏广告
        showSpotAd();

    }

    @Override
    public void widgetClick(View v) throws Exception {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void onBackPressed() {
        // 点击后退关闭插屏广告
        if (SpotManager.getInstance(ActivityHome.this).isSpotShowing()) {
            SpotManager.getInstance(ActivityHome.this).hideSpot();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 插屏广告
        SpotManager.getInstance(ActivityHome.this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 插屏广告
        SpotManager.getInstance(ActivityHome.this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 插屏广告
        SpotManager.getInstance(ActivityHome.this).onDestroy();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
//        Toast.makeText(this, "用户自定义拦截方法", Toast.LENGTH_SHORT).show();
    }

    private void requestMovieListData(Handler myHandler){
        HashMap doubanMovieList = new HashMap();
        doubanMovieList.put("start", start+"");
        doubanMovieList.put("count", count+"");
        Uri classTowUri = NetWorkUtil.createUri(Constants.DOUBAN_MOVIE_URL, doubanMovieList);
        NetWorkUtil.sendRequestWithOkHttp(classTowUri.toString(), Constants.DOUBAN_MOVIE_REQUEST, myHandler );
        start += 10;
    }

    private void startActivityCardSilde(){
        HashMap classTowparameters = new HashMap();
        classTowparameters.put("yeshu", "1");
        classTowparameters.put("type", "18");
        Uri classTowUri = NetWorkUtil.createUri(Constants.CARTOON_VIDEO_URL, classTowparameters);
        Bundle bundle = new Bundle();
//        bundle.putString("uri", classTowUri.toString());
        bundle.putSerializable("resultList", (Serializable)dataList);
        startActivity(ActivityCardSilde.class, bundle);
    }

    /**
     * 进行网络请求
     *
     * @param startPosition
     * @param count
     * @return
     */
    private List<DoubanMovie.SubjectsBean> loadData(int startPosition, int count){
        List<DoubanMovie.SubjectsBean> subjectsBeanList = new ArrayList<>();
        if (true == isDataFresh) {
            subjectsBeanList = doubanMovie.getSubjects();
            //显示数据之后，将标志设置为false，等待下一次网络请求，数据更新完成
            isDataFresh = false;
            //启动下一次数据刷新
            requestMovieListData(handler);
        }
        return subjectsBeanList;
    }

    //定义MyDataSource类，继承自DataSource三个子类之一
    private class MyDataSource extends PositionalDataSource<DoubanMovie.SubjectsBean>{

        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<DoubanMovie.SubjectsBean> callback) {
            callback.onResult(loadData(0, 10), 0, 10);
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<DoubanMovie.SubjectsBean> callback) {
            callback.onResult(loadData(params.startPosition, count));
        }
    }
    //定义MyDataSourceFactory，是DataSource.Factory的实现类
    private class MyDataSourceFactory extends DataSource.Factory<Integer, DoubanMovie.SubjectsBean>{

        @Override
        public DataSource<Integer, DoubanMovie.SubjectsBean> create() {
            return new MyDataSource();
        }
    }

    //将生产config和liveData的代码封装在这个方法中
    private void initPaging(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(10)    //每页显示的词条数
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10) //首次加载的数据量
                .setPrefetchDistance(5)     //距离底部还有多少条数据时开始预加载
                .build();

        /**
         * LiveData<PagedList<DataBean>> 用LivePagedListBuilder生成
         * LivePagedListBuilder 用 DataSource.Factory<Integer,DataBean> 和PagedList.Config.Builder 生成
         * DataSource.Factory<Integer,DataBean> 用 PositionalDataSource<DataBean>
         */
        LiveData<PagedList<DoubanMovie.SubjectsBean>> liveData = new LivePagedListBuilder(new MyDataSourceFactory(), config)
//                .setBoundaryCallback(null)
//                .setFetchExecutor(null)
                .build();
        //观察者模式，将Adapter注册进去，当liveData发生改变事通知Adapter
        liveData.observe(this, new Observer<PagedList<DoubanMovie.SubjectsBean>>() {

            @Override
            public void onChanged(@Nullable PagedList<DoubanMovie.SubjectsBean> subjectsBeans) {
                adapterHomeInfo.submitList(subjectsBeans);
            }
        });
    }

    /**
     * 设置插屏广告
     */
    private void setupSpotAd() {
        // 设置插屏图片类型，默认竖图
        //		// 横图
        //		SpotManager.getInstance(mContext).setImageType(SpotManager
        // .IMAGE_TYPE_HORIZONTAL);
        // 竖图
        SpotManager.getInstance(ActivityHome.this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

        // 设置动画类型，默认高级动画
        //		// 无动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        //				.ANIMATION_TYPE_NONE);
        //		// 简单动画
        //		SpotManager.getInstance(mContext)
        //		                    .setAnimationType(SpotManager.ANIMATION_TYPE_SIMPLE);
        // 高级动画
        SpotManager.getInstance(ActivityHome.this)
                .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);
    }


    /**
     * 展示插屏广告
     */
    private void showSpotAd(){
        SpotManager.getInstance(ActivityHome.this).showSpot(ActivityHome.this, new SpotListener() {

            @Override
            public void onShowSuccess() {
                Log.i(TAG, "插屏展示成功");
            }

            @Override
            public void onShowFailed(int errorCode) {
                Log.e(TAG, "插屏展示失败");
                switch (errorCode) {
                    case ErrorCode.NON_NETWORK:
                        showToast("网络异常");
                        break;
                    case ErrorCode.NON_AD:
                        showToast("暂无插屏广告");
                        break;
                    case ErrorCode.RESOURCE_NOT_READY:
                        showToast("插屏资源还没准备好");
                        break;
                    case ErrorCode.SHOW_INTERVAL_LIMITED:
                        showToast("请勿频繁展示");
                        break;
                    case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                        showToast("请设置插屏为可见状态");
                        break;
                    default:
                        showToast("请稍后再试");
                        break;
                }
            }

            @Override
            public void onSpotClosed() {
                Log.d(TAG, "插屏被关闭");
            }

            @Override
            public void onSpotClicked(boolean isWebPage) {
                Log.d(TAG, "插屏被点击");
                Log.i(TAG, String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
            }
        });
    }
}
