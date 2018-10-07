package com.example.pc_0775.naugthyvideo.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.CardConfig;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.CardItemTouchHelperCallback;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.OnCardSwipeListener;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.myLayoutManager.CardLayoutManager;
import com.example.pc_0775.naugthyvideo.util.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityCardSilde extends BaseActivity {

    //view
    @ViewInject(R.id.rv_card_slide)
    private RecyclerView rv_cardSlide;

    private List<Integer> list = new ArrayList<>();

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_card_silde;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(final View view) {
        ViewInjectUtils.inject(this);
        rv_cardSlide.setItemAnimator(new DefaultItemAnimator());//??这个是什么方法
        rv_cardSlide.setAdapter(new MyAdapetr());
        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(rv_cardSlide.getAdapter(), list);
        cardCallback.setOnCardSwipeListener(new OnCardSwipeListener<Integer>() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                MyAdapetr.MyViewHolder holder = (MyAdapetr.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
                if (CardConfig.SWIPING_LEFT == direction) {
                    holder.iv_dislike.setAlpha(Math.abs(ratio));
                } else if (CardConfig.SWIPING_RIGHT == direction) {
                    holder.iv_like.setAlpha(Math.abs(ratio));
                }else {
                    holder.iv_dislike.setAlpha(0f);
                    holder.iv_like.setAlpha(0f);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, Integer integer, int direction) {
                MyAdapetr.MyViewHolder holder = (MyAdapetr.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                holder.iv_dislike.setAlpha(0f);
                holder.iv_like.setAlpha(0f);
                Toast.makeText(ActivityCardSilde.this, ItemTouchHelper.LEFT == direction ? "swiped left" : "swiped right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(ActivityCardSilde.this, "data clear", Toast.LENGTH_SHORT).show();
                //这是什么函数？？
                rv_cardSlide.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        rv_cardSlide.getAdapter().notifyDataSetChanged();
                    }
                }, 3000L);
            }
        });
        //ItemTouchHelper的用法
        ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        CardLayoutManager cardLayoutManager = new CardLayoutManager(rv_cardSlide, touchHelper);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_cardSlide.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(rv_cardSlide);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void widgetClick(View v) throws Exception {

    }

    @Override
    public void doBusiness(Context mContext) {
        initData();
        int count = rv_cardSlide.getAdapter().getItemCount();
        Log.d(TAG, "initView: "+count);
    }

    public void initData(){
        list.add(R.color.red_dark);
        list.add(R.color.yellow);
        list.add(R.color.blue);
        list.add(R.color.green);
        list.add(R.color.indigo);
        list.add(R.color.paleturquoise);
        list.add(R.color.darkorchid);
        list.add(R.color.pink);
    }

    private class MyAdapetr extends RecyclerView.Adapter<MyAdapetr.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder{

            ImageView iv_avatar;
            ImageView iv_dislike;
            ImageView iv_like;

            public MyViewHolder(View itemView) {
                super(itemView);
                iv_avatar = itemView.findViewById(R.id.iv_avatar);
                iv_dislike = itemView.findViewById(R.id.iv_dislike);
                iv_like = itemView.findViewById(R.id.iv_like);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_slide, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ImageView iv_avatar = holder.iv_avatar;
            iv_avatar.setBackgroundColor(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
