package com.github.ttaf.gpsphotogallery.image

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.ttaf.gpsphotogallery.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity: AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image)
    }

    override fun onResume() {
        super.onResume()
        intent.extras.getString(EXTRA_IMAGE)?.let {
            Picasso.get().load("file://$it").into(bigImage)
        }
    }
}