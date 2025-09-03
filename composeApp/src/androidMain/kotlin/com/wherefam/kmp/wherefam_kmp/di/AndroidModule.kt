package com.wherefam.kmp.wherefam_kmp.di

import com.wherefam.kmp.wherefam_kmp.data.DataStoreRepository
import com.wherefam.kmp.wherefam_kmp.data.IpcManager
import com.wherefam.kmp.wherefam_kmp.managers.LocationManager
import com.wherefam.kmp.wherefam_kmp.viewmodel.HomeViewModel
import com.wherefam.kmp.wherefam_kmp.viewmodel.PeopleViewModel
import com.wherefam.kmp.wherefam_kmp.ui.home.share.ShareViewModel
import com.wherefam.kmp.wherefam_kmp.ui.onboarding.OnboardingViewModel
import com.wherefam.kmp.wherefam_kmp.ui.onboarding.SplashViewModel
import com.wherefam.kmp.wherefam_kmp.ui.onboarding.ThirdPageViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    single { LocationManager(get()) }
    single { DataStoreRepository(get()) }
}

val androidViewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { ThirdPageViewModel(get(), get()) }
    viewModel { ShareViewModel(get(), get()) }
}