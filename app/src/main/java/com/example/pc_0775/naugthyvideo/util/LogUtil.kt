package com.example.pc_0775.naugthyvideo.util

import android.util.Log

/**
 * Created by PC-0775 on 2019/3/2.
 */
class LogUtil {
    companion object{
        private val VERBOSE = 1
        private val DEBUG = 2
        private val INFO = 3
        private val WARN = 4
        private val ERROR = 5
        private val NOTHING = 6
        /**控制想要打印的日志级别
         * 等于VERBOSE，则就会打印所有级别的日志
         * 等于WARN，则只会打印警告级别以上的日志
         * 等于NOTHING，则会屏蔽掉所有日志*/
        private var level = VERBOSE

        fun v(tag:String, msg:String){
            if (level <= VERBOSE){
                Log.v(tag, msg)
            }
        }

        fun d(tag:String, msg:String){
            if (level <= DEBUG){
                Log.d(tag, msg)
            }
        }
        fun i(tag:String, msg:String){
            if (level <= INFO){
                Log.i(tag, msg)
            }
        }
        fun w(tag:String, msg:String){
            if (level <= WARN){
                Log.w(tag, msg)
            }
        }
        fun e(tag:String, msg:String){
            if (level <= ERROR){
                Log.e(tag, msg)
            }
        }

        /*
        * 生成包名，调用时传入Thread.currentThread().getStackTrace();
        * */
        fun getTag(element: StackTraceElement):String{
            //获取类名(去包名)
            var callerClazzName:String = element.className
            callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".")+1)
            //生成Tag
            return "$callerClazzName.${element.methodName}(${element.lineNumber})"
        }
    }
}