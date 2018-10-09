package com.example.pc_0775.naugthyvideo.view;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.partSildeAdapter.CoinAdapter;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.bean.CoinInfo;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.myRecyclerView.HRecyclerView;
import com.example.pc_0775.naugthyvideo.Anno.annoUtil.ViewInjectUtils;
import com.example.pc_0775.naugthyvideo.viewHolder.CommonViewHolder;

import java.util.ArrayList;

public class ActivityPartSlide extends BaseActivity {

    @ViewInject(R.id.hrv_hrecyclerview)
    private HRecyclerView hRecyclerView;

    private ArrayList<CoinInfo> mDataModels;

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_part_slide;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {
        ViewInjectUtils.inject(this);
        mDataModels = new ArrayList<>();
        for(int i=0;i<10000;i++) {
            CoinInfo coinInfo = new CoinInfo();
            coinInfo.name = "USDT";
            coinInfo.priceLast="20.0";
            coinInfo.riseRate24="0.2";
            coinInfo.vol24="10020";
            coinInfo.close="22.2";
            coinInfo.open="40.0";
            coinInfo.bid="33.2";
            coinInfo.ask="19.0";
            coinInfo.amountPercent = "33.3%";
            mDataModels.add(coinInfo);
        }

        hRecyclerView.setHeaderListData(getResources().getStringArray(R.array.right_title_name));

        CoinAdapter adapter = new CoinAdapter(this, mDataModels, R.layout.item_part_slide_layout, new CommonViewHolder.OnItemCommonClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Toast.makeText(ActivityPartSlide.this, "position--->"+position, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemLongClickListener(int position) {
                Toast.makeText(ActivityPartSlide.this, "long clicks", Toast.LENGTH_SHORT).show();
            }
        });
        hRecyclerView.setAdapter(adapter);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
