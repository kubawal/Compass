package com.mfkw.compass.screen.main.viewmodel

import androidx.lifecycle.*
import com.mfkw.compass.api.geocoder.GeocoderApi
import com.mfkw.compass.service.ApplicationService
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.util.MessageStream
import com.mfkw.compass.screen.main.viewmodel.MainScreenViewModel.*
import com.mfkw.compass.service.CourseService
import com.mfkw.compass.service.LocationService
import com.mfkw.compass.service.OrientationService
import com.mfkw.compass.util.mutableLiveDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainScreenViewModelImpl(
    private val applicationService: ApplicationService,
    private val locationService: LocationService,
    private val orientationService: OrientationService,
    private val courseService: CourseService
) : ViewModel(), MainScreenViewModel {
    override val outputMessages = MessageStream<OutputMessage>()
    override val currentCoordinates = MutableLiveData<Coordinates>()
    override val targetCoordinates = MutableLiveData<Coordinates>()
    override val compass = MutableLiveData<Float>()
    override val course = MutableLiveData<Float>()
    override val info = mutableLiveDataOf("Waiting for location...")
    private val absoluteCourse = MutableLiveData<Float>()

    init {
        syncTargetCoordinates()
        syncCourse()
        ensureLocationEnabled()
        locationService.startLocationUpdates { updateCurrentCoordinates(it) }
        orientationService.startOrientationUpdates { updateOrientation(it) }
    }

    override fun onCleared() {
        locationService.stopLocationUpdates()
        orientationService.stopOrientationUpdates()
    }

    override val inputMessages = Observer<InputMessage> {
        when(it) {
            is InputMessage.SelectTargetButtonClick ->
                outputMessages.publish(
                    OutputMessage.CallPickCoordinatesScreen(
                        oldCoordinates = applicationService.targetCoordinates
                    )
                )
            is InputMessage.SearchButtonClick ->
                outputMessages.publish(OutputMessage.CallSearchScreen)
            is InputMessage.ReturnFromPickCoordinatesScreen -> {
                applicationService.targetCoordinates = it.newCoordinates
                syncTargetCoordinates()
                syncCourse()
            }
            is InputMessage.ReturnFromSearchScreen -> {
                applicationService.targetCoordinates = it.newCoordinates
                syncTargetCoordinates()
                syncCourse()
            }
        }
    }

    private fun ensureLocationEnabled() = viewModelScope.launch {
        val exception = locationService.ensureLocationEnabled()
        if(exception != null)
            outputMessages.publish(OutputMessage.ResolveApiException(exception))
    }

    private fun syncTargetCoordinates() {
        targetCoordinates.value = applicationService.targetCoordinates
        syncAbsoluteCourse()
    }

    private fun updateCurrentCoordinates(coordinates: Coordinates) {
        currentCoordinates.value = coordinates
        info.value = ""
        syncAbsoluteCourse()
    }

    private fun updateOrientation(orientationValue: Float) {
        compass.value = -orientationValue
        syncCourse()
    }

    private fun syncAbsoluteCourse() {
        if(currentCoordinates.value != null && targetCoordinates.value != null && compass.value != null) {
            absoluteCourse.value =
                courseService.computeCourse(currentCoordinates.value!!, targetCoordinates.value!!).toFloat()
            syncCourse()
        }
    }

    private fun syncCourse() {
        if(absoluteCourse.value != null && compass.value != null)
            course.value = absoluteCourse.value!! + compass.value!!
    }
}
