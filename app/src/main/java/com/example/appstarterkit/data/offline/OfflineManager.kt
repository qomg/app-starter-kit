package com.example.appstarterkit.data.offline

import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.example.appstarterkit.data.local.dao.ExampleDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

/**
 * Cache Strategy for offline support
 * Manages network requests and local caching
 */
object CacheStrategy {

    const val DEFAULT_CACHE_DURATION_MS = 5 * 60 * 1000L // 5 minutes
    const val MIN_CACHE_DURATION_MS = 2 * 60 * 1000L  // 2 minutes

    /**
     * Determine if cached data is stale
     */
    fun isCacheValid(timestamp: Long): Boolean {
        val cacheAge = System.currentTimeMillis() - timestamp
        return cacheAge < DEFAULT_CACHE_DURATION_MS
    }
}

/**
 * Offline Manager
 * Manages offline mode and cached data
 */
class OfflineManager @Inject constructor(
    private val database: AppDatabase
) {

    // Offline state
    private val _isOffline = MutableStateFlow(false)
    val isOffline: Flow<Boolean> = _isOffline.asStateFlow()

    /**
     * Get cached examples from local database
     */
    fun getCachedExamples(): Flow<List<ExampleEntity>> {
        return database.exampleDao().getAllExamples()
    }

    /**
     * Save examples to cache
     */
    suspend fun saveToCache(examples: List<ExampleEntity>) {
        database.withTransaction {
            examples.forEach { exampleDao.insertExample(it) }
        }
        Timber.d("Saved ${examples.size} examples to cache")
    }

    /**
     * Clear all cached data
     */
    suspend fun clearCache() {
        database.exampleDao().deleteAllExamples()
        Timber.d("Cleared cache")
    }

    /**
     * Get cache size (number of items)
     */
    suspend fun getCacheSize(): Int {
        return database.exampleDao().getAllExamples().map { 1 }.count()
    }

    /**
     * Check if cache is empty
     */
    suspend fun isCacheEmpty(): Boolean {
        return getCacheSize() == 0
    }
}

/**
 * Network Manager
 * Manages network connectivity state
 */
class NetworkManager @Inject constructor() {

    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: Flow<Boolean> = _isNetworkAvailable.asStateFlow()

    /**
     * Update network state
     */
    fun updateNetworkState(available: Boolean) {
        _isNetworkAvailable.value = available
        Timber.d("Network available: $available")
    }
}

/**
 * Combined Offline & Network Manager
 */
class OfflineNetworkManager @Inject constructor(
    private val offlineManager: OfflineManager,
    private val networkManager: NetworkManager
) {

    /**
     * Load from cache if offline, from network if online
     */
    suspend fun <T> loadDataOrCache(
        loadFromNetwork: suspend () -> Result<T>,
        loadFromCache: suspend () -> Result<T>
    ): Result<T> {
        return if (networkManager.isNetworkAvailable.collectAsState().value) {
            loadFromNetwork()
        } else {
            Timber.d("Offline, loading from cache")
            loadFromCache()
        }
    }

    /**
     * Save to cache if network available
     */
    suspend fun <T> saveDataOrCache(
        saveToNetwork: suspend (data: T) -> Result<Unit>,
        saveToCache: suspend (T) -> Result<Unit>,
        data: T
    ): Result<Unit> {
        return if (networkManager.isNetworkAvailable.collectAsState().value) {
            Timber.d("Online, saving to network")
            saveToNetwork(data)
        } else {
            Timber.d("Offline, saving to cache")
            saveToCache(data)
        }
    }
}

/**
 * Repository for offline data
 */
class OfflineRepository @Inject constructor(
    private val offlineManager: OfflineManager
) {

    /**
     * Check if data is cached and valid
     */
    suspend fun isDataCachedAndValid(): Boolean {
        val examples = offlineManager.getCachedExamples()
        if (examples.isEmpty()) return false

        // Check cache validity
        return examples.firstOrNull()?.let { entity ->
            CacheStrategy.isCacheValid(entity.timestamp)
        } ?: false
    }

    /**
     * Get cache statistics
     */
    suspend fun getCacheStats(): CacheStats {
        return CacheStats(
            size = offlineManager.getCacheSize(),
            isEmpty = offlineManager.isCacheEmpty()
        )
    }
}

/**
 * Data class for cache statistics
 */
data class CacheStats(
    val size: Int,
    val isEmpty: Boolean
)
