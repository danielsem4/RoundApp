package org.example.roundapp

import androidx.compose.runtime.Composable
import org.example.roundapp.core.designsystem.theme.AppTheme
import org.example.roundapp.navigation.NavigationRoot

@Composable
fun App() {
    AppTheme {
        NavigationRoot()
    }
}
