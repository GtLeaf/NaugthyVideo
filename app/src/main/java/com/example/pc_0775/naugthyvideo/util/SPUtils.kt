package com.example.pc_0775.naugthyvideo.util

import android.content.Context

/**
 * Created by PC-0775 on 2019/1/27.
 */
class SPUtils {


    companion object {
        /*
        * 保存在手机中的SP文件名
        * */
        private val FILE_NAME = "my_sp"

        /*
        * 保存数据
        * */
        fun putApply(context: Context, key:String, obj: Any){
            val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            val editor = sp.edit()
            when (obj) {
                is Boolean -> editor.putBoolean(key, obj)
                is Float -> editor.putFloat(key, obj)
                is Int -> editor.putInt(key, obj)
                is Long -> editor.putLong(key, obj)
                else -> editor.putString(key, obj as String)
            }
            editor.apply()
        }

        fun put(context: Context, key:String, obj: Any):Boolean{
            val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            val editor = sp.edit()
            when (obj) {
                is Boolean -> editor.putBoolean(key, obj as Boolean)
                is Float -> editor.putFloat(key, obj)
                is Int -> editor.putInt(key, obj)
                is Long -> editor.putLong(key, obj)
                else -> editor.putString(key, obj as String)
            }
            return editor.commit()
        }

        /*
        * 获取指定数据
        * */
        fun get(context: Context, key: String, defaultObj:Any):Any?{
            val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            return when(defaultObj){
                is Boolean -> sp.getBoolean(key, defaultObj)
                is Float -> sp.getFloat(key, defaultObj)
                is Int -> sp.getInt(key, defaultObj)
                is Long -> sp.getLong(key, defaultObj)
                is String -> sp.getString(key, defaultObj)
                else -> null
            }
        }

        /*
        * 删除指定数据
        * */
        fun removeApply(context: Context, key: String){
            val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.remove(key)
            editor.apply()
        }

        fun remove(context: Context, key: String):Boolean{
            val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.remove(key)
            return editor.commit()
        }

        /*
        * 删除所有数据
        * */
        fun clearApply(context: Context){
            val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.clear()
            editor.apply()
        }

        fun clear(context: Context):Boolean{
            val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.clear()
            return editor.commit()
        }

        /*
        * 检查key对应的数据是否存在
        * */
        fun contains(context: Context, key: String):Boolean{
            val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            return sp.contains(key)
        }
    }

}