package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.model.bean.MessageEvent;
import com.example.pc_0775.naugthyvideo.model.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.util.GifCacheUtil;
import com.example.pc_0775.naugthyvideo.ui.ActivityIjkLivePlay;
import com.example.pc_0775.naugthyvideo.ui.ActivityLivePlay;
import com.example.pc_0775.naugthyvideo.ui.ActivityVideoPlay;

import org.greenrobot.eventbus.EventBus;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by PC-0775 on 2018/8/21.
 */

public class AdapterFunctionVideo extends PagedListAdapter<VideoInfo ,AdapterFunctionVideo.ViewHolder> {

    private Context mContext;

    private static final DiffUtil.ItemCallback<VideoInfo> mDiffCallback = new AdapterFunctionVideo.videolistItemCallback();

    public AdapterFunctionVideo() {
        super(mDiffCallback);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout ll_videoInfo;
        private GifImageView giv_functionVideoCover;
        private TextView tv_functionVideoCover;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_videoInfo = itemView.findViewById(R.id.ll_video_info);
            giv_functionVideoCover = itemView.findViewById(R.id.giv_function_video_cover);
            tv_functionVideoCover = itemView.findViewById(R.id.tv_function_video_cover);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null == mContext){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_info, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoInfo videoInfo = getItem(position);
        String imgUrl = videoInfo.getImg();
        String title = videoInfo.getTitle()+"--"+position;
        holder.tv_functionVideoCover.setText(title);
        String[] strs = imgUrl.split("[.]");
        //通过后缀判断图片格式
        String pictureFormat = strs[strs.length-1];
        if(pictureFormat.equals("gif")){
            GifCacheUtil.loadImage(holder.giv_functionVideoCover, imgUrl);
        }else {
            Glide.with(mContext).load(imgUrl).into(holder.giv_functionVideoCover);
        }

        holder.ll_videoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, videoInfo.getUrl(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new MessageEvent(videoInfo.getUrl()));
                if (0 == Constants.PLAY_MODE){
                    ActivityVideoPlay.actionStart(mContext, videoInfo.getUrl());
                }else {
                    if (0 == Constants.PLAYER_SELECT){
                        ActivityLivePlay.actionStart(mContext, videoInfo.getUrl());
                    }else {
                        ActivityIjkLivePlay.Companion.actionStart(mContext, videoInfo.getUrl());
                    }
                }

            }
        });
    }

    private static class videolistItemCallback extends DiffUtil.ItemCallback<VideoInfo>{
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
