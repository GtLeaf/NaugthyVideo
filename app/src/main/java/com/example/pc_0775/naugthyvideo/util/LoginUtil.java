package com.example.pc_0775.naugthyvideo.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.pc_0775.naugthyvideo.view.ActivityLogin;

/**
 * Created by PC-0775 on 2018/10/24.
 */

public class LoginUtil extends Activity {
    private int REQUEST_CODE_LOGIN = 1;
    static LoginCallback mCallback;

    public interface LoginCallback{
        void onLogin();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ActivityLogin.class);
    }
}
