package com.mfkw.compass.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class MessageStream<EVENT> : LiveData<EVENT>() {
    private val isPending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in EVENT>) =
        super.observe(
            owner,
            wrapObserver(observer)
        )

    fun publish(event: EVENT) {
        value = event
    }

    fun post(event: EVENT) = postValue(event)

    override fun setValue(event: EVENT?) {
        isPending.set(true)
        super.setValue(event)
    }

    override fun postValue(event: EVENT) {
        isPending.set(true)
        super.postValue(event)
    }

    private fun wrapObserver(observer: Observer<in EVENT>) = Observer<EVENT> { event ->
        takeIf { isPending.compareAndSet(true, false) }?.also { observer.onChanged(event) }
    }
}
