package com.mfkw.compass.configuration

import android.content.Context
import android.hardware.SensorManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import com.google.android.gms.location.FusedLocationProviderClient
import com.mfkw.compass.service.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val serviceModule = Kodein.Module("service") {
    bind<FusedLocationProviderClient>() with singleton { FusedLocationProviderClient(instance<Context>()) }
    bind<SensorManager>() with
            singleton { instance<Context>().getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    bind<InputMethodManager>() with
            singleton { instance<Context>().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    bind<ApplicationService>() with singleton { ApplicationService() }
    bind<CourseService>() with singleton { CourseService() }
    bind<LocationService>() with singleton { LocationService(instance(), instance()) }
    bind<OrientationService>() with singleton { OrientationService(instance()) }
    bind<SoftwareKeyboardService>() with singleton { SoftwareKeyboardService(instance()) }
}
