package org.example.roundapp.core.data.auth

import io.ktor.client.HttpClient
import org.example.roundapp.core.data.dto.AuthResponseDto
import org.example.roundapp.core.data.dto.AuthTokensDto
import org.example.roundapp.core.data.dto.LoginRequest
import org.example.roundapp.core.data.dto.RefreshTokenRequest
import org.example.roundapp.core.data.mapper.toDomain
import org.example.roundapp.core.data.networking.post
import org.example.roundapp.core.domain.auth.AuthResult
import org.example.roundapp.core.domain.auth.AuthService
import org.example.roundapp.core.domain.dto.AuthTokens
import org.example.roundapp.core.domain.util.DataError
import org.example.roundapp.core.domain.util.EmptyResult
import org.example.roundapp.core.domain.util.Result
import org.example.roundapp.core.domain.util.asEmptyResult
import org.example.roundapp.core.domain.util.map

class KtorAuthService(
    private val httpClient: HttpClient,
) : AuthService {

    override suspend fun login(
        email: String,
        password: String,
    ): Result<AuthResult, DataError.Remote> =
        httpClient.post<LoginRequest, AuthResponseDto>(
            route = "auth/login/",
            body = LoginRequest(email = email, password = password),
        ).map { it.toDomain() }

    override suspend fun refreshToken(
        refreshToken: String,
    ): Result<AuthTokens, DataError.Remote> =
        httpClient.post<RefreshTokenRequest, AuthTokensDto>(
            route = "auth/refresh/",
            body = RefreshTokenRequest(refresh = refreshToken),
        ).map { it.toDomain() }

    override suspend fun logout(): EmptyResult<DataError.Remote> =
        httpClient.post<Unit, Unit>(route = "auth/logout/", body = Unit).asEmptyResult()
}
