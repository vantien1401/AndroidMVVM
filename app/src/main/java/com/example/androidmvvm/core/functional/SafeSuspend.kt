package com.example.androidmvvm.core.functional

import com.example.androidmvvm.core.error.DefaultFailure
import com.example.androidmvvm.core.error.FailureHandler

suspend fun <R> safeSuspend(
    vararg failureHandlers: FailureHandler,
    action: suspend () -> ResultWrapper<R>
): ResultWrapper<R> = try {
    action()
} catch (exception: Exception) {
    val failure = failureHandlers.firstNotNullOfOrNull { handler ->
        handler.handleThrowable(exception)
    } ?: DefaultFailure(exception)
    ResultWrapper.Error(failure)
}

suspend fun <R> safeSuspendIgnoreFailure(
    action: suspend () -> R
): R? = try {
    action()
} catch (exception: Exception) {
    null
}