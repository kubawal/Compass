package com.mfkw.compass.screen.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mfkw.compass.application.HostActivity
import com.mfkw.compass.model.Coordinates
import com.mfkw.compass.screen.search.result.SearchScreenResultProducer
import com.mfkw.compass.screen.search.view.SearchScreenView
import com.mfkw.compass.screen.search.viewmodel.SearchScreenViewModelImpl
import com.mfkw.compass.screen.search.viewmodel.SearchScreenViewModel.*
import com.mfkw.compass.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

class SearchScreen : Fragment(), KodeinAware {
    override val kodein by kodein()

    private val resultProducer by activityViewModelProvider<SearchScreenResultProducer>()
    private val viewModel by viewModelProvider { direct.instance<SearchScreenViewModelImpl>() }
    private val view by lifecycleBound { SearchScreenView(this, viewModel, direct.instance()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        view.inflate(inflater, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.outputMessages.observe(this, ::processMessage)
    }

    private fun processMessage(message: OutputMessage) =
        when(message) {
            is OutputMessage.ReturnWithResult -> {
                findNavController().navigateUp()
                resultProducer.produce(message.result)
            }
        }

    companion object {
        fun obtainResultProducer(hostActivity: HostActivity): ValueProducer<Coordinates> =
            hostActivity.provideViewModel<SearchScreenResultProducer>()
    }
}
