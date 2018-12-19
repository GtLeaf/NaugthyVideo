package com.example.pc_0775.naugthyvideo.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.transition.TransitionSet
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pc_0775.naugthyvideo.Constants.Constants
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.base.BaseActivity
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovieDetail
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovieEntry
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.lang.ref.WeakReference

class ActivityMovieDetail : BaseActivity() {

    //bean
    private var movieInfo:DoubanMovie.SubjectsBean = DoubanMovie.SubjectsBean()
    private var movieDetail: DoubanMovieDetail = DoubanMovieDetail()
    private var movieEntry: DoubanMovieEntry = DoubanMovieEntry()

    //sharePreferences本地存储
    private var preferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    //handler
    private var handler = MyHandler(this)
    class MyHandler(activity:ActivityMovieDetail) : Handler(){

        private var weakreference:WeakReference<ActivityMovieDetail>? = null

        init {
            weakreference = WeakReference(activity)
        }

        override fun handleMessage(msg: Message?) {
            val activity = weakreference?.get()
            if (null == msg?.obj){
                return
            }
            when(msg.what){
                Constants.DOUBAN_MOVIE_DETAIL_REQUEST -> {
                    activity?.movieDetail = NetWorkUtil.parseJsonWithGson(msg.obj.toString(), DoubanMovieDetail::class.java)
                    activity?.initViewAfterDetailData()
                }
                Constants.DOUBAN_MOVIE_ENTRY_REQUEST -> {
                    activity?.movieEntry = NetWorkUtil.parseJsonWithGson(msg.obj.toString(), DoubanMovieEntry::class.java)
                    if (null == activity?.movieEntry){
                        return
                    }
                    activity?.cacheMovieEntry(activity?.movieEntry)
                    activity?.initViewAfterEntryData()
                }
            }
        }
    }

    override fun initParams(params: Bundle?) {
        if (null == params)
            return
        movieInfo = intent.extras.getSerializable("movieInfo") as DoubanMovie.SubjectsBean
    }

    override fun bindLayout(): Int {
        return R.layout.activity_movie_detail
    }

    override fun bindView(): View {
        return LayoutInflater.from(this).inflate(bindLayout(), null)
    }

    override fun initView(view: View?) {

        //电影海报
        Glide.with(this)
                .load(movieInfo.images.medium)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(iv_detail_movie)

        //电影标题名
        tv_detail_movie_title.text = movieInfo.title
        //电影原名,如果原名和标题名相同，则不需要
        tv_detail_movie_original_title.text = movieInfo.original_title
        if (movieInfo.title.equals(movieInfo.original_title)){
            tv_detail_movie_original_title.visibility = View.GONE
        }

        //电影的评分,当平均分大于8分是，为金色
        tv_detail_movie_average.text = movieInfo.rating.average.toString()
        if (movieInfo.rating.average > 8.0){
            tv_detail_movie_average.setTextColor(ContextCompat.getColor(this, R.color.gold))
        }else{
            tv_detail_movie_average.setTextColor(ContextCompat.getColor(this, R.color.gray))
        }
        tv_detail_movie_summary.text = movieInfo.alt

        //导演，拼接导演们的名字
        var directStr = ""
        var isFirstDirect = true
        for (direct in movieInfo.directors){
            //第一个导演名字前不用加分隔符
            directStr += if(isFirstDirect) "导演："+direct.name else "/"+direct.name
            isFirstDirect = false
        }

        //演员阵容：
        var castsStr = ""
        var isFirstCast = true
        for (cast in movieInfo.casts){
            //第一个演员名字前不用加分隔符
            castsStr += if(isFirstCast) "演员阵容："+ cast.name else "/"+ cast.name
            isFirstCast = false
        }
        tv_detail_movie_casts.text = castsStr

        //电影类型：
        var genreStr = ""
        var isFirstGenre = true
        for (genre in movieInfo.genres){
            //第一个类型名字前不用加分隔符
            genreStr += if(isFirstGenre) "类型："+ genre else "/"+ genre
            isFirstGenre = false
        }
        tv_detail_movie_genre.text = genreStr

        //电影简介:
        tv_detail_movie_direct.text = directStr

        //电影评分人数
        tv_detail_ratings_count.text = ""

        //上映时间:

        //影片时长:

        //设置动画
        setTransition()
        //请求电影详情信息
        requestMovieDetaildata()
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
    private fun setTransition(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            return
        }
        /**
         * 1、设置相同的TransitionName
         * //已经在xml中设置好
         */
        /**
         * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
         */
        window.enterTransition = Fade()
        window.exitTransition = Fade()
        /*
         * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
         * */
        val transitionSet = TransitionSet()
        transitionSet.addTransition(ChangeBounds())
        transitionSet.addTransition(ChangeTransform())
        transitionSet.addTarget(iv_detail_movie)
        transitionSet.addTarget(tv_detail_movie_average)
        transitionSet.addTarget(tv_detail_movie_direct)
        transitionSet.addTarget(tv_detail_movie_genre)
        window.sharedElementEnterTransition = transitionSet
        window.sharedElementExitTransition = transitionSet
    }


