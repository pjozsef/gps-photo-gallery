package com.github.ttaf.gpsphotogallery.model.photo

import com.github.ttaf.gpsphotogallery.search.FilterMode
import io.reactivex.Flowable

class PhotoRepository(val photoDao: PhotoDao) {
    fun getPhotos(filterMode: FilterMode): Flowable<List<Photo>> = when (filterMode) {
        is FilterMode.All -> {
            photoDao.getAll()
        }
        is FilterMode.PositionAndRadius -> {
            Flowable.just(emptyList())
        }
        is FilterMode.CoordinateAndRadius -> {
            Flowable.just(emptyList())
        }
        is FilterMode.BoundingBox -> {
            Flowable.just(emptyList())
        }
    }
}