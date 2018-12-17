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
import android.view.LayoutInflater
import android.view.View
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivity
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.AdapterMovieDetail
import kotlinx.android.synthetic.main.activity_movie_detail_2.*

class ActivityMovieDetail_2 : BaseActivity() {

    //adapter
    var adapterMovieDetail: AdapterMovieDetail = AdapterMovieDetail(this)
    var manager: LinearLayoutManager = LinearLayoutManager(this)

    //bean
    var movieInfo: DoubanMovie.SubjectsBean = DoubanMovie.SubjectsBean()
    var movieInfoList:List<DoubanMovie.SubjectsBean> = ArrayList<DoubanMovie.SubjectsBean>()

    override fun initParams(params: Bundle?) {
        movieInfo = intent.extras.getSerializable("movieInfo") as DoubanMovie.SubjectsBean
        this.movieInfoList = intent.extras.getSerializable("movieInfoList") as List<DoubanMovie.SubjectsBean>
    }

    override fun bindLayout(): Int {
        return R.layout.activity_movie_detail_2
    }

    override fun bindView(): View {
        return LayoutInflater.from(this).inflate(bindLayout(), null);
    }

    override fun initView(view: View?) {
        /**
         * 1、设置相同的TransitionName
         * //已经在xml中设置好
         */

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            /**
             * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
             */
            /*window.enterTransition = Fade()
            window.exitTransition = Fade()*/
            /**
             * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
             */
        }

        //设置adapter
        rv_movie_detail.layoutManager = manager
        rv_movie_detail.adapter = adapterMovieDetail
        manager.orientation = LinearLayoutManager.HORIZONTAL
        initPaging()
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

    companion object {

        fun actionStart(context: Context, bundle: Bundle, compat: ActivityOptionsCompat){
            var intent = Intent(context, ActivityMovieDetail_2::class.java)
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

    /*
    * Paging组件之一MovieInfoDataSource
    * */
    inner class MovieInfoDataSource: ItemKeyedDataSource<Int, DoubanMovie.SubjectsBean>(){

        var page:Int = 1

        override fun getKey(item: DoubanMovie.SubjectsBean): Int {
            return page
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<DoubanMovie.SubjectsBean>) {

        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<DoubanMovie.SubjectsBean>) {
            //请求后续数据，异步
            page++
        }

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<DoubanMovie.SubjectsBean>) {
            //初始请求数据,必须要同步请求
            callback.onResult(this@ActivityMovieDetail_2.movieInfoList, 0, this@ActivityMovieDetail_2.movieInfoList?.size)
        }

    }

    /*
    * Paging组件之一MovieInfoFactory
    * */
    inner class MovieInfoFactory: DataSource.Factory<Int, DoubanMovie.SubjectsBean>(){

        var sourceLiveData = MutableLiveData<MovieInfoDataSource>()

        override fun create(): DataSource<Int, DoubanMovie.SubjectsBean> {
            val dataSource = MovieInfoDataSource()
            sourceLiveData.postValue(dataSource)
            return dataSource
        }

    }

    /*
    * 实例化Paging的方法
    * */
    fun initPaging(){
        val sourceFactory = MovieInfoFactory()
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(20)
                .setPageSize(20)
                .setPrefetchDistance(10)
                .build()
        val pagedList: LiveData<PagedList<DoubanMovie.SubjectsBean>> = LivePagedListBuilder(sourceFactory, pagedListConfig).build()
        pagedList.observe(this, Observer {
            adapterMovieDetail.submitList(it)
        })
    }
}