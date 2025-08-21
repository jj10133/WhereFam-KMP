package com.wherefam.kmp.wherefam_kmp.di

import com.wherefam.kmp.wherefam_kmp.AppSettings
import com.wherefam.kmp.wherefam_kmp.processing.GenericMessageProcessor
import org.koin.dsl.module

val sharedModule = module {
    single {
        GenericMessageProcessor()
    }

    single { AppSettings(get()) }
}