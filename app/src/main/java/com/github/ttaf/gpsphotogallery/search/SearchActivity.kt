package com.github.ttaf.gpsphotogallery.search

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.ttaf.gpsphotogallery.detail.DetailActivity
import com.github.ttaf.gpsphotogallery.R
import com.github.ttaf.gpsphotogallery.databinding.ActivitySearchBinding
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SearchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)
        val viewModel = SearchViewModel(FilterModeValidator(), FilterModeFactory())
        binding.viewmodel = viewModel

        search.setOnClickListener {
            viewModel.searchClicked()
            startActivity(Intent(this, DetailActivity::class.java))
        }
    }
}
