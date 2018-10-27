package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;

/**
 * Created by PC-0775 on 2018/10/26.
 */

public class AdapterPaging extends PagedListAdapter<VideoInfo, AdapterPaging.ViewHolder> {

    private Context mContext;

    public static final DiffUtil.ItemCallback<VideoInfo> mDiffCallback = new AdapterPaging.VideoInfoItemCallback();

    protected AdapterPaging() {
        super(mDiffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoInfo videoInfo = getItem(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout ll_videoInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_videoInfo = itemView.findViewById(R.id.ll_video_info);
        }
    }


    private static class VideoInfoItemCallback extends DiffUtil.ItemCallback<VideoInfo>{
        @Override
        public boolean areItemsTheSame(VideoInfo oldItem, VideoInfo newItem) {
            return oldItem.getUrl() == newItem.getUrl();
        }

        @Override
        public boolean areContentsTheSame(VideoInfo oldItem, VideoInfo newItem) {
            return (oldItem == newItem);
        }
    }
}
