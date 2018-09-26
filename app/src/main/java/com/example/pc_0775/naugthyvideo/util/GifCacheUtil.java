package com.example.pc_0775.naugthyvideo.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.LruCache;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by PC-0775 on 2018/8/21.
 * 图片缓存
 * 参考：https://blog.csdn.net/guangyu_sun/article/details/53024694
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
public class GifCacheUtil {
    static LruCache<String, GifDrawable> cache = new LruCache<>(5);

    public static void loadImage(GifImageView gifImageView, String url){
        GifDrawable gifDrawable = cache.get(url);
        if (gifDrawable != null){
            gifImageView.setImageDrawable(gifDrawable);
        }else {
            new GifImageLoader(gifImageView).execute(url);
        }
    }
}
