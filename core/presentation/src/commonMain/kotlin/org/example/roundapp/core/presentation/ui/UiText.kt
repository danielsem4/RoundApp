package org.example.roundapp.core.presentation.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class StringRes(val resource: StringResource) : UiText

    @Composable
    fun asString(): String = when (this) {
        is Dynamic -> value
        is StringRes -> stringResource(resource)
    }
}
