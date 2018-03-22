package com.github.ttaf.gpsphotogallery.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = TABLE_PHOTO)
data class Photo(
        @PrimaryKey
        val path: String,
        val lat: Double,
        val lon: Double,
        val alt: Double
)