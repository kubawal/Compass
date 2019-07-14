package com.mfkw.compass.util

import androidx.lifecycle.Observer

typealias MessageSink<T> = Observer<T>

fun <T> MessageSink<T>.send(message: T) = onChanged(message)
