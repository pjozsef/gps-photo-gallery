package com.github.ttaf.gpsphotogallery.detail

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.github.ttaf.gpsphotogallery.model.photo.PhotoRepository
import com.github.ttaf.gpsphotogallery.search.FilterMode
import com.github.ttaf.gpsphotogallery.search.SearchViewModel

class DetailViewModel(
        private val searchViewModel: SearchViewModel,
        private val photoRepository: PhotoRepository): ViewModel() {
    val info = ObservableField<String>("Photos")
    val coordinates = ObservableField<String>("")
    val photos = searchViewModel.search.flatMap {
        photoRepository.getPhotos(it)
    }

    init {
        println(searchViewModel.search)
        searchViewModel.search.subscribe {
            when(it){
                FilterMode.All -> {
                    info.set("All photos")
                }
                is FilterMode.CoordinateAndRadius -> {
                    info.set("CoordinateAndRadius")
                }
                is FilterMode.PositionAndRadius -> {
                    info.set("PositionAndRadius")
                }
                is FilterMode.BoundingBox -> {
                    info.set("BoundingBox")
                }
            }
        }
    }
}