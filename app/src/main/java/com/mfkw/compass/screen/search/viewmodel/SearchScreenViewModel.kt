package com.mfkw.compass.screen.search.viewmodel

import androidx.lifecycle.LiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.util.MessageSink
import com.mfkw.compass.util.MessageStream

interface SearchScreenViewModel {
    sealed class InputMessage {
        class QueryEntered(val query: String) : InputMessage()
        class ListItemClick(val position: Int) : InputMessage()
    }

    sealed class OutputMessage {
        class ReturnWithResult(val result: Coordinates) : OutputMessage()
    }

    val inputMessages: MessageSink<InputMessage>
    val outputMessages: MessageStream<OutputMessage>
    val places: LiveData<List<String>>
    val placesInfo: LiveData<String>
}
