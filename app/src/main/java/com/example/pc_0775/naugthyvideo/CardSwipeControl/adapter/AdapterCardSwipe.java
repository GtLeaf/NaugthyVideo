package com.example.pc_0775.naugthyvideo.CardSwipeControl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardConfig;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.MessageEvent;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.view.ActivityCardSilde;
import com.example.pc_0775.naugthyvideo.view.ActivityVideoPlay;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-0775 on 2018/10/8.
 */

public class AdapterCardSwipe extends Adapter<AdapterCardSwipe.ViewHolder>{

    /**
     * 外部传入的图片地址
     */
//    private List<T> mDataList;
    /**
     * 从网络获取的视频信息
     */
    private List<VideoInfo> mVideoInfoList;
    /**
     * 通过glide获取，缓存的图片
     */
    private List<GlideDrawable> mGlideDrawableList = new ArrayList<>();
    /**
     * 测试数据-纯色
     */
    private List<Integer> list = new ArrayList<>();
    /**
     * 测试数据-网络图片
     */
    private List<String> stringList = new ArrayList<>();

    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout rl_layoutCard;
        public ImageView iv_avatar;
        public ImageView iv_dislike;
        public ImageView iv_like;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_layoutCard = itemView.findViewById(R.id.rl_layout_card);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            iv_dislike = itemView.findViewById(R.id.iv_dislike);
            iv_like = itemView.findViewById(R.id.iv_like);
        }
    }

    public AdapterCardSwipe(Context context, List<VideoInfo> videoInfoList){
        this.mContext = context;
        this.mVideoInfoList = videoInfoList;
        initData();
        updateGlideDrawableList();
    }

//    public void setmDataList(List<T> dataList){
//        this.mDataList = dataList;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_slide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoInfo videoInfo = mVideoInfoList.get(position);
        ImageView iv_avatar = holder.iv_avatar;
        iv_avatar.setImageDrawable(mGlideDrawableList.get(position));
        holder.rl_layoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, videoInfo.getUrl(), Toast.LENGTH_SHORT).show();
                ActivityVideoPlay.actionStart(mContext, videoInfo.getUrl());
            }
        });
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, videoInfo.getUrl(), Toast.LENGTH_SHORT).show();
                ActivityVideoPlay.actionStart(mContext, videoInfo.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGlideDrawableList.size();
    }

    public void updateGlideDrawableList(){
        for (int i = mGlideDrawableList.size(); i < CardConfig.DEFAULT_SHOW_ITEM+1; i++){
            if (mVideoInfoList.size() <= 0) {
                return;
//                initData();
            }
            VideoInfo videoInfo = mVideoInfoList.remove(0);
//            String url = stringList.remove(0);
            Glide.with(mContext)
                    .load(videoInfo.getImg())
//                    .load(url)
                    .into(new SimpleTarget<GlideDrawable>(336, 326) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mGlideDrawableList.add(resource);
                            AdapterCardSwipe.this.notifyDataSetChanged();
                        }
                    });
        }
    }

    public List<GlideDrawable> getmGlideDrawableList(){
        return mGlideDrawableList;
    }

    public void initData(){
        list.add(R.drawable.movie1);
        list.add(R.drawable.movie2);
        list.add(R.drawable.movie3);
        list.add(R.drawable.movie4);
        list.add(R.drawable.movie5);
        list.add(R.drawable.movie6);
        list.add(R.drawable.movie7);
        list.add(R.drawable.nav_icon);
        stringList.add("https://i.postimg.cc/L5zT2CBW/QQ_20171007202548.jpg");
        stringList.add("https://i.postimg.cc/59857TX8/image.png");
        stringList.add("https://i.postimg.cc/8kykSPfG/config.png");
        stringList.add("https://i.postimg.cc/vZnGfrq8/github.png");
        stringList.add("https://i.postimg.cc/mrVJHhs2/Blog.png");
        stringList.add("https://i.postimg.cc/VkRn0SyD/Card_View_border.jpg");
        stringList.add("https://i.postimg.cc/66mHTmVy/Card_View.jpg");
        stringList.add("https://i.postimg.cc/L5zT2CBW/QQ_20171007202548.jpg");
    }
}
