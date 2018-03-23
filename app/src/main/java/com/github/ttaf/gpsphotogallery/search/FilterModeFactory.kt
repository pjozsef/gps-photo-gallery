package com.github.ttaf.gpsphotogallery.search

import kotlin.reflect.KClass

class FilterModeFactory {

    fun create(
            rad: String,
            lat1: String,
            lon1: String,
            lat2: String,
            lon2: String,
            mode: KClass<out FilterMode>): FilterMode = when (mode) {
        FilterMode.All::class -> {
            FilterMode.All
        }
        FilterMode.CoordinateAndRadius::class -> {
            error("Not yet implemented")
        }
        FilterMode.PositionAndRadius::class -> {
            FilterMode.PositionAndRadius(rad.toDouble(), lat1.toDouble(), lon1.toDouble())
        }
        FilterMode.BoundingBox::class -> {
            FilterMode.BoundingBox(lat1.toDouble(), lon1.toDouble(), lat2.toDouble(), lon2.toDouble())
        }
        else -> error("Unknown FilterMode: $mode")
    }

}