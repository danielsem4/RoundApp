package org.example.roundapp.feature.home.data

import kotlinx.coroutines.delay
import org.example.roundapp.core.domain.util.DataError
import org.example.roundapp.core.domain.util.Result
import org.example.roundapp.feature.home.domain.HomeData
import org.example.roundapp.feature.home.domain.HomeItem
import org.example.roundapp.feature.home.domain.HomeService

class StubHomeService : HomeService {
    override suspend fun loadHome(): Result<HomeData, DataError.Remote> {
        delay(300)
        return Result.Success(
            HomeData(
                welcomeMessage = "Welcome to RoundApp",
                items = listOf(
                    HomeItem("1", "First item", "Edit feature:home to wire real data"),
                    HomeItem("2", "Second item", "Follow the proms architecture pattern"),
                    HomeItem("3", "Third item", "Add more features under feature/"),
                ),
            ),
        )
    }
}
