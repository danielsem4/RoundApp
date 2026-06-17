package org.example.roundapp.core.domain.auth

import org.example.roundapp.core.domain.dto.AuthTokens
import org.example.roundapp.core.domain.util.DataError
import org.example.roundapp.core.domain.util.EmptyResult
import org.example.roundapp.core.domain.util.Result

interface AuthService {
    suspend fun login(email: String, password: String): Result<AuthResult, DataError.Remote>
    suspend fun refreshToken(refreshToken: String): Result<AuthTokens, DataError.Remote>
    suspend fun logout(): EmptyResult<DataError.Remote>
}
