package com.example.appstarterkit.startup

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.startup.Initializer
import com.example.appstarterkit.data.datastore.PreferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import timber.log.Timber

/**
 * DataStore Initializer
 * Initializes DataStore for preferences at app startup
 */
class DataStoreInitializer : Initializer<PreferencesDataStore> {

    override fun create(context: Context): PreferencesDataStore {
        Timber.d("Initializing DataStore...")

        return runBlocking(Dispatchers.IO.asExecutor().asCoroutineDispatcher()) {
            PreferencesDataStore(context).also {
                Timber.d("DataStore initialized")
            }
        }
    }

    /**
     * Define dependencies
     * DataStore has no direct dependencies
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}