package org.example.roundapp.core.domain.auth

import org.example.roundapp.core.domain.dto.AuthTokens
import org.example.roundapp.core.domain.dto.User

sealed interface AuthResult {
    data class Authenticated(val tokens: AuthTokens, val user: User) : AuthResult
}
