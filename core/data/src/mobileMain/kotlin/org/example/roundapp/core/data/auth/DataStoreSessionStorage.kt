package org.example.roundapp.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.example.roundapp.core.domain.auth.SessionStorage
import org.example.roundapp.core.domain.dto.AuthInfo

class DataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>,
) : SessionStorage {

    private val authInfoKey = stringPreferencesKey("KEY_AUTH_INFO")
    private val json = Json { ignoreUnknownKeys = true }

    override fun observeAuthInfo(): Flow<AuthInfo?> = dataStore.data.map { prefs ->
        prefs[authInfoKey]?.let { json.decodeFromString<AuthInfo>(it) }
    }

    override suspend fun set(info: AuthInfo?) {
        dataStore.edit { prefs ->
            if (info == null) {
                prefs.remove(authInfoKey)
            } else {
                prefs[authInfoKey] = json.encodeToString(AuthInfo.serializer(), info)
            }
        }
    }
}
