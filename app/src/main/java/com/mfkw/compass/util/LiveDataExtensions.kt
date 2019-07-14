package com.mfkw.compass.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations

fun <T> mutableLiveDataOf(initialValue: T) =
        MutableLiveData<T>().apply { value = initialValue }

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
        observe(lifecycleOwner, Observer(observer))

fun <A, B> LiveData<A>.map(mapper: (A) -> B): LiveData<B> =
        Transformations.map(this, mapper)
