package com.example.appstarterkit.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

/**
 * Navigation Event
 * Represents a navigation action
 */
sealed class NavigationEvent {
    data object NavigateToHome : NavigationEvent()
    data object NavigateToExample : NavigationEvent()
    data class NavigateToExampleDetail(val exampleId: String) : NavigationEvent()
    data object NavigateToSettings : NavigationEvent()
    data object NavigateToAbout : NavigationEvent()
    data object NavigateBack : NavigationEvent()
    data class NavigateBackWithResult<T>(val result: T) : NavigationEvent()
}

/**
 * Navigation ViewModel
 * Handles navigation events from ViewModels
 */
@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvents = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    /**
     * Handle navigation event
     */
    fun onNavigationEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvents.send(event)
        }
    }

    /**
     * Navigate to home
     */
    fun navigateToHome() {
        onNavigationEvent(NavigationEvent.NavigateToHome)
    }

    /**
     * Navigate to example screen
     */
    fun navigateToExample() {
        onNavigationEvent(NavigationEvent.NavigateToExample)
    }

    /**
     * Navigate to example detail
     */
    fun navigateToExampleDetail(exampleId: String) {
        onNavigationEvent(NavigationEvent.NavigateToExampleDetail(exampleId))
    }

    /**
     * Navigate to settings
     */
    fun navigateToSettings() {
        onNavigationEvent(NavigationEvent.NavigateToSettings)
    }

    /**
     * Navigate to about
     */
    fun navigateToAbout() {
        onNavigationEvent(NavigationEvent.NavigateToAbout)
    }

    /**
     * Navigate back
     */
    fun navigateBack() {
        onNavigationEvent(NavigationEvent.NavigateBack)
    }

    /**
     * Navigate back with result
     */
    fun <T> navigateBackWithResult(result: T) {
        onNavigationEvent(NavigationEvent.NavigateBackWithResult(result))
    }
}

/**
 * Handle navigation events in composable
 *
 * @param navigationEvents Flow of navigation events
 * @param navController NavController to handle navigation
 */
@androidx.compose.runtime.Composable
fun HandleNavigationEvents(
    navigationEvents: kotlinx.coroutines.flow.Flow<NavigationEvent>,
    navController: NavController
) {
    androidx.compose.runtime.LaunchedEffect(Unit) {
        navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToHome -> {
                    navController.navigateToHome()
                }
                is NavigationEvent.NavigateToExample -> {
                    navController.navigateToExample()
                }
                is NavigationEvent.NavigateToExampleDetail -> {
                    navController.navigateToExampleDetail(event.exampleId)
                }
                is NavigationEvent.NavigateToSettings -> {
                    navController.navigateToSettings()
                }
                is NavigationEvent.NavigateToAbout -> {
                    navController.navigateToAbout()
                }
                is NavigationEvent.NavigateBack -> {
                    navController.navigateUp()
                }
                is NavigationEvent.NavigateBackWithResult<*> -> {
                    navController.navigateBack(event.result)
                }
            }
        }
    }
}
