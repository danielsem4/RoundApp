package org.example.roundapp.feature.home.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.example.roundapp.feature.home.presentation.HomeScreenRoot

object HomeGraphRoutes {
    @Serializable data object Graph
    @Serializable data object Home
}

fun NavGraphBuilder.homeGraph(onLogout: () -> Unit) {
    navigation<HomeGraphRoutes.Graph>(startDestination = HomeGraphRoutes.Home) {
        composable<HomeGraphRoutes.Home> {
            HomeScreenRoot(onLoggedOut = onLogout)
        }
    }
}
