package com.github.ttaf.gpsphotogallery.search

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.github.ttaf.gpsphotogallery.util.toFlowable
import io.reactivex.Flowable
import io.reactivex.functions.Function6
import io.reactivex.processors.BehaviorProcessor
import kotlin.reflect.KClass

class SearchViewModel(
        val filterModeValidator: FilterModeValidator,
        val filterModeFactory: FilterModeFactory
) : ViewModel() {

    companion object {
        const val TAG = "SearchViewModel"
    }

    val radius = ObservableField<String>("1.0")
    val lat1 = ObservableField<String>("11.0")
    val lon1 = ObservableField<String>("13.0")
    val lat2 = ObservableField<String>("100.0")
    val lon2 = ObservableField<String>("105.0")
    val canSearch = ObservableField<Boolean>(false)
    private val mode = ObservableField<KClass<out FilterMode>>(FilterMode.All::class)

    private val rxRadius = radius.toFlowable()
    private val rxLat1 = lat1.toFlowable()
    private val rxLon1 = lon1.toFlowable()
    private val rxLat2 = lat2.toFlowable()
    private val rxLon2 = lon2.toFlowable()
    private val rxMode = mode.toFlowable()
    private val rxCanSearch: Flowable<Boolean> = Flowable.combineLatest(
            rxRadius,
            rxLat1,
            rxLon1,
            rxLat2,
            rxLon2,
            rxMode,
            Function6 { r, lat1, lon1, lat2, lon2, mode ->
                filterModeValidator.validate(r, lat1, lon1, lat2, lon2, mode)
            }
    )

    private val _search = BehaviorProcessor.createDefault<FilterMode>(FilterMode.All)
    val search: Flowable<FilterMode> = _search.hide()

    init {
        rxCanSearch.subscribe { canSearch.set(it) }
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
                        lat1.get(),
                        lon1.get(),
                        lat2.get(),
                        lon2.get(),
                        mode.get()))
    }
}