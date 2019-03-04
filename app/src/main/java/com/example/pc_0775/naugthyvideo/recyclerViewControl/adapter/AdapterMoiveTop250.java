package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter;

import android.app.Activity;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.HomeInfoData;
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie;
import com.example.pc_0775.naugthyvideo.ui.ActivityMovieDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-0775 on 2018/9/22.
 */

public class AdapterMoiveTop250 extends PagedListAdapter<DoubanMovie.SubjectsBean, RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    public static final DiffUtil.ItemCallback<DoubanMovie.SubjectsBean> mDiffCallback = new AdapterMoiveTop250.movielistItemCallback();
    /**
     * ListViewHolder中rv_homeListChild1的数据源
     */
    private List<HomeInfoData> homeChild1InfoDataList = new ArrayList(){};

    public AdapterMoiveTop250() {
        super(mDiffCallback);
    }

    //纵向Item布局
    static class InfoViewHolder extends RecyclerView.ViewHolder{
        //view
        private LinearLayout ll_itemMoive;
        private ImageView iv_itemHomeMovie;
        private TextView tv_itemHomeTitle;
        private TextView tv_itemHomeAverage;
        private TextView tv_itemTop250Direct;

        public InfoViewHolder(final View itemView) {
            super(itemView);
            tv_itemHomeTitle = itemView.findViewById(R.id.tv_item_top250_title);
            tv_itemHomeAverage = itemView.findViewById(R.id.tv_item_home_average);
            iv_itemHomeMovie = itemView.findViewById(R.id.iv_item_home_movie);
            tv_itemTop250Direct = itemView.findViewById(R.id.tv_item_top250_direct);
            ll_itemMoive = itemView.findViewById(R.id.ll_item_moive);

        }
    }

    //横向listViewItem布局
    static class ListViewHolder extends RecyclerView.ViewHolder{
        //view
        private RecyclerView rv_homeListChild1;

        public ListViewHolder(View itemView) {
            super(itemView);
            rv_homeListChild1 = itemView.findViewById(R.id.rv_home_list_child1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(null == context){
            context = parent.getContext();
            layoutInflater = LayoutInflater.from(context);
        }

        //不同的Item有不一样的布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_top250, parent, false);
        if(viewType == Constants.ITEM_TYPE.ITEM_TYPE_INFO.ordinal()){
            return new InfoViewHolder(layoutInflater.inflate(R.layout.item_movie_top250, parent, false));
        }
        if (viewType == Constants.ITEM_TYPE.ITEM_TYPE_LIST.ordinal()) {
            return new ListViewHolder(layoutInflater.inflate(R.layout.item_home_list, parent, false));
        }
        return new ListViewHolder(layoutInflater.inflate(R.layout.item_home_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof InfoViewHolder) {
            final DoubanMovie.SubjectsBean movie = getItem(position);
            final InfoViewHolder infoViewHolder = ((InfoViewHolder) holder);
            Glide.with(context).load(movie.getImages().getMedium()).into(((InfoViewHolder) holder).iv_itemHomeMovie);
            infoViewHolder.tv_itemHomeTitle.setText(movie.getTitle());

            //评分，大于8分设置为金色
            infoViewHolder.tv_itemHomeAverage.setText(movie.getRating().getAverage()+"");
            if (movie.getRating().getAverage() > 8.0){
                infoViewHolder.tv_itemHomeAverage.setTextColor(ContextCompat.getColor(context, R.color.gold));
            }else{
                infoViewHolder.tv_itemHomeAverage.setTextColor(ContextCompat.getColor(context, R.color.gray));
            }


            //导演，拼接导演们的名字
            String directStr = "";
            Boolean isFirstDirect = true;
            for (DoubanMovie.SubjectsBean.DirectorsBean direct : movie.getDirectors()){
                //第一个导演名字前不用加分隔符
                directStr += isFirstDirect ? "导演："+direct.getName() : "/"+direct.getName();
                isFirstDirect = false;
            }
            infoViewHolder.tv_itemTop250Direct.setText(directStr);

            //设置点击事件
            infoViewHolder.ll_itemMoive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat compat = null;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movieInfo", movie);
                    List movieInfoList = new ArrayList<DoubanMovie.SubjectsBean>();
                    movieInfoList.addAll(getCurrentList());
                    bundle.putSerializable("movieInfoList", (Serializable) movieInfoList);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
                        Pair imagePair = Pair.create(infoViewHolder.iv_itemHomeMovie, infoViewHolder.iv_itemHomeMovie.getTransitionName());
                        Pair namePair = Pair.create(infoViewHolder.tv_itemHomeTitle, infoViewHolder.tv_itemHomeTitle.getTransitionName());
                        Pair averagePair = Pair.create(infoViewHolder.tv_itemHomeAverage, infoViewHolder.tv_itemHomeAverage.getTransitionName());
                        compat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, imagePair, namePair, averagePair);
                    }
                    ActivityMovieDetail.Companion.actionStart(context, bundle, compat);
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        //此处可以拓展不同的itemView
        return Constants.ITEM_TYPE.ITEM_TYPE_INFO.ordinal();
    }

    private static class movielistItemCallback extends DiffUtil.ItemCallback<DoubanMovie.SubjectsBean>{
        @Override
        public boolean areItemsTheSame(DoubanMovie.SubjectsBean oldItem, DoubanMovie.SubjectsBean newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(DoubanMovie.SubjectsBean oldItem, DoubanMovie.SubjectsBean newItem) {
            return (oldItem == newItem);
        }
    }

}
