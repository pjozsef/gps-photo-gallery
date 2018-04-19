package com.github.ttaf.gpsphotogallery.map

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.github.ttaf.gpsphotogallery.R
import com.github.ttaf.gpsphotogallery.image.ImageActivity
import com.github.ttaf.gpsphotogallery.search.SearchActivity
import com.github.ttaf.gpsphotogallery.util.PermissionUtils
import com.github.ttaf.gpsphotogallery.util.withPermission
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.maps.android.clustering.ClusterManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_map.*
import org.koin.android.ext.android.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val REQUEST_WRITE_EXTERNAL = 180
        const val REQUEST_TAKE_PHOTO = 190
    }

    val viewModel by inject<MapViewModel>()

    lateinit var map: GoogleMap
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_map)
        mapView.onCreate(savedInstanceState)

        cameraFab.setOnClickListener {
            withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_WRITE_EXTERNAL) {
                takePhoto()
            }
        }
    }

    private fun takePhoto() {
        println(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
//            val photoFile = createImageFile()
//            photoFile?.let {
//                val photoURI = FileProvider.getUriForFile(
//                        this,
//                        "com.github.ttaf.gpsphotogallery",
//                        it)
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                startActivity(takePictureIntent)
//            }
        }
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        )
        currentPhotoPath = image.absolutePath
        return image
    }

//    private fun galleryAddPic() {
//        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//        val f = File(currentPhotoPath)
//        val contentUri = Uri.fromFile(f)
//        mediaScanIntent.data = contentUri
//        this.sendBroadcast(mediaScanIntent)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
//                galleryAddPic()
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) = when (requestCode) {

        PermissionUtils.REQUEST_CODE -> {
            mapView.getMapAsync(this)
        }
        REQUEST_WRITE_EXTERNAL -> {
            takePhoto()
        }
        else -> {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_search -> {
                    viewModel.bounds.onNext(map.projection.visibleRegion.latLngBounds)
                    viewModel.center.onNext(map.cameraPosition.target)
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    @SuppressLint("MissingPermission")
    override fun onMapReady(m: GoogleMap) {
        map = m
        withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) {
            map.isMyLocationEnabled = true

            with(map.uiSettings) {
                isCompassEnabled = true
                isZoomControlsEnabled = true
                isZoomGesturesEnabled = true
                isRotateGesturesEnabled = true
                isMyLocationButtonEnabled = true
            }

            val clusterManager = ClusterManager<PhotoMarker>(this, map)
            map.setOnCameraIdleListener(clusterManager)
            map.setOnMarkerClickListener(clusterManager)
            map.setOnInfoWindowClickListener(clusterManager)
            clusterManager.setOnClusterItemInfoWindowClickListener {
                val intent = Intent(this, ImageActivity::class.java)
                intent.putExtra(ImageActivity.EXTRA_IMAGE, it.url)
                startActivity(intent)
            }

            viewModel.photos.observeOn(AndroidSchedulers.mainThread()).subscribe {
                clusterManager.clearItems()
                map.clear()

                it.forEach {
                    val item = PhotoMarker(it.lat, it.lon, it.path, "%.2f - %.2f".format(it.lat, it.lon))
                    clusterManager.addItem(item)
                }

            }
        }
    }
}
