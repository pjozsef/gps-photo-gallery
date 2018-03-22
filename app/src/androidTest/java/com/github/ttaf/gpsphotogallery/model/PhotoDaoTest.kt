package com.github.ttaf.gpsphotogallery.model

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class PhotoDaoTest {
    lateinit var db: PhotoDatabase
    lateinit var dao: PhotoDao
    lateinit var photosSubscriber: TestSubscriber<List<Photo>>
    lateinit var pathsSubscriber: TestSubscriber<List<String>>

    private val photo1 = Photo("url1", 1.0, 2.0, 3.0)
    private val photo2 = Photo("url2", 1.5, 2.5, 3.5)
    private val photo3 = Photo("url3", 10.0, 20.0, 30.0)

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
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetAll() {
        dao.insert(photo1, photo2, photo3)
        dao.getAll().subscribe(photosSubscriber)

        photosSubscriber.await(5, TimeUnit.SECONDS)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(photos)
    }

    @Test
    fun testGetAllPaths(){
        dao.insert(photo1, photo2, photo3)
        dao.getAllPaths().subscribe(pathsSubscriber)

        pathsSubscriber.await(5, TimeUnit.SECONDS)

        pathsSubscriber.assertNoErrors()
        pathsSubscriber.assertValueCount(1)
        pathsSubscriber.assertValues(paths)
    }

    @Test
    fun testUpdate() {
        dao.insert(photo1, photo2, photo3)
        val updatedPhoto = photo2.copy(lat = -1.0, lon = -1.0, alt = -1.0)
        dao.update(updatedPhoto)
        dao.getAll().subscribe(photosSubscriber)

        photosSubscriber.await(5, TimeUnit.SECONDS)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(listOf(photo1, updatedPhoto, photo3))
    }

    @Test
    fun testDelete() {
        dao.insert(photo1, photo2, photo3)
        dao.delete(photo2)
        dao.getAll().subscribe(photosSubscriber)

        photosSubscriber.await(5, TimeUnit.SECONDS)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(listOf(photo1, photo3))
    }

    @Test
    fun testDeleteByPath() {
        dao.insert(photo1, photo2, photo3)
        dao.deleteByPath(photo2.path)
        dao.getAll().subscribe(photosSubscriber)

        photosSubscriber.await(5, TimeUnit.SECONDS)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(listOf(photo1, photo3))
    }

    @Test
    fun testDeleteAll() {
        dao.insert(photo1, photo2, photo3)
        dao.deleteAll()
        dao.getAll().subscribe(photosSubscriber)

        photosSubscriber.await(5, TimeUnit.SECONDS)

        photosSubscriber.assertNoErrors()
        photosSubscriber.assertValueCount(1)
        photosSubscriber.assertValues(listOf())
    }
}