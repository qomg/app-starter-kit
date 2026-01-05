package com.example.appstarterkit.startup

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.appstarterkit.di.WorkManagerInitializerEntryPoint
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * WorkManager Initializer
 * Initializes WorkManager with Hilt worker factory
 */
class WorkManagerInitializer : Initializer<WorkManager> {

    override fun create(context: Context): WorkManager {
        // Configure WorkManager with Hilt support
        val config = Configuration.Builder()
            .setWorkerFactory(getWorkerFactory(context))
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.initialize(context, config)

        return workManager
    }

    /**
     * Define dependencies
     * Timber should be initialized before WorkManager
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }

    /**
     * Get Hilt worker factory
     */
    private fun getWorkerFactory(context: Context): HiltWorkerFactory {
        val workManagerInitializerEntryPoint = 
            EntryPointAccessors.fromApplication(
                context.applicationContext,
                WorkManagerInitializerEntryPoint::class.java
            )
        return workManagerInitializerEntryPoint.hiltWorkerFactory()
    }

    /**
     * Entry point for Hilt to provide worker factory
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkManagerInitializerEntryPoint {
        fun hiltWorkerFactory(): HiltWorkerFactory
    }
}
