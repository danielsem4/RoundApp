package org.example.roundapp.core.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import org.example.roundapp.core.data.dto.RefreshTokenRequest
import org.example.roundapp.core.data.dto.AuthTokensDto
import org.example.roundapp.core.data.mapper.toDomain
import org.example.roundapp.core.domain.auth.SessionStorage
import org.example.roundapp.core.domain.dto.AuthInfo
import org.example.roundapp.core.domain.logger.AppLogger
import org.example.roundapp.core.domain.util.DataError
import org.example.roundapp.core.domain.util.Result

class HttpClientFactory(
    private val logger: AppLogger,
    private val sessionStorage: SessionStorage,
) {
    fun create(engine: HttpClientEngine): HttpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 20_000L
            requestTimeoutMillis = 20_000L
        }
        install(Logging) {
            this.logger = object : Logger {
                override fun log(message: String) {
                    this@HttpClientFactory.logger.debug(message)
                }
            }
            level = LogLevel.INFO
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = sessionStorage.getAccessToken()
                    val refreshToken = sessionStorage.getRefreshToken()
                    if (accessToken != null && refreshToken != null) {
                        BearerTokens(accessToken, refreshToken)
                    } else null
                }
                refreshTokens {
                    val refreshToken = sessionStorage.getRefreshToken() ?: return@refreshTokens null
                    val response = client.post<RefreshTokenRequest, AuthTokensDto>(
                        route = "auth/refresh/",
                        body = RefreshTokenRequest(refresh = refreshToken),
                    )
                    when (response) {
                        is Result.Success -> {
                            val newTokens = response.data.toDomain()
                            val current = sessionStorage.observeAuthInfo().firstOrNull()
                            sessionStorage.set(
                                current?.copy(tokens = newTokens) ?: AuthInfo(tokens = newTokens),
                            )
                            BearerTokens(newTokens.access, newTokens.refresh)
                        }
                        is Result.Failure -> {
                            if (response.error == DataError.Remote.UNAUTHORIZED ||
                                response.error == DataError.Remote.FORBIDDEN
                            ) {
                                sessionStorage.set(null)
                            }
                            null
                        }
                    }
                }
            }
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
}
