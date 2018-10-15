package com.example.pc_0775.naugthyvideo.CardSwipeControl;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.adapter.AdapterCardSwipe;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.myLayoutManager.CardLayoutManager;

import java.util.List;

/**
 * Created by PC-0775 on 2018/10/6.
 */

public class CardItemTouchHelperCallback<T> extends ItemTouchHelper.Callback {

    private AdapterCardSwipe adapterCardSwipe;
    private List<GlideDrawable> glideDrawableList;

    private RecyclerView.Adapter adapter;
    private List<T> dataList;
    private OnCardSwipeListener<T> onCardSwipeListener;

    public CardItemTouchHelperCallback(@NonNull RecyclerView.Adapter adapter, @NonNull List<T> dataList) {
        this.adapter = adapter;
        this.dataList = dataList;
    }
    public CardItemTouchHelperCallback(@NonNull AdapterCardSwipe adapterCardSwipe) {
        this.adapterCardSwipe = adapterCardSwipe;
    }

    public void setOnCardSwipeListener(OnCardSwipeListener<T> onCardSwipeListener){
        this.onCardSwipeListener = onCardSwipeListener;
    }

    /**
     * 设置滑动类型标记
     * @param recyclerView
     * @param viewHolder
     * @return 返回一个整数类型的标识，用于判断Item那种移动行为是允许的
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof CardLayoutManager){
            swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 拖拽切换Item的回调
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return 如果Item切换了位置，返回true；反之，返回false
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * 划出时会执行
     * @param viewHolder
     * @param direction 左侧划出为:4,右侧划出为:8
     */
    //滑出之后为什么要消除数据？？
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // 移除之前设置的 onTouchListener, 否则触摸滑动会乱了
        viewHolder.itemView.setOnTouchListener(null);
        //删除对应的数据
        int layoutPosition = viewHolder.getLayoutPosition();
        T remove = ((List<T>)adapterCardSwipe.getmCardShowInfoBeanList()).remove(layoutPosition);
        adapterCardSwipe.updateGlideDrawableList();
        adapterNotifyDataSetChanged(adapterCardSwipe);

        // 卡片滑出后回调 OnSwipeListener 监听器
        if (onCardSwipeListener != null) {
            onCardSwipeListener.onSwiped(viewHolder, remove, direction);
        }
        // 当没有数据时回调 OnSwipeListener 监听器
        if (0 == adapterCardSwipe.getItemCount()) {
            if (onCardSwipeListener != null){
                onCardSwipeListener.onSwipedClear();
            }
        }
    }

    /**
     * Item是否支持滑动
     * @return
     *          true  支持滑动操作
     *          false 不支持滑动操作
     */
    //为什么不允许滑动？？
    @Override
    public boolean isItemViewSwipeEnabled() {
//        return super.isItemViewSwipeEnabled();
        return false;
    }

    /**
     * 拖动时会执行的方法
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //得到滑动的阈值, dX是什么：dX,dY为在X、Y方向上的位移距离
            float distance = (float)Math.sqrt(dX*dX+dY*dY);
            float ratio = distance / getThreshold(recyclerView, viewHolder);
//            float ratio = dX / getThreshold(recyclerView, viewHolder);
            // ratio 最大为 1 或 -1
            if (ratio > 1) {
                ratio = 1;
            }else if (ratio < -1){
                ratio = -1;
            }
            // 默认最大的旋转角度为 15 度
            itemView.setRotation(ratio * CardConfig.DEFAULT_ROTATE_DEGREE);
            int childCount = recyclerView.getChildCount();
            // 当数据源个数大于最大显示数时
            if (childCount > CardConfig.DEFAULT_SHOW_ITEM) {
                for (int position = 1; position < childCount - 1; position++){
                    int index = childCount - position - 1;
                    View view = recyclerView.getChildAt(position);
                    // 和之前 onLayoutChildren 是一个意思，不过是做相反的动画
                    //在这一段是哪里的动画？？
                    view.setScaleX(1 - index*CardConfig.DEFAULT_SCALE + Math.abs(ratio)*CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - index*CardConfig.DEFAULT_SCALE + Math.abs(ratio)*CardConfig.DEFAULT_SCALE);
                    //将第四个view(在下方，窗口外)往上推
                    view.setTranslationY((index - Math.abs(ratio))*itemView.getMeasuredHeight()/CardConfig.DEFAULT_TRANSLATE_Y);
                }
            }else {
                // 当数据源个数小于或等于最大显示数时
                //小于时动画会有什么不同：不需要将视野外的下一个布局往上推了，因为没有了
                for (int position = 0; position < childCount-1; position++){
                    int index = childCount - position -1;
                    View view = recyclerView.getChildAt(position);
                    view.setScaleX(1 - index*CardConfig.DEFAULT_SCALE + Math.abs(ratio)*CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - index*CardConfig.DEFAULT_SCALE + Math.abs(ratio)*CardConfig.DEFAULT_SCALE);
                    view.setTranslationY((index - Math.abs(ratio))*itemView.getMeasuredHeight()/CardConfig.DEFAULT_TRANSLATE_Y);
                }
            }
            // 回调监听器
            if (null != onCardSwipeListener) {
                if (0 != ratio) {
                    onCardSwipeListener.onSwiping(viewHolder, ratio, ratio>0 ? CardConfig.SWIPING_RIGHT:CardConfig.SWIPING_LEFT);
                }else {
                    onCardSwipeListener.onSwiping(viewHolder, ratio, CardConfig.SWIPING_NONE);
                }
            }

        }
    }

    //计算公式为什么是这个？？阈值是什么：阈值即为临界点，用户滑动的距离，当用户滑动距离超过屏幕一半时，滑出去。
    private float getThreshold(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){
        return recyclerView.getWidth() * getSwipeThreshold(viewHolder);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setRotation(0f);
    }

    /**
     * 判空，并对不同类型的adapter都可进行notify
     */
    public void adapterNotifyDataSetChanged(RecyclerView.Adapter adapter){
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}
