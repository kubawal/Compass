package com.mfkw.compass.service

import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

class SoftwareKeyboardService(
    private val inputMethodManager: InputMethodManager
) {
    fun hide(fragment: Fragment) {
        val token = fragment.view?.rootView?.windowToken
        inputMethodManager.hideSoftInputFromWindow(token, 0)
    }
}
