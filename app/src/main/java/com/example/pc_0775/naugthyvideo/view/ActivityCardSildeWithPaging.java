package com.example.pc_0775.naugthyvideo.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PositionalDataSource;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardConfig;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardItemTouchHelperCallback;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardShowInfoBean;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.OnCardSwipeListener;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.adapter.AdapterCardSwipeMovie;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.myLayoutManager.CardLayoutManager;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.bean.EuropeVideoInfo;

import java.util.ArrayList;
import java.util.List;

public class ActivityCardSildeWithPaging extends BaseActivity {

    //view
    @ViewInject(R.id.dl_card_slide)
    private DrawerLayout dl_cardSlide;
    @ViewInject(R.id.rv_card_slide)
    private RecyclerView rv_cardSlide;
    @ViewInject(R.id.rl_right_layout)
    private RelativeLayout rl_leftLayout;
    @ViewInject(R.id.rl_left_layout)
    private RelativeLayout rl_rightLayout;

    //adapter
    private AdapterCardSwipeMovie adapterCardSwipeMovie;

    //other
    /**
     * 滑动ItemTouchHelper的回调函数
     */
    private CardItemTouchHelperCallback cardCallback;

    private ActionBarDrawerToggle drawerbar;

    //paging分页控件


    //data
    private List videoInfoDataList = new ArrayList();

    private Uri uri;

    @Override
    public void initParams(Bundle params) {
        Bundle bundle = getIntent().getExtras();
        this.uri = Uri.parse(bundle.getString(Constants.INTENT_URI));
        this.videoInfoDataList = (List)bundle.getSerializable(Constants.INTENT_RESULT_LIST);
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

        adapterCardSwipeMovie = new AdapterCardSwipeMovie(this, videoInfoDataList, uri);
        initPaging();
        rv_cardSlide.setItemAnimator(new DefaultItemAnimator());//设置动画
        rv_cardSlide.setAdapter(adapterCardSwipeMovie);
        cardCallback = new CardItemTouchHelperCallback(adapterCardSwipeMovie);

        //ItemTouchHelper的用法
        ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        CardLayoutManager cardLayoutManager = new CardLayoutManager(rv_cardSlide, touchHelper);
        rv_cardSlide.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(rv_cardSlide);
        initDrawer();


    }

    @Override
    public void setListener() {
        //swipe滑动监听事件
        cardCallback.setOnCardSwipeListener(new OnCardSwipeListener<CardShowInfoBean>() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                AdapterCardSwipeMovie.ViewHolder holder = (AdapterCardSwipeMovie.ViewHolder) viewHolder;
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
            public void onSwiped(RecyclerView.ViewHolder viewHolder, CardShowInfoBean cardShowInfoBean, int direction) {
                AdapterCardSwipeMovie.ViewHolder holder = (AdapterCardSwipeMovie.ViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                holder.iv_dislike.setAlpha(0f);
                holder.iv_like.setAlpha(0f);
//                Toast.makeText(ActivityCardSilde.this, ItemTouchHelper.LEFT == direction ? "swiped left" : "swiped right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(ActivityCardSildeWithPaging.this, "data clear", Toast.LENGTH_SHORT).show();
                //这是什么函数：延迟3秒执行线程
                rv_cardSlide.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rv_cardSlide.getAdapter().notifyDataSetChanged();
                    }
                }, 3000L);
            }
        });

    }

    @Override
    public void widgetClick(View v) throws Exception {

    }

    @Override
    public void doBusiness(Context mContext) {
        int count = rv_cardSlide.getAdapter().getItemCount();
        Log.d(TAG, "initView: "+count);
    }


    public void initDrawer(){
        drawerbar = new ActionBarDrawerToggle(this, dl_cardSlide, R.drawable.nav_icon, R.string.dl_open, R.string.dl_close){
            //菜单打开
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            //菜单关闭
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        dl_cardSlide.setDrawerListener(drawerbar);
    }


    private class MyDataSourceFactory extends DataSource.Factory<Integer, EuropeVideoInfo>{

        @Override
        public DataSource<Integer, EuropeVideoInfo> create() {
            return new MyDataSource();
        }
    }

    private class MyDataSource extends PositionalDataSource<EuropeVideoInfo>{


        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<EuropeVideoInfo> callback) {
            callback.onResult(loadData(0, 10), 0, 10);
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<EuropeVideoInfo> callback) {
            callback.onResult(loadData(params.startPosition, 10));
        }
    }


    /**
     * 假设这里需要做一些后台线程的数据加载任务。
     *
     * @param startPosition
     * @param count
     * @return
     */
    private List<EuropeVideoInfo> loadData(int startPosition, int count){
        List<EuropeVideoInfo> list = new ArrayList<>();

        for (int i=0; i<count; i++){
            list.add((EuropeVideoInfo) videoInfoDataList.get((i+startPosition)%videoInfoDataList.size()));
        }
        return list;
    }

    /**
     * 实例化paging相关东西
     */
    public void initPaging(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPrefetchDistance(2)
                .build();
        /**
         * LiveData<PagedList<DataBean>> 用LivePagedListBuilder生成
         * LivePagedListBuilder 用 DataSource.Factory<Integer,DataBean> 和PagedList.Config.Builder 生成
         * DataSource.Factory<Integer,DataBean> 用 PositionalDataSource<DataBean>
         */
        LiveData<PagedList<EuropeVideoInfo>> liveData = new LivePagedListBuilder(new MyDataSourceFactory(), config)
//                .setBoundaryCallback(null)
//                .setFetchExecutor(null)
                .build();
        liveData.observe(this, new Observer<PagedList<EuropeVideoInfo>>() {
            @Override
            public void onChanged(@Nullable PagedList<EuropeVideoInfo> europeVideoInfos) {
//                adapterPagingCardSwipe.submitList(europeVideoInfos);
            }
        });
    }
}
