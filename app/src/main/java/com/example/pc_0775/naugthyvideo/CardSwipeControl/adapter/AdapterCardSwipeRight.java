package com.example.pc_0775.naugthyvideo.CardSwipeControl.adapter;

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
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;

import java.util.List;

/**
 * Created by PC-0775 on 2018/10/15.
 */

public class AdapterCardSwipeRight extends RecyclerView.Adapter<AdapterCardSwipeRight.MyViewHolder>{

    private Context context;

    private List titleList;

    private android.os.Handler handler;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_cardSwipeRightTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_cardSwipeRightTitle = itemView.findViewById(R.id.tv_card_swipe_right_title);
        }
    }

    public AdapterCardSwipeRight(List<String> titleList, Handler handler){
        this.titleList = titleList;
        this.handler = handler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null == context) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_swipe_right, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!(titleList.get(0) instanceof LivePlatform)) {
            return;
        }
        LivePlatform livePlatform = (LivePlatform)titleList.get(position);
        final String url = Constants.LIVE_ROOM_URL+"?url="+livePlatform.getAddress();
        holder.tv_cardSwipeRightTitle.setText(livePlatform.getTitle());
        holder.tv_cardSwipeRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_card_swipe_right_title:
                        Toast.makeText(context, "请稍等...", Toast.LENGTH_SHORT).show();
                        NetWorkUtil.sendRequestWithOkHttp(url, Constants.LIVE_ROOM_REQUEST, handler);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

}
