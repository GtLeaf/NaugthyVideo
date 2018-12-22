package com.example.pc_0775.naugthyvideo.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import cdc.sed.yff.nm.cm.ErrorCode
import cdc.sed.yff.nm.sp.SpotListener
import cdc.sed.yff.nm.sp.SpotManager
import com.example.pc_0775.naugthyvideo.base.BaseActivity

/**
 * Created by PC-0775 on 2018/12/21.
 */
class AdUtil {

    companion object {

        private var TAG="AdUtil"

        /**
         * 有米广告
         * 设置插屏广告
         */
        fun setupSpotAd(context: Context) {
            // 设置插屏图片类型，默认竖图
            //		// 横图
            //		SpotManager.getInstance(mContext).setImageType(SpotManager
            // .IMAGE_TYPE_HORIZONTAL);
            // 竖图
            SpotManager.getInstance(context).setImageType(SpotManager.IMAGE_TYPE_VERTICAL)

            // 设置动画类型，默认高级动画
            //		// 无动画
            //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
            //				.ANIMATION_TYPE_NONE);
            //		// 简单动画
            //		SpotManager.getInstance(mContext)
            //		                    .setAnimationType(SpotManager.ANIMATION_TYPE_SIMPLE);
            // 高级动画
            SpotManager.getInstance(context)
                    .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED)
        }

        /**
         * 展示插屏广告
         */
        fun showSpotAd(context: Context) {
            SpotManager.getInstance(context).showSpot(context, object : SpotListener {

                override fun onShowSuccess() {
                    Log.i(TAG, "插屏展示成功")
                }

                override fun onShowFailed(errorCode: Int) {
                    Log.e(TAG, "插屏展示失败")
                    when (errorCode) {
                        ErrorCode.NON_NETWORK -> showToast(context,"网络异常")
                        ErrorCode.NON_AD -> showToast(context,"暂无插屏广告")
                        ErrorCode.RESOURCE_NOT_READY -> showToast(context,"插屏资源还没准备好")
                        ErrorCode.SHOW_INTERVAL_LIMITED -> showToast(context,"请勿频繁展示")
                        ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE -> showToast(context,"请设置插屏为可见状态")
                        else -> showToast(context,"请稍后再试")
                    }
                }

                override fun onSpotClosed() {
                    Log.d(TAG, "插屏被关闭")
                }

                override fun onSpotClicked(isWebPage: Boolean) {
                    Log.d(TAG, "插屏被点击")
                    Log.i(TAG, String.format("是否是网页广告？%s", if (isWebPage) "是" else "不是"))
                }
            })
        }

         fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }


}