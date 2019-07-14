package com.mfkw.compass.screen.search.view

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfkw.compass.R
import com.mfkw.compass.screen.search.SearchScreen
import com.mfkw.compass.screen.search.viewmodel.SearchScreenViewModel
import com.mfkw.compass.screen.search.viewmodel.SearchScreenViewModel.InputMessage
import com.mfkw.compass.service.SoftwareKeyboardService
import com.mfkw.compass.util.InflatableView
import com.mfkw.compass.util.observe
import com.mfkw.compass.util.send
import kotlinx.android.synthetic.main.screen_search.*

class SearchScreenView(
    private val screen: SearchScreen,
    private val viewModel: SearchScreenViewModel,
    private val softwareKeyboardService: SoftwareKeyboardService
) : InflatableView(R.layout.screen_search) {
    private val placesListAdapter = PlacesListAdapter(itemClickCallback = ::sendItemClick)

    override fun onViewCreated() {
        setupPlacesList()

        viewModel.places.observe(screen) { placesListAdapter.submitList(it) }
        viewModel.placesInfo.observe(screen) { placesInfo.text = it }

        searchButton.setOnClickListener { sendQueryEntered() }
    }

    private fun setupPlacesList() {
        placesList.adapter = placesListAdapter
        placesList.layoutManager = LinearLayoutManager(screen.context!!)
        placesList.addItemDecoration(DividerItemDecoration(screen.context, DividerItemDecoration.VERTICAL))
    }

    private fun sendQueryEntered() {
        softwareKeyboardService.hide(screen)
        viewModel.inputMessages.send(InputMessage.QueryEntered(queryEdit.text.toString()))
    }

    private fun sendItemClick(position: Int) =
        viewModel.inputMessages.send(InputMessage.ListItemClick(position))
}
