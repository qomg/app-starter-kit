package com.example.appstarterkit.startup

import android.content.Context
import androidx.startup.Initializer
import com.example.appstarterkit.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking

/**
 * Room Database Initializer
 * Initializes Room database at app startup
 * This initializer ensures database is ready before any component tries to use it
 */
class RoomInitializer : Initializer<AppDatabase> {

    override fun create(context: Context): AppDatabase {
        Timber.d("Initializing Room database...")

        return runBlocking(Dispatchers.IO.asCoroutineDispatcher()) {
            AppDatabase.getInstance(context).also {
                Timber.d("Room database initialized")
            }
        }
    }

    /**
     * Define dependencies
     * Room has no direct dependencies, but can depend on Timber
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}
