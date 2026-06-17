package org.example.roundapp.core.domain.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.example.roundapp.core.domain.dto.AuthInfo

interface SessionStorage {
    fun observeAuthInfo(): Flow<AuthInfo?>
    suspend fun set(info: AuthInfo?)

    suspend fun getAccessToken(): String? =
        observeAuthInfo().firstOrNull()?.tokens?.access

    suspend fun getRefreshToken(): String? =
        observeAuthInfo().firstOrNull()?.tokens?.refresh
}
