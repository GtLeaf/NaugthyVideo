package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.liveBean.LivePlatform;
import com.example.pc_0775.naugthyvideo.bean.liveBean.LiveRoomInfo;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;
import com.example.pc_0775.naugthyvideo.view.ActivityLivePlay;

import java.util.List;

/**
 * Created by PC-0775 on 2018/10/15.
 */

public class AdapterCardSwipeCollection extends RecyclerView.Adapter<AdapterCardSwipeCollection.MyViewHolder>{

    private Context context;

    private List<LiveRoomInfo> collectionList;

    private Handler handler;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_cardSwipeCollection;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_cardSwipeCollection = itemView.findViewById(R.id.tv_card_swipe_collection);
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

}
