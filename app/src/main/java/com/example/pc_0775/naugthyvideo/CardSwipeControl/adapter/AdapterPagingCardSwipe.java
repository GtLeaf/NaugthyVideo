package com.example.pc_0775.naugthyvideo.CardSwipeControl.adapter;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardShowInfoBean;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.EuropeVideoInfo;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-0775 on 2018/10/10.
 */
//暂时封印该类
public class AdapterPagingCardSwipe extends PagedListAdapter<EuropeVideoInfo, AdapterPagingCardSwipe.ViewHolder>{

    /**
     * 通过glide获取，缓存的图片
     */
    private List<CardShowInfoBean> mCardShowInfoBeanList = new ArrayList<>();

    private static final DiffUtil.ItemCallback<EuropeVideoInfo> mDiffCallback = new EuropeVideoInfoItemCallback();

    public AdapterPagingCardSwipe(){
        super(mDiffCallback);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout rl_layoutCard;
        public ImageView iv_avatar;
        public ImageView iv_dislike;
        public ImageView iv_like;
        public TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_layoutCard = itemView.findViewById(R.id.rl_layout_card);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            iv_dislike = itemView.findViewById(R.id.iv_dislike);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_name = itemView.findViewById(R.id.tv_name);
        }

        public void bindTo(EuropeVideoInfo europeVideoInfo){


        }

        public void clear(){}
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_slide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EuropeVideoInfo europeVideoInfo = getItem(position);
        if (europeVideoInfo != null) {
            holder.bindTo(europeVideoInfo);
        }else {
            holder.clear();
        }
        holder.tv_name.setText(europeVideoInfo.getName());
    }

    private static class EuropeVideoInfoItemCallback extends DiffUtil.ItemCallback<EuropeVideoInfo> {
        @Override
        public boolean areItemsTheSame(EuropeVideoInfo oldItem, EuropeVideoInfo newItem) {
            Log.d("DiffCallback", "areItemsTheSame: ");
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(EuropeVideoInfo oldItem, EuropeVideoInfo newItem) {
            Log.d("DiffCallback","areContentsTheSame");
            return (oldItem == newItem);
        }
    }
}
