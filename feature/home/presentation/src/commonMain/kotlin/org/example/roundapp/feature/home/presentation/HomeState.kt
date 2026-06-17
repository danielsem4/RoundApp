package org.example.roundapp.feature.home.presentation

import org.example.roundapp.feature.home.domain.HomeData

data class HomeState(
    val isLoading: Boolean = false,
    val data: HomeData? = null,
    val error: String? = null,
)

sealed interface HomeAction {
    data object Refresh : HomeAction
    data object Logout : HomeAction
}

sealed interface HomeEvent {
    data object LoggedOut : HomeEvent
}
