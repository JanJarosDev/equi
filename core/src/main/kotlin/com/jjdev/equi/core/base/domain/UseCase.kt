package com.jjdev.equi.core.base.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class UseCase<in Parameters, Success, BusinessRuleError>(private val dispatcher: CoroutineDispatcher) {

    @Suppress("TooGenericExceptionCaught")
    suspend operator fun invoke(parameters: Parameters): Result<Success, BusinessRuleError> {
        return try {
            withContext(dispatcher) {
                execute(parameters)
            }
        } catch (e: RuntimeException) {
            Timber.e("An error occurred while executing the use case", e)
            Result.Error(e.mapToAppError())
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: Parameters): Result<Success, BusinessRuleError>
}
