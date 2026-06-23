package org.example.roundapp.feature.auth.presentation.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.roundapp.core.designsystem.components.AppButton
import org.example.roundapp.core.designsystem.components.AppInputField
import org.example.roundapp.core.designsystem.components.icons.AppIcons
import org.example.roundapp.core.designsystem.components.icons.Email
import org.example.roundapp.core.designsystem.components.icons.Lock
import org.example.roundapp.core.designsystem.components.icons.Visibility
import org.example.roundapp.core.designsystem.components.icons.VisibilityOff
import org.example.roundapp.core.designsystem.components.layouts.AppFormLayout
import org.example.roundapp.core.designsystem.components.layouts.AppSnackbarScaffold
import org.example.roundapp.core.designsystem.components.logo.AppLogo
import org.example.roundapp.core.presentation.ObserveAsEvents
import org.example.roundapp.feature.auth.presentation.resources.Res
import org.example.roundapp.feature.auth.presentation.resources.button_login
import org.example.roundapp.feature.auth.presentation.resources.hint_email
import org.example.roundapp.feature.auth.presentation.resources.hint_password
import org.example.roundapp.feature.auth.presentation.resources.label_email
import org.example.roundapp.feature.auth.presentation.resources.label_password
import org.example.roundapp.feature.auth.presentation.resources.title_welcome
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LoginEvent.Success -> onLoginSuccess()
        }
    }

    LoginScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    AppSnackbarScaffold(snackbarHostState = snackbarHostState) {
        AppFormLayout(
            headerText = stringResource(Res.string.title_welcome),
            errorText = state.loginError?.asString(),
            logo = { AppLogo() },
        ) {
            AppInputField(
                state = state.emailTextState,
                title = stringResource(Res.string.label_email),
                placeholder = stringResource(Res.string.hint_email),
                supportingText = state.emailError?.asString(),
                isError = state.emailError != null,
                enabled = !state.isLoggingIn,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                leadingIcon = AppIcons.Email,
                onFocusChanged = { focused ->
                    if (focused) onAction(LoginAction.OnInputTextFocusGain)
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppInputField(
                state = state.passwordTextState,
                title = stringResource(Res.string.label_password),
                placeholder = stringResource(Res.string.hint_password),
                supportingText = state.passwordError?.asString(),
                isError = state.passwordError != null,
                enabled = !state.isLoggingIn,
                isSecure = true,
                isContentVisible = state.isPasswordVisible,
                imeAction = ImeAction.Done,
                leadingIcon = AppIcons.Lock,
                trailingIcon = if (state.isPasswordVisible) AppIcons.VisibilityOff
                else AppIcons.Visibility,
                onTrailingIconClick = {
                    onAction(LoginAction.OnTogglePasswordVisibilityClick)
                },
                onFocusChanged = { focused ->
                    if (focused) onAction(LoginAction.OnInputTextFocusGain)
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(
                text = stringResource(Res.string.button_login),
                onClick = { onAction(LoginAction.OnLoginClick) },
                enabled = state.canLogin,
                isLoading = state.isLoggingIn,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
