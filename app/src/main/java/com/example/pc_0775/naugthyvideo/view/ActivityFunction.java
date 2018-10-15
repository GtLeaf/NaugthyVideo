package com.example.pc_0775.naugthyvideo.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Anno.OnClick;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.bean.MessageEvent;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public class ActivityFunction extends BaseActivity {

    //view
    private Button btn_funtcionClassOne;
    private Button btn_funtcionClassTwo;
    private Button btn_funtcionClassThree;
    private Button btn_functionTest;
    private EditText et_functionUrl;

    //other
    private String laster_url = "";

    //handler
    private MyHandler handler = new MyHandler(this);
    private static class MyHandler extends Handler {
        WeakReference<ActivityFunction> weakReference;

        public MyHandler(ActivityFunction activityFunction){
            weakReference = new WeakReference<ActivityFunction>(activityFunction);
        }

        @Override
        public void handleMessage(Message msg) {
            ActivityFunction activity = weakReference.get();
            if(null == msg.obj){
                Toast.makeText(activity, "获取数据失败，请检查网络", Toast.LENGTH_SHORT).show();
                return;
            }
            List<VideoInfo> videoInfoList = NetWorkUtil.parseJsonArray(msg.obj.toString(), VideoInfo.class);
            if(videoInfoList.size() == 0){
                Toast.makeText(activity, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (msg.what){
                case Constants.CLASS_ONE_REQUEST:
                    HashMap parameters = new HashMap();
                    parameters.put("leixing", "1");
                    Uri uri = NetWorkUtil.createUri(Constants.CLASS_ONE_VIDEO_URL, parameters);
                    ActivityFunctionVideo.actionStart(activity, uri, videoInfoList);
                    break;
                case Constants.CLASS_TWO_REQUEST:
                    HashMap classTowparameters = new HashMap();
                    classTowparameters.put("leixing", "movielist1");
                    Uri classTowUri = NetWorkUtil.createUri(Constants.CLASS_TWO_VIDEO_URL, classTowparameters);
                    ActivityFunctionVideo.actionStart(activity, classTowUri, videoInfoList);
                    break;
                case Constants.CLASS_THREE_REQUEST:
                    HashMap classThreeparameters = new HashMap();
                    classThreeparameters.put("leixing", "toupaizipai");
                    Uri classThreeUri = NetWorkUtil.createUri(Constants.CLASS_THREE_VIDEO_URL, classThreeparameters);
                    ActivityFunctionVideo.actionStart(activity, classThreeUri, videoInfoList);
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

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_function;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {
        btn_funtcionClassOne = $(R.id.btn_funtcion_class_one);
        btn_funtcionClassTwo = $(R.id.btn_funtcion_class_two);
        btn_funtcionClassThree = $(R.id.btn_funtcion_class_three);
        btn_functionTest = $(R.id.btn_function_test);
        et_functionUrl = $(R.id.et_function_url);

        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public void setListener() {
        btn_funtcionClassOne.setOnClickListener(this);
        btn_funtcionClassTwo.setOnClickListener(this);
        btn_funtcionClassThree.setOnClickListener(this);
        btn_functionTest.setOnClickListener(this);

        et_functionUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                laster_url = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
//    @OnClick({R.id.btn_funtcion_class_one, R.id.btn_funtcion_class_two, R.id.btn_funtcion_class_three, R.id.btn_function_test})
    public void widgetClick(View v) throws Exception {
        switch (v.getId()){
            case R.id.btn_funtcion_class_one:
                HashMap parameters = new HashMap();
                parameters.put("leixing", "se55");
                parameters.put("yeshu", "1");
                Uri uri = NetWorkUtil.createUri(Constants.CLASS_ONE_VIDEO_URL, parameters);
                NetWorkUtil.sendRequestWithOkHttp(uri.toString(), Constants.CLASS_ONE_REQUEST, handler );
                break;
            case R.id.btn_funtcion_class_two:
                HashMap classTowparameters = new HashMap();
                classTowparameters.put("leixing", "se56");
                classTowparameters.put("yeshu", "1");
                Uri classTowuri = NetWorkUtil.createUri(Constants.CLASS_TWO_VIDEO_URL, classTowparameters);
                NetWorkUtil.sendRequestWithOkHttp(classTowuri.toString(), Constants.CLASS_TWO_REQUEST, handler );
                break;
            case R.id.btn_funtcion_class_three:
                HashMap classThreeparameters = new HashMap();
                classThreeparameters.put("leixing", "se57");
                classThreeparameters.put("yeshu", "1");
                Uri classThreeUri = NetWorkUtil.createUri(Constants.CLASS_THREE_VIDEO_URL, classThreeparameters);
                NetWorkUtil.sendRequestWithOkHttp(classThreeUri.toString(), Constants.CLASS_THREE_REQUEST, handler );
                break;
            case R.id.btn_function_test:
                if (!laster_url.equals("")) {
                    Constants.TEST_VIDEO_URL = laster_url;
                }
                ActivityVideoPlay.actionStart(this.getApplicationContext(), Constants.TEST_VIDEO_URL, true);
                break;
            default:
                break;
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, ActivityFunction.class);
        context.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent){
        laster_url = messageEvent.getMessage();
        et_functionUrl.setText(messageEvent.getMessage());
    }
}
