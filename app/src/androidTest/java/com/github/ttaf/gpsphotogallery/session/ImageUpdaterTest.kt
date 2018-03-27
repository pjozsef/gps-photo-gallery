package com.github.ttaf.gpsphotogallery.session

import android.provider.MediaStore
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.github.ttaf.gpsphotogallery.detail.DetailActivity
import com.github.ttaf.gpsphotogallery.model.mediastore.MediaStoreRepository
import com.github.ttaf.gpsphotogallery.model.photo.Photo
import com.github.ttaf.gpsphotogallery.model.photo.PhotoDao
import com.github.ttaf.gpsphotogallery.testutil.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class ImageUpdaterTest : KoinTest {

    private lateinit var updater: ImageUpdater
    private lateinit var photoDao: PhotoDao
    private lateinit var mediaRepository: MediaStoreRepository
    val photo1 = Photo("path1", 1.0, 2.0, 3.0, 1)
    val photo2 = Photo("path2", 10.0, 20.0, 30.0, 10)
    val photo3 = Photo("path3", 100.0, 200.0, 300.0, 100)
    private val photos = listOf(photo1, photo2, photo3)

    @get:Rule
    var activityRule = ActivityTestRule<DetailActivity>(DetailActivity::class.java)

    @Before
    fun setUp() {
        photoDao = mock(PhotoDao::class.java)
        mediaRepository = mock(MediaStoreRepository::class.java)
        updater = ImageUpdater(photoDao, mediaRepository)
    }

    @Test
    fun testUpdate_savesNewerImages_toDb() {
        whenever(photoDao.getLatestTimestampSync()).thenReturn(1L)
        whenever(mediaRepository
                .queryExternalImages(
                        arrayOf(
                                MediaStore.Images.Media._ID,
                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                                MediaStore.Images.Media.DATE_TAKEN,
                                MediaStore.Images.Media.LATITUDE,
                                MediaStore.Images.Media.LONGITUDE,
                                MediaStore.Images.Media.DATA),
                        "datetaken >= ?",
                        arrayOf("1"),
                        null))
                .thenReturn(photos)

        updater.update(activityRule.activity)

        verify(photoDao, timeout(250)).insert(photo1, photo2, photo3)
    }

}