package com.github.ttaf.gpsphotogallery.model.photo

import com.github.ttaf.gpsphotogallery.search.FilterMode
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
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
            val point = LatLng(filterMode.lat, filterMode.lon)
            val distance = filterMode.radius * 1000
            val northEast = SphericalUtil.computeOffset(point, distance, 45.0)
            val southWest = SphericalUtil.computeOffset(point, distance, 225.0)
            photoDao.getByBoundingBox(
                    northEast.latitude,
                    northEast.longitude,
                    southWest.latitude,
                    southWest.longitude)
                    .map { rawResult ->
                        rawResult.filter { photo ->
                            val otherPoint = LatLng(photo.lat, photo.lon)
                            SphericalUtil.computeDistanceBetween(point, otherPoint) <= distance
                        }
                    }
        }
        is FilterMode.BoundingBox -> {
            photoDao.getByBoundingBox(
                    filterMode.lat1,
                    filterMode.lon1,
                    filterMode.lat2,
                    filterMode.lon2)
        }
    }.also { println(filterMode) }
}