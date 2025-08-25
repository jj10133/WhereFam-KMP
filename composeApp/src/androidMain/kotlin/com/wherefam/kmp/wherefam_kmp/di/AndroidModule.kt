package com.wherefam.kmp.wherefam_kmp.di

import com.wherefam.kmp.wherefam_kmp.data.DataStoreRepository
import com.wherefam.kmp.wherefam_kmp.data.IPCProvider
import com.wherefam.kmp.wherefam_kmp.database.getDatabaseBuilder
import com.wherefam.kmp.wherefam_kmp.managers.LocationManager
import com.wherefam.kmp.wherefam_kmp.ui.home.HomeViewModel
import com.wherefam.kmp.wherefam_kmp.ui.home.people.PeopleViewModel
import com.wherefam.kmp.wherefam_kmp.ui.home.share.ShareViewModel
import com.wherefam.kmp.wherefam_kmp.ui.onboarding.OnboardingViewModel
import com.wherefam.kmp.wherefam_kmp.ui.onboarding.SplashViewModel
import com.wherefam.kmp.wherefam_kmp.ui.onboarding.ThirdPageViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    single { LocationManager(get()) }
    single { IPCProvider.ipc }
    single { DataStoreRepository(get()) }
}

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { ThirdPageViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ShareViewModel(get(), get()) }
    viewModel { PeopleViewModel(get(), get()) }
}

actual val targetModule: Module = module {
    single { getDatabaseBuilder(get()) }
}