package com.github.ttaf.gpsphotogallery.model

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.github.ttaf.gpsphotogallery.model.photo.Photo
import com.github.ttaf.gpsphotogallery.model.photo.PhotoDao
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoDaoTest {
    private lateinit var db: PhotoDatabase
    private lateinit var dao: PhotoDao
    private lateinit var photosSubscriber: TestSubscriber<List<Photo>>
    private lateinit var pathsSubscriber: TestSubscriber<List<String>>
    private lateinit var latestTimestampSubscriber: TestSubscriber<Long>

    private val photo1 = Photo("url1", 1.0, 2.0, 3.0, 1)
    private val photo2 = Photo("url2", 1.5, 2.5, 3.5, 2)
    private val photo3 = Photo("url3", 10.0, 20.0, 30.0, 3)

    val photos = listOf(photo1, photo2, photo3)
    val paths = photos.map(Photo::path)

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                PhotoDatabase::class.java
        ).build()
        dao = db.photoDao()

        photosSubscriber = TestSubscriber.create()
        pathsSubscriber = TestSubscriber.create()
        latestTimestampSubscriber = TestSubscriber.create()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetAll() {
        dao.insert(photo1, photo2, photo3)
        dao.getAll().subscribe(photosSubscriber)

        photosSubscriber.awaitCount(1)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(photos)
    }

    @Test
    fun testGetAllPaths() {
        dao.insert(photo1, photo2, photo3)
        dao.getAllPaths().subscribe(pathsSubscriber)

        pathsSubscriber.awaitCount(1)

        pathsSubscriber.assertNoErrors()
        pathsSubscriber.assertValueCount(1)
        pathsSubscriber.assertValues(paths)
    }

    @Test
    fun testGetLatestTimestamp() {
        dao.insert(photo1, photo2, photo3)
        dao.getLatestTimestamp().subscribe(latestTimestampSubscriber)

        latestTimestampSubscriber.awaitCount(1)

        latestTimestampSubscriber.assertNoErrors()
        latestTimestampSubscriber.assertValueCount(1)
        latestTimestampSubscriber.assertValues(3)
    }

    @Test
    fun testUpdate() {
        dao.insert(photo1, photo2, photo3)
        val updatedPhoto = photo2.copy(lat = -1.0, lon = -1.0, alt = -1.0)
        dao.update(updatedPhoto)
        dao.getAll().subscribe(photosSubscriber)

        photosSubscriber.awaitCount(1)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(listOf(photo1, updatedPhoto, photo3))
    }

    @Test
    fun testDelete() {
        dao.insert(photo1, photo2, photo3)
        dao.delete(photo2)
        dao.getAll().subscribe(photosSubscriber)

        photosSubscriber.awaitCount(1)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(listOf(photo1, photo3))
    }

    @Test
    fun testDeleteByPath() {
            dao.insert(photo1, photo2, photo3)
            dao.deleteByPath(photo2.path)
            dao.getAll().subscribe(photosSubscriber)

            photosSubscriber.awaitCount(1)

            photosSubscriber.assertNoErrors()
            photosSubscriber.assertValueCount(1)
            photosSubscriber.assertValues(listOf(photo1, photo3))
    }

    @Test
    fun testDeleteAll() {
        dao.insert(photo1, photo2, photo3)
        dao.deleteAll()
        dao.getAll().subscribe(photosSubscriber)

        photosSubscriber.awaitCount(1)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(listOf())
    }

    @Test
    fun testGetByBoundingBox(){
        dao.insert(photo1, photo2, photo3)
        dao.getByBoundingBox(1.5, 2.5, 1.0, 2.0).subscribe(photosSubscriber)

        photosSubscriber.awaitCount(1)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(listOf(photo1, photo2))
    }

    @Test
    fun testGetByBoundingBox_emptyResult(){
        dao.insert(photo1, photo2, photo3)
        dao.getByBoundingBox(0.5, 0.5, 0.0, 0.0).subscribe(photosSubscriber)

        photosSubscriber.awaitCount(1)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(listOf())
    }
}