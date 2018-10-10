package com.example.pc_0775.naugthyvideo.CardSwipeControl.myLayoutManager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardConfig;

/**
 * Created by PC-0775 on 2018/10/6.
 */

public class CardLayoutManager extends RecyclerView.LayoutManager{
    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    public CardLayoutManager(@NonNull RecyclerView recyclerView, @NonNull ItemTouchHelper itemTouchHelper){
        this.recyclerView = recyclerView;
        this.itemTouchHelper = itemTouchHelper;
    }


    private <T> T checkIsNull(T t){
        if (null == t) {
            throw new NullPointerException();
        }
        return t;
    }
    //当ViewGroup中所有子View都不捕获Down事件时，将触发ViewGroup自身的onTouch事件
    //点击---dispatch---Ontouch返回值为ture  不执行---ontouchEvent---onclick
    //点击---dispatch---Ontouch返回值为false 执行---ontouchEvent---onclick
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(v);

            if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
                itemTouchHelper.startSwipe(childViewHolder);
            }
            return false;
        }
    };

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        removeAllViews();
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        if (itemCount> CardConfig.DEFAULT_SHOW_ITEM) {
            for(int position = CardConfig.DEFAULT_SHOW_ITEM; position >= 0; position--){
                final View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                //获取放入view后的剩余宽高
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
                //通过设置四条边的位置，将view放在正中间
                layoutDecoratedWithMargins(view, widthSpace/2, heightSpace/2,
                        widthSpace/2+getDecoratedMeasuredWidth(view),
                        heightSpace/2+getDecoratedMeasuredHeight(view));
                if (CardConfig.DEFAULT_SHOW_ITEM == position) {
                    //设置拉伸
                    view.setScaleX(1-(position-1)*CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1-(position-1)*CardConfig.DEFAULT_SCALE);
                    //Y轴平移
                    view.setTranslationY((position-1)*view.getMeasuredHeight()/CardConfig.DEFAULT_TRANSLATE_Y);
                }else if(position > 0){
                    view.setScaleX(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setTranslationY(position*view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                }else{
                    view.setOnTouchListener(onTouchListener);
                }
            }
        }else {
            // 当数据源个数小于或等于最大显示数时
            for (int position = itemCount -1; position >= 0; position--){
                View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
                // recyclerview 布局
                layoutDecoratedWithMargins(view, widthSpace/2, heightSpace/2,
                        widthSpace/2 + getDecoratedMeasuredWidth(view),
                        heightSpace/2 + getDecoratedMeasuredHeight(view));
                if (position > 0) {
                    view.setScaleX(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - position * CardConfig.DEFAULT_SCALE);
                    view.setTranslationY(position * view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                }else {
                    view.setOnTouchListener(onTouchListener);
                }
            }
        }
    }
}
