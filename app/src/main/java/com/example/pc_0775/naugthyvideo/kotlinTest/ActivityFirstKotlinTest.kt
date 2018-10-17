package com.example.pc_0775.naugthyvideo.kotlinTest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_first_kotlin_test.*

import com.example.pc_0775.naugthyvideo.R

class ActivityFirstKotlinTest : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_kotlin_test)

        tv_kotlin.text = "hello kotlin"
    }
}
