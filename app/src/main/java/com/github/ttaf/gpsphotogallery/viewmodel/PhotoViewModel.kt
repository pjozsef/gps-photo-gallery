package com.github.ttaf.gpsphotogallery.viewmodel

import android.arch.lifecycle.ViewModel
import com.github.ttaf.gpsphotogallery.model.Photo
import com.github.ttaf.gpsphotogallery.model.PhotoRepository
import com.github.ttaf.gpsphotogallery.search.FilterMode
import io.reactivex.Flowable

class PhotoViewModel(
        val repository: PhotoRepository
) : ViewModel() {

    var filterMode: FilterMode = FilterMode.All

    fun searchPhotos(): Flowable<List<Photo>> {
        return repository.getPhotos(filterMode)
    }

}