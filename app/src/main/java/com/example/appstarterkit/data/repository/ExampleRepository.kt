package com.example.appstarterkit.data.repository

import com.example.appstarterkit.data.local.dao.ExampleDao
import com.example.appstarterkit.data.remote.ApiService
import com.example.appstarterkit.data.remote.dto.ExampleDto
import com.example.appstarterkit.domain.model.Example
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Example Repository implementation
 * Manages data from both local and remote sources
 */
@Singleton
class ExampleRepository @Inject constructor(
    private val apiService: ApiService,
    private val exampleDao: ExampleDao
) : BaseRepository {

    /**
     * Get all examples from local database as a Flow
     */
    fun getAllExamples(): Flow<Result<List<Example>>> {
        return exampleDao.getAllExamples().map { entities ->
            entities.map { it.toDomainModel() }
        }.map {
            Result.success(it)
        }
    }

    /**
     * Get a single example by ID
     */
    fun getExampleById(id: String): Flow<Example> {
        return exampleDao.getExampleById(id).map { entity ->
            entity.toDomainModel()
        }
    }

    /**
     * Fetch examples from remote API and save to local database
     */
    suspend fun fetchExamplesFromRemote(
        page: Int = 1,
        limit: Int = 20
    ): Result<List<Example>> {
        return try {
            val examples = apiService.getExamples(page, limit)
            val entities = examples.map { it.toEntity() }
            exampleDao.insertExamples(entities)
            Result.success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Save example to local database
     */
    suspend fun saveExample(example: Example) {
        exampleDao.insertExample(example.toEntity())
    }

    /**
     * Update example in local database
     */
    suspend fun updateExample(example: Example) {
        exampleDao.updateExample(example.toEntity())
    }

    /**
     * Delete example from local database
     */
    suspend fun deleteExample(id: String) {
        exampleDao.deleteExampleById(id)
    }

    /**
     * Clear all cached data
     */
    override suspend fun clearCache() {
        exampleDao.deleteAllExamples()
    }
}

// Extension functions for mapping between DTOs, Entities, and Domain Models
private fun ExampleDto.toEntity() = com.example.appstarterkit.data.local.ExampleEntity(
    id = id,
    name = name,
    description = description,
    timestamp = createdAt
)

private fun com.example.appstarterkit.data.local.ExampleEntity.toDomainModel() = Example(
    id = id,
    name = name,
    description = description,
    timestamp = timestamp
)

private fun Example.toEntity() = com.example.appstarterkit.data.local.ExampleEntity(
    id = id,
    name = name,
    description = description,
    timestamp = timestamp
)
