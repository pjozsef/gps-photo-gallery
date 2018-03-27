package com.github.ttaf.gpsphotogallery.model.mediastore

import com.github.ttaf.gpsphotogallery.model.photo.Photo

interface MediaStoreRepository {
    fun queryExternalImages(
            projection: Array<String> = arrayOf("*"),
            selection: String? = null,
            selectionArgs: Array<String>? = null,
            sortOrder: String? = null): List<Photo>
}