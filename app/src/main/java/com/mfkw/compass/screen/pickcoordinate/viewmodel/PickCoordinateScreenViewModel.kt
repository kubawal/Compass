package com.mfkw.compass.screen.pickcoordinate.viewmodel

import androidx.lifecycle.LiveData
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.util.MessageSink
import com.mfkw.compass.util.MessageStream

interface PickCoordinateScreenViewModel {
    sealed class InputMessage {
        class LatitudePicked(val newValue: Double) : InputMessage()
        class LongitudePicked(val newValue: Double) : InputMessage()
        object SelectClicked : InputMessage()
    }

    sealed class OutputMessage {
        class ReturnWithResult(val result: Coordinates) : OutputMessage()
    }

    val inputMessages: MessageSink<InputMessage>
    val outputMessages: LiveData<OutputMessage>
    val coordinates: LiveData<Coordinates>
    val isSelectButtonEnabled: LiveData<Boolean>
    val latitudeError: LiveData<String?>
    val longitudeError: LiveData<String?>
    val coordinatesMatchName: LiveData<String>
}
