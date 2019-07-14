package com.mfkw.compass.screen.pickcoordinate.view

import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import com.mfkw.compass.R
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.screen.pickcoordinate.PickCoordinateScreen
import com.mfkw.compass.screen.pickcoordinate.viewmodel.PickCoordinateScreenViewModel
import com.mfkw.compass.screen.pickcoordinate.viewmodel.PickCoordinateScreenViewModel.*
import com.mfkw.compass.util.InflatableView
import com.mfkw.compass.util.observe
import com.mfkw.compass.util.send
import kotlinx.android.synthetic.main.screen_coordinatepick.*
import kotlin.math.absoluteValue

class PickCoordinateScreenView(
    private val screen: PickCoordinateScreen,
    private val viewModel: PickCoordinateScreenViewModel
) : InflatableView(R.layout.screen_coordinatepick) {

    override fun onViewCreated() {
        viewModel.coordinates.observe(screen) { updateCoordinates(it) }
        viewModel.latitudeError.observe(screen) { latitudeEdit.error = it }
        viewModel.longitudeError.observe(screen) { longitudeEdit.error = it }
        viewModel.isSelectButtonEnabled.observe(screen){ selectButton.isEnabled = it }
        viewModel.coordinatesMatchName.observe(screen) { targetMatch.text = it }

        selectButton.setOnClickListener { viewModel.inputMessages.send(InputMessage.SelectClicked) }
        latitudeEdit.setOnEditorActionListener { _, _, _ -> sendLatitude(); false }
        latitudeChips.setOnCheckedChangeListener { _, _ -> sendLatitude() }
        longitudeEdit.setOnEditorActionListener { _, _, _ -> sendLongitude(); false }
        longitudeChips.setOnCheckedChangeListener { _, _ -> sendLongitude() }
    }

    private fun updateCoordinates(coordinates: Coordinates) {
        val (latitude, longitude) = coordinates
        latitudeEdit.setText("%.4f".format(latitude), TextView.BufferType.EDITABLE)
        latitudeChips.check(if(latitude > 0) R.id.chipN else R.id.chipS)

        longitudeEdit.setText("%.4f".format(longitude), TextView.BufferType.EDITABLE)
        longitudeChips.check(if(longitude > 0) R.id.chipE else R.id.chipW)
    }

    private fun sendLatitude() =
        viewModel.inputMessages.send(InputMessage.LatitudePicked(readLatitude()))

    private fun sendLongitude() =
        viewModel.inputMessages.send(InputMessage.LongitudePicked(readLongitude()))

    private fun readLatitude() = latitudeEdit
        .readDouble()
        .let { if(latitudeChips.checkedChipId == R.id.chipN) it else -it }

    private fun readLongitude() = longitudeEdit
        .readDouble()
        .let { if(longitudeChips.checkedChipId == R.id.chipE) it else -it }

    private fun EditText.readDouble() = text
        .toString()
        .replace(',', '.')
        .toDouble()
        .absoluteValue
}
