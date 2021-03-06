package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pc_0775.naugthyvideo.ui.view.SwipeLayout;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.model.bean.liveBean.LiveRoomInfo;

import java.util.List;

/**
 * Created by PC-0775 on 2018/10/15.
 * 侧滑删除，用户体验不好，已经弃用
 * 博客地址：
 * https://blog.csdn.net/huangxiaoguo1/article/details/54177004?utm_source=blogxgwz0
 */

public class AdapterCardSwipeCollectionDelete_Discarded extends RecyclerView.Adapter<AdapterCardSwipeCollectionDelete_Discarded.MyViewHolder>{

    private Context context;

    private List<LiveRoomInfo> collectionList;

    private SwipeLayout preLayout;//记录上一个打开

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onOpen(SwipeLayout layout);

        void onClose(SwipeLayout layout);

        void onSwiping(SwipeLayout layout);

        void onStartOpen(SwipeLayout layout);

        void onStartClose(SwipeLayout layout);

        void onpLacedTop(int position);

        void onNoRead(int position);

        void onDelete(int position);

        void onItemClick(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        SwipeLayout sl_swipeCollection;
        LinearLayout ll_swipeCollectionBack;
        TextView tv_cardSwipeCollection;
        RelativeLayout rl_swipeCollectionFront;
        TextView tv_swipeCollectionDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_swipeCollectionBack = itemView.findViewById(R.id.ll_swipe_collection_back);
            tv_cardSwipeCollection = itemView.findViewById(R.id.tv_card_swipe_collection);
            rl_swipeCollectionFront = itemView.findViewById(R.id.rl_swipe_collection_front);
            tv_swipeCollectionDelete = itemView.findViewById(R.id.tv_swipe_collection_delete);
            sl_swipeCollection = itemView.findViewById(R.id.sl_swipe_collection);
        }
    }

    public AdapterCardSwipeCollectionDelete_Discarded(List<LiveRoomInfo> collectionList){
        this.collectionList = collectionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null == context) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_swipe_collection_discarded, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (!(collectionList.get(position) instanceof LiveRoomInfo)) {
            return;
        }
        final LiveRoomInfo liveRoomInfo = collectionList.get(position);
        holder.tv_cardSwipeCollection.setText(liveRoomInfo.getTitle());
        /*holder.tv_cardSwipeCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_card_swipe_collection:
                        Toast.makeText(context, "请稍等...", Toast.LENGTH_SHORT).show();
                        ActivityLivePlay.actionStart(context, liveRoomInfo.getAddress());
                        break;
                    default:
                        break;
                }
            }
        });*/
        holder.sl_swipeCollection.setOnSwipeChangeListener(new SwipeLayout.OnSwipeChangeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                preLayout = layout;

                if (onItemClickListener != null) {
                    onItemClickListener.onOpen(layout);
                }
            }

            @Override
            public void onClose(SwipeLayout layout) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClose(layout);
                }
            }

            @Override
            public void onSwiping(SwipeLayout layout) {
                if (onItemClickListener != null) {
                    onItemClickListener.onSwiping(layout);
                }
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                if (preLayout != null) {
                    preLayout.close();
                }
                if (onItemClickListener != null) {
                    onItemClickListener.onStartOpen(layout);
                }
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                if (onItemClickListener != null) {
                    onItemClickListener.onStartClose(layout);
                }
            }
        });
        holder.rl_swipeCollectionFront.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        holder.rl_swipeCollectionFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        holder.tv_swipeCollectionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onDelete(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public SwipeLayout getPreLayout(){
        return preLayout;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
