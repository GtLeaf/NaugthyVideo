/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.example.pc_0775.naugthyvideo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

import com.example.pc_0775.naugthyvideo.util.LogUtil;

import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.callback.ProgressUpdateCallback;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.model.Message;


public class PhotoView extends android.support.v7.widget.AppCompatImageView implements IPhotoView {

	private final PhotoViewAttacher mAttacher;

	private ScaleType mPendingScaleType;

	//绘制加载效果
	private float per;

	private boolean isfinished = false;


	private Paint paintLayer;
	private Paint textPaint;
	private Message message;

	private Rect textBound;

	public PhotoView(boolean fromChatActivity, Context context) {
		this(fromChatActivity, context, null);

	}

	public PhotoView(boolean fromChatActivity, Context context, AttributeSet attr) {
		this(fromChatActivity, context, attr, 0);
	}
	
	public PhotoView(boolean fromChatActivity, Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		super.setScaleType(ScaleType.MATRIX);
		mAttacher = new PhotoViewAttacher(this, fromChatActivity, context);

		if (null != mPendingScaleType) {
			setScaleType(mPendingScaleType);
			mPendingScaleType = null;
		}
		init();
	}

	private void init(){
		paintLayer = new Paint();
		paintLayer.setColor(Color.LTGRAY);
		paintLayer.setAlpha(100);
		textPaint = new Paint();
		textPaint.setColor(Color.DKGRAY);
		textPaint.setTextSize(25);
		textBound = new Rect();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isfinished){
			return;
		}
		String perStr = (int) (per*100) + "%";
		//获取文字区域的矩形大小，以便确定文字正中间的位置
		textPaint.getTextBounds(perStr, 0, perStr.length(), textBound);
		float layer_w = getWidth();
		float layer_h = getHeight() * per;
		//画遮蔽层
		canvas.drawRect(0, layer_h, layer_w,getHeight(), paintLayer);
		//画文字
		canvas.drawText(perStr, getWidth()/2 - textBound.width()/2, getHeight()/2+ textBound.height()/2, textPaint);
		LogUtil.Companion.d("PhotoView", per+"");
	}

	public void setMessage(Message message, DownloadCompletionCallback callback){
		message.setOnContentDownloadProgressCallback(new ProgressUpdateCallback(){
			@Override
			public void onProgressUpdate(double v) {
				if (v<1.0){
					setPer((float) v);
				}else{
					finish();
				}
			}
		});
		((ImageContent)message.getContent()).downloadOriginImage(message, callback);
	}

	public void setPer(float per){
		this.per = per;
		//在主线程刷新
		postInvalidate();
	}

	public void finish(){
		isfinished = true;
		postInvalidate();
	}

	@Override
	public boolean canZoom() {
		return mAttacher.canZoom();
	}

	@Override
	public RectF getDisplayRect() {
		return mAttacher.getDisplayRect();
	}

	@Override
	public float getMinScale() {
		return mAttacher.getMinScale();
	}

	@Override
	public float getMidScale() {
		return mAttacher.getMidScale();
	}

	@Override
	public float getMaxScale() {
		return mAttacher.getMaxScale();
	}

	@Override
	public float getScale() {
		return mAttacher.getScale();
	}

	@Override
	public ScaleType getScaleType() {
		return mAttacher.getScaleType();
	}

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    @Override
	public void setMinScale(float minScale) {
		mAttacher.setMinScale(minScale);
	}

	@Override
	public void setMidScale(float midScale) {
		mAttacher.setMidScale(midScale);
	}

	@Override
	public void setMaxScale(float maxScale) {
		mAttacher.setMaxScale(maxScale);
	}

	@Override
	// setImageBitmap calls through to this method
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	@Override
	public void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener listener) {
		mAttacher.setOnMatrixChangeListener(listener);
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		mAttacher.setOnLongClickListener(l);
	}

	@Override
	public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener) {
		mAttacher.setOnPhotoTapListener(listener);
	}

	@Override
	public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener) {
		mAttacher.setOnViewTapListener(listener);
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (null != mAttacher) {
			mAttacher.setScaleType(scaleType);
		} else {
			mPendingScaleType = scaleType;
		}
	}

	@Override
	public void setZoomable(boolean zoomable) {
		mAttacher.setZoomable(zoomable);
	}

	@Override
	public void zoomTo(float scale, float focalX, float focalY) {
		mAttacher.zoomTo(scale, focalX, focalY);
	}

	@Override
	protected void onDetachedFromWindow() {
		mAttacher.cleanup();
		super.onDetachedFromWindow();
	}

}