package com.github.ttaf.gpsphotogallery.detail

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.github.ttaf.gpsphotogallery.R
import com.github.ttaf.gpsphotogallery.adapter.PhotoAdapter
import com.github.ttaf.gpsphotogallery.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.ext.android.inject

class DetailActivity : AppCompatActivity() {

    val detailViewModel by inject<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail)
        binding.viewmodel = detailViewModel
    }

    override fun onStart() {
        super.onStart()
        detailRecyclerView.layoutManager = GridLayoutManager(this, 3)
        detailRecyclerView.adapter = PhotoAdapter(detailViewModel.photos, lifecycle)
    }
}
