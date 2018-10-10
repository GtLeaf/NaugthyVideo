package com.example.pc_0775.naugthyvideo.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardInfoBean;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.adapter.AdapterCardSwipe;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.base.EuropeVideoInfo;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardConfig;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.CardItemTouchHelperCallback;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.OnCardSwipeListener;
import com.example.pc_0775.naugthyvideo.CardSwipeControl.myLayoutManager.CardLayoutManager;
import com.example.pc_0775.naugthyvideo.Anno.annoUtil.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityCardSilde extends BaseActivity {

    //view
    @ViewInject(R.id.dl_card_slide)
    private DrawerLayout dl_cardSlide;
    @ViewInject(R.id.rv_card_slide)
    private RecyclerView rv_cardSlide;
    @ViewInject(R.id.rl_left_layout)
    private RelativeLayout rl_leftLayout;
    @ViewInject(R.id.rl_right_layout)
    private RelativeLayout rl_rightLayout;

    //adapter
    private AdapterCardSwipe adapterCardSwipe;

    //other
    /**
     * 滑动ItemTouchHelper的回调函数
     */
    private CardItemTouchHelperCallback cardCallback;

    private ActionBarDrawerToggle drawerbar;

    private List videoInfoDataList = new ArrayList();
    private List<Integer> list = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();

    //data
    protected List<VideoInfo> videoInfoList;
    private int pageNumber = 1;
    private Uri uri;
    private int requestNum = 0;

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


        adapterCardSwipe = new AdapterCardSwipe(ActivityCardSilde.this, videoInfoDataList);
        rv_cardSlide.setItemAnimator(new DefaultItemAnimator());//??这个是什么方法
        rv_cardSlide.setAdapter(adapterCardSwipe);
        cardCallback = new CardItemTouchHelperCallback(adapterCardSwipe, adapterCardSwipe.getmCardInfoBeanList());

        //ItemTouchHelper的用法
        ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        CardLayoutManager cardLayoutManager = new CardLayoutManager(rv_cardSlide, touchHelper);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_cardSlide.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(rv_cardSlide);
        initDrawer();
    }

    @Override
    public void setListener() {
        cardCallback.setOnCardSwipeListener(new OnCardSwipeListener<CardInfoBean>() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                AdapterCardSwipe.ViewHolder holder = (AdapterCardSwipe.ViewHolder) viewHolder;
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
            public void onSwiped(RecyclerView.ViewHolder viewHolder, CardInfoBean cardInfoBean, int direction) {
                AdapterCardSwipe.ViewHolder holder = (AdapterCardSwipe.ViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                holder.iv_dislike.setAlpha(0f);
                holder.iv_like.setAlpha(0f);
//                Toast.makeText(ActivityCardSilde.this, ItemTouchHelper.LEFT == direction ? "swiped left" : "swiped right", Toast.LENGTH_SHORT).show();
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
        list.add(R.drawable.movie1);
        list.add(R.drawable.movie2);
        list.add(R.drawable.movie3);
        list.add(R.drawable.movie4);
        list.add(R.drawable.movie5);
        list.add(R.drawable.movie6);
        list.add(R.drawable.movie7);
        list.add(R.drawable.nav_icon);
        stringList.add("https://i.postimg.cc/L5zT2CBW/QQ_20171007202548.jpg");
        stringList.add("https://i.postimg.cc/59857TX8/image.png");
        stringList.add("https://i.postimg.cc/8kykSPfG/config.png");
        stringList.add("https://i.postimg.cc/vZnGfrq8/github.png");
        stringList.add("https://i.postimg.cc/mrVJHhs2/Blog.png");
        stringList.add("https://i.postimg.cc/VkRn0SyD/Card_View_border.jpg");
        stringList.add("https://i.postimg.cc/66mHTmVy/Card_View.jpg");
        stringList.add("https://i.postimg.cc/L5zT2CBW/QQ_20171007202548.jpg");
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

}
