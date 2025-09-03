package com.wherefam.kmp.wherefam_kmp.di

import com.wherefam.kmp.wherefam_kmp.domain.PeerRepository
import com.wherefam.kmp.wherefam_kmp.domain.UserService
import com.wherefam.kmp.wherefam_kmp.processing.GenericMessageProcessor
import com.wherefam.kmp.wherefam_kmp.processing.MessageProcessor
import com.wherefam.kmp.wherefam_kmp.processing.UserRepository
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule())
    }

//called by iOS
fun initKoin() = initKoin(appDeclaration = {})

fun commonModule() = module {
    single<UserRepository> { UserService() }
    single { GenericMessageProcessor(get()) }
    single { PeerRepository() }


    includes(viewModelModule)
    includes(platformModule())
}
//
//@OptIn(BetaInteropApi::class)
//fun Koin.get(objCClass: ObjCClass): Any {
//    val kClazz = getOriginalKotlinClass(objCClass)!!
//    return get(kClazz, null, null)
//}
//
//@OptIn(BetaInteropApi::class)
//fun Koin.get(objCClass: ObjCClass, qualifier: Qualifier?, parameter: Any): Any {
//    val kClazz = getOriginalKotlinClass(objCClass)!!
//    return get(kClazz, qualifier) { parametersOf(parameter) }
//}