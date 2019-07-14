package com.mfkw.compass.application

import android.app.Application
import android.content.Context
import com.mfkw.compass.configuration.geocoderModule
import com.mfkw.compass.configuration.screenModule
import com.mfkw.compass.configuration.serviceModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

class CompassApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@CompassApplication))
        import(geocoderModule)
        import(serviceModule)
        import(screenModule)

        bind<Context>() with provider { applicationContext }
    }
}
