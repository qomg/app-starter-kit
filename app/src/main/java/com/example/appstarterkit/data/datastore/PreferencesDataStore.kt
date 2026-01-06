package com.example.appstarterkit.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataStore keys
 */
object PreferencesKeys {
    val DARK_THEME = booleanPreferencesKey("dark_theme")
    val DYNAMIC_COLORS = booleanPreferencesKey("dynamic_colors")
    val LANGUAGE = stringPreferencesKey("language")
    val USER_ID = stringPreferencesKey("user_id")
    val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
    val THEME_COLOR_SCHEME = intPreferencesKey("theme_color_scheme")
}

/**
 * Preferences DataStore
 * Manages app preferences using DataStore
 */
@Singleton
class PreferencesDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

    /**
     * Get dark theme preference
     */
    val darkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DARK_THEME] ?: false
        }

    /**
     * Set dark theme preference
     */
    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = isDark
        }
    }

    /**
     * Get dynamic colors preference
     */
    val dynamicColors: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DYNAMIC_COLORS] ?: true
        }

    /**
     * Set dynamic colors preference
     */
    suspend fun setDynamicColors(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DYNAMIC_COLORS] = enabled
        }
    }

    /**
     * Get language preference
     */
    val language: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LANGUAGE]
        }

    /**
     * Set language preference
     */
    suspend fun setLanguage(lang: String?) {
        context.dataStore.edit { preferences ->
            if (lang != null) {
                preferences[PreferencesKeys.LANGUAGE] = lang
            } else {
                preferences.remove(PreferencesKeys.LANGUAGE)
            }
        }
    }

    /**
     * Get user ID
     */
    val userId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_ID]
        }

    /**
     * Set user ID
     */
    suspend fun setUserId(id: String?) {
        context.dataStore.edit { preferences ->
            if (id != null) {
                preferences[PreferencesKeys.USER_ID] = id
            } else {
                preferences.remove(PreferencesKeys.USER_ID)
            }
        }
    }

    /**
     * Get last sync time
     */
    val lastSyncTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LAST_SYNC_TIME] ?: 0L
        }

    /**
     * Set last sync time
     */
    suspend fun setLastSyncTime(timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_SYNC_TIME] = timestamp
        }
    }

    /**
     * Get theme color scheme
     */
    val themeColorScheme: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME_COLOR_SCHEME] ?: 0
        }

    /**
     * Set theme color scheme
     */
    suspend fun setThemeColorScheme(scheme: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_COLOR_SCHEME] = scheme
        }
    }

    /**
     * Clear all preferences
     */
    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
