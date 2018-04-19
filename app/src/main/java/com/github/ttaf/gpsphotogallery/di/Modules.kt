package com.github.ttaf.gpsphotogallery.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.github.ttaf.gpsphotogallery.detail.DetailViewModel
import com.github.ttaf.gpsphotogallery.map.LocationStream
import com.github.ttaf.gpsphotogallery.map.MapViewModel
import com.github.ttaf.gpsphotogallery.model.PhotoDatabase
import com.github.ttaf.gpsphotogallery.model.mediastore.DefaultMediaStoreRepository
import com.github.ttaf.gpsphotogallery.model.mediastore.MediaStoreRepository
import com.github.ttaf.gpsphotogallery.model.photo.PhotoRepository
import com.github.ttaf.gpsphotogallery.search.FilterModeFactory
import com.github.ttaf.gpsphotogallery.search.FilterModeValidator
import com.github.ttaf.gpsphotogallery.search.SearchViewModel
import com.github.ttaf.gpsphotogallery.session.ImageUpdater
import com.github.ttaf.gpsphotogallery.session.SessionStartListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.processors.BehaviorProcessor
import org.koin.dsl.module.applicationContext

val dbModule = applicationContext {
    bean { Room.databaseBuilder(get(), PhotoDatabase::class.java, "PhotoDatabase.db").build() }
    bean { get<PhotoDatabase>().photoDao() }
}

val mapModule = applicationContext {
    bean { MapViewModel(get(), get("bounds"), get("center")) }
    bean("bounds") { BehaviorProcessor.createDefault<LatLngBounds>(LatLngBounds(LatLng(0.0, 0.0), LatLng(1.0, 1.0))) }
    bean("center") { BehaviorProcessor.createDefault<LatLng>(LatLng(0.0, 0.0)) }
    bean { LocationStream(get()) }
//    bean { ReactiveLocationProvider(get()) }
}

val searchModule = applicationContext {
    bean { SearchViewModel(get(), get(), get("bounds"), get("center"), get()) }
    bean { FilterModeValidator() }
    bean { FilterModeFactory() }
}

val detailModule = applicationContext {
    bean { DetailViewModel(get(), get()) }
    bean { PhotoRepository(get()) }
}

val photoUpdateModule = applicationContext {
    bean { SessionStartListener(get()) as Application.ActivityLifecycleCallbacks }
    bean { ImageUpdater(get(), get()) }
}

val mediaStoreModuel = applicationContext {
    bean { DefaultMediaStoreRepository(get()) as MediaStoreRepository }
    bean { get<Context>().contentResolver }
}

val allModules = listOf(
        dbModule,
        mapModule,
        searchModule,
        detailModule,
        photoUpdateModule,
        mediaStoreModuel)
