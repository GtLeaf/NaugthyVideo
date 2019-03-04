package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter

import android.app.Activity
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.ui.ActivityMovieDetail
import io.vov.vitamio.utils.Base64
import java.io.ByteArrayOutputStream

/**
 * Created by PC-0775 on 2018/12/18.
 */
class AdapterHome(context: Context): PagedListAdapter<DoubanMovie.SubjectsBean, AdapterHome.HomeViewHolder>(object :DiffUtil.ItemCallback<DoubanMovie.SubjectsBean>(){
    override fun areContentsTheSame(oldItem: DoubanMovie.SubjectsBean?, newItem: DoubanMovie.SubjectsBean?): Boolean {
        return oldItem?.id == newItem?.id
    }

    override fun areItemsTheSame(oldItem: DoubanMovie.SubjectsBean?, newItem: DoubanMovie.SubjectsBean?): Boolean {
        return oldItem == newItem
    }
}) {
    var preListItem = 0
    var context:Context? = null
    /*
    * 存储缓存的图片
    * */
    var bitmapMap = HashMap<String, Bitmap>()

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder{
        preListItem = itemCount
        return HomeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        var movieInfo = getItem(position)
        holder.bind(movieInfo, context)
    }

    override fun onCurrentListChanged(currentList: PagedList<DoubanMovie.SubjectsBean>?) {
        super.onCurrentListChanged(currentList)
//        getGlideDrawerList(getDifferentData())//图片太大，暂时放弃
    }

    /*
    * 获取发生改变，新增的数组
    * */
    fun getDifferentData():ArrayList<DoubanMovie.SubjectsBean>{
        var differeetList = ArrayList<DoubanMovie.SubjectsBean>()
        if (null == currentList){
            return differeetList
        }

        for (index in preListItem until itemCount){
            differeetList.add(currentList!!.get(index)!!)
        }

        return differeetList
    }


    class HomeViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        //view
        private var cv_wholeItem = itemView.findViewById<CardView>(R.id.cv_whole_item)
        private var iv_homeMovieImg = itemView.findViewById<ImageView>(R.id.iv_home_movie_img)
        private var tv_homeMovieDirect = itemView.findViewById<TextView>(R.id.tv_home_movie_direct)
        private var tv_homeMovieGenre = itemView.findViewById<TextView>(R.id.tv_home_movie_genre)
        private var tv_homeMovieAverge = itemView.findViewById<TextView>(R.id.tv_home_movie_averge)

        companion object {
            fun create(parent: ViewGroup):HomeViewHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_home, parent, false)
                return HomeViewHolder(view)
            }
        }

        fun bind(movieInfo: DoubanMovie.SubjectsBean?, context: Context?){
            //电影海报
            Glide.with(context!!).asBitmap().load(movieInfo?.images?.medium).into(iv_homeMovieImg)//为什么要asBitmap??

            //拼接导演们的名字
            var directStr = ""
            var isFirstDirect = true
            for (direct in movieInfo!!.directors){
                //第一个导演名字前不用加分隔符
                directStr += if(isFirstDirect) direct.name else "/"+direct.name
                isFirstDirect = false
            }
            tv_homeMovieDirect.text = directStr

            //电影的类型
            var genreStr = ""
            var isFirstGenre = true
            for (genre in movieInfo!!.genres){
                //第一个类型名字前不用加分隔符
                genreStr += if(isFirstGenre) genre else "/"+genre
                isFirstGenre = false
            }
            tv_homeMovieGenre.text = genreStr

            //电影的评分,当平均分大于8分是，为金色
            tv_homeMovieAverge.text = movieInfo.rating.average.toString()
            if (movieInfo.rating.average > 8.0){
                tv_homeMovieAverge.setTextColor(ContextCompat.getColor(context!!, R.color.gold))
            }else{
                tv_homeMovieAverge.setTextColor(ContextCompat.getColor(context!!, R.color.gray))
            }

            //点击事件
            cv_wholeItem.setOnClickListener(object :View.OnClickListener{
                override fun onClick(v: View?) {
                    //获取imageView中加载好的图片，传到下一个activity中，防止图片闪烁
                    var bundle = Bundle()
                    bundle.putSerializable("movieInfo", movieInfo)

                    var compat:ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((context as Activity))
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
                        var imagePair:Pair<View, String> = Pair(iv_homeMovieImg, iv_homeMovieImg.transitionName)
                        //下面三个动画效果太差，全都不要
                        var averagePair:Pair<View, String> = Pair(tv_homeMovieAverge, tv_homeMovieAverge.transitionName)
                        var genrePair:Pair<View, String> = Pair(tv_homeMovieGenre, tv_homeMovieGenre.transitionName)
                        var DirectPair:Pair<View, String> = Pair(tv_homeMovieDirect, tv_homeMovieDirect.transitionName)
                        compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context, imagePair)
                    }
                    ActivityMovieDetail.actionStart(context!!, bundle, compat)
                }
            })
        }

        //不传图片了，图片太大，会发生错误
        fun bitmapToBase64(img:Bitmap):String{
            // 把Bitmap转码成字符串
            var baos = ByteArrayOutputStream()
            img.compress(Bitmap.CompressFormat.PNG, 100, baos)
            return String(Base64.encode(baos.toByteArray(), 0))
        }
    }
}