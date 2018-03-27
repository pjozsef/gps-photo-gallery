package com.github.ttaf.gpsphotogallery.model.mediastore;

import android.content.ContentResolver
import android.media.ExifInterface
import android.provider.MediaStore
import android.util.Log
import com.github.ttaf.gpsphotogallery.model.photo.Photo
import com.github.ttaf.gpsphotogallery.util.getDouble
import com.github.ttaf.gpsphotogallery.util.getLong
import com.github.ttaf.gpsphotogallery.util.getString
import com.github.ttaf.gpsphotogallery.util.map

class DefaultMediaStoreRepository(val contentResolver: ContentResolver) : MediaStoreRepository {

    override fun queryExternalImages(
            projection: Array<String>,
            selection: String?,
            selectionArgs: Array<String>?,
            sortOrder: String?): List<Photo> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cur = contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder)
        return cur.use {
            Log.d("cursor-read", "size: ${cur.count}")
            it.map {
                val path = it.getString(MediaStore.Images.Media.DATA)
                val alt = ExifInterface(path).getAltitude(0.0)
                val lat = cur.getDouble(MediaStore.Images.Media.LATITUDE)
                val lon = cur.getDouble(MediaStore.Images.Media.LONGITUDE)
                val timestamp = it.getLong(MediaStore.Images.Media.DATE_TAKEN)
                Photo(path, lat, lon, alt, timestamp)
            }
        }
    }
}
