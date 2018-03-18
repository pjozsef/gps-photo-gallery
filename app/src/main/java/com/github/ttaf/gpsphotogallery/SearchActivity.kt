package com.github.ttaf.gpsphotogallery

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search.setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java))
        }
    }
}
