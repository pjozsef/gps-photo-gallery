package com.github.ttaf.gpsphotogallery

import android.app.Application
import com.github.ttaf.gpsphotogallery.di.allModules
import org.koin.android.ext.android.startKoin
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class PhotoGalleryApplication: Application(), KoinComponent {

    val sessionStartListener by inject<ActivityLifecycleCallbacks>()

    override fun onCreate() {
        super.onCreate()

        startKoin(this, allModules)

        registerActivityLifecycleCallbacks(sessionStartListener)
    }
}