package com.example.appstarterkit.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable

/**
 * Navigation extension functions
 * Provides convenient methods for navigation in the app
 */

/**
 * Navigate to a route with optional parameters
 */
fun NavController.navigateTo(
    route: String,
    params: Map<String, String> = emptyMap(),
    navOptions: NavOptions? = null
) {
    val finalRoute = if (params.isNotEmpty()) {
        params.entries.fold(route) { acc, (key, value) ->
            acc.replace("{$key}", value)
        }
    } else {
        route
    }
    navigate(finalRoute, navOptions)
}

/**
 * Navigate to home screen
 */
fun NavController.navigateToHome() {
    navigate(Routes.HOME) {
        popUpTo(Routes.HOME) {
            inclusive = true
        }
    }
}

/**
 * Navigate to example screen
 */
fun NavController.navigateToExample() {
    navigate(Routes.EXAMPLE)
}

/**
 * Navigate to example detail screen
 */
fun NavController.navigateToExampleDetail(exampleId: String) {
    navigate(Routes.EXAMPLE_DETAIL.replace("{exampleId}", exampleId))
}

/**
 * Navigate to settings screen
 */
fun NavController.navigateToSettings() {
    navigate(Routes.SETTINGS)
}

/**
 * Navigate to about screen
 */
fun NavController.navigateToAbout() {
    navigate(Routes.ABOUT)
}

/**
 * Navigate back with optional result
 */
fun <T> NavController.navigateBack(result: T? = null) {
    previousBackStackEntry?.savedStateHandle?.set("result", result)
    navigateUp()
}

/**
 * Get navigation result from previous screen
 */
fun <T> NavController.getNavigationResult(key: String = "result"): T? {
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val result = savedStateHandle?.get<T>(key)
    // Remove the result to avoid stale data
    savedStateHandle?.remove<T>(key)
    return result
}

/**
 * Observe navigation result from previous screen
 */
@Composable
fun <T> NavController.observeNavigationResult(
    key: String = "result",
    onResult: (T?) -> Unit
) {
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val result = savedStateHandle?.get<T>(key)
    androidx.compose.runtime.LaunchedEffect(result) {
        if (result != null) {
            onResult(result)
            savedStateHandle?.remove<T>(key)
        }
    }
}
