package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.model.bean.liveBean.LiveRoomInfo;
import com.example.pc_0775.naugthyvideo.ui.ActivityLivePlay;

import java.util.List;

/**
 * Created by PC-0775 on 2018/10/15.
 */

public class AdapterCardSwipeCollection extends RecyclerView.Adapter<AdapterCardSwipeCollection.MyViewHolder>{

    private Context context;

    private List<LiveRoomInfo> collectionList;

    private int position;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView tv_cardSwipeCollection;
        RelativeLayout rl_swipeCollectionFront;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_cardSwipeCollection = itemView.findViewById(R.id.tv_card_swipe_collection);
            rl_swipeCollectionFront = itemView.findViewById(R.id.rl_swipe_collection_front);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,1,0,"删除");
        }
    }

    public AdapterCardSwipeCollection(List<LiveRoomInfo> collectionList){
        this.collectionList = collectionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null == context) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_swipe_collection, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (!(collectionList.get(position) instanceof LiveRoomInfo)) {
            return;
        }
        final LiveRoomInfo liveRoomInfo = collectionList.get(position);
        holder.tv_cardSwipeCollection.setText(liveRoomInfo.getTitle());
        holder.tv_cardSwipeCollection.setOnClickListener(new View.OnClickListener() {
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
        });
        holder.tv_cardSwipeCollection.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getLayoutPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    /**
     * 移除 OnLongClickListener 监听
     * @param holder
     */
    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
