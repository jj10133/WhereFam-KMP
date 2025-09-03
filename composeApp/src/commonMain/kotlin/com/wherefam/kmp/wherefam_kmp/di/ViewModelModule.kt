package com.wherefam.kmp.wherefam_kmp.di

import com.wherefam.kmp.wherefam_kmp.viewmodel.HomeViewModel
import com.wherefam.kmp.wherefam_kmp.viewmodel.PeopleViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::PeopleViewModel)
}