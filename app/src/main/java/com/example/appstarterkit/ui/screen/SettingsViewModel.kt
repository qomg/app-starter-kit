package com.example.appstarterkit.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appstarterkit.data.datastore.PreferencesDataStore
import com.example.appstarterkit.startup.getLazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Settings ViewModel demonstrating lazy initialization
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    /**
     * Dark theme preference
     * Uses DataStore which is initialized via Startup
     */
    val darkTheme: StateFlow<Boolean> = preferencesDataStore.darkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    /**
     * Dynamic colors preference
     */
    val dynamicColors: StateFlow<Boolean> = preferencesDataStore.dynamicColors
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    /**
     * Toggle dark theme
     */
    fun toggleDarkTheme() {
        viewModelScope.launch {
            val currentTheme = darkTheme.value
            preferencesDataStore.setDarkTheme(!currentTheme)
        }
    }

    /**
     * Toggle dynamic colors
     */
    fun toggleDynamicColors() {
        viewModelScope.launch {
            val currentColors = dynamicColors.value
            preferencesDataStore.setDynamicColors(!currentColors)
        }
    }

    /**
     * Set language preference
     */
    fun setLanguage(language: String?) {
        viewModelScope.launch {
            preferencesDataStore.setLanguage(language)
        }
    }

    /**
     * Clear all preferences
     */
    fun clearPreferences() {
        viewModelScope.launch {
            preferencesDataStore.clearAll()
        }
    }
}
