package com.github.ttaf.gpsphotogallery.map

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.github.ttaf.gpsphotogallery.R
import com.github.ttaf.gpsphotogallery.search.SearchActivity
import com.github.ttaf.gpsphotogallery.util.PermissionUtils
import com.github.ttaf.gpsphotogallery.util.withPermission
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.maps.android.clustering.ClusterManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_map.*
import org.koin.android.ext.android.inject


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    val viewModel by inject<MapViewModel>()
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_map)
        mapView.onCreate(savedInstanceState)
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

            viewModel.photos.observeOn(AndroidSchedulers.mainThread()).subscribe {
                clusterManager.clearItems()
                map.clear()

                it.forEach {
                    //                    val marker = MarkerOptions().apply {
//                        position(LatLng(it.lat, it.lon))
//                        title("${it.lat} - ${it.lon}")
//                        title("%.2f - %.2f".format(it.lat, it.lon))
//                    }
//                    map.addMarker(marker)
                    val item = PhotoMarker(it.lat, it.lon, "%.2f - %.2f".format(it.lat, it.lon))
                    clusterManager.addItem(item)
                }

            }
        }
    }
}
