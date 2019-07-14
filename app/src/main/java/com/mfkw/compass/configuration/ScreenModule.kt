package com.mfkw.compass.configuration

import com.mfkw.compass.screen.main.view.MainScreenViewFormatHelper
import com.mfkw.compass.screen.main.viewmodel.MainScreenViewModelImpl
import com.mfkw.compass.screen.pickcoordinate.PickCoordinateScreenArgs
import com.mfkw.compass.screen.pickcoordinate.viewmodel.PickCoordinateScreenViewModelImpl
import com.mfkw.compass.screen.search.viewmodel.SearchScreenViewModelImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.*

val screenModule = Kodein.Module("screen") {
    bind<MainScreenViewFormatHelper>() with singleton { MainScreenViewFormatHelper() }
    bind<MainScreenViewModelImpl>() with
            provider { MainScreenViewModelImpl(instance(), instance(), instance(), instance()) }
    bind<PickCoordinateScreenViewModelImpl>() with
            factory { args: PickCoordinateScreenArgs -> PickCoordinateScreenViewModelImpl(args, instance()) }
    bind<SearchScreenViewModelImpl>() with
            provider { SearchScreenViewModelImpl(instance()) }
}
