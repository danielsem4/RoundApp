package org.example.roundapp.core.data.networking

import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import org.example.roundapp.core.domain.util.DataError
import org.example.roundapp.core.domain.util.Result

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>,
): Result<T, DataError.Remote> = try {
    handleResponse(execute())
} catch (e: UnresolvedAddressException) {
    Result.Failure(DataError.Remote.NO_INTERNET)
} catch (e: HttpRequestTimeoutException) {
    Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
} catch (e: SerializationException) {
    Result.Failure(DataError.Remote.SERIALIZATION_ERROR)
} catch (e: Exception) {
    coroutineContext.ensureActive()
    Result.Failure(DataError.Remote.UNKNOWN)
}
