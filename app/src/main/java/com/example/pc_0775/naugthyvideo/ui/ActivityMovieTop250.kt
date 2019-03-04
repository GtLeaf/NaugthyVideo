package com.example.pc_0775.naugthyvideo.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.paging.DataSource
import android.arch.paging.ItemKeyedDataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.pc_0775.naugthyvideo.Constants.Constants
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivity
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter.AdapterMoiveTop250
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil
import kotlinx.android.synthetic.main.activity_movie_top_250.*
import java.lang.ref.WeakReference

class ActivityMovieTop250 : BaseActivity() {

    //adapter
    private var adapterMoiveTop250 = AdapterMoiveTop250()
    private var manager = LinearLayoutManager(this)

    //data
    private var start = 0
    private val count = 20
    private var movieInfoList = ArrayList< DoubanMovie.SubjectsBean>()

    //paging
    private var isInitPaging = false

    //handelr
    var myhandler = MyHandler(this)
    inner class MyHandler(activityMovieTop250:ActivityMovieTop250):Handler(){

        var weakReference = WeakReference<ActivityMovieTop250>(activityMovieTop250)



        override fun handleMessage(msg: Message?) {
            var activity = weakReference.get()
            //网络状况不好时，返回obj为null
            if (null == msg?.obj)
            {
                Toast.makeText(activity, "获取数据失败，请检查网络", Toast.LENGTH_SHORT).show()
                return
            }

            when(msg?.what){
                Constants.DOUBAN_MOVIE_REQUEST -> {
                    var doubanMovie = NetWorkUtil.parseJsonWithGson<DoubanMovie>(msg.obj.toString(), DoubanMovie::class.java)
                    activity?.movieInfoList?.addAll(doubanMovie.subjects)
                    if (!isInitPaging){
                        initPaging()
                        isInitPaging = true
                    }

                }
            }
        }
    }

    override fun initParams(params: Bundle?) {
    }

    override fun bindLayout(): Int {
        return R.layout.activity_movie_top_250;
    }

    override fun bindView(): View {
        return LayoutInflater.from(this).inflate(bindLayout(), null);
    }

    override fun initView(view: View?) {
        rv_moive_top_250.adapter = adapterMoiveTop250
        rv_moive_top_250.layoutManager = manager

        initPaging()
    }

    override fun setListener() {
    }

    override fun widgetClick(v: View?) {
    }

    override fun doBusiness(mContext: Context?) {
    }

    /*
    * 请求豆瓣电影数据,同步
    * */
    private fun requestMovieTop250ListData():DoubanMovie {
        var doubanMovieList = hashMapOf<String, String>()
        doubanMovieList.put("start", start.toString())
        doubanMovieList.put("movieInfoCount", count.toString())
        val classTowUri = NetWorkUtil.createUri(Constants.DOUBAN_MOVIE_TOP_250_URL, doubanMovieList)
        start += count
        return NetWorkUtil.syncRequest(classTowUri.toString(), DoubanMovie::class.java)
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
            callback.onResult(requestMovieTop250ListData().subjects)
            page++
        }

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<DoubanMovie.SubjectsBean>) {
            //初始请求数据,必须要同步请求
//            callback.onResult(this@ActivityMovieTop250.movieInfoList, 0, this@ActivityMovieTop250.movieInfoList?.size)
            var dataList = requestMovieTop250ListData().subjects
            callback.onResult(dataList, 0, dataList.size)
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
            adapterMoiveTop250.submitList(it)
        })
    }

    companion object {

        fun actionStart(context: Context, bundle: Bundle){
            var intent = Intent(context, ActivityMovieTop250::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        fun actionStart(context: Context){
            var intent = Intent(context, ActivityMovieTop250::class.java)
            context.startActivity(intent)
        }
    }
}
