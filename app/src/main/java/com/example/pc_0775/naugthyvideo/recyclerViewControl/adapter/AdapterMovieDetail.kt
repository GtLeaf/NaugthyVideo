package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailViewHolder{
        var holder = MovieDetailViewHolder.create(parent)
        return holder
    }

    class MovieDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //view
        private val iv_detailMovieImg = itemView.findViewById<ImageView>(R.id.iv_detail_movie_img)
        private val tv_detailMovieAverage = itemView.findViewById<TextView>(R.id.tv_detail_movie_average)
        private val tv_detailMovieDescribe = itemView.findViewById<TextView>(R.id.tv_detail_movie_summary)
        private val tv_detailMovieTitle = itemView.findViewById<TextView>(R.id.tv_detail_movie_title)

        fun bind(movieInfo: DoubanMovie.SubjectsBean?, context: Context?){
            Glide.with(context!!).load(movieInfo?.images?.medium).into(iv_detailMovieImg)
            tv_detailMovieAverage.text = movieInfo?.rating?.average.toString()
            tv_detailMovieDescribe.text = movieInfo?.alt
            tv_detailMovieTitle.text = movieInfo?.title
        }


        companion object {
            fun create(parent: ViewGroup):MovieDetailViewHolder{
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_movie_home, parent, false)
                return MovieDetailViewHolder(view)
            }
        }
    }

}