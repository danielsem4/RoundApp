package org.example.roundapp.core.domain.util

object EmailValidator {
    private val regex = Regex("^[A-Za-z0-9+_.-]+@(.+)$")

    fun isValid(email: String): Boolean = regex.matches(email.trim())
}
