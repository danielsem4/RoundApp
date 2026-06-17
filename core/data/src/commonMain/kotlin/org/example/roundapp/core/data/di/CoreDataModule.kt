package org.example.roundapp.core.data.di

import org.example.roundapp.core.data.auth.KtorAuthService
import org.example.roundapp.core.data.logging.KermitLogger
import org.example.roundapp.core.data.networking.HttpClientFactory
import org.example.roundapp.core.domain.auth.AuthService
import org.example.roundapp.core.domain.logger.AppLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)

    single<AppLogger> { KermitLogger }
    single { HttpClientFactory(get(), get()).create(get()) }
    singleOf(::KtorAuthService) bind AuthService::class
}
