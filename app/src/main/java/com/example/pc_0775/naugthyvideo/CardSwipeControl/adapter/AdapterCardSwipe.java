package com.example.pc_0775.naugthyvideo.CardSwipeControl.adapter;

import android.content.Context;
import android.net.Uri;
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardConfig;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardShowInfoBean;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.ListObservable;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.ListObserver;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.EuropeVideoInfo;
import com.example.pc_0775.naugthyvideo.bean.MessageEvent;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.bean.liveBean.LiveRoomInfo;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;
import com.example.pc_0775.naugthyvideo.view.ActivityVideoPlay;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-0775 on 2018/10/8.
 */

public class AdapterCardSwipe extends Adapter<AdapterCardSwipe.ViewHolder> implements ListObserver {

    /**
     * 外部传入的图片地址
     */
//    private List<T> mDataList;
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
     * 测试数据-网络图片
     */
    private List<String> stringList = new ArrayList<>();

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
    public void onUpdate(List list) {
        mCacheList = list;
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
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            iv_dislike = itemView.findViewById(R.id.iv_dislike);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    public AdapterCardSwipe(Context context, List videoInfoDataList, Uri uri){
        this.mCardInfoDataList = videoInfoDataList;
        this.mContext = context;
        this.uri = uri;
        listObservable = new ListObservable(context);
        listObservable.registerObserver(this);
        updateGlideDrawableList();
    }

//    public void setmDataList(List<T> dataList){
//        this.mDataList = dataList;
//    }

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
        ImageView iv_avatar = holder.iv_avatar;
        iv_avatar.setImageDrawable(mCardShowInfoBeanList.get(position).getGlideDrawable());
        holder.tv_name.setText(mCardShowInfoBeanList.get(position).getVideoInfo().getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mCardShowInfoBeanList.get(position).getVideoInfo().getUrl(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new MessageEvent(mCardShowInfoBeanList.get(position).getVideoInfo().getUrl()));
                ActivityVideoPlay.actionStart(mContext, mCardShowInfoBeanList.get(position).getVideoInfo().getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardShowInfoBeanList.size();
    }

    public void updateGlideDrawableList(){
        for (int i = mCardShowInfoBeanList.size(); i < CardConfig.DEFAULT_SHOW_ITEM+CardConfig.DEFAULT_CACHE_ITEM; i++){
            //当没发送网络请求，而且mCardInfoDataList小于预加载临界数时
            if (!isSendRequest && (mCardInfoDataList.size() <= CardConfig.PER_FETCH_DISTABCE)) {
                int currentPageNumber = Integer.parseInt(uri.getQueryParameter(Constants.PAGE_NUMBER));
                String url = NetWorkUtil.replace(uri.toString(),
                        Constants.PAGE_NUMBER, currentPageNumber+1+"");
                listObservable.setUrl(url);
            }
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
                    .into(new SimpleTarget<GlideDrawable>(336, 326) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            cardShowInfoBean.setGlideDrawable(resource);
                            mCardShowInfoBeanList.add(cardShowInfoBean);
                            AdapterCardSwipe.this.notifyDataSetChanged();
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
        if (object instanceof VideoInfo) {
             cardShowInfoBean.setVideoInfo((VideoInfo) object);
             return cardShowInfoBean;

        }
        if (object instanceof EuropeVideoInfo) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setImg(((EuropeVideoInfo) object).getImage_small());
            videoInfo.setTitle(((EuropeVideoInfo) object).getName());
            videoInfo.setUrl(((EuropeVideoInfo) object).getUrl());

            cardShowInfoBean.setVideoInfo(videoInfo);
            return cardShowInfoBean;
        }
        if (object instanceof LiveRoomInfo) {
        }
        return cardShowInfoBean;
    }

    /**
     * 测试方法
     */
    /*public void initData(){
        stringList.add("https://i.postimg.cc/L5zT2CBW/QQ_20171007202548.jpg");
        stringList.add("https://i.postimg.cc/59857TX8/image.png");
        stringList.add("https://i.postimg.cc/8kykSPfG/config.png");
        stringList.add("https://i.postimg.cc/vZnGfrq8/github.png");
        stringList.add("https://i.postimg.cc/mrVJHhs2/Blog.png");
        stringList.add("https://i.postimg.cc/VkRn0SyD/Card_View_border.jpg");
        stringList.add("https://i.postimg.cc/66mHTmVy/Card_View.jpg");
        stringList.add("https://i.postimg.cc/L5zT2CBW/QQ_20171007202548.jpg");
    }*/

}
