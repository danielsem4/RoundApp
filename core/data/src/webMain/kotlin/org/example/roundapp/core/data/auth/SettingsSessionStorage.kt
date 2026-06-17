package org.example.roundapp.core.data.auth

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.example.roundapp.core.domain.auth.SessionStorage
import org.example.roundapp.core.domain.dto.AuthInfo

class SettingsSessionStorage(
    private val settings: Settings,
) : SessionStorage {

    private val key = "KEY_AUTH_INFO"
    private val json = Json { ignoreUnknownKeys = true }

    private val state: MutableStateFlow<AuthInfo?> = MutableStateFlow(load())

    private fun load(): AuthInfo? =
        settings.getStringOrNull(key)?.let { json.decodeFromString<AuthInfo>(it) }

    override fun observeAuthInfo(): StateFlow<AuthInfo?> = state.asStateFlow()

    override suspend fun set(info: AuthInfo?) {
        if (info == null) {
            settings.remove(key)
        } else {
            settings.putString(key, json.encodeToString(AuthInfo.serializer(), info))
        }
        state.value = info
    }
}