    /*
    * 网络请求电影数据
    * */
    fun requestMovieDetaildata(){
        NetWorkUtil.sendRequestWithOkHttp(Constants.DOUBAN_MOVIE_DETAIL_URL+movieInfo.id, Constants.DOUBAN_MOVIE_DETAIL_REQUEST, handler)
        //没有本地数据，执行网络请求，否则调用本地数据
        if (!getCacheMovieEntry(movieInfo.id)){
            NetWorkUtil.sendRequestWithOkHttp(Constants.DOUBAN_MOVIE_ENTRY_URL+movieInfo.id+"?apikey="+Constants.DOUBAN_MOVIE_APIKEY, Constants.DOUBAN_MOVIE_ENTRY_REQUEST, handler)
        }else{
            initViewAfterEntryData()
        }
    }


    /*
     * 得到Detail数据后再次初始化某些控件
     * */
    private fun initViewAfterDetailData(){
        //剧情简介
        tv_detail_movie_summary.setText("    " + movieDetail?.summary)

        //电影评分人数
        tv_detail_ratings_count.setText(movieDetail?.ratings_count.toString() + "人评价")


    }
    /*
     * 得到Entry数据后再次初始化某些控件
     * */
    private fun initViewAfterEntryData(){
        //上映时间：
        var pubdateStr = "上映时间："
        for (date in movieEntry.pubdates){
            //第一个类型名字前不用加分隔符
            pubdateStr += "\n"+ date
        }
        tv_detail_movie_pubdate.text = pubdateStr

        //片长
        tv_detail_movie_durations.setText("片长："+movieEntry.durations.get(0))

        //短评
        for (comment in movieEntry.popular_comments){
            var shortCommentView = ShortCommentaryViewHolder.create(this).bind(comment, this)
            ll_short_commentary_layout.addView(shortCommentView)
        }
    }

    class ShortCommentaryViewHolder(itemView:View){

        private var tv_detailShortCommentaryAuthorName = itemView.findViewById<TextView>(R.id.tv_detail_short_commentary_author_name)
        private var tv_detailShortCommentaryAverage = itemView.findViewById<TextView>(R.id.tv_detail_short_commentary_average)
        private var tv_detailShortCommentaryCreataAt = itemView.findViewById<TextView>(R.id.tv_detail_short_commentary_creata_at)
        private var tv_detailShortCommentaryContent = itemView.findViewById<TextView>(R.id.tv_detail_short_commentary_content)
        private val view = itemView

        companion object {

            fun create(context: Context):ShortCommentaryViewHolder{
                val view = LayoutInflater.from(context).inflate(R.layout.item_movie_short_commentary, null)
                return ShortCommentaryViewHolder(view);
            }
        }

        fun bind(comment:DoubanMovieEntry.PopularCommentsBean, context: Context):View{
            tv_detailShortCommentaryAuthorName.text = comment.author.name+"  看过 "
            tv_detailShortCommentaryCreataAt.text = comment.created_at.split(" ").get(0)//按空分割，只取年月日，即0位置
            tv_detailShortCommentaryContent.text = "    "+comment.content

            //评分
            tv_detailShortCommentaryAverage.text = comment.rating.value.toString()+"分"
            if (comment.rating.value>3){
                tv_detailShortCommentaryAverage.setTextColor(ContextCompat.getColor(context, R.color.gold))
            }else{
                tv_detailShortCommentaryAverage.setTextColor(ContextCompat.getColor(context, R.color.gray))
            }

            return view
        }
    }

    private fun initPreferences() {
        if (null == preferences) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this)
            editor = preferences?.edit()
        }
        if (null == editor) {
            if (null == preferences) {
                return //获取不到
            }
            editor = preferences?.edit()
        }
    }

    //
    /**
     * 本地缓存MovieEntry数据
     * @param phone_number
     * @param password
     */
    fun <T> cacheMovieEntry(t: T):Boolean{
        //初始化本地存储
//        initPreferences()
        editor!!.putString(movieInfo.id, NetWorkUtil.getGson().toJson(t))
        return editor!!.commit()
    }

    /**
     * 记从本地缓存读取,若不为空，直接赋值并返回true，为空返回false
     * @param moive_id 电影id
     */
    private fun getCacheMovieEntry(moive_id: String):Boolean {
        //初始化本地存储
        initPreferences()
        var entryStr = preferences?.getString(moive_id, "")
        if (entryStr.equals("")){
            return false
        }
        movieEntry = NetWorkUtil.parseJsonWithGson(entryStr, DoubanMovieEntry::class.java)
        return true
    }

    companion object {

        fun actionStart(context: Context, bundle: Bundle, compat: ActivityOptionsCompat?){
            val intent = Intent(context, ActivityMovieDetail::class.java)
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

