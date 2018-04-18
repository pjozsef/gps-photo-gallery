package com.github.ttaf.gpsphotogallery.search

import kotlin.reflect.KClass

class FilterModeFactory {

    fun create(
            rad: String,
            currentLat: String,
            currentLon: String,
            centerLat: String,
            centerLon: String,
            lat1: String,
            lon1: String,
            lat2: String,
            lon2: String,
            mode: KClass<out FilterMode>): FilterMode = when (mode) {
        FilterMode.All::class -> {
            FilterMode.All
        }
        FilterMode.PositionAndRadius::class -> {
            FilterMode.CoordinateAndRadius(rad.toDouble(), currentLat.toDouble(), currentLon.toDouble())
        }
        FilterMode.CoordinateAndRadius::class -> {
            FilterMode.CoordinateAndRadius(rad.toDouble(), centerLat.toDouble(), centerLon.toDouble())
        }
        FilterMode.BoundingBox::class -> {
            FilterMode.BoundingBox(lat1.toDouble(), lon1.toDouble(), lat2.toDouble(), lon2.toDouble())
        }
        else -> error("Unknown FilterMode: $mode")
    }

}