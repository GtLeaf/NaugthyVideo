package com.example.pc_0775.naugthyvideo.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.pc_0775.naugthyvideo.R

class ActivityMovieDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

         var item = intent.getParcelableArrayExtra(ContactsActivity.KEY_CONTACTS)
    }
}
