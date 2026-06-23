package org.example.roundapp.feature.auth.presentation.login

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.roundapp.core.domain.util.DataError
import org.example.roundapp.core.domain.util.EmailValidator
import org.example.roundapp.core.domain.util.Result
import org.example.roundapp.core.presentation.ui.UiText
import org.example.roundapp.feature.auth.domain.LoginUseCase
import org.example.roundapp.feature.auth.presentation.resources.Res
import org.example.roundapp.feature.auth.presentation.resources.error_invalid_email
import org.example.roundapp.feature.auth.presentation.resources.error_login_failed
import org.example.roundapp.feature.auth.presentation.resources.error_no_internet
import org.example.roundapp.feature.auth.presentation.resources.error_server

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeInputs()
    }

    private fun observeInputs() {
        val emailFlow = snapshotFlow { _state.value.emailTextState.text.toString() }
            .distinctUntilChanged()
            .onEach { email ->
                _state.update { it.copy(isEmailValid = EmailValidator.isValid(email)) }
            }
        val passwordFlow = snapshotFlow { _state.value.passwordTextState.text.toString() }
            .distinctUntilChanged()

        combine(emailFlow, passwordFlow) { _, password ->
            _state.value.isEmailValid && password.isNotBlank()
        }
            .distinctUntilChanged()
            .onEach { canLogin -> _state.update { it.copy(canLogin = canLogin) } }
            .launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnInputTextFocusGain -> _state.update {
                it.copy(emailError = null, passwordError = null, loginError = null)
            }
            LoginAction.OnTogglePasswordVisibilityClick -> _state.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            LoginAction.OnLoginClick -> login()
        }
    }

    private fun login() {
        val current = _state.value
        val email = current.emailTextState.text.toString()
        val password = current.passwordTextState.text.toString()

        if (!EmailValidator.isValid(email)) {
            _state.update {
                it.copy(emailError = UiText.StringRes(Res.string.error_invalid_email))
            }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(isLoggingIn = true, loginError = null, emailError = null, passwordError = null)
            }
            val result = loginUseCase(email, password)
            when (result) {
                is Result.Success -> {
                    _state.update { it.copy(isLoggingIn = false) }
                    _events.send(LoginEvent.Success)
                }
                is Result.Failure -> _state.update {
                    it.copy(isLoggingIn = false, loginError = result.error.toUiText())
                }
            }
        }
    }
}

private fun DataError.Remote.toUiText(): UiText = when (this) {
    DataError.Remote.UNAUTHORIZED,
    DataError.Remote.FORBIDDEN,
    DataError.Remote.BAD_REQUEST -> UiText.StringRes(Res.string.error_login_failed)
    DataError.Remote.NO_INTERNET -> UiText.StringRes(Res.string.error_no_internet)
    else -> UiText.StringRes(Res.string.error_server)
}
