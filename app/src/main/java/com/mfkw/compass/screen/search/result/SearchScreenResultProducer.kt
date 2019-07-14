package com.mfkw.compass.screen.search.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.util.MessageStream
import com.mfkw.compass.util.ValueProducer

class SearchScreenResultProducer : ViewModel(), ValueProducer<Coordinates> {
    override val value = MessageStream<Coordinates>()

    fun produce(result: Coordinates) {
        value.publish(result)
    }
}
