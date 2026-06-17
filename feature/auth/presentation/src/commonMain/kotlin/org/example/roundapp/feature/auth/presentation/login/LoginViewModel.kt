package org.example.roundapp.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.roundapp.core.domain.util.EmailValidator
import org.example.roundapp.core.domain.util.Result
import org.example.roundapp.feature.auth.domain.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.EmailChanged -> _state.update { it.copy(email = action.value, error = null) }
            is LoginAction.PasswordChanged -> _state.update { it.copy(password = action.value, error = null) }
            LoginAction.Submit -> submit()
        }
    }

    private fun submit() {
        val current = _state.value
        if (!EmailValidator.isValid(current.email)) {
            _state.update { it.copy(error = "Invalid email") }
            return
        }
        if (current.password.length < 6) {
            _state.update { it.copy(error = "Password too short") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase(current.email, current.password)
            _state.update { it.copy(isLoading = false) }
            when (result) {
                is Result.Success -> _events.send(LoginEvent.LoginSuccess)
                is Result.Failure -> _events.send(LoginEvent.Error(result.error.name))
            }
        }
    }
}
