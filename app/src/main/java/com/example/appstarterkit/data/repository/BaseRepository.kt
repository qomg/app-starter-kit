package com.example.appstarterkit.data.repository

/**
 * Base Repository interface that defines common repository operations.
 * All repositories should implement this interface to ensure consistency.
 */
interface BaseRepository {
    /**
     * Clear all cached data
     */
    suspend fun clearCache()
}
