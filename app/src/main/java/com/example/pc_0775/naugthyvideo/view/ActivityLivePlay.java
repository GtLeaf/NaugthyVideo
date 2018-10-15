package com.example.pc_0775.naugthyvideo.view;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.Anno.annoUtil.ViewInjectUtils;
import com.example.pc_0775.naugthyvideo.R;

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
        initView();
        setListener();
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
            Uri uri = Uri.parse(path);
            vv_liveVitamio.setVideoURI(uri);
            vv_liveVitamio.setMediaController(new MediaController(this));
            vv_liveVitamio.requestFocus();
            vv_liveVitamio.setBufferSize(10240);//设置视频缓冲大小。默认1024KB，单位byte
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
}
