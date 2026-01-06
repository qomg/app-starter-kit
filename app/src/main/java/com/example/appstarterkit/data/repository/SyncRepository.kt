package com.example.appstarterkit.data.repository

import com.example.appstarterkit.data.local.dao.ExampleDao
import com.example.appstarterkit.data.remote.ApiService
import com.example.appstarterkit.domain.model.Example
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Sync Repository
 * Manages data synchronization between remote and local sources
 */
class SyncRepository @Inject constructor(
    private val apiService: ApiService,
    private val exampleDao: ExampleDao
) {

    private val _syncInProgress = MutableStateFlow(false)
    val syncInProgress: Flow<Boolean> = _syncInProgress.asStateFlow()

    private val _lastSyncTime = MutableStateFlow(0L)
    val lastSyncTime: Flow<Long> = _lastSyncTime.asStateFlow()

    /**
     * Fetch examples from remote API and sync with local database
     */
    suspend fun syncExamples(): Result<List<Example>> {
        try {
            _syncInProgress.value = true
            Timber.d("Starting data sync")

            // Fetch from remote API
            val remoteExamples = apiService.getExamples(page = 1, limit = 100)
            Timber.d("Fetched ${remoteExamples.size} examples from API")

            // Update local database
            val entities = remoteExamples.map { it.toEntity() }
            exampleDao.insertExamples(entities)
            Timber.d("Synced ${entities.size} examples to database")

            // Update last sync time
            _lastSyncTime.value = System.currentTimeMillis()
            Timber.d("Data sync completed successfully")

            Result.success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Timber.e(e, "Data sync failed")
            _syncInProgress.value = false
            Result.failure(e)
        } finally {
            _syncInProgress.value = false
        }
    }

    /**
     * Get sync status
     */
    fun isSyncInProgress(): Boolean = _syncInProgress.value

    /**
     * Get last sync time
     */
    fun getLastSyncTime(): Long = _lastSyncTime.value

    /**
     * Check if sync is needed (more than 30 minutes since last sync)
     */
    fun shouldSync(): Boolean {
        val thirtyMinutesAgo = System.currentTimeMillis() - (30 * 60 * 1000)
        return lastSyncTime.value < thirtyMinutesAgo
    }
}

/**
 * Sync Manager
 * Manages automatic background sync
 */
class SyncManager @Inject constructor(
    private val syncRepository: SyncRepository
) {

    /**
     * Force sync immediately
     */
    suspend fun forceSync(): Result<List<Example>> {
        return syncRepository.syncExamples()
    }

    /**
     * Check and sync if needed
     */
    suspend fun syncIfNeeded(): Result<List<Example>> {
        return if (syncRepository.shouldSync()) {
            syncRepository.syncExamples()
        } else {
            Result.success(emptyList())
        }
    }

    /**
     * Get sync status
     */
    fun isSyncInProgress(): Boolean = syncRepository.isSyncInProgress()

    /**
     * Get last sync timestamp
     */
    fun getLastSyncTime(): Long = syncRepository.getLastSyncTime()
}
