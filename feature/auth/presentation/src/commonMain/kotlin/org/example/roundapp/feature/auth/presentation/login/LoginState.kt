package org.example.roundapp.feature.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed interface LoginEvent {
    data object LoginSuccess : LoginEvent
    data class Error(val message: String) : LoginEvent
}

sealed interface LoginAction {
    data class EmailChanged(val value: String) : LoginAction
    data class PasswordChanged(val value: String) : LoginAction
    data object Submit : LoginAction
}
