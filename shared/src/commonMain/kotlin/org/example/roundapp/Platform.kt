package org.example.roundapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform