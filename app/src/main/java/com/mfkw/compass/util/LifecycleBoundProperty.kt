package com.mfkw.compass.util

import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KProperty

class LifecycleBoundProperty<T : Any>(
    private val lifecycleOwner: LifecycleOwner,
    private val factory: () -> T
) {
    private var lifecycle: Lifecycle? = null
    private var instance: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val currentLifecycle = lifecycleOwner.lifecycle
        if(currentLifecycle.currentState == Lifecycle.State.DESTROYED)
            throw RuntimeException("Property usage after lifecycle DESTROYED")
        if(lifecycle !== currentLifecycle)
            instance = factory()
        lifecycle = currentLifecycle
        return instance!!
    }
}

fun <T : Any> LifecycleOwner.lifecycleBound(factory: () -> T) =
    LifecycleBoundProperty(this, factory)
