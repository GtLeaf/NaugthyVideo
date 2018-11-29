package com.example.pc_0775.naugthyvideo.LogCrash

import android.app.usage.ExternalStorageStats
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import io.vov.vitamio.utils.Log.TAG
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by PC-0775 on 2018/11/29.
 */
class CrashExceptionHandler private constructor(context:Context):Thread.UncaughtExceptionHandler{

    private var context:Context? = null;
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null;
    private val info = HashMap<String, String>()

    init {
        init(context)
    }

    private fun init(context: Context){
        this.context = context
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        collectDeviceInfo(context)
        writeCrashInfoIntoFile(e)
        defaultHandler!!.uncaughtException(t, e)
    }

    private fun writeCrashInfoIntoFile(ex:Throwable?){
        if (ex == null){
            return
        }
        //设备信息
        val strbuilder = StringBuilder()
        var value:String
        for (key in info.keys){
            value = info[key]!!
            strbuilder.append(key).append("=").append(value).append("\n")
        }
        //错误信息
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause:Throwable? = ex.cause
        while (cause != null){
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        strbuilder.append(result)
        //保存到文件
        var fos:FileOutputStream? = null
        val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
        val timestamp = System.currentTimeMillis()
        val time = formatter.format(Date())
        val fileName = "$time-$timestamp.txt"
        try {
//            val file1 = ExternalStorageUtils.getDiskCacheDir(context!!, "crash")
            val file = File(" ")
            if (!file.exists()){
                file.mkdirs()
            }
            val newFile = File(file.absolutePath + File.separator + fileName)
            fos = FileOutputStream(newFile)
            fos.write(strbuilder.toString().toByteArray())
        }catch (fne:FileNotFoundException){

        }catch (e: Exception) {
            Log.e("TAG", e.message)
        } finally {
            if(fos != null){
                try {
                    fos.close()
                }catch (e:IOException){
                    Log.e(TAG, e.message)
                }
            }
        }
    }

    private fun collectDeviceInfo(context: Context?){
        try {
            val pm = context!!.packageManager
            val pi = pm.getPackageArchiveInfo(context.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null){
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                info.put("versionName", versionName)
                info.put("versionCode", versionCode)
            }
        }catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }

        val fields = Build::class.java.declaredFields
        try {
            for (field in fields){
                field.isAccessible = true
                info.put(field.name, field.get(null).toString())
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    companion object {
        val TAG = "CrashExceptionHandler"
        private var instance:CrashExceptionHandler? = null

        fun getInstance(context: Context):CrashExceptionHandler {
            if (instance == null){
                instance = CrashExceptionHandler(context)
            }
            return instance!!
        }
    }

    fun getDiskCacheDir(context: Context, uniqueName:String):File{
        var cachePath = context.cacheDir.path
        try {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() ){

            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.e("getDiskCacheDir", e.message)
        }

        return File(cachePath + File.separator + uniqueName)
    }
}