package com.github.ttaf.gpsphotogallery.model

import com.github.ttaf.gpsphotogallery.search.FilterMode
import io.reactivex.Flowable

class PhotoRepository {
    fun getPhotos(filterMode: FilterMode): Flowable<List<Photo>>{
        return Flowable.just(emptyList())
    }
}