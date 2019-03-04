package com.example.pc_0775.naugthyvideo.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.base.BaseActivity;
import com.example.pc_0775.naugthyvideo.util.AdFilterTool;
import com.example.pc_0775.naugthyvideo.Anno.annoUtil.ViewInjectUtils;

import static android.view.KeyEvent.KEYCODE_BACK;

public class ActivityVideoPlay extends BaseActivity {

    //view
    @ViewInject(R.id.wv_function_video_play)
    private WebView wv_functionVideoPlay;

    //setting
    private WebSettings webSettings;

    //data
    private String videoUrl;


    @Override
    public void initParams(Bundle params) {
        Bundle bundle = getIntent().getExtras();
        videoUrl = bundle.getString(Constants.INTENT_VIDEO_URL);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_video_play;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {
        ViewInjectUtils.inject(this);

        webSettings = wv_functionVideoPlay.getSettings();
        setWebView();

    }

    @Override
    public void setListener() {

    }

    @Override
    public void widgetClick(View v) throws Exception {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KEYCODE_BACK && wv_functionVideoPlay.canGoBack()){
            wv_functionVideoPlay.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        destroyWebView();
        super.onDestroy();
    }

    //可将String换成URL类型
    public static void actionStart(Context context, String viedoUrl){
        Intent intent = new Intent(context, ActivityVideoPlay.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_VIDEO_URL, viedoUrl);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    //可将String换成URL类型
    public static void actionStart(Context context, String viedoUrl, boolean isFragment){
        Intent intent = new Intent(context, ActivityVideoPlay.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_VIDEO_URL, viedoUrl);
        intent.putExtras(bundle);
        if(isFragment){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        }
        context.startActivity(intent);

    }

    /**
     * 配置webView相关属性
     */
    private void setWebView(){
        wv_functionVideoPlay.loadUrl(videoUrl);
        wv_functionVideoPlay.setWebChromeClient(new WebChromeClient(){
            private ProgressDialog dialog;
            private Context context = ActivityVideoPlay.this;
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(100 == newProgress){
                    closeDialog();
                }else {
                    openDialog(newProgress);
                }
            }

            private void openDialog(int newProgress){
                if(!((Activity)context).isFinishing()){
                    if(null == dialog){
                        dialog = new ProgressDialog(context);
                        dialog.setTitle("加载中...");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setProgress(newProgress);
                        dialog.setCancelable(true);
                        dialog.show();
                    }else {
                        dialog.setProgress(newProgress);
                    }
                }

            }

            private void closeDialog(){
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });

        wv_functionVideoPlay.setWebViewClient(new WebViewClient(){

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                //广告过滤
                if(!AdFilterTool.isAd(ActivityVideoPlay.this, url)){
                    return super.shouldInterceptRequest(view, url);
                }else {
                    return new WebResourceResponse(null, null, null);
                }
//                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String js1 = "if($('.top_box') != null){$('.top_box').remove();}";
                String js2 = "if($('.footer') != null){$('.footer').nextAll().remove();}";
                String js3 = "if($('#title_content_meiyuan') != null){$('#title_content_meiyuan').remove();}";


                wv_functionVideoPlay.loadUrl("javascript:"+js3);
            }
        });

        setWebSettings();
    }

    /**
     * 配置setWebSettings
     */
    private void setWebSettings(){

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);

        //缩放操作
        //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(true);
        //支持缩放，默认为true。是上一句的前提下。
        webSettings.setSupportZoom(true);
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);

        //其他细节操作
        //关闭webview中缓存,无论有无本地缓存，都直接从网络获取
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //支持通过JS打开新窗口
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");
    }
    private void destroyWebView(){
        if (wv_functionVideoPlay != null){
            wv_functionVideoPlay.destroy();
            wv_functionVideoPlay = null;
        }

    }
}
