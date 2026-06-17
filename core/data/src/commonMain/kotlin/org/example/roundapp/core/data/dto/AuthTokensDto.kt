package org.example.roundapp.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokensDto(
    val access: String,
    val refresh: String,
)
