package com.wherefam.kmp.wherefam_kmp.di

import com.wherefam.kmp.wherefam_kmp.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

val iosModule = module {
}

actual val targetModule: Module = module {
    single { getDatabaseBuilder() }
}