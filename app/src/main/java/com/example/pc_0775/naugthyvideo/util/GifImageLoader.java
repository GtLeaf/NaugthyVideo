package com.example.pc_0775.naugthyvideo.util;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by PC-0775 on 2018/8/21.
 * 实现网络图片的加载
 * 参考：https://blog.csdn.net/guangyu_sun/article/details/53024694
 */

public class GifImageLoader extends AsyncTask<String, Void, GifDrawable> {

    private GifImageView gifImageView;
    private String url;

    public GifImageLoader(GifImageView gifImageView){
        this.gifImageView = gifImageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        GifImageLoader loader = (GifImageLoader)gifImageView.getTag();
        if(loader != null){
            loader.cancel(false);
        }
        gifImageView.setTag(this);
    }

    @Override
    protected GifDrawable doInBackground(String... params) {
        url = params[0];
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int code = connection.getResponseCode();
            if(200 == code){
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte bytes[] = new byte[10240];
                int len = 0;
                while ((len = inputStream.read(bytes)) != -1){
                    baos.write(bytes, 0, len);
                }
                byte data[] = baos.toByteArray();
                return new GifDrawable(data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(GifDrawable gifDrawable) {
        super.onPostExecute(gifDrawable);
        if(gifDrawable != null){
            try{
                gifImageView.setImageDrawable(gifDrawable);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {

        }
        gifImageView.setTag(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onCancelled(GifDrawable gifFrom) {
        if(gifFrom != null){
            GifCacheUtil.cache.put(url, gifFrom);
        }
    }
}

