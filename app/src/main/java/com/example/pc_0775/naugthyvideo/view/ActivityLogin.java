package com.example.pc_0775.naugthyvideo.view;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;


public class ActivityLogin extends BaseActivity{

    //view
    @ViewInject(R.id.et_login_username)
    private EditText et_loginUsername;
    @ViewInject(R.id.et_login_passowrd)
    private EditText et_loginPassowrd;
    @ViewInject(R.id.switch_login_if_show)
    private Switch switch_loginIfShow;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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




    }

    @Override
    public void setListener() {
        switch_loginIfShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //密码可见
                    et_loginPassowrd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }else {
                    //密码不可见
                    et_loginPassowrd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });
    }

    @Override
    public void widgetClick(View v) throws Exception {
        switch (v.getId()){

        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }


}
