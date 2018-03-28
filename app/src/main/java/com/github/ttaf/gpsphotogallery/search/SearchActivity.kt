package com.github.ttaf.gpsphotogallery.search

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.ttaf.gpsphotogallery.R
import com.github.ttaf.gpsphotogallery.databinding.ActivitySearchBinding
import com.github.ttaf.gpsphotogallery.detail.DetailActivity
import com.github.ttaf.gpsphotogallery.session.ImageUpdater
import com.github.ttaf.gpsphotogallery.util.PermissionUtils
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject

class SearchActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SearchActivity"
    }

    val viewModel by inject<SearchViewModel>()
    val updater by inject<ImageUpdater>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)
        binding.viewmodel = viewModel

        search.setOnClickListener {
            viewModel.searchClicked()
            startActivity(Intent(this, DetailActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) = when (requestCode) {

        PermissionUtils.REQUEST_CODE -> {
            updater.update(this)
        }
        else -> {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
