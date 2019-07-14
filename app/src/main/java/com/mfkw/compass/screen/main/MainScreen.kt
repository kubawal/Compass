package com.mfkw.compass.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mfkw.compass.application.hostActivity
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.screen.main.view.MainScreenView
import com.mfkw.compass.screen.main.viewmodel.MainScreenViewModel
import com.mfkw.compass.screen.main.viewmodel.MainScreenViewModel.*
import com.mfkw.compass.screen.main.viewmodel.MainScreenViewModelImpl
import com.mfkw.compass.screen.pickcoordinate.PickCoordinateScreen
import com.mfkw.compass.screen.search.SearchScreen
import com.mfkw.compass.util.lifecycleBound
import com.mfkw.compass.util.send
import com.mfkw.compass.util.viewModelProvider
import com.mfkw.compass.util.observe
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

class MainScreen : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val viewModel by viewModelProvider { direct.instance<MainScreenViewModelImpl>() }
    private val view by lifecycleBound { MainScreenView(this, viewModel, direct.instance()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            view.inflate(inflater, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.outputMessages.observe(this) { processMessage(it) }

        PickCoordinateScreen.obtainResultProducer(hostActivity)
            .value
            .observe(this) { returnFromPickCoordinatesScreen(it) }

        SearchScreen.obtainResultProducer(hostActivity)
            .value
            .observe(this) { returnFromSearchScreen(it) }
    }

    private fun processMessage(message: OutputMessage) {
        when (message) {
            is OutputMessage.CallPickCoordinatesScreen -> {
                val action = MainScreenDirections.actionMainScreenToPickCoordinateScreen(message.oldCoordinates)
                findNavController().navigate(action)
            }
            is OutputMessage.CallSearchScreen -> {
                val action = MainScreenDirections.actionMainScreenToSearchScreen()
                findNavController().navigate(action)
            }
            is OutputMessage.ResolveApiException ->
                message.exception.startResolutionForResult(hostActivity, 0)
        }
    }

    private fun returnFromPickCoordinatesScreen(result: Coordinates) =
            viewModel.inputMessages.send(
                InputMessage.ReturnFromPickCoordinatesScreen(
                    newCoordinates = result
                )
            )

    private fun returnFromSearchScreen(result: Coordinates) =
        viewModel.inputMessages.send(
            InputMessage.ReturnFromSearchScreen(
                newCoordinates = result
            )
        )
}
