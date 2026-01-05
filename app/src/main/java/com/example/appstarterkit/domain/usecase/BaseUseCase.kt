package com.example.appstarterkit.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Base UseCase class that provides common functionality for all use cases.
 * Follows the Clean Architecture principle where use cases contain business logic.
 */
abstract class BaseUseCase<in Params, out Type> where Type : Any {

    /**
     * Execute the use case synchronously
     */
    abstract suspend fun execute(params: Params): Result<Type>

    /**
     * Execute the use case as a Flow
     */
    operator fun invoke(params: Params): Flow<Result<Type>> = flow {
        emit(execute(params))
    }
}

/**
 * Base UseCase class that doesn't require parameters
 */
abstract class BaseNoParamsUseCase<out Type> where Type : Any {

    /**
     * Execute the use case synchronously
     */
    abstract suspend fun execute(): Result<Type>

    /**
     * Execute the use case as a Flow
     */
    operator fun invoke(): Flow<Result<Type>> = flow {
        emit(execute())
    }
}

/**
 * UseCase that returns a Flow directly (for reactive data streams)
 */
abstract class BaseFlowUseCase<in Params, out Type> where Type : Any {

    /**
     * Execute the use case and return a Flow
     */
    abstract fun execute(params: Params): Flow<Result<Type>>

    /**
     * Execute the use case
     */
    operator fun invoke(params: Params): Flow<Result<Type>> = execute(params)
}

/**
 * UseCase for operations that don't return a value (Void)
 */
abstract class BaseVoidUseCase<in Params> {

    /**
     * Execute the use case
     */
    abstract suspend fun execute(params: Params): Result<Unit>

    /**
     * Execute the use case as a Flow
     */
    operator fun invoke(params: Params): Flow<Result<Unit>> = flow {
        emit(execute(params))
    }
}
