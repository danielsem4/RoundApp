package org.example.roundapp.feature.home.domain

import org.example.roundapp.core.domain.util.DataError
import org.example.roundapp.core.domain.util.Result

data class HomeData(
    val welcomeMessage: String,
    val items: List<HomeItem>,
)

data class HomeItem(
    val id: String,
    val title: String,
    val subtitle: String,
)

interface HomeService {
    suspend fun loadHome(): Result<HomeData, DataError.Remote>
}
