package com.github.ttaf.gpsphotogallery.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


class PhotoMarker(lat: Double, lng: Double, title: String? = null, snippet: String? = null) : ClusterItem {
    private val _position: LatLng = LatLng(lat, lng)
    private val _title: String? = title
    private val _snippet: String? = snippet

    override fun getPosition(): LatLng {
        return _position
    }

    override fun getTitle(): String? {
        return _title
    }

    override fun getSnippet(): String? {
        return _snippet
    }

}