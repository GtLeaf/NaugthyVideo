package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.partSildeAdapter;

import android.content.Context;

import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.model.bean.CoinInfo;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.viewHolder.CommonViewHolder;

import java.util.List;

/**
 * Created by PC-0775 on 2018/10/2.
 */

public class CoinAdapter extends CommonAdapter<CoinInfo> {

    private CommonViewHolder.OnItemCommonClickListener commonClickListener;

    public CoinAdapter(Context context, List<CoinInfo> dataList, int layoutId,
                       CommonViewHolder.OnItemCommonClickListener listener) {
        super(context, dataList, layoutId);
        commonClickListener = listener;
    }

    @Override
    public void bindData(CommonViewHolder holder, CoinInfo data) {
        holder.setText(R.id.id_name, data.name)
                .setText(R.id.id_tv_price_last, data.priceLast)
                .setText(R.id.id_tv_rise_rate24, data.riseRate24)
                .setText(R.id.id_tv_vol24, data.vol24)
                .setText(R.id.id_tv_close, data.close)
                .setText(R.id.id_tv_open, data.open)
                .setText(R.id.id_tv_bid, data.bid)
                .setText(R.id.id_tv_ask, data.ask)
                .setText(R.id.id_tv_percent, data.amountPercent)
                .setCommonClickListener(commonClickListener);
    }
}
