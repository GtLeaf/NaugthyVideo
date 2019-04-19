package com.example.pc_0775.naugthyvideo.model.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.pc_0775.naugthyvideo.model.bean.douban.DoubanMovie
import io.reactivex.Single

/**
 * Created by PC-0775 on 2019/4/14.
 */
@Dao
interface DoubanMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDoubanMovie(doubanMovie: DoubanMovie)

    @Query("SELECT * FROM doubanMovie")
    fun getDoubanMoive(): Single<DoubanMovie>

}