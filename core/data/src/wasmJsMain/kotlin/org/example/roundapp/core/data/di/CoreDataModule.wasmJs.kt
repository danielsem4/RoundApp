package org.example.roundapp.core.data.di

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import org.example.roundapp.core.data.auth.SettingsSessionStorage
import org.example.roundapp.core.domain.auth.SessionStorage
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformCoreDataModule: Module = module {
    single<HttpClientEngine> { Js.create() }
    single<Settings> { StorageSettings() }
    singleOf(::SettingsSessionStorage) bind SessionStorage::class
}
