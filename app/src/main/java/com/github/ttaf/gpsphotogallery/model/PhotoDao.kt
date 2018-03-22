package com.github.ttaf.gpsphotogallery.model

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface PhotoDao {
    @Query(SELECT_ALL_PHOTOS)
    fun getAll(): Flowable<List<Photo>>

    @Query(SELECT_ALL_PATHS)
    fun getAllPaths(): Flowable<List<String>>

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