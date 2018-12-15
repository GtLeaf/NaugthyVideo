package com.example.pc_0775.naugthyvideo.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.Anno.annoUtil.ViewInjectUtils;

public class Main2Activity extends BaseActivity {

    //view


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initParams(Bundle params) {
        getWindow().setExitTransition(new Slide(Gravity.LEFT));
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main2;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {
        ViewInjectUtils.inject(this);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void widgetClick(View v) throws Exception {

    }

    @Override
    public void doBusiness(Context mContext) {

    }


}
