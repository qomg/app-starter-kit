package com.example.appstarterkit.startup

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.startup.Initializer
import com.example.appstarterkit.data.datastore.PreferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import timber.log.Timber

/**
 * DataStore Initializer
 * Initializes DataStore for preferences at app startup
 */
class DataStoreInitializer : Initializer<PreferencesDataStore> {

    // DataStore for preferences
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

    override fun create(context: Context): PreferencesDataStore {
        Timber.d("Initializing DataStore...")

        return runBlocking(Dispatchers.IO.asCoroutineDispatcher()) {
            PreferencesDataStore(context.dataStore).also {
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

/**
 * Wrapper class for DataStore preferences
 */
class PreferencesDataStore(val dataStore: DataStore<Preferences>)
