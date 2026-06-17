package org.example.roundapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.example.roundapp.MainViewModel
import org.example.roundapp.feature.auth.presentation.navigation.AuthGraphRoutes
import org.example.roundapp.feature.auth.presentation.navigation.authGraph
import org.example.roundapp.feature.home.presentation.navigation.HomeGraphRoutes
import org.example.roundapp.feature.home.presentation.navigation.homeGraph
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavigationRoot(viewModel: MainViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val navController: NavHostController = rememberNavController()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            navController.navigate(HomeGraphRoutes.Graph) {
                popUpTo(AuthGraphRoutes.Graph) { inclusive = true }
            }
        } else {
            navController.navigate(AuthGraphRoutes.Graph) {
                popUpTo(HomeGraphRoutes.Graph) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = AuthGraphRoutes.Graph) {
        authGraph(onLoginSuccess = {
            navController.navigate(HomeGraphRoutes.Graph) {
                popUpTo(AuthGraphRoutes.Graph) { inclusive = true }
            }
        })
        homeGraph(onLogout = {
            navController.navigate(AuthGraphRoutes.Graph) {
                popUpTo(HomeGraphRoutes.Graph) { inclusive = true }
            }
        })
    }
}
