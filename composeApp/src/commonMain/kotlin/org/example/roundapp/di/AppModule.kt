package org.example.roundapp.di

import org.example.roundapp.MainViewModel
import org.example.roundapp.core.data.di.coreDataModule
import org.example.roundapp.feature.auth.presentation.di.authPresentationModule
import org.example.roundapp.feature.home.data.di.homeDataModule
import org.example.roundapp.feature.home.presentation.di.homePresentationModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
}

val allModules = listOf(
    coreDataModule,
    authPresentationModule,
    homeDataModule,
    homePresentationModule,
    appModule,
)
