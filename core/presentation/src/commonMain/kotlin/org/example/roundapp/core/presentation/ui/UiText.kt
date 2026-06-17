package org.example.roundapp.core.presentation.ui

sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class Static(val resKey: String) : UiText
}
