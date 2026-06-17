package org.example.roundapp.core.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfo(
    val tokens: AuthTokens? = null,
    val user: User? = null,
)
