package org.example.roundapp.feature.auth.presentation.di

import org.example.roundapp.feature.auth.domain.LoginUseCase
import org.example.roundapp.feature.auth.presentation.login.LoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    singleOf(::LoginUseCase)
    viewModelOf(::LoginViewModel)
}
