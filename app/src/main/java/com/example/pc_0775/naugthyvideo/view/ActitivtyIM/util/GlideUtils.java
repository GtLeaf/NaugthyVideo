package com.example.pc_0775.naugthyvideo.view.ActitivtyIM.util;

import android.content.Context;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.pc_0775.naugthyvideo.R;


public class GlideUtils {

 	public static void loadChatImage(final Context mContext, String imgUrl, final ImageView imageView) {


		Glide.with(mContext)
				.load(imgUrl) // 图片地址
				.asBitmap()
				.placeholder(R.mipmap.default_img_failed)// 正在加载中的图片
				.error(R.mipmap.default_img_failed)// 加载失败的图片
				.into(new SimpleTarget<Bitmap>() {
					@Override
					public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
						ImageSize imageSize = BitmapUtil.getImageSize(resource);
						RelativeLayout.LayoutParams imageLP =(RelativeLayout.LayoutParams )(imageView.getLayoutParams());
						imageLP.width = imageSize.getWidth();
						imageLP.height = imageSize.getHeight();
						imageView.setLayoutParams(imageLP);

						Glide.with(mContext)
								.load(resource)
								.placeholder(R.mipmap.default_img_failed)// 正在加载中的图片
								.error(R.mipmap.default_img_failed)// 加载失败的图片
								.into(imageView);
					}
				});
 	}

}