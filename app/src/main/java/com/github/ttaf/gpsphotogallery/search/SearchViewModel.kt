package com.github.ttaf.gpsphotogallery.search

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.github.ttaf.gpsphotogallery.map.LocationStream
import com.github.ttaf.gpsphotogallery.util.decimals
import com.github.ttaf.gpsphotogallery.util.toFlowable
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Flowable
import io.reactivex.functions.Function8
import io.reactivex.processors.BehaviorProcessor
import kotlin.reflect.KClass

class SearchViewModel(
        val filterModeValidator: FilterModeValidator,
        val filterModeFactory: FilterModeFactory,
        val bounds: BehaviorProcessor<LatLngBounds>,
        val center: BehaviorProcessor<LatLng>,
        val locationStream: LocationStream
) : ViewModel() {

    companion object {
        const val TAG = "SearchViewModel"
    }

    val radius = ObservableField<String>("1.0")
    val currentLat = ObservableField<String>("49.0")
    val currentLon = ObservableField<String>("21.201")
    val centerLat = ObservableField<String>("50.0")
    val centerLon = ObservableField<String>("30.0")
    val lat1 = ObservableField<String>("11.0")
    val lon1 = ObservableField<String>("13.0")
    val lat2 = ObservableField<String>("100.0")
    val lon2 = ObservableField<String>("105.0")
    val canSearch = ObservableField<Boolean>(false)
    private val mode = ObservableField<KClass<out FilterMode>>(FilterMode.All::class)

    private val rxRadius = radius.toFlowable()
    private val rxCurrentLat = currentLat.toFlowable()
    private val rxCurrentLon = currentLon.toFlowable()
    private val rxCenterLat = centerLat.toFlowable()
    private val rxCenterLon = centerLon.toFlowable()
    private val rxLat1 = lat1.toFlowable()
    private val rxLon1 = lon1.toFlowable()
    private val rxLat2 = lat2.toFlowable()
    private val rxLon2 = lon2.toFlowable()
    private val rxMode = mode.toFlowable()
    private val rxCanSearch: Flowable<Boolean> = Flowable.combineLatest(
            rxRadius,
            rxCenterLat,
            rxCenterLon,
            rxLat1,
            rxLon1,
            rxLat2,
            rxLon2,
            rxMode,
            Function8 { r, clat, clon, lat1, lon1, lat2, lon2, mode ->
                filterModeValidator.validate(r, clat, clon, lat1, lon1, lat2, lon2, mode)
            }
    )

    private val _search = BehaviorProcessor.createDefault<FilterMode>(FilterMode.All)
    val search: Flowable<FilterMode> = _search.hide()

    init {
        println(_search)
        println(search)
        rxCanSearch.subscribe { canSearch.set(it) }
        val len = 5
        bounds.subscribe {
            lat1.set(it.northeast.latitude.decimals(len))
            lon1.set(it.northeast.longitude.decimals(len))
            lat2.set(it.southwest.latitude.decimals(len))
            lon2.set(it.southwest.longitude.decimals(len))
        }
        center.subscribe {
            centerLat.set(it.latitude.decimals(len))
            centerLon.set(it.longitude.decimals(len))
        }
        locationStream.subscribe {
            currentLat.set(it.latitude.decimals(len))
            currentLon.set(it.longitude.decimals(len))
        }
    }

    fun noFilterSelected() {
        mode.set(FilterMode.All::class)
    }

    fun positionAndRadiusSelected() {
        mode.set(FilterMode.PositionAndRadius::class)
    }

    fun coordinateAndRadiusSelected() {
        mode.set(FilterMode.CoordinateAndRadius::class)
    }

    fun boundingBoxSelected() {
        mode.set(FilterMode.BoundingBox::class)
    }

    fun searchClicked() {
        _search.onNext(
                filterModeFactory.create(
                        radius.get(),
                        currentLat.get(),
                        currentLon.get(),
                        centerLat.get(),
                        centerLon.get(),
                        lat1.get(),
                        lon1.get(),
                        lat2.get(),
                        lon2.get(),
                        mode.get()))
    }
}