package com.wherefam.kmp.wherefam_kmp.di

import com.wherefam.kmp.wherefam_kmp.data.getRoomDatabase
import com.wherefam.kmp.wherefam_kmp.processing.GenericMessageProcessor
import org.koin.core.module.Module
import org.koin.dsl.module


expect val targetModule: Module
val sharedModule = module {
    single {
        GenericMessageProcessor()
    }

    single { getRoomDatabase(get()) }

}