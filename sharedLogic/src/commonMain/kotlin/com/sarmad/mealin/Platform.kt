package com.sarmad.mealin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform