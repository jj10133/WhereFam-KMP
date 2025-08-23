package com.wherefam.kmp.wherefam_kmp.di

import com.wherefam.kmp.wherefam_kmp.data.DataStoreRepository
import com.wherefam.kmp.wherefam_kmp.database.getDatabaseBuilder
import com.wherefam.kmp.wherefam_kmp.managers.LocationManager
import com.wherefam.kmp.wherefam_kmp.ui.onboarding.SplashViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    single { LocationManager(get()) }
    single { DataStoreRepository(get()) }

}

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
}

actual val targetModule: Module = module {
    single { getDatabaseBuilder(get()) }
}