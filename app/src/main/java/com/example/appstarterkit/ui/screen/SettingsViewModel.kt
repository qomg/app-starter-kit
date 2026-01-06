package com.example.appstarterkit.ui.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appstarterkit.core.util.ToastController
import com.example.appstarterkit.data.offline.OfflineManager
import com.example.appstarterkit.data.repository.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Settings ViewModel
 * Manages app settings, theme, and data synchronization
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesDataStore: com.example.appstarterkit.data.datastore.PreferencesDataStore,
    private val syncRepository: SyncRepository,
    private val offlineManager: OfflineManager
) : ViewModel() {

    // Theme settings
    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme.asStateFlow()

    // Dynamic colors
    private val _dynamicColors = MutableStateFlow(true)
    val dynamicColors: StateFlow<Boolean> = _dynamicColors.asStateFlow()

    // Auto sync
    private val _autoSync = MutableStateFlow(false)
    val autoSync: StateFlow<Boolean> = _autoSync.asStateFlow()

    // Sync job
    private val _syncJob = MutableStateFlow<kotlinx.coroutines.Job?>(null)
    val syncJob: StateFlow<kotlinx.coroutines.Job?> = _syncJob.asStateFlow()

    init {
        // Load settings
        loadSettings()
    }

    /**
     * Load all settings
     */
    private fun loadSettings() {
        viewModelScope.launch {
            preferencesDataStore.darkTheme.collect {
                _darkTheme.value = it
            }.launch {
                preferencesDataStore.dynamicColors.collect {
                    _dynamicColors.value = it
                }
            }.launch {
                preferencesDataStore.autoSync.collect {
                    _autoSync.value = it
                }
            }
        }
    }

    /**
     * Toggle dark theme
     */
    fun toggleDarkTheme(context: Context) {
        viewModelScope.launch {
            val currentTheme = darkTheme.value
            preferencesDataStore.setDarkTheme(!currentTheme)
            
            // Show feedback
            val message = if (!currentTheme) "Dark mode enabled" else "Dark mode disabled"
            ToastController.show(context, message)
            Timber.d("Theme toggled to ${!currentTheme}")
        }
    }

    /**
     * Toggle dynamic colors
     */
    fun toggleDynamicColors(context: Context) {
        viewModelScope.launch {
            val currentColors = dynamicColors.value
            preferencesDataStore.setDynamicColors(!currentColors)
            
            val message = if (!currentColors) "Dynamic colors enabled" else "Dynamic colors disabled"
            ToastController.show(context, message)
            Timber.d("Dynamic colors toggled to ${!currentColors}")
        }
    }

    /**
     * Toggle auto sync
     */
    fun toggleAutoSync() {
        viewModelScope.launch {
            val currentSync = autoSync.value
            preferencesDataStore.setAutoSync(!currentSync)
            Timber.d("Auto sync toggled to ${!currentSync}")
        }
    }

    /**
     * Start manual sync
     */
    fun startManualSync() {
        viewModelScope.launch {
            _syncJob.value = syncRepository.syncIfNeeded()
        }
    }

    /**
     * Clear cache
     */
    fun clearCache(context: Context) {
        viewModelScope.launch {
            try {
                offlineManager.clearCache()
                ToastController.success(context, "Cache cleared")
                Timber.d("Cache cleared")
            } catch (e: Exception) {
                Timber.e(e, "Failed to clear cache")
                ToastController.error(context, "Failed to clear cache")
            }
        }
    }

    /**
     * Get cache statistics
     */
    fun getCacheStats(): CacheStats {
        return offlineManager.getCacheStats()
    }
}
