package com.example.pc_0775.naugthyvideo.adapter.homeAdapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.HomeInfoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-0775 on 2018/9/22.
 */

public class AdapterHomeInfo extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<HomeInfoData> homeInfoList;
    private LayoutInflater layoutInflater;
    /**
     * ListViewHolder中rv_homeListChild1的数据源
     */
    private List<HomeInfoData> homeChild1InfoDataList = new ArrayList(){};


    static class InfoViewHolder extends RecyclerView.ViewHolder{
        //view
        private TextView tv_itemHomeTitle;
        private TextView tv_itemHomeDescribe;

        public InfoViewHolder(View itemView) {
            super(itemView);
            tv_itemHomeTitle = itemView.findViewById(R.id.tv_item_home_describe);
            tv_itemHomeDescribe = itemView.findViewById(R.id.tv_item_home_describe);
        }
    }

    static class ListViewHolder extends RecyclerView.ViewHolder{
        //view
        private RecyclerView rv_homeListChild1;

        public ListViewHolder(View itemView) {
            super(itemView);
            rv_homeListChild1 = itemView.findViewById(R.id.rv_home_list_child1);
        }
    }

    public AdapterHomeInfo(List homeInfoList) {
        this.homeInfoList = homeInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(null == context){
            context = parent.getContext();
            layoutInflater = LayoutInflater.from(context);
        }



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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InfoViewHolder) {
        }
        if (holder instanceof ListViewHolder) {
            initData();
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ((ListViewHolder) holder).rv_homeListChild1.setLayoutManager(layoutManager);
            ((ListViewHolder) holder).rv_homeListChild1.setAdapter(new AdapterHomeInfo(homeChild1InfoDataList));
        }
    }

    @Override
    public int getItemViewType(int position) {

        return homeInfoList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return homeInfoList.size();
    }

    private void initData(){
        for (int i = 0; i<20; i++){
            int type = Constants.ITEM_TYPE.ITEM_TYPE_INFO.ordinal();
            HomeInfoData homeInfoData = new HomeInfoData("title:"+i, "describe:"+i, type);
            homeChild1InfoDataList.add(homeInfoData);
        }
    }

}
