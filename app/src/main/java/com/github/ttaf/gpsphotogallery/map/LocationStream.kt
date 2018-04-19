package com.github.ttaf.gpsphotogallery.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable
import org.reactivestreams.Subscriber

class LocationStream(val context: Context): Flowable<LatLng>() {

    init {
        println((context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).getProviders(true))
    }

    @SuppressLint("MissingPermission")
    override fun subscribeActual(s: Subscriber<in LatLng>) {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val loc = manager.getLastKnownLocation("gps")
        s.onNext(LatLng(loc.latitude, loc.longitude))

        manager.requestLocationUpdates("gps", 1_000, 15.0f, object : LocationListener{
            override fun onLocationChanged(loc: Location) {
                println(loc)
                s.onNext(LatLng(loc.latitude, loc.longitude))
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            }

            override fun onProviderEnabled(p0: String?) {
            }

            override fun onProviderDisabled(p0: String?) {
            }

        })
    }
}