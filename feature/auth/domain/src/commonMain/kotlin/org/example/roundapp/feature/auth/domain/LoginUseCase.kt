package org.example.roundapp.feature.auth.domain

import org.example.roundapp.core.domain.auth.AuthResult
import org.example.roundapp.core.domain.auth.AuthService
import org.example.roundapp.core.domain.auth.SessionStorage
import org.example.roundapp.core.domain.dto.AuthInfo
import org.example.roundapp.core.domain.util.DataError
import org.example.roundapp.core.domain.util.Result

class LoginUseCase(
    private val authService: AuthService,
    private val sessionStorage: SessionStorage,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Result<AuthResult.Authenticated, DataError.Remote> {
        return when (val result = authService.login(email, password)) {
            is Result.Failure -> Result.Failure(result.error)
            is Result.Success -> {
                val authenticated = result.data as AuthResult.Authenticated
                sessionStorage.set(AuthInfo(tokens = authenticated.tokens, user = authenticated.user))
                Result.Success(authenticated)
            }
        }
    }
}
