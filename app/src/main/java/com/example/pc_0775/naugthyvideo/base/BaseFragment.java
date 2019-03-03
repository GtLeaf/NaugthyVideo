package com.example.pc_0775.naugthyvideo.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    protected View view;
    LayoutInflater inflater;
    protected final String TAG = this.getClass().getSimpleName();
    /**
     * Fragment附属的Activity
     */
    protected Context mContext;
    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        initParams(bundle);
        view = bindView(inflater, container);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mIsPrepare = true;
        setListener();
        doBusiness();
    }

    protected abstract void initParams(Bundle bundle);

    protected abstract View bindView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initView(final View view);

    protected abstract void setListener();

    protected abstract void widgetClick(View v);

    protected abstract void doBusiness();

    /**
     * [绑定控件]
     *
     * @param resId
     *
     * @return
     */
    protected <T extends View> T $(int resId) {
        return (T) view.findViewById(resId);
    }

    protected void showToast(String msg) {
        Toast.makeText(mContext,msg, Toast.LENGTH_SHORT).show();
    }

    public void startActivity(Class<?> clz) {
        startActivity(new Intent(getActivity(),clz));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
