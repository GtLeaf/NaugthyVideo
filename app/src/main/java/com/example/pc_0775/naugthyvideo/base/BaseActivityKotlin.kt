package com.example.pc_0775.naugthyvideo.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

/**
 * Created by PC-0775 on 2018/12/23.
 */
abstract class BaseActivityKotlin:AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle = intent.extras
        initParams(bundle)
        val mContextView = LayoutInflater.from(this).inflate(bindLayout(), null)
        setContentView(mContextView)
        initView(mContextView)
        setListener()
        doBusiness(this)
    }

    /**
     * [初始化参数]
     *
     * @param params
     */
    abstract fun initParams(params: Bundle?)

    /**
     * [绑定布局]
     *
     * @return
     */
    abstract fun bindLayout(): Int
    /**
     * [初始化控件]
     *
     * @param view
     */
    abstract fun initView(view:View)
    /**
     * [设置监听]
     */
    abstract fun setListener()
    /**
     * [业务操作]
     *
     * @param mContext
     */
    abstract fun doBusiness(mContext: Context)

    /**
     * [页面跳转]
     *
     * @param clz
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(this@BaseActivityKotlin, clz))
    }

    @Throws(Exception::class)
    abstract fun widgetClick(v:View)

    override fun onClick(v: View?) {
        try {
            widgetClick(v!!)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    /**
     * [简化Toast]
     * @param msg
     */
    protected fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}