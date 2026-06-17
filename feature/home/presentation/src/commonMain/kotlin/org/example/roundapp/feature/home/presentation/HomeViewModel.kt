package org.example.roundapp.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.roundapp.core.domain.auth.SessionStorage
import org.example.roundapp.core.domain.util.Result
import org.example.roundapp.feature.home.domain.HomeService

class HomeViewModel(
    private val homeService: HomeService,
    private val sessionStorage: SessionStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    init {
        load()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.Refresh -> load()
            HomeAction.Logout -> logout()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = homeService.loadHome()) {
                is Result.Success -> _state.update { it.copy(isLoading = false, data = result.data) }
                is Result.Failure -> _state.update { it.copy(isLoading = false, error = result.error.name) }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            sessionStorage.set(null)
            _events.send(HomeEvent.LoggedOut)
        }
    }
}
