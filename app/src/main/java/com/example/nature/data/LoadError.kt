package com.example.nature.data

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

sealed interface LoadError {
    data object NoNetwork : LoadError
    data class Server(val code: Int) : LoadError
    data object BadResponse : LoadError
    data object Unknown : LoadError
}

fun Throwable.toLoadError(): LoadError = when (this) {
    is IOException -> LoadError.NoNetwork
    is HttpException -> LoadError.Server(code())
    is SerializationException -> LoadError.BadResponse
    else -> LoadError.Unknown
}
