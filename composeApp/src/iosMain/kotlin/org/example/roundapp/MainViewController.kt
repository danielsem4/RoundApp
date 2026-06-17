package org.example.roundapp

import androidx.compose.ui.window.ComposeUIViewController
import org.example.roundapp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() },
) {
    App()
}
