package com.github.ttaf.gpsphotogallery.search

import kotlin.reflect.KClass

class FilterModeValidator {

    fun validate(
            rad: String,
            clat: String,
            clon: String,
            lat1: String,
            lon1: String,
            lat2: String,
            lon2: String,
            mode: KClass<out FilterMode>): Boolean = when (mode) {
        FilterMode.All::class -> {
            true
        }
        FilterMode.CoordinateAndRadius::class -> {
            rad.isDouble()
        }
        FilterMode.PositionAndRadius::class -> {
            rad.isDouble() && clat.isDouble() && clon.isDouble()
        }
        FilterMode.BoundingBox::class -> {
            lat1.isDouble() && lon1.isDouble() && lat2.isDouble() && lon2.isDouble()
        }
        else -> false
    }

    private fun String.isDouble() = this.toDoubleOrNull() != null
}