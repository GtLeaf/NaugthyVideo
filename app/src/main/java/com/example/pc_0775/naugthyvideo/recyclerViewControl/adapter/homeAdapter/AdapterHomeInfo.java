package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.homeAdapter;

import android.app.Activity;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
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
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;
import com.example.pc_0775.naugthyvideo.view.ActivityMovieDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-0775 on 2018/9/22.
 */

public class AdapterHomeInfo extends PagedListAdapter<DoubanMovie.SubjectsBean, RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    public static final DiffUtil.ItemCallback<DoubanMovie.SubjectsBean> mDiffCallback = new AdapterHomeInfo.movielistItemCallback();
    /**
     * ListViewHolder中rv_homeListChild1的数据源
     */
    private List<HomeInfoData> homeChild1InfoDataList = new ArrayList(){};

    public AdapterHomeInfo() {
        super(mDiffCallback);
    }

    //纵向Item布局
    static class InfoViewHolder extends RecyclerView.ViewHolder{
        //view
        private LinearLayout ll_itemMoive;
        private ImageView iv_itemHomeMovie;
        private TextView tv_itemHomeTitle;
        private TextView tv_itemHomeAverage;

        public InfoViewHolder(final View itemView) {
            super(itemView);
            tv_itemHomeTitle = itemView.findViewById(R.id.tv_item_home_title);
            tv_itemHomeAverage = itemView.findViewById(R.id.tv_item_home_average);
            iv_itemHomeMovie = itemView.findViewById(R.id.iv_item_home_movie);
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_list_info, parent, false);
        if(viewType == Constants.ITEM_TYPE.ITEM_TYPE_INFO.ordinal()){
            return new InfoViewHolder(layoutInflater.inflate(R.layout.item_home_list_info, parent, false));
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
            infoViewHolder.tv_itemHomeAverage.setText(movie.getRating().getAverage()+"");
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
                        NetWorkUtil.movieHomePositon = position;

                        ActivityMovieDetail.Companion.actionStart(context, bundle, compat);
                    }else {
                        ActivityMovieDetail.Companion.actionStart(context, bundle, compat);
                    }

                }
            });

        }
        if (holder instanceof ListViewHolder) {
            /*initData();
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ((ListViewHolder) holder).rv_homeListChild1.setLayoutManager(layoutManager);
            ((ListViewHolder) holder).rv_homeListChild1.setAdapter(new AdapterHomeInfo(homeChild1InfoDataList));*/

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
