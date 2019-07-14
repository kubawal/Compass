package com.mfkw.compass.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.mfkw.compass.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.*

abstract class InflatableView(@LayoutRes private val layoutId: Int) : LayoutContainer {
    final override var containerView: View? = null
        protected set

    fun inflate(inflater: LayoutInflater, container: ViewGroup?): View {
        val inflatedView = inflater.inflate(layoutId, container, false)
        containerView = inflatedView
        clearFindViewByIdCache()
        onViewCreated()
        return inflatedView
    }

    open fun onViewCreated() = Unit
}
