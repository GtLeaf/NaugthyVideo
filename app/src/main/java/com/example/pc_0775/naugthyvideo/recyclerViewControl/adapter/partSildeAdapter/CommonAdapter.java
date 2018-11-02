package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.partSildeAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.recyclerViewControl.viewHolder.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-0775 on 2018/9/28.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    private LayoutInflater layoutInflater;
    private List<T> dataList;
    private int layoutId;
    private int fixX;
    private ArrayList<View> moveViewList = new ArrayList<>();

    public CommonAdapter(Context context, List<T> dataList, int layoutId){
        layoutInflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.layoutId = layoutId;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(layoutId, parent, false);
        CommonViewHolder holder = new CommonViewHolder(itemView);
        //获取可滑动的view布局
        LinearLayout moveLayout = holder.getView(R.id.id_move_layout);
        //?
        moveLayout.scrollTo(fixX, 0);
        moveViewList.add(moveLayout);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        bindData(holder, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return null == dataList ? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public abstract void bindData(CommonViewHolder holder, T data);

    public ArrayList<View> getMoveViewList(){
        return moveViewList;
    }

    public void setFixX(int fixX){
        this.fixX =fixX;
    }
}
