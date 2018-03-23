package com.github.ttaf.gpsphotogallery.util

import android.databinding.Observable
import android.databinding.ObservableField
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

fun <T> ObservableField<T>.toFlowable(
        strategy: BackpressureStrategy = BackpressureStrategy.LATEST
): Flowable<T> = Flowable.create({ emitter ->
    val onNextCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(dataBindingObservable: Observable, propertyId: Int) {
            if (dataBindingObservable === this@toFlowable) {
                emitter.onNext(this@toFlowable.get())
            }
        }
    }
    emitter.onNext(this.get())
    this.addOnPropertyChangedCallback(onNextCallback)
    emitter.setCancellable { this.removeOnPropertyChangedCallback(onNextCallback) }

}, strategy)


fun Flowable<String>.toDoubleFlowable(): Flowable<Double> = this.map {
    val maybeValue = it.toDoubleOrNull()
    if (maybeValue != null) Some(maybeValue) else Option.empty<Double>()
}.filter {
    it is Some<Double>
}.map {
    it.getOrElse { error("There shouldn't be empty values") }
}