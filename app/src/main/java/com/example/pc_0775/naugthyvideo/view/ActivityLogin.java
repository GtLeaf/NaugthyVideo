package com.example.pc_0775.naugthyvideo.view;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.fragment.FragmentLogin;
import com.example.pc_0775.naugthyvideo.fragment.FragmentRegister;

import cdc.sed.yff.AdManager;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class ActivityLogin extends BaseActivity implements FragmentLogin.OnFragmentInteractionListener{

    //view
    @ViewInject(R.id.fl_login_register)
    private FrameLayout fl_loginRegister;
    @ViewInject(R.id.tv_login)
    private TextView tv_login;
    @ViewInject(R.id.tv_register)
    private TextView tv_register;
    @ViewInject(R.id.btn_register_get_identifying_code)
    private Button btn_registergetidentifyingCode;
    //fragment
    private FragmentManager fragmentManager;
    private FragmentLogin fragmentLogin;
    private FragmentRegister fragmentRegister;


    @Override
    public void initParams(Bundle params) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {

        fragmentManager = getFragmentManager();
        fragmentLogin = new FragmentLogin();
        fragmentManager.beginTransaction().add(R.id.fl_login_register, fragmentLogin).commit();

        tv_login.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void setListener() {
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        /*btn_registergetidentifyingCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    public void widgetClick(View v) throws Exception {
        reset();
        switch (v.getId()){
            case R.id.tv_login:
                ((TextView)v).setTextColor(getResources().getColor(R.color.black));
                if (null == fragmentLogin) {
                    fragmentLogin = new FragmentLogin();
                    fragmentManager.beginTransaction().add(R.id.fl_login_register, fragmentLogin).commit();
                }else {
                    fragmentManager.beginTransaction().show(fragmentLogin).commit();
                }
                break;
            case R.id.tv_register:
                ((TextView)v).setTextColor(getResources().getColor(R.color.black));
                if (null == fragmentRegister){
                    fragmentRegister = new FragmentRegister();
                    fragmentManager.beginTransaction().add(R.id.fl_login_register, fragmentRegister).commit();
                }else {
                    fragmentManager.beginTransaction().show(fragmentRegister).commit();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     *  重置Fragment和TextView的状态
     */
    public void reset(){
        //重置textView的颜色
        tv_login.setTextColor(getResources().getColor(R.color.gray));
        tv_register.setTextColor(getResources().getColor(R.color.gray));

        //隐藏Fragment防止重影
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (null != fragmentLogin) {
            transaction.hide(fragmentLogin);
        }
        if (null != fragmentRegister) {
            transaction.hide(fragmentRegister);
        }
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
