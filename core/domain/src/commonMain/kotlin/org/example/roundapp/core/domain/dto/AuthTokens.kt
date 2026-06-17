package org.example.roundapp.core.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokens(
    val access: String,
    val refresh: String,
)
