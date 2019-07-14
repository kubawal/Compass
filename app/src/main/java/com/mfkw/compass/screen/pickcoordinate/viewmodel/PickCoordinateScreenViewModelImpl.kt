package com.mfkw.compass.screen.pickcoordinate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfkw.compass.api.geocoder.GeocoderApi
import com.mfkw.compass.screen.pickcoordinate.PickCoordinateScreenArgs
import com.mfkw.compass.screen.pickcoordinate.viewmodel.PickCoordinateScreenViewModel.InputMessage
import com.mfkw.compass.screen.pickcoordinate.viewmodel.PickCoordinateScreenViewModel.OutputMessage
import com.mfkw.compass.util.MessageSink
import com.mfkw.compass.util.MessageStream
import com.mfkw.compass.util.mutableLiveDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PickCoordinateScreenViewModelImpl(
    args: PickCoordinateScreenArgs,
    private val geocoderApi: GeocoderApi
) : ViewModel(), PickCoordinateScreenViewModel {
    override val coordinates = mutableLiveDataOf(args.oldCoordinates)
    override val isSelectButtonEnabled = mutableLiveDataOf(true)
    override val outputMessages = MessageStream<OutputMessage>()
    override val latitudeError = MutableLiveData<String?>()
    override val longitudeError = MutableLiveData<String?>()
    override val coordinatesMatchName = MutableLiveData<String>()

    init {
        syncCoordinatesMatch()
    }

    override val inputMessages = MessageSink<InputMessage> {
        when(it) {
            is InputMessage.LatitudePicked -> {
                if(it.newValue != coordinates.value!!.latitude) {
                    coordinates.value = coordinates.value!!.copy(latitude = it.newValue)
                    validateCoordinates()
                    if (latitudeError.value == null)
                        syncCoordinatesMatch()
                }
            }
            is InputMessage.LongitudePicked -> {
                if(it.newValue != coordinates.value!!.longitude) {
                    coordinates.value = coordinates.value!!.copy(longitude = it.newValue)
                    validateCoordinates()
                    if (longitudeError.value == null)
                        syncCoordinatesMatch()
                }
            }
            is InputMessage.SelectClicked -> {
                if(isSelectButtonEnabled.value!!)
                    outputMessages.publish(OutputMessage.ReturnWithResult(coordinates.value!!))
            }
        }
    }

    private fun validateCoordinates() {
        val (latitude, longitude) = coordinates.value!!
        var error = false
        if(latitude < -90.0 || latitude > 90) {
            latitudeError.value = "Invalid latitude"
            error = true
        }
        if(longitude < -180 || longitude > 180) {
            longitudeError.value = "Invalid longitude"
            error = true
        }
        if(error)
            isSelectButtonEnabled.value = false
        else {
            latitudeError.value = null
            longitudeError.value = null
            isSelectButtonEnabled.value = true
        }
    }

    private fun syncCoordinatesMatch() = viewModelScope.launch {
        coordinatesMatchName.value = "Loading..."
        val (latitude, longitude) = coordinates.value!!
        coordinatesMatchName.value = withContext(Dispatchers.IO) {
            runCatching { geocoderApi.queryCoordinates(latitude, longitude).displayName }
                .getOrDefault("Not available")
        }
    }
}
