package com.github.ttaf.gpsphotogallery.di

import android.app.Application
import org.koin.dsl.module.applicationContext
import org.mockito.Mockito.mock

val baseMockModule = applicationContext {
    bean { mock(Application.ActivityLifecycleCallbacks::class.java) }
}

val allModules = listOf(baseMockModule)