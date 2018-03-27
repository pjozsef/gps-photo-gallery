package com.github.ttaf.gpsphotogallery.session

import android.Manifest
import android.app.Activity
import android.provider.MediaStore
import android.util.Log
import com.github.ttaf.gpsphotogallery.model.mediastore.MediaStoreRepository
import com.github.ttaf.gpsphotogallery.model.photo.PhotoDao
import com.github.ttaf.gpsphotogallery.util.withPermission
import kotlin.concurrent.thread

class ImageUpdater(val photoDao: PhotoDao, val mediaStoreRepository: MediaStoreRepository) {

    fun update(activity: Activity) {
        activity.withPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            thread {
                Log.e("SESSIONN", "running!")

                val latestTimestamp = photoDao.getLatestTimestampSync()

                val projection = arrayOf(
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_TAKEN,
                        MediaStore.Images.Media.LATITUDE,
                        MediaStore.Images.Media.LONGITUDE,
                        MediaStore.Images.Media.DATA)

                val photos = mediaStoreRepository.queryExternalImages(
                        projection,
                        "datetaken > ?",
                        arrayOf(latestTimestamp.toString()))

                photoDao.insert(*photos.toTypedArray())
            }
        }
    }
}
