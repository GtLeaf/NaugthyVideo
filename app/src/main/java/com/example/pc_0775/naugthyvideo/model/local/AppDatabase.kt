package com.example.pc_0775.naugthyvideo.model.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.pc_0775.naugthyvideo.model.bean.douban.DoubanMovie
import com.example.pc_0775.naugthyvideo.model.local.dao.DoubanMovieDao

/**
 * Created by PC-0775 on 2019/4/14.
 */

@Database(entities = arrayOf(DoubanMovie::class), version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun doubanDao(): DoubanMovieDao

    companion object {
        @Volatile
        private var INSTANCE:AppDatabase? = null

        fun getInstance(context: Context):AppDatabase = INSTANCE ?: synchronized(this){
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "app.bd")
                        .build()
    }
}