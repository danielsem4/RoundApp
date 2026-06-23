package org.example.roundapp.feature.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import org.example.roundapp.core.presentation.ui.UiText

data class LoginState(
    val emailTextState: TextFieldState = TextFieldState(),
    val passwordTextState: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val loginError: UiText? = null,
    val isLoggingIn: Boolean = false,
    val canLogin: Boolean = false,
    val isPasswordVisible: Boolean = false,
)

sealed interface LoginAction {
    data object OnLoginClick : LoginAction
    data object OnInputTextFocusGain : LoginAction
    data object OnTogglePasswordVisibilityClick : LoginAction
}

sealed interface LoginEvent {
    data object Success : LoginEvent
}
