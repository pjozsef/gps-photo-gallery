package com.github.ttaf.gpsphotogallery.search

sealed class FilterMode {

    data class PositionAndRadius(
            val radius: Double,
            val lat: Double,
            val lon: Double) : FilterMode()

    data class CoordinateAndRadius(
            val radius: Double,
            val lat: Double,
            val lon: Double) : FilterMode()

    data class BoundingBox(
            val lat1: Double,
            val lon1: Double,
            val lat2: Double,
            val lon2: Double
    ) : FilterMode()

    object All : FilterMode()

}

