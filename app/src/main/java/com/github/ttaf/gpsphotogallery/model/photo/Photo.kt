package com.github.ttaf.gpsphotogallery.model.photo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.github.ttaf.gpsphotogallery.model.TABLE_PHOTO

@Entity(tableName = TABLE_PHOTO)
data class Photo(
        @PrimaryKey
        val path: String,
        val lat: Double,
        val lon: Double,
        val alt: Double,
        val timestamp: Long
)