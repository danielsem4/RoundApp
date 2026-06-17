package org.example.roundapp.core.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
)
