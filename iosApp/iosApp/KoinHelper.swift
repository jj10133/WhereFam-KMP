//
//  KoinHelper.swift
//  iosApp
//
//  Created by joker on 2025-08-19.
//

import Foundation
import ComposeApp

func get<A: AnyObject>() -> A {
    return DependenciesProviderHelper.companion.koin.get(objCClass: A.self) as! A
}

func get<A: AnyObject>(_ type: A.Type) -> A {
    return DependenciesProviderHelper.companion.koin.get(objCClass: A.self) as! A
}

func get<A: AnyObject>(_ type: A.Type, qualifier: (any Koin_coreQualifier)? = nil, parameter: Any) -> A {
    return DependenciesProviderHelper.companion.koin.get(objCClass: A.self, qualifier: qualifier, parameter: parameter) as! A
}
