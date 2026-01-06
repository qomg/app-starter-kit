package com.example.appstarterkit.domain.usecase

import com.example.appstarterkit.data.repository.ExampleRepository
import com.example.appstarterkit.domain.model.Example
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for getting all examples
 */
class GetExamplesUseCase @Inject constructor(
    private val repository: ExampleRepository
) : BaseFlowUseCase<Unit, List<Example>>() {

    override fun execute(params: Unit) = repository.getAllExamples()
}

/**
 * Use case for fetching examples from remote API
 */
class FetchExamplesUseCase @Inject constructor(
    private val repository: ExampleRepository
) : BaseUseCase<FetchExamplesUseCase.Params, List<Example>>() {

    data class Params(
        val page: Int = 1,
        val limit: Int = 20
    )

    override suspend fun execute(params: Params): Result<List<Example>> {
        return repository.fetchExamplesFromRemote(params.page, params.limit)
    }
}

/**
 * Use case for getting a single example by ID
 */
class GetExampleByIdUseCase @Inject constructor(
    private val repository: ExampleRepository
) : BaseFlowUseCase<String, Example>() {

    override fun execute(params: String): Flow<Result<Example>> {
        return repository.getExampleById(params).map { Result.success(it) }
    }
}

/**
 * Use case for saving an example
 */
class SaveExampleUseCase @Inject constructor(
    private val repository: ExampleRepository
) : BaseVoidUseCase<Example>() {

    override suspend fun execute(params: Example): Result<Unit> {
        return try {
            repository.saveExample(params)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Use case for deleting an example
 */
class DeleteExampleUseCase @Inject constructor(
    private val repository: ExampleRepository
) : BaseVoidUseCase<String>() {

    override suspend fun execute(params: String): Result<Unit> {
        return try {
            repository.deleteExample(params)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
