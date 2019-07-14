package com.mfkw.compass.util

import androidx.lifecycle.LiveData

interface ValueProducer<T> {
    val value: LiveData<T>
}
