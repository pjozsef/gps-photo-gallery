package com.github.ttaf.gpsphotogallery.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.github.ttaf.gpsphotogallery.model.photo.Photo
import com.github.ttaf.gpsphotogallery.model.photo.PhotoDao


@Database(
        entities = [Photo::class],
        version = 1)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}