package org.example.roundapp.feature.home.data.di

import org.example.roundapp.feature.home.data.StubHomeService
import org.example.roundapp.feature.home.domain.HomeService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeDataModule = module {
    singleOf(::StubHomeService) bind HomeService::class
}
