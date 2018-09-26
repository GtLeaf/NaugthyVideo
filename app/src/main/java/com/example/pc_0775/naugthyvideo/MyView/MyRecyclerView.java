package com.example.pc_0775.naugthyvideo.MyView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * Created by PC-0775 on 2018/9/22.
 */

public class MyRecyclerView extends RecyclerView{
    public MyRecyclerView(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
