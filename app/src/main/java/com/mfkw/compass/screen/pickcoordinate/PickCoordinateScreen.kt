package com.mfkw.compass.screen.pickcoordinate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mfkw.compass.application.HostActivity
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.screen.pickcoordinate.result.PickCoordinateScreenResultProducer
import com.mfkw.compass.screen.pickcoordinate.view.PickCoordinateScreenView
import com.mfkw.compass.screen.pickcoordinate.viewmodel.PickCoordinateScreenViewModel.*
import com.mfkw.compass.screen.pickcoordinate.viewmodel.PickCoordinateScreenViewModelImpl
import com.mfkw.compass.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

class PickCoordinateScreen : Fragment(), KodeinAware {
    override val kodein by kodein()

    private val args by navArgs<PickCoordinateScreenArgs>()
    private val resultProducer by activityViewModelProvider<PickCoordinateScreenResultProducer>()
    private val viewModel by viewModelProvider<PickCoordinateScreenViewModelImpl> { direct.instance(arg = args) }
    private val view by lifecycleBound { PickCoordinateScreenView(this, viewModel) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        view.inflate(inflater, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.outputMessages.observe(this, Observer {
            when(it) {
                is OutputMessage.ReturnWithResult -> {
                    findNavController().navigateUp()
                    resultProducer.produce(it.result)
                }
            }
        })
    }

    companion object {
        fun obtainResultProducer(hostActivity: HostActivity): ValueProducer<Coordinates> =
                hostActivity.provideViewModel<PickCoordinateScreenResultProducer>()
    }
}
