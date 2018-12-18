package com.example.pc_0775.naugthyvideo.recyclerViewControl.adapter

import android.app.Activity
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.example.pc_0775.naugthyvideo.R
import com.example.pc_0775.naugthyvideo.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.view.ActivityMovieDetail
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
    * 获取海报图片对应的GlideDrawer
    * */
    fun getGlideDrawerList(differentList:List<DoubanMovie.SubjectsBean>){
        for (moive in differentList){
            Glide.with(context)
                    .load(moive.images.large)
                    .asBitmap()
                    .into(object :SimpleTarget<Bitmap>(380, 500){
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    bitmapMap.put(moive.id, resource!!)
                }
            })
        }
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

        companion object {
            fun create(parent: ViewGroup):HomeViewHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_home, parent, false)
                return HomeViewHolder(view)
            }
        }

        fun bind(movieInfo: DoubanMovie.SubjectsBean?, context: Context?){
            Glide.with(context)
                    .load(movieInfo?.images?.medium)
                    .asBitmap()
                    .into(iv_homeMovieImg)

            cv_wholeItem.setOnClickListener(object :View.OnClickListener{
                override fun onClick(v: View?) {
                    //获取imageView中加载好的图片，传到下一个activity中，防止图片闪烁
                    var bundle = Bundle()
                    bundle.putSerializable("movieInfo", movieInfo)

                    var compat:ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((context as Activity))
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
                        var imagePair:Pair<View, String> = Pair(iv_homeMovieImg, iv_homeMovieImg.transitionName)
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