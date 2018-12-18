package com.example.pc_0775.naugthyvideo.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.transition.TransitionSet
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivity
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import io.vov.vitamio.utils.Base64
import kotlinx.android.synthetic.main.activity_movie_detail.*

class ActivityMovieDetail : BaseActivity() {

    //bean
    var movieInfo:DoubanMovie.SubjectsBean = DoubanMovie.SubjectsBean()

    override fun initParams(params: Bundle?) {
        if (null == params)
            return
        movieInfo = intent.extras.getSerializable("movieInfo") as DoubanMovie.SubjectsBean
    }

    override fun bindLayout(): Int {
        return R.layout.activity_movie_detail
    }

    override fun bindView(): View {
        return LayoutInflater.from(this).inflate(bindLayout(), null);
    }

    override fun initView(view: View?) {
        /**
         * 1、设置相同的TransitionName
         * //已经在xml中设置好
         */
        Glide.with(this)
                .load(movieInfo.images.medium)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(iv_detail_movie)
        tv_detail_movie_average.text = movieInfo.rating.average.toString()
        tv_detail_movie_describe.text = movieInfo.alt
        setTransition()
    }

    override fun setListener() {
    }

    override fun widgetClick(v: View?) {
    }

    override fun doBusiness(mContext: Context?) {
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    /*
     * 设置动画
     * */
    fun setTransition(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            return
        }
        /**
         * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
         */
        window.enterTransition = Fade()
        window.exitTransition = Fade()
        /*
         * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
         * */
        var transitionSet = TransitionSet()
        transitionSet.addTransition(ChangeBounds())
        transitionSet.addTransition(ChangeTransform())
        transitionSet.addTarget(iv_detail_movie)
        transitionSet.addTarget(tv_detail_movie_average)
        transitionSet.addTarget(tv_detail_movie_name)
        window.sharedElementEnterTransition = transitionSet
        window.sharedElementExitTransition = transitionSet
    }

    companion object {

        fun actionStart(context: Context, bundle: Bundle, compat: ActivityOptionsCompat?){
            var intent = Intent(context, ActivityMovieDetail::class.java)
            intent.putExtras(bundle)
            /**
             *4、生成带有共享元素的Bundle，这样系统才会知道这几个元素需要做动画
             */
            if (compat != null){
                ActivityCompat.startActivity(context, intent, compat.toBundle())
            }else{
                context.startActivity(intent)
            }
        }
    }
}

