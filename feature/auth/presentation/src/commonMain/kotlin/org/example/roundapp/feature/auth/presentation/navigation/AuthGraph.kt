package org.example.roundapp.feature.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.example.roundapp.feature.auth.presentation.login.LoginScreenRoot

object AuthGraphRoutes {
    @Serializable data object Graph
    @Serializable data object Login
}

fun NavGraphBuilder.authGraph(onLoginSuccess: () -> Unit) {
    navigation<AuthGraphRoutes.Graph>(startDestination = AuthGraphRoutes.Login) {
        composable<AuthGraphRoutes.Login> {
            LoginScreenRoot(onLoginSuccess = onLoginSuccess)
        }
    }
}
