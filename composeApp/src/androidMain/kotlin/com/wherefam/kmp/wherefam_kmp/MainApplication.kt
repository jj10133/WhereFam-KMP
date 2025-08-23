package com.wherefam.kmp.wherefam_kmp

import android.app.Application
import com.wherefam.kmp.wherefam_kmp.di.androidModule
import com.wherefam.kmp.wherefam_kmp.di.sharedModule
import com.wherefam.kmp.wherefam_kmp.di.targetModule
import com.wherefam.kmp.wherefam_kmp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(androidModule, viewModelModule, targetModule, sharedModule)
        }
    }
}