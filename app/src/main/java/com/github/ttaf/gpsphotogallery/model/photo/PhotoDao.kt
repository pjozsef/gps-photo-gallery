package com.github.ttaf.gpsphotogallery.model.photo

import android.arch.persistence.room.*
import com.github.ttaf.gpsphotogallery.model.*
import io.reactivex.Flowable

@Dao
interface PhotoDao {
    @Query(SELECT_ALL_PHOTOS)
    fun getAll(): Flowable<List<Photo>>

    @Query(SELECT_ALL_PATHS)
    fun getAllPaths(): Flowable<List<String>>

    @Query(SELECT_ALL_PATHS)
    fun getAllPathsSync(): List<String>

    @Query(SELECT_LATEST_TIMESTAMP)
    fun getLatestTimestamp(): Flowable<Long>

    @Query(SELECT_LATEST_TIMESTAMP)
    fun getLatestTimestampSync(): Long

    @Insert
    fun insert(vararg photos: Photo)

    @Update
    fun update(vararg photos: Photo)

    @Delete
    fun delete(vararg photos: Photo)

    @Query(DELETE_BY_PATH)
    fun deleteByPath(path: String)

    @Query(DELETE_ALL_PHOTOS)
    fun deleteAll()
}