package com.example.appstarterkit.di

import androidx.hilt.work.HiltWorkerFactory
import com.example.appstarterkit.data.local.AppDatabase
import com.example.appstarterkit.data.local.dao.ExampleDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Note: AppDatabase is now initialized via RoomInitializer (Startup)
     * This module provides DAOs from the already-initialized database instance
     */

    @Provides
    @Singleton
    fun provideExampleDao(): ExampleDao {
        // Database is already initialized by RoomInitializer
        // Access the singleton instance
        return AppDatabase.getInstance(
            // This is a temporary solution - in production, you should
            // inject the context and get the instance that way
            android.app.ActivityThread.currentApplication()
        ).exampleDao()
    }
}

/**
 * WorkManager Hilt Module
 * Provides HiltWorkerFactory for WorkManager integration
 */
@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {

    @Provides
    @Singleton
    fun provideHiltWorkerFactory(): HiltWorkerFactory {
        return HiltWorkerFactory.getInstance()
    }
}
