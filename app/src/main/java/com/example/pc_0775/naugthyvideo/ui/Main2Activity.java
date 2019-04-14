package com.example.pc_0775.naugthyvideo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;

import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.ui.base.BaseActivity;
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

    private void gotoDetailActivity(Context context, final View avatarImg, final View nameTxt) {
        Intent intent = new Intent(context,Main2Activity.class);
        Pair<View,String> pair1 = Pair.create(avatarImg, ViewCompat.getTransitionName(avatarImg));
        Pair<View,String> pair2 = Pair.create(nameTxt,ViewCompat.getTransitionName(nameTxt));
        /**
         *4、生成带有共享元素的Bundle，这样系统才会知道这几个元素需要做动画
         */
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, pair1, pair2);
        ActivityCompat.startActivity(context,intent,activityOptionsCompat.toBundle());
    }


}
