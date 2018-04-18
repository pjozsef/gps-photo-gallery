package com.github.ttaf.gpsphotogallery.map

import android.arch.lifecycle.ViewModel
import com.github.ttaf.gpsphotogallery.model.photo.PhotoRepository
import com.github.ttaf.gpsphotogallery.search.FilterMode
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.processors.BehaviorProcessor

class MapViewModel(
        photoRepository: PhotoRepository,
        val bounds: BehaviorProcessor<LatLngBounds>,
        val center: BehaviorProcessor<LatLng>) : ViewModel() {
    val photos = photoRepository.getPhotos(FilterMode.All)
}