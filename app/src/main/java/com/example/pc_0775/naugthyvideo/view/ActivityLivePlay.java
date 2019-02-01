package com.example.pc_0775.naugthyvideo.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.Anno.annoUtil.ViewInjectUtils;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;

import java.io.File;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class ActivityLivePlay extends AppCompatActivity implements MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    @ViewInject(R.id.vv_live_vitamio)
    private VideoView vv_liveVitamio;
    @ViewInject(R.id.tv_buffer_percent)
    private TextView tv_bufferPercent;
    @ViewInject(R.id.tv_net_speed)
    private TextView tv_netSpeed;
    @ViewInject(R.id.pb_live)
    private ProgressBar pb_live;
    private int mVideoLayout = 0;

    //data



    /**
     * 视频播放地址
     */
    private String path = "rtmp://a10.tjial.com/live/910582_3e619d9d2cff84da86a3?auth_key=1539445482-0-0-049b2a45871fba1090996688a15cc84e";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }
        setContentView(R.layout.activity_live_play);
        ViewInjectUtils.inject(this);
        initParams();
        initView();
        setListener();
    }

    public void initParams(){
        path = getIntent().getExtras().getString(Constants.INTENT_VIDEO_URL);

    }

    public void initView() {

        if ("" == path) {
            // Tell the user to provide a media file URL/path.
            Toast.makeText(
                    ActivityLivePlay.this,
                    "Please edit VideoBuffer Activity, and set path"
                            + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
            return;
        }else {
//            Uri uri = Uri.parse(isDownloadAtTheSameTime(path));
            vv_liveVitamio.setVideoPath(path);
            vv_liveVitamio.setMediaController(new MediaController(this));
            vv_liveVitamio.requestFocus();
            vv_liveVitamio.setBufferSize(10240*2);//设置视频缓冲大小10240*5KB。默认1024KB，单位byte
            vv_liveVitamio.setOnInfoListener(this);
            vv_liveVitamio.setOnBufferingUpdateListener(this);
            vv_liveVitamio.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//好像没啥用
            vv_liveVitamio.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // optional need Vitamio 4.0
                    mp.setPlaybackSpeed(1.0f);
                }
            });
        }
    }

    public void setListener() {

    }


    public void changeLayout(View view){
        mVideoLayout++;
        if (4 == mVideoLayout) {
            mVideoLayout = 0;
        }
        switch (mVideoLayout){
            case 0:
                mVideoLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
                break;
        }
        vv_liveVitamio.setVideoLayout(mVideoLayout, 0);
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        tv_bufferPercent.setText(percent + "%");
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what){
            //开始缓冲
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (vv_liveVitamio.isPlaying()) {
                    vv_liveVitamio.pause();
                    pb_live.setVisibility(View.VISIBLE);
                    tv_bufferPercent.setText("");
                    tv_netSpeed.setText("");
                    tv_bufferPercent.setVisibility(View.VISIBLE);
                    tv_netSpeed.setVisibility(View.VISIBLE);
                }
                break;
            //正在缓冲结束
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                vv_liveVitamio.start();
                pb_live.setVisibility(View.GONE);
                tv_bufferPercent.setVisibility(View.GONE);
                tv_netSpeed.setVisibility(View.GONE);
                break;
            //正在缓冲
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                tv_netSpeed.setText(extra + "kb/s");
                break;
        }
        return true;
    }

    public static void actionStart(Context context, String viedoUrl){
        Intent intent = new Intent(context, ActivityLivePlay.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_VIDEO_URL, viedoUrl);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    /**
     * vitamio自带缓存，不能缓存HLS流，已弃用
     * @param path
     * @return
     */
    private String isDownloadAtTheSameTime(String path){
        if (Constants.DOWNLOAD_AT_THE_SAME_TIME){
            File videoFile = new File(Constants.VIDEO_PATH);
            if (!videoFile.exists()){
                videoFile.mkdirs();
            }
            String fileName = "download_"+System.currentTimeMillis()+".mp4";
            path="cache:"+Constants.VIDEO_PATH+fileName+":"+path;
        }

        return path;
    }

}
