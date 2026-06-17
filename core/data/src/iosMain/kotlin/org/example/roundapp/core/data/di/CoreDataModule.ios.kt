package org.example.roundapp.core.data.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.example.roundapp.core.data.auth.DataStoreSessionStorage
import org.example.roundapp.core.data.auth.createDataStore
import org.example.roundapp.core.domain.auth.SessionStorage
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformCoreDataModule: Module = module {
    single<HttpClientEngine> { Darwin.create() }
    single { createDataStore() }
    singleOf(::DataStoreSessionStorage) bind SessionStorage::class
}
