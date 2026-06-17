package org.example.roundapp.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refresh: String,
)
