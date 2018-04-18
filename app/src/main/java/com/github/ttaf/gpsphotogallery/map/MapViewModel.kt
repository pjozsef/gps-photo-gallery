package com.github.ttaf.gpsphotogallery.map

import android.arch.lifecycle.ViewModel
import com.github.ttaf.gpsphotogallery.model.photo.PhotoRepository
import com.github.ttaf.gpsphotogallery.search.FilterMode

class MapViewModel(val photoRepository: PhotoRepository) : ViewModel() {
    val photos = photoRepository.getPhotos(FilterMode.All)
}