package org.example.roundapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.example.roundapp.core.domain.auth.SessionStorage

data class MainState(
    val isLoggedIn: Boolean,
)

class MainViewModel(
    sessionStorage: SessionStorage,
) : ViewModel() {
    val state: StateFlow<MainState> = sessionStorage.observeAuthInfo()
        .map { MainState(isLoggedIn = it?.tokens != null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MainState(false))
}
