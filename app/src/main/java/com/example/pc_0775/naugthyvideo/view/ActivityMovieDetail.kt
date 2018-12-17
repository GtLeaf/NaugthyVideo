package com.example.pc_0775.naugthyvideo.view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.paging.DataSource
import android.arch.paging.ItemKeyedDataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.TransitionSet
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivity
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.AdapterMovieDetail
import kotlinx.android.synthetic.main.activity_movie_detail.*

class ActivityMovieDetail : BaseActivity() {


    //bean
    var movieInfo:DoubanMovie.SubjectsBean = DoubanMovie.SubjectsBean()
    var movieInfoList:List<DoubanMovie.SubjectsBean> = ArrayList<DoubanMovie.SubjectsBean>()

    override fun initParams(params: Bundle?) {
        movieInfo = intent.extras.getSerializable("movieInfo") as DoubanMovie.SubjectsBean
        this.movieInfoList = intent.extras.getSerializable("movieInfoList") as List<DoubanMovie.SubjectsBean>
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

        fun actionStart(context: Context, bundle: Bundle, compat: ActivityOptionsCompat){
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

