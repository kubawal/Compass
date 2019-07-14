package com.mfkw.compass.screen.search.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mfkw.compass.R

private val stringDiffCallback = object : DiffUtil.ItemCallback<String>() {
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem === newItem
}

class PlacesListAdapter(
    private val itemClickCallback: (position: Int) -> Unit
) : ListAdapter<String, PlacesListAdapter.ViewHolder>(stringDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_places, parent, false)
            .let { ViewHolder(it as TextView) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), clickCallback = { itemClickCallback(position) })

    class ViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {
        fun bind(value: String, clickCallback: () -> Unit) {
            view.text = value
            view.setOnClickListener { clickCallback() }
        }
    }
}
