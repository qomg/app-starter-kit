package com.example.appstarterkit.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class that provides common functionality for all ViewModels.
 * Includes state management with StateFlow and coroutine handling.
 */
abstract class BaseViewModel<State : Any> : ViewModel() {

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    /**
     * Create the initial state for the ViewModel
     */
    abstract fun createInitialState(): State

    /**
     * Update the UI state
     */
    protected fun updateState(reducer: State.() -> State) {
        _uiState.value = _uiState.value.reducer()
    }

    /**
     * Launch a coroutine in the ViewModel's scope
     */
    protected fun launchViewModelScope(
        onError: (Throwable) -> Unit = {},
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
