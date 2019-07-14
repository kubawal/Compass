package com.mfkw.compass.screen.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfkw.compass.api.geocoder.GeocoderApi
import com.mfkw.compass.api.geocoder.model.GeocoderPlace
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.util.MessageStream
import com.mfkw.compass.screen.search.viewmodel.SearchScreenViewModel.*
import com.mfkw.compass.util.MessageSink
import com.mfkw.compass.util.map
import com.mfkw.compass.util.mutableLiveDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchScreenViewModelImpl(
    private val geocoderApi: GeocoderApi
) : ViewModel(), SearchScreenViewModel {
    private val placesRaw = mutableLiveDataOf(emptyList<GeocoderPlace>())

    override val outputMessages = MessageStream<OutputMessage>()
    override val places = placesRaw.map { list -> list.map { it.displayName } }
    override val placesInfo = mutableLiveDataOf("")

    override val inputMessages = MessageSink<InputMessage> {
        when(it) {
            is InputMessage.QueryEntered ->
                searchPlaces(it.query)
            is InputMessage.ListItemClick ->
                sendResult(it.position)
        }
    }

    private fun searchPlaces(query: String) = viewModelScope.launch {
        placesRaw.value = emptyList()
        placesInfo.value = "Loading..."
        val result = withContext(Dispatchers.IO) {
            runCatching { geocoderApi.queryPlaces(query) }.getOrNull()
        }
        if(result == null)
            placesInfo.value = "Not available"
        else {
            placesRaw.value = result
            placesInfo.value = ""
        }
    }

    private fun sendResult(position: Int) =
        placesRaw.value!![position]
            .let { outputMessages.publish(OutputMessage.ReturnWithResult(Coordinates(it.lat, it.lon))) }
}
