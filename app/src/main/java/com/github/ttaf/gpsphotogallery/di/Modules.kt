package com.github.ttaf.gpsphotogallery.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.github.ttaf.gpsphotogallery.model.PhotoDatabase
import com.github.ttaf.gpsphotogallery.model.mediastore.DefaultMediaStoreRepository
import com.github.ttaf.gpsphotogallery.model.mediastore.MediaStoreRepository
import com.github.ttaf.gpsphotogallery.search.FilterModeFactory
import com.github.ttaf.gpsphotogallery.search.FilterModeValidator
import com.github.ttaf.gpsphotogallery.search.SearchViewModel
import com.github.ttaf.gpsphotogallery.session.ImageUpdater
import com.github.ttaf.gpsphotogallery.session.SessionStartListener
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val dbModule = applicationContext {
    bean { Room.databaseBuilder(get(), PhotoDatabase::class.java, "PhotoDatabase.db").build() }
    bean { get<PhotoDatabase>().photoDao() }
}

val searchModule = applicationContext {
    viewModel { SearchViewModel(get(), get()) }
    bean { FilterModeValidator() }
    bean { FilterModeFactory() }
}

val photoUpdateModule = applicationContext {
    bean { SessionStartListener(get()) as Application.ActivityLifecycleCallbacks }
    bean { ImageUpdater(get(), get()) }
}

val mediaStoreModuel = applicationContext {
    bean { DefaultMediaStoreRepository(get()) as MediaStoreRepository}
    bean { get<Context>().contentResolver }
}

val allModules = listOf(dbModule, searchModule, photoUpdateModule, mediaStoreModuel)
