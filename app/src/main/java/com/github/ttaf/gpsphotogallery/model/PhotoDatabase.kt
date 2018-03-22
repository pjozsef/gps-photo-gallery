package com.github.ttaf.gpsphotogallery.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


@Database(
        entities = [Photo::class],
        version = 1)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}