package com.wherefam.kmp.wherefam_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform