package com.example.pc_0775.naugthyvideo.other.CardSwipeControl.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.pc_0775.naugthyvideo.other.CardSwipeControl.CardConfig;
import com.example.pc_0775.naugthyvideo.other.CardSwipeControl.CardShowInfoBean;
import com.example.pc_0775.naugthyvideo.other.CardSwipeControl.ListObservable;
import com.example.pc_0775.naugthyvideo.other.CardSwipeControl.ListObserver;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.model.bean.MessageEvent;
import com.example.pc_0775.naugthyvideo.model.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.model.bean.liveBean.LiveRoomInfo;
import com.example.pc_0775.naugthyvideo.model.bean.mmBean.LiveRoomMiMi;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;
import com.example.pc_0775.naugthyvideo.ui.ActivityLivePlay;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-0775 on 2018/10/8.
 */

public class AdapterCardSwipeMovie extends Adapter<AdapterCardSwipeMovie.ViewHolder> implements ListObserver<LiveRoomMiMi> {

    /**
     * 从网络获取的视频信息
     */
    private List mCardInfoDataList;
    /**
     * 缓存数组
     */
    private List mCacheList = new ArrayList();
    /**
     * 通过glide获取，缓存的图片
     */
    private List<CardShowInfoBean> mCardShowInfoBeanList = new ArrayList<>();

    /**
     * 数据源地址
     */
    private Uri uri;

    private ListObservable listObservable;

    /**
     * 是否发送网络请求，防止重复发送
     */
    private boolean isSendRequest = false;
    private Context mContext;

    @Override
    public void onUpdate(LiveRoomMiMi liveRoomMiMi) {
        mCacheList = liveRoomMiMi.getLives();
        if (isSendRequest) {
            updateGlideDrawableList();
        }
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
            iv_avatar = itemView.findViewById(R.id.iv_detail_movie_img);
            iv_dislike = itemView.findViewById(R.id.iv_dislike);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    public AdapterCardSwipeMovie(Context context, List videoInfoDataList, Uri uri){
        this.mCardInfoDataList = videoInfoDataList;
        this.mContext = context;
        this.uri = uri;
        listObservable = new ListObservable(context);
        listObservable.registerObserver(this);
        updateGlideDrawableList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null == mContext) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card_slide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CardShowInfoBean cardShowInfo = mCardShowInfoBeanList.get(position);
        holder.iv_avatar.setImageDrawable(mCardShowInfoBeanList.get(position).getDrawable());
        holder.tv_name.setText(mCardShowInfoBeanList.get(position).getVideoInfo().getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, cardShowInfo.getVideoInfo().getUrl(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new MessageEvent(cardShowInfo.getVideoInfo().getUrl()));
                ActivityLivePlay.actionStart(mContext, cardShowInfo.getVideoInfo().getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardShowInfoBeanList.size();
    }

    //请求缓存List和请求缓存图片都在这，这个类太复杂了，需优化
    public void updateGlideDrawableList(){
        for (int i = mCardShowInfoBeanList.size(); i < CardConfig.DEFAULT_SHOW_ITEM+CardConfig.DEFAULT_CACHE_ITEM; i++){

            requestCacheData();

            if (mCardInfoDataList.size() <= 0) {
                if (mCacheList.size() <= 0) {
                    return;
                }
                mCardInfoDataList.addAll(mCacheList);
                return;
            }
            Object object = mCardInfoDataList.remove(0);
            final CardShowInfoBean cardShowInfoBean = setcardShowInfoBean(object);
            Glide.with(mContext)
                    .load(cardShowInfoBean.getVideoInfo().getImg())
                    .into(new SimpleTarget<Drawable>(336, 326) {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            cardShowInfoBean.setDrawable(resource);
                            mCardShowInfoBeanList.add(cardShowInfoBean);
                            AdapterCardSwipeMovie.this.notifyDataSetChanged();
                        }
                    });
        }
    }

    public List<CardShowInfoBean> getmCardShowInfoBeanList(){
        return mCardShowInfoBeanList;
    }

    /**
     * 根据list中不同的类型，选取内容装入cardShowInfoBean中
     * @param object
     * @return
     */
    private CardShowInfoBean setcardShowInfoBean(Object object){
        CardShowInfoBean cardShowInfoBean = new CardShowInfoBean();
        //LiveRoomInfo
        if (object instanceof LiveRoomInfo) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setImg(((LiveRoomInfo) object).getImg());
            videoInfo.setUrl((((LiveRoomInfo) object).getAddress()));
            videoInfo.setTitle(((LiveRoomInfo) object).getTitle());

            cardShowInfoBean.setType(Constants.LIVE_TYPE);
            cardShowInfoBean.setVideoInfo(videoInfo);
        }
        return cardShowInfoBean;
    }

    private void requestCacheData(){
        //当没发送网络请求，而且mCardInfoDataList小于预加载临界数时
        if (!isSendRequest && (mCardInfoDataList.size() <= CardConfig.PER_FETCH_DISTABCE)) {
            //uri为空不必下一页
            if (null == uri) {
                return;
            }
            int currentPageNumber = Integer.parseInt(uri.getQueryParameter(Constants.PAGE_NUMBER));
            String url = NetWorkUtil.replace(uri.toString(),
                    Constants.PAGE_NUMBER, currentPageNumber+1+"");
            listObservable.setUrl(url);
        }
    }

}
