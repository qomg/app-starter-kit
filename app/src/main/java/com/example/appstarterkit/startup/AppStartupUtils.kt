package com.example.appstarterkit.startup

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import timber.log.Timber
import kotlin.time.Duration
import kotlin.time.TimeSource

/**
 * App Startup Configuration
 * Provides utilities to measure and manage app startup performance
 */
object AppStartupConfig {

    // Track startup times for debugging
    private val startupTimings = mutableMapOf<String, Long>()

    /**
     * Record initialization time for a component
     */
    fun recordInitTime(name: String) {
        val currentTime = TimeSource.Monotonic.markNow()
        startupTimings[name] = currentTime.elapsedNow().inWholeMilliseconds
        Timber.d("Initializer completed: $name")
    }

    /**
     * Get all initialization timings
     */
    fun getStartupTimings(): Map<String, Long> = startupTimings

    /**
     * Calculate total startup time
     */
    fun getTotalStartupTime(): Long {
        if (startupTimings.isEmpty()) return 0L
        val startTime = startupTimings.values.minOrNull() ?: 0L
        val endTime = startupTimings.values.maxOrNull() ?: 0L
        return endTime - startTime
    }

    /**
     * Clear startup timings
     */
    fun clearTimings() {
        startupTimings.clear()
    }
}

/**
 * Custom Application Initializer Manager
 * This class can be extended to add custom initialization logic
 * without modifying the AndroidManifest.xml
 */
abstract class CustomAppInitializer {
    
    /**
     * Called when custom initialization is needed
     * Implement this to add custom startup logic
     */
    abstract fun onInitialize(context: Context)
}

/**
 * Lazy Initialization Manager
 * Manages lazy initialization of components that don't need to run at startup
 */
class LazyInitManager {

    private val lazyComponents = mutableMapOf<String, LazyInitComponent<*>>()

    /**
     * Register a lazy component
     */
    fun <T> register(
        key: String,
        initializer: () -> T
    ) {
        lazyComponents[key] = LazyInitComponent(initializer)
    }

    /**
     * Get a lazy component, initializing it on first access
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T? {
        return lazyComponents[key]?.get() as? T
    }

    /**
     * Check if a component has been initialized
     */
    fun isInitialized(key: String): Boolean {
        return lazyComponents[key]?.isInitialized == true
    }

    /**
     * Clear all lazy components
     */
    fun clear() {
        lazyComponents.clear()
    }
}

/**
 * Lazy Init Component
 */
private class LazyInitComponent<T>(private val initializer: () -> T) {
    private var instance: T? = null
    var isInitialized: Boolean = false
        private set

    fun get(): T {
        if (!isInitialized) {
            instance = initializer()
            isInitialized = true
        }
        return instance!!
    }
}

/**
 * Global lazy init manager instance
 */
val lazyInitManager = LazyInitManager()

/**
 * Convenience function to get a lazy initialized component
 */
inline fun <reified T> getLazy(key: String): T? {
    return lazyInitManager.get<T>(key)
}
