package org.example.roundapp.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val tokens: AuthTokensDto,
    val user: UserDto,
)
