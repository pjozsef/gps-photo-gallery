package com.github.ttaf.gpsphotogallery.session

import android.app.Activity
import android.app.Application
import android.os.Bundle

class SessionStartListener(
        private val updater: ImageUpdater) : Application.ActivityLifecycleCallbacks {

    private var currentActivity: Activity? = null

    override fun onActivityResumed(activity: Activity) {
        if (currentActivity == null) {
            updater.update(activity)
        }
        currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity?) {
        if (currentActivity == activity) currentActivity = null
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }
}