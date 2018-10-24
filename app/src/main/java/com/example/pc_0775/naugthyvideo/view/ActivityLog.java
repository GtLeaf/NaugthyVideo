package com.example.pc_0775.naugthyvideo.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.fragment.FragmentFunction;
import com.example.pc_0775.naugthyvideo.fragment.FragmentHome;
import com.example.pc_0775.naugthyvideo.fragment.FragmentUser;

import java.net.MalformedURLException;
import java.net.URL;


public class ActivityLog extends BaseActivity implements FragmentFunction.OnFragmentFunctionInteractionListener,
        FragmentHome.OnFragmentHomeInteractionListener,
        FragmentUser.OnFragmentUserInteractionListener {

    //view
    private TextView tv_homeTabFunction;
    private TextView tv_homeTabHome;
    private TextView tv_homeTabUser;

    /*private LinearLayout ll_homeTabFunction;
    private LinearLayout ll_homeTabHome;
    private LinearLayout ll_homeTabUser;*/

    private FragmentFunction fragmentFunction;
    private FragmentHome fragmentHome;
    private FragmentUser fragmentUser;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {
        tv_homeTabFunction = $(R.id.tv_home_tab_function);
        tv_homeTabHome = $(R.id.tv_home_tab_home);
        tv_homeTabUser = $(R.id.tv_home_tab_user);


        fragmentManager = getFragmentManager();
        fragmentHome = FragmentHome.newInstance("FragmentHome", "FragmentHome");
        fragmentManager.beginTransaction().add(R.id.fl_showContent, fragmentHome).commit();

    }

    @Override
    public void setListener() {
        tv_homeTabFunction.setOnClickListener(this);
        tv_homeTabHome.setOnClickListener(this);
        tv_homeTabUser.setOnClickListener(this);

    }

    @Override
    public void widgetClick(View v) throws Exception {
        reset();//重置fragment页面
        ((TextView)v).setTextColor(getResources().getColor(R.color.voteblue));
        switch (v.getId()){
            case R.id.tv_home_tab_function:
                if(null == fragmentFunction){
                    fragmentFunction = FragmentFunction.newInstance("FragmentFunction", "FragmentFunction");
                    fragmentManager.beginTransaction().add(R.id.fl_showContent, fragmentFunction, null).commit();
                }else
                {
                    fragmentManager.beginTransaction().show(fragmentFunction).commit();
                }
                break;
            case R.id.tv_home_tab_home:
                if(null == fragmentHome){
                    fragmentHome = FragmentHome.newInstance("FragmentHome", "FragmentHome");
                    fragmentManager.beginTransaction().add(R.id.fl_showContent, fragmentHome).commit();
                }else
                {
                    fragmentManager.beginTransaction().show(fragmentHome).commit();
                }
                break;
            case R.id.tv_home_tab_user:
                if(null == fragmentUser){
                    fragmentUser = FragmentUser.newInstance("FragmentUser", "FragmentUser");
                    fragmentManager.beginTransaction().add(R.id.fl_showContent, fragmentUser, null).commit();
                }else
                {
                    fragmentManager.beginTransaction().show(fragmentUser).commit();
                }
                break;
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void onFragmentFunctionInteraction(Uri uri) {
        URL url;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentHomeInteraction(Uri uri) {

    }

    @Override
    public void onFragmentUserInteraction(Uri uri) {

    }

    public void reset(){
        tv_homeTabFunction.setTextColor(getResources().getColor(R.color.table_text_normal_color));
        tv_homeTabHome.setTextColor(getResources().getColor(R.color.table_text_normal_color));
        tv_homeTabUser.setTextColor(getResources().getColor(R.color.table_text_normal_color));

        //隐藏之前的Fragment，防止重影
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (null != fragmentFunction)
        {
            transaction.hide(fragmentFunction);
        }
        if (null != fragmentHome)
        {
            transaction.hide(fragmentHome);
        }
        if (null != fragmentUser)
        {
            transaction.hide(fragmentUser);
        }
        transaction.commit();
    }
}
