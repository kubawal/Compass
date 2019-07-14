package com.mfkw.compass.screen.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ResolvableApiException
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.util.MessageStream

interface MainScreenViewModel {
    sealed class InputMessage {
        object SelectTargetButtonClick : InputMessage()
        object SearchButtonClick : InputMessage()
        class ReturnFromPickCoordinatesScreen(val newCoordinates: Coordinates) : InputMessage()
        class ReturnFromSearchScreen(val newCoordinates: Coordinates) : InputMessage()
    }

    sealed class OutputMessage {
        class CallPickCoordinatesScreen(val oldCoordinates: Coordinates) : OutputMessage()
        object CallSearchScreen : OutputMessage()
        class ResolveApiException(val exception: ResolvableApiException) : OutputMessage()
    }

    val currentCoordinates: LiveData<Coordinates>
    val targetCoordinates: LiveData<Coordinates>
    val compass: LiveData<Float>
    val course: LiveData<Float>
    val info: LiveData<String>
    val outputMessages: MessageStream<OutputMessage>
    val inputMessages: Observer<InputMessage>
}
