package com.mfkw.compass.screen.main.view

import androidx.lifecycle.Observer
import com.mfkw.compass.R
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.screen.main.MainScreen
import com.mfkw.compass.screen.main.viewmodel.MainScreenViewModel
import com.mfkw.compass.screen.main.viewmodel.MainScreenViewModel.InputMessage
import com.mfkw.compass.util.InflatableView
import com.mfkw.compass.util.observe
import com.mfkw.compass.util.send
import kotlinx.android.synthetic.main.screen_main.*

class MainScreenView(
    private val screen: MainScreen,
    private val viewModel: MainScreenViewModel,
    private val formatHelper: MainScreenViewFormatHelper
) : InflatableView(R.layout.screen_main) {

    override fun onViewCreated() {
        viewModel.targetCoordinates.observe(screen) { updateTargetCoordinates(it) }
        viewModel.compass.observe(screen) { updateCompass(it) }
        viewModel.course.observe(screen) { updateCourse(it) }
        viewModel.info.observe(screen) { info.text = it }

        selectTargetButton.setOnClickListener { viewModel.inputMessages.send(InputMessage.SelectTargetButtonClick) }
        searchButton.setOnClickListener { viewModel.inputMessages.send(InputMessage.SearchButtonClick) }
    }

    private fun updateTargetCoordinates(coordinates: Coordinates) {
        targetCoordinates.text = screen.getString(
            R.string.main_target_coordinates,
            formatHelper.formatLatitude(coordinates.latitude),
            formatHelper.formatLongitude(coordinates.longitude)
        )
    }

    private fun updateCompass(compass: Float) {
        compassView.rotation = Math.toDegrees(compass.toDouble()).toFloat()
    }

    private fun updateCourse(course: Float) {
        courseView.rotation = Math.toDegrees(course.toDouble()).toFloat()
    }
}
