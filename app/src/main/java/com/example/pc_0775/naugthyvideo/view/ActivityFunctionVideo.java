package com.example.pc_0775.naugthyvideo.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.AdapterFunctionVideo;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.util.Constant;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

public class ActivityFunctionVideo extends BaseActivity {

    //view
    private RecyclerView rv_functionVideoList;
    private Button btn_functionPre;
    private Button btn_functionNext;
    private TextView tv_functionPageNumber;

    //adapter
    private AdapterFunctionVideo adapterFunctionVideo;

    //Constant
    private static final String INTENT_URI = "uri";
    private static final String INTENT_RESULT_LIST = "resultList";

    //data
    protected List videoInfoList;
    private int pageNumber = 1;
    private Uri uri;
    private int requestNum = 0;

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
            List<VideoInfo> resultList = NetWorkUtil.parseJsonArray(string, VideoInfo.class);
            //可能出现获取到数据为[]的情况
            if(0 == resultList.size()){
                if(activity.requestNum >= 5){
                    Toast.makeText(activity, "失败次数过多，请检查网络!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果获得的size为0，则重新获取，最多重试5次
                activity.pageChangeRequest(activity.uri);
                activity.requestNum++;
                return;
            }
            //请求成功后将累计请求次数归零
            activity.requestNum = 0;
            //videoInfoList此时不为空,重新为recycler设置数据源
//                    activity.resetRecyclerView();
            Toast.makeText(activity, "网络请求成功"+activity.videoInfoList.size(), Toast.LENGTH_SHORT).show();
            activity.videoInfoList.clear();
            activity.videoInfoList.addAll(resultList);
            activity.adapterFunctionVideo.notifyDataSetChanged();

           
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initParams(Bundle params) {
        //获得Uri进行后网络请求
        this.uri = Uri.parse(getIntent().getStringExtra(INTENT_URI));
        this.videoInfoList = (List) getIntent().getSerializableExtra(INTENT_RESULT_LIST);
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
        adapterFunctionVideo = new AdapterFunctionVideo(videoInfoList);
        rv_functionVideoList.setAdapter(adapterFunctionVideo);

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
                    pageChangeRequest(uri);
                }
                break;
            case R.id.btn_function_next:
                pageNumber++;
                pageChangeRequest(uri);
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
     * 页数发生改变时调用，只依赖传入的uri
     * @param uri
     */
    private void pageChangeRequest(Uri uri){
        if(null != tv_functionPageNumber){
            tv_functionPageNumber.setText("-"+pageNumber+"-");
        }
        Uri videoListUri = uri.buildUpon().appendQueryParameter("yeshu", pageNumber+"").build();
        NetWorkUtil.sendRequestWithOkHttp(videoListUri.toString(), Constant.CLASS_ONE_REQUEST, handler);
    }

    /**
     * 获取到数据时，重新为recycler设置Aadapter
     */
    private void resetRecyclerView(){
        adapterFunctionVideo = new AdapterFunctionVideo(videoInfoList);
        rv_functionVideoList.setAdapter(adapterFunctionVideo);
    }
}
