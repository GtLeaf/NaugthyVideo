package com.example.pc_0775.naugthyvideo.myView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pc_0775.naugthyvideo.adapter.CommonAdapter;

import java.util.ArrayList;

import static com.example.pc_0775.naugthyvideo.util.DensityUtil.dp2px;

/**
 * Created by PC-0775 on 2018/9/27.
 */

public class HRecyclerView extends RelativeLayout {

    private Context context;
    //手指按下时的位置
    private float startX = 0;
    //滑动时和按下时的差值
    private int moveOffsetX = 0;
    //最大可滑动差值
    private int fixX = 0;
    //头部title布局
    private LinearLayout mRightTiTleLayout;
    //展示数据用的RecyclerView
    private RecyclerView dataRecyclerView;
    //RecyclerView的Adapter
    private Object adapter;
    //需要滑动的View集合
    private ArrayList<View> moveViewList = new ArrayList();
    //左边标题集合
    private String[] leftTextList;
    //左边标题的宽度集合
    private int[] leftTextWidthList;
    //右边标题集合
    private String[] rightTitleList = new String[]{};
    //右边标题的宽度集合
    private int[] rightTitleWidthList = null;
    //右边可滑动的总宽度
    private int rightTotalWidth = 0;
    //右边单个view的宽度
    private int mRightItemWidth = 60;
    //左边view的宽度
    private int leftViewWidth = 80;
    //左边view的高度
    private int leftViewHeight = 40;
    //触发拦截手势的最小值
    private int triggerMoveDis = 30;

    public HRecyclerView(Context context) {
        this(context, null);
    }

    public HRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void initView(){
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(createHeadLayout());
        linearLayout.addView(createMoveRecyclerView());
        addView(linearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 创建头部布局
     * @return
     */
    private View createHeadLayout(){
        LinearLayout headLayout = new LinearLayout(getContext());
        headLayout.setGravity(Gravity.CENTER);
        LinearLayout leftLayout = new LinearLayout(getContext());
        addListHeaderTextView(leftTextList[0], leftTextWidthList[0], leftLayout);
        leftLayout.setGravity(Gravity.CENTER);
        headLayout.addView(leftLayout, 0, new ViewGroup.LayoutParams(dp2px(context, leftViewWidth), dp2px(context, leftViewHeight)));

        mRightTiTleLayout = new LinearLayout(getContext());
        for(int i = 0; i < rightTitleList.length; i++){
            addListHeaderTextView(rightTitleList[i], rightTitleWidthList[i], mRightTiTleLayout);
        }
        headLayout.addView(mRightTiTleLayout);
        return headLayout;
    }

    /**
     * 设置头部title单个布局
     * @param headerName
     * @param width
     * @param leftLayout
     * @return
     */
    private TextView addListHeaderTextView(String headerName, int width, LinearLayout leftLayout){
        TextView textView = new TextView(getContext());
        textView.setText(headerName);
        textView.setGravity(Gravity.CENTER);
        leftLayout.addView(textView, width, dp2px(getContext(), 50));
        return textView;
    }

    /**
     * 创建数据展示布局
     * @return
     */
    private View createMoveRecyclerView(){
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        dataRecyclerView = new RecyclerView(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dataRecyclerView.setLayoutManager(layoutManager);
        if (null != adapter) {
            if (adapter instanceof CommonAdapter) {
                dataRecyclerView.setAdapter((CommonAdapter) adapter);
                moveViewList = ((CommonAdapter) adapter).getMoveViewList();
            }
        }

        relativeLayout.addView(dataRecyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return relativeLayout;
    }

    public void setAdapter(Object adapter){
        this.adapter = adapter;
        initView();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = (int) Math.abs(ev.getX() - startX);
                if(offsetX > triggerMoveDis){
                    return true;
                }else {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private int getRightTitleTotalWidth(){
        if (0 == rightTotalWidth) {
            for (int i =0; i<rightTitleWidthList.length; i++){
                rightTotalWidth = rightTotalWidth + rightTitleWidthList[i];
            }
        }
        return rightTotalWidth;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                int offsetX = (int) Math.abs(event.getX() - startX);
                if(offsetX > 30){
                    moveOffsetX = (int)(startX - event.getX() + fixX);
                    if (0 > moveOffsetX){
                        moveOffsetX = 0;
                    }else {
                        //当滑动大于最大宽度时，不在滑动（右边到头了）
                        if((mRightTiTleLayout.getWidth() + moveOffsetX) > rightTotalWidth){
                            moveOffsetX = getRightTitleTotalWidth() - mRightTiTleLayout.getWidth();
                        }
                    }
                }
                //跟随手指向右滚动
                mRightTiTleLayout.scrollTo(moveOffsetX, 0);
                if (null != moveViewList) {
                    for (int i = 0; i<moveViewList.size(); i++){
                        //使每个item随着手指向右滚动
                        moveViewList.get(i).scrollTo(moveOffsetX, 0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                fixX = moveOffsetX;//设置最大水平平移的宽度
                //每次左右滑动都要更新CommonAdapter中的mFixX的值
                ((CommonAdapter)adapter).setFixX(fixX);
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setHeaderListData(String[] headerListData){
        rightTitleList = headerListData;
        rightTitleWidthList = new int[headerListData.length];
        for (int i = 0; i < headerListData.length; i++){
            rightTitleWidthList[i] = dp2px(context, mRightItemWidth);
        }
        leftTextWidthList = new int[]{dp2px(context, leftViewWidth)};
        leftTextList = new String[]{"名称"};
    }
}
