package com.wherefam.kmp.wherefam_kmp

import android.app.Application
import com.wherefam.kmp.wherefam_kmp.di.androidModule
import com.wherefam.kmp.wherefam_kmp.di.androidViewModelModule
import com.wherefam.kmp.wherefam_kmp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.maplibre.android.MapLibre

class MainApplication: Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(this)

        initKoin {
            androidContext(this@MainApplication)
            modules(androidModule, androidViewModelModule)
        }
    }
}