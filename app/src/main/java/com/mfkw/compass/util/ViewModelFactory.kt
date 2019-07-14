package com.mfkw.compass.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KProperty

inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    noinline creator: () -> VM = { VM::class.java.newInstance() }
) = ViewModelProperty { provideViewModel(creator) }

inline fun <reified VM : ViewModel> Fragment.activityViewModelProvider(
    noinline creator: () -> VM = { VM::class.java.newInstance() }
) = ViewModelProperty { provideActivityViewModel(creator) }

inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
    noinline creator: () -> VM = { VM::class.java.newInstance() }
) = ViewModelProperty { provideViewModel(creator) }

inline fun <reified VM : ViewModel> Fragment.provideViewModel(
    noinline creator: () -> VM = { VM::class.java.newInstance() }
) = ViewModelProviders.of(this, createViewModelFactoryFor(creator)).get(VM::class.java)

inline fun <reified VM : ViewModel> Fragment.provideActivityViewModel(
    noinline creator: () -> VM = { VM::class.java.newInstance() }
) = ViewModelProviders.of(activity!!, createViewModelFactoryFor(creator)).get(VM::class.java)

inline fun <reified VM : ViewModel> FragmentActivity.provideViewModel(
    noinline creator: () -> VM = { VM::class.java.newInstance() }
) = ViewModelProviders.of(this, createViewModelFactoryFor(creator)).get(VM::class.java)

fun <VM : ViewModel> createViewModelFactoryFor(creator: () -> VM): ViewModelProvider.Factory
        = FactoryWithCreator(creator)

class ViewModelProperty<VM : ViewModel>(private val provider: () -> VM) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = provider()
}

private class FactoryWithCreator<VM : ViewModel>(val creator: () -> VM) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <V : ViewModel?> create(modelClass: Class<V>): V = creator() as V
}
