package com.example.pc_0775.naugthyvideo.view;

import android.app.ActionBar;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PositionalDataSource;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
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
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.MyView.CommonPopupWindow;
import com.example.pc_0775.naugthyvideo.MyView.CommonPopupWindow.LayoutGravity;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie;
import com.example.pc_0775.naugthyvideo.myInterface.BookService;
import com.example.pc_0775.naugthyvideo.myInterface.MovieService;
import com.example.pc_0775.naugthyvideo.receiver.SimpleJPushReceiver;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.AdapterHome;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.retrofit.MovieLoader;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;
import com.example.pc_0775.naugthyvideo.retrofit.RetrofitServiceManager;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cdc.sed.yff.nm.sp.SpotManager;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivityHome extends BaseActivity {

    //view
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer_layout;
    @ViewInject(R.id.nav_header_view)
    private NavigationView nav_headerView;
    @ViewInject(R.id.rv_home_list)
    private RecyclerView rv_homeList;
    private CircleImageView nav_userImage;

    //popupWindow
    private CommonPopupWindow window;
    private RecyclerView rv_homePopupMessage;
    private LayoutGravity layoutGravity;

    //MessageDetailPopupWindow
    private CommonPopupWindow messageDetailWindow;
    private LayoutGravity detaiLayoutGravity;

    //defined player
    private CommonPopupWindow playerUrlWindow;
    private LayoutGravity playerUrlGravity;
    private EditText et_playerUrl;
    private Button btn_playerUrlYes;

    //广播接收器
    private SimpleJPushReceiver jPushReceiver = new SimpleJPushReceiver();


    //adapter
    private AdapterHome adapterHome;

    //flag
    /**
     * 是否允许启动ActivityCardSilde页面，当用户点击后为真
     */
    private boolean isAllowStartActivityCardSilde = false;


    //data
    /**
     * rv_homeList的数据源
     */
    private List dataList;


    //豆瓣电影top250 data
    private DoubanMovie doubanMovie = null;
    private List<DoubanMovie.SubjectsBean> subjectsBeanList = new ArrayList<>();
    /**
     * 判断doubanMovie是否为新的数据
     */
    private boolean isInitPaging = false;
    private int start = 0;
    private int count = 5;
    //handler
    private ActivityHome.MyHandler handler = new ActivityHome.MyHandler(this);
    private static class MyHandler extends Handler {
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

                case Constants.DOUBAN_LATEST_MOVIE_REQUEST:
                    activity.doubanMovie = NetWorkUtil.parseJsonWithGson(msg.obj.toString(), DoubanMovie.class);
                    if (!activity.isInitPaging){
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
    public void setWindowConfig() {
        super.setWindowConfig();
        //设置转场动画
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            /**
             *1、打开FEATURE_CONTENT_TRANSITIONS开关(可选)，这个开关默认是打开的
             */
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            /**
             *2、设置除ShareElement外其它View的退出方式(左边滑出)
             */
            getWindow().setExitTransition(new Slide(Gravity.START));
            getWindow().setEnterTransition(new Slide(Gravity.BOTTOM));
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
        //retrofitTest();
//        retrofitTest2();

        // 设置插屏广告
//        AdUtil.Companion.setupSpotAd(this);

        //配置rv_homeList
        adapterHome = new AdapterHome(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_homeList.setLayoutManager(layoutManager);
        rv_homeList.setAdapter(adapterHome);

        nav_userImage = nav_headerView.getHeaderView(0).findViewById(R.id.nav_user_image);

        //配置popupwindow
        initPopup();
        initPopupMessageDetail();
        initPopupDefinedPlayer();

        nav_headerView.setCheckedItem(R.id.nav_home);
        requestLatestMoviesData(handler);
        IntentFilter filter = new IntentFilter();
        filter.addAction("cn.jpush.android.intent.REGISTRATION");
        filter.addAction("cn.jpush.android.intent.MESSAGE_RECEIVED");
        filter.addAction("cn.jpush.android.intent.NOTIFICATION_RECEIVED");
        filter.addAction("cn.jpush.android.intent.NOTIFICATION_OPENED");
        filter.addAction("cn.jpush.android.intent.NOTIFICATION_CLICK_ACTION");
        filter.addAction("cn.jpush.android.intent.CONNECTION");
        filter.addCategory("com.example.pc_0775.naugthyvideo");
        registerReceiver(jPushReceiver, filter);
    }

    @Override
    public void setListener() {
        //左侧导航栏的事件监听
        nav_headerView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer_layout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_home:
                        break;
                    case R.id.nav_function:
                        if (Constants.user.isIsVIP()){
                            ActivityFunction.actionStart(ActivityHome.this);
                        }else {
                            showToast("功能暂未开放");
                        }
                        break;
                    case R.id.nav_top250:
//                        startActivity(ActivityPartSlide.class);//部分滑动的Activity,已经用不上这个了
                        ActivityMovieTop250.Companion.actionStart(ActivityHome.this);
                        break;
                    case R.id.nav_card_slide:
                        showToast("功能尚未开放");
                        /*isAllowStartActivityCardSilde = true;
                        if (null != liveInfoList && 0 != liveInfoList.size()) {
                            startActivityCardSilde();
                        }else {
//                            requestMovieListData(handler);
                        }*/
                        break;
                    case R.id.nav_live_card_slide:
                        if (Constants.user.isIsVIP()){
                            startActivity(ActivityLiveCardSilde.class);
                        }else {
                            showToast("功能暂未开放");
                        }
                        break;
                    case R.id.nav_player:
                        /*playerUrlWindow.showBashOfAnchor(drawer_layout, playerUrlGravity, 0, 0);
                        setWindowBlack();*/
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


        nav_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityLogin.class);
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
    protected void onStart() {
        super.onStart();
        setNavigationViewMenu();
    }

    @Override
    public void onBackPressed() {
        //关闭抽屉
        if (drawer_layout.isDrawerOpen(nav_headerView)){
            drawer_layout.closeDrawers();
        }else if (SpotManager.getInstance(ActivityHome.this).isSpotShowing()) {
            // 点击后退关闭插屏广告
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
        unregisterReceiver(jPushReceiver);
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
                window.showBashOfAnchor(rv_homeList, layoutGravity, 0, 0);
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


    /**
     * 请求豆瓣最新电影数据，异步，初始化使用
     *
     */
    private void requestLatestMoviesData(MyHandler myHandler){
        /*HashMap<String, String> parameter = new HashMap<>();
        parameter.put("start", start+"");
        parameter.put("count", count+"");
        Uri latestMovieUri = NetWorkUtil.createUri(Constants.DOUBAN_LATEST_MOVIE_URL, parameter);
        start += count;
        NetWorkUtil.sendRequestWithOkHttp(latestMovieUri.toString(), Constants.DOUBAN_LATEST_MOVIE_REQUEST, myHandler);*/

        MovieLoader movieLoader = new MovieLoader();
        movieLoader.getLatestMovie(start, count)
                .subscribe(new io.reactivex.Observer<List<DoubanMovie.SubjectsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<DoubanMovie.SubjectsBean> subjectsBeans) {
                        subjectsBeanList = subjectsBeans;
                        if (!isInitPaging){
                            initPaging();
                            isInitPaging = true;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        start += count;
    }

    /**
     * 请求豆瓣最新电影数据，同步，后续加载使用
     *
     */
    private DoubanMovie syncRequestLatestMoviesData(){
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("start", start+"");
        parameter.put("count", count+"");
        Uri latestMovieUri = NetWorkUtil.createUri(Constants.DOUBAN_LATEST_MOVIE_URL, parameter);
        start += count;
        return NetWorkUtil.syncRequest(latestMovieUri.toString(), DoubanMovie.class);
    }

    private void startActivityCardSilde(){
        HashMap<String, String> classTowparameters = new HashMap<>();
        classTowparameters.put("yeshu", "1");
        classTowparameters.put("type", "18");
        Uri classTowUri = NetWorkUtil.createUri(Constants.CARTOON_VIDEO_URL, classTowparameters);
        Bundle bundle = new Bundle();
//        bundle.putString("uri", classTowUri.toString());
        bundle.putSerializable("resultList", (Serializable)dataList);
        startActivity(ActivityCardSilde.class, bundle);
    }

    //定义MyDataSource类，继承自DataSource三个子类之一
    private class MyDataSource extends PositionalDataSource<DoubanMovie.SubjectsBean>{

        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<DoubanMovie.SubjectsBean> callback) {
            callback.onResult(subjectsBeanList, 0, count);
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<DoubanMovie.SubjectsBean> callback) {

            callback.onResult(syncRequestLatestMoviesData().getSubjects());
        }
    }

//    private class MyDataSourecs extends ItemKeyedDataSource
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
                .setPageSize(count)    //每页显示的词条数
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(count) //首次加载的数据量
                .setPrefetchDistance(8)     //距离底部还有多少条数据时开始预加载
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
                adapterHome.submitList(subjectsBeans);
            }
        });
    }

    //配置popupwindow
    private void initPopup(){
        window = new CommonPopupWindow(this, R.layout.activity_popup_message, ViewGroup.LayoutParams.WRAP_CONTENT, 400) {
            @Override
            protected void initView() {
                View view = getContentView();
                rv_homePopupMessage = view.findViewById(R.id.rv_home_popup_message);
                BaseQuickAdapter adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_popup_message, initData(3)) {
                    @Override
                    protected void convert(BaseViewHolder helper, String item) {
                        helper.setText(R.id.tv_popup_message_title, item);
                    }
                };
                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Toast.makeText(ActivityHome.this, "点击了第" + (adapter.getData().get(position)), Toast.LENGTH_SHORT).show();
                        window.getPopupWindow().dismiss();
                        messageDetailWindow.showBashOfAnchor(drawer_layout, detaiLayoutGravity, 0, 0);
                        setWindowBlack();
                    }
                });
                LinearLayoutManager manager = new LinearLayoutManager(ActivityHome.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_homePopupMessage.setLayoutManager(manager);
                rv_homePopupMessage.setAdapter(adapter);
            }

            @Override
            protected void initEvent() {

            }
        };
        layoutGravity=new LayoutGravity(LayoutGravity.ALIGN_RIGHT|LayoutGravity.ALIGN_ABOVE);
    }

    private List<String> initData(int number){
        List<String> strList = new ArrayList<>();
        for (int i=0; i<number; i++){
            strList.add("title:"+i+","+number);
        }
        return strList;
    }

    private void  initPopupMessageDetail(){
        // get the height and width of screen
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight=metrics.heightPixels;
        int screenWidth=metrics.widthPixels;

        messageDetailWindow = new CommonPopupWindow(this, R.layout.activity_popup_message_detail, (int)(screenWidth*0.7), (int)(screenHeight*0.5)) {
            @Override
            protected void initView() {

            }

            @Override
            protected void initEvent() {

            }

            @Override
            protected void initWindow() {
                super.initWindow();
                PopupWindow instance = getPopupWindow();
                instance.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp=getWindow().getAttributes();
                        lp.alpha=1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };
        detaiLayoutGravity = new LayoutGravity(LayoutGravity.CENTER_HORI|LayoutGravity.CENTER_VERT);
        messageDetailWindow.getPopupWindow().setAnimationStyle(R.style.animScale);
    }

    /**
     * 初始化播放器地址弹窗
     */
    private void initPopupDefinedPlayer(){
        // get the height and width of screen
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth=metrics.widthPixels;

        playerUrlWindow = new CommonPopupWindow(this, R.layout.activity_pop_player_url, (int)(screenWidth*0.7), ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {

                et_playerUrl = contentView.findViewById(R.id.et_player_url);
                btn_playerUrlYes = contentView.findViewById(R.id.btn_player_url_yes);

                btn_playerUrlYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!et_playerUrl.getText().toString().equals("")){
                            ActivityLivePlay.actionStart(ActivityHome.this, et_playerUrl.getText().toString());
                        }
                    }
                });
            }

            @Override
            protected void initEvent() {

            }

            @Override
            protected void initWindow() {
                super.initWindow();
                PopupWindow instance = getPopupWindow();
                instance.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp=getWindow().getAttributes();
                        lp.alpha=1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };
        playerUrlGravity = new LayoutGravity(LayoutGravity.CENTER_HORI|LayoutGravity.CENTER_VERT);
        playerUrlWindow.getPopupWindow().setAnimationStyle(R.style.animScale);
    }

    //弹窗出现后将背景设置为半透明黑色
    private void setWindowBlack(){
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    /**
     * 动态设置NavigationView的menu
     */
    private void setNavigationViewMenu(){
        MenuItem nav_liveCardSlide = nav_headerView.getMenu().findItem(R.id.nav_live_card_slide);
        MenuItem nav_function = nav_headerView.getMenu().findItem(R.id.nav_function);
        if (Constants.user == null || !Constants.user.isIsVIP()){
            nav_liveCardSlide.setVisible(false);
            nav_liveCardSlide.setEnabled(false);
            nav_function.setVisible(false);
            nav_function.setEnabled(false);
        }else {
            nav_liveCardSlide.setVisible(true);
            nav_liveCardSlide.setEnabled(true);
            nav_function.setVisible(true);
            nav_function.setEnabled(true);
        }
    }

    /*
    * Retrofit测试
    * */
    public void retrofitTest(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/")
                .build();
        BookService bookService = retrofit.create(BookService.class);
        Call<ResponseBody> call = bookService.getBook(1220562);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    Log.e(TAG, s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void retrofitTest2(){
        MovieLoader movieLoader = new MovieLoader();
        movieLoader.getMovieTop250(0, 10).subscribe(new io.reactivex.Observer<List<DoubanMovie.SubjectsBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<DoubanMovie.SubjectsBean> subjectsBeans) {
                Log.d(TAG, "onNext: ");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

}
