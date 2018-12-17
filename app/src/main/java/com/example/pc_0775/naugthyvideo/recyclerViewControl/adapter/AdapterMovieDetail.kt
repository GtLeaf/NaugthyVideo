package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter

import android.app.Activity
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.os.Build
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Fade
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil

/**
 * Created by PC-0775 on 2018/12/16.
 * 无法实现从recyclerViewItem到Item的动画，弃用
 * 具体原因：无法确定要为哪个item添加动画
 */
class AdapterMovieDetail(context: Context) : PagedListAdapter<DoubanMovie.SubjectsBean, AdapterMovieDetail.MovieDetailViewHolder>(
        object :DiffUtil.ItemCallback<DoubanMovie.SubjectsBean>(){
            override fun areItemsTheSame(oldItem: DoubanMovie.SubjectsBean?, newItem: DoubanMovie.SubjectsBean?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: DoubanMovie.SubjectsBean?, newItem: DoubanMovie.SubjectsBean?): Boolean {
                return (oldItem == newItem)
            }
        }){

    private var context:Context ?= null

    init {
        this.context = context
    }

    override fun onBindViewHolder(holder: MovieDetailViewHolder, position: Int) {
        holder.bind(getItem(position), context)
        NetWorkUtil.movieDetailPositon = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailViewHolder{
        var holder = MovieDetailViewHolder.create(parent)
        return holder
    }

    class MovieDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //view
        private val iv_detailMovieImg = itemView.findViewById<ImageView>(R.id.iv_detail_movie_img)
        private val tv_detailMovieAverage = itemView.findViewById<TextView>(R.id.tv_detail_movie_average)
        private val tv_detailMovieDescribe = itemView.findViewById<TextView>(R.id.tv_detail_movie_describe)
        private val tv_detailMovieName = itemView.findViewById<TextView>(R.id.tv_detail_movie_name)

        fun bind(movieInfo: DoubanMovie.SubjectsBean?, context: Context?){
            Glide.with(context).load(movieInfo?.images?.medium).into(iv_detailMovieImg)
            tv_detailMovieAverage.text = movieInfo?.rating?.average.toString()
            tv_detailMovieDescribe.text = movieInfo?.alt
            tv_detailMovieName.text = movieInfo?.title
            setTransition(context)
        }

        /*
        * 设置动画
        * */
        fun setTransition(context: Context?){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                return
            }
            if (null == context){
                return
            }
            /**
             * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
             */
            (context as Activity).window.enterTransition = Fade()
            context.window.exitTransition = Fade()
            /*
             * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
             * */
            var transitionSet = TransitionSet()
            transitionSet.addTransition(ChangeBounds())
            transitionSet.addTransition(ChangeTransform())
            transitionSet.addTarget(iv_detailMovieImg)
            transitionSet.addTarget(tv_detailMovieAverage)
            transitionSet.addTarget(tv_detailMovieName)
            context.window.sharedElementEnterTransition = transitionSet
            context.window.sharedElementExitTransition = transitionSet
        }

        companion object {
            fun create(parent: ViewGroup):MovieDetailViewHolder{
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_movie_detail, parent, false)
                return MovieDetailViewHolder(view)
            }
        }
    }
    companion object {


    }

}