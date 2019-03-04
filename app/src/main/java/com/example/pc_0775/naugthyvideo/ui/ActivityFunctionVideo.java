package com.example.pc_0775.naugthyvideo.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PositionalDataSource;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.mmBean.VideoInfoMiMi;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.AdapterFunctionVideo;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ActivityFunctionVideo extends BaseActivity {

    //view
    private RecyclerView rv_functionVideoList;
    private Button btn_functionPre;
    private Button btn_functionNext;
    private TextView tv_functionPageNumber;

    //adapter
    private AdapterFunctionVideo adapterFunctionVideo;

    //Constants
    private static final String INTENT_URI = "uri";
    private static final String INTENT_RESULT_LIST = "resultList";

    //data
    protected List videoInfoList = new ArrayList<>();
    private int pageNumber = 1;
    private Uri uri;
    private int requestNum = 0;
    private int videoIndex = 1;

    //handler
    private MyHandler handler = new MyHandler(this);
    private static class MyHandler extends Handler{
        private WeakReference<ActivityFunctionVideo> weakReference;

        public MyHandler(ActivityFunctionVideo activityFunctionVideo){
            weakReference = new WeakReference<ActivityFunctionVideo>(activityFunctionVideo);
        }

        @Override
        public void handleMessage(Message msg) {
            ActivityFunctionVideo activity = weakReference.get();
            if(null == msg.obj){
                Toast.makeText(activity, "获取数据失败，请检查网络", Toast.LENGTH_SHORT).show();
                return;
            }
            String string = msg.obj.toString();
            VideoInfoMiMi videoInfoMiMi = NetWorkUtil.parseJsonWithGson(string, VideoInfoMiMi.class);
            //可能出现获取到数据为[]的情况
            if(null == videoInfoMiMi){
                if(activity.requestNum >= 5){
                    Toast.makeText(activity, "失败次数过多，请检查网络!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果获得的size为0，则重新获取，最多重试5次
                activity.syncRequestData();
                activity.requestNum++;
                return;
            }
            //请求成功后将累计请求次数归零
            activity.requestNum = 0;

            Toast.makeText(activity, "网络请求成功"+activity.videoInfoList.size(), Toast.LENGTH_SHORT).show();
            activity.videoInfoList = videoInfoMiMi.getVideos();
//            activity.adapterFunctionVideo.notifyDataSetChanged();
        }
    }

    @Override
    public void initParams(Bundle params) {
        //获得Uri进行后网络请求
        this.uri = Uri.parse(getIntent().getStringExtra(INTENT_URI));
        this.videoInfoList = (List) getIntent().getSerializableExtra(INTENT_RESULT_LIST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_function_video;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {
        btn_functionPre = $(R.id.btn_function_per);
        btn_functionNext = $(R.id.btn_function_next);
        tv_functionPageNumber = $(R.id.tv_function_page_number);
        rv_functionVideoList = $(R.id.rv_function_video_list);

        tv_functionPageNumber.setText("-"+pageNumber+"-");
        LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_functionVideoList.setLayoutManager(layoutManager);
        adapterFunctionVideo = new AdapterFunctionVideo();
        rv_functionVideoList.setAdapter(adapterFunctionVideo);

        initPaging();
    }

    @Override
    public void setListener() {
        btn_functionPre.setOnClickListener(this);
        btn_functionNext.setOnClickListener(this);

    }

    @Override
    public void widgetClick(View v) throws Exception {
        switch (v.getId()){
            case R.id.btn_function_per:
                if(pageNumber >1){
                    pageNumber--;
                }
                break;
            case R.id.btn_function_next:
                pageNumber++;
                break;
            default:
                break;
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    public static void actionStart(Context context,Uri uri, List resultList){
        Intent intent = new Intent(context, ActivityFunctionVideo.class);
        //不是很明白，但要加上去，
        // 参考：https://blog.csdn.net/watermusicyes/article/details/44963773
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

        intent.putExtra("uri", uri.toString());
        intent.putExtra("resultList", (Serializable) resultList);
        context.startActivity(intent);
    }


    /**
     * 将VideoInfoMiMi.VideoBean转为VideoInfo
     * @param videoInfoList
     * @return
     */
    private List<VideoInfo> videoMiMiToVideoInfo(List videoInfoList){
        List<VideoInfoMiMi.VideoBean> videoMiMiList;
        List<VideoInfo> list = new ArrayList<>();
        if (videoInfoList.size() == 0) return list;
        if(!(videoInfoList.get(0) instanceof VideoInfoMiMi.VideoBean)){
            return list;
        }
        videoMiMiList = videoInfoList;
        for (VideoInfoMiMi.VideoBean miMi : videoMiMiList){
            list.add(new VideoInfo(miMi.getTitle(), miMi.getVurl(), miMi.getCoverimg(), miMi.getUpdatedate()));
        }
        return list;
    }
    private void asynRequestData(MyHandler handler){
        int currentPageNumber = Integer.parseInt(uri.getQueryParameter(Constants.VIDEO_PARAMTER_PAGEINDEX));
        String url = NetWorkUtil.replace(uri.toString(), Constants.VIDEO_PARAMTER_PAGEINDEX, currentPageNumber+1+"");
        NetWorkUtil.sendRequestWithOkHttp(url, Constants.EUROPE_VIDEO_REQUEST, handler);
    }

    private VideoInfoMiMi syncRequestData(){
        int currentPageNumber = Integer.parseInt(uri.getQueryParameter(Constants.VIDEO_PARAMTER_PAGEINDEX));
        String url = NetWorkUtil.replace(uri.toString(), Constants.VIDEO_PARAMTER_PAGEINDEX, currentPageNumber+1+"");
        return NetWorkUtil.syncRequest(url,  VideoInfoMiMi.class);
    }

    /**
     * Paging中的MyDataSource
     */
    private class MyDataSource extends PositionalDataSource<VideoInfo> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<VideoInfo> callback) {
            List<VideoInfo> dataList = videoMiMiToVideoInfo(videoInfoList);
            callback.onResult(dataList, 0, videoInfoList.size());
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<VideoInfo> callback) {
            callback.onResult(videoMiMiToVideoInfo(syncRequestData().getVideos()));
        }
    }

    /**
     * Paging中的MyDataSourceFactory
     */
    private class MyDataSourceFactory extends DataSource.Factory<Integer, VideoInfo>{

        @Override
        public DataSource<Integer, VideoInfo> create() {
            return new MyDataSource();
        }
    }

    private void initPaging(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(30)
                .setPrefetchDistance(5)
                .build();

        LiveData<PagedList<VideoInfo>> liveData = new LivePagedListBuilder(new MyDataSourceFactory(), config)
                .build();

        liveData.observe(this, new Observer<PagedList<VideoInfo>>() {
            @Override
            public void onChanged(@Nullable PagedList<VideoInfo> videoBeans) {

                adapterFunctionVideo.submitList(videoBeans);
            }
        });
    }
}
