package com.example.appstarterkit.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.appstarterkit.ui.components.navigation.AdaptiveNavigationSuite
import com.example.appstarterkit.ui.navigation.AppNavigation
import com.example.appstarterkit.ui.navigation.Routes

/**
 * Main Screen with Adaptive Navigation
 * Automatically adapts navigation based on screen size:
 * - Compact: Bottom Navigation Bar
 * - Medium: Navigation Rail
 * - Expanded: Permanent Navigation Drawer
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(configuration)

    // Check if current route should show navigation
    val showNavigation = rememberShowNavigation(navController)

    if (showNavigation) {
        // Show adaptive navigation based on window size
        AdaptiveNavigationSuite(
            widthSizeClass = windowSizeClass.widthSizeClass,
            navController = navController
        ) { padding ->
            AppNavigation(
                navController = navController,
                modifier = androidx.compose.ui.Modifier.padding(padding)
            )
        }
    } else {
        // Full screen content without navigation (detail screens)
        AppNavigation(
            navController = navController
        )
    }
}

/**
 * Determine whether to show navigation
 * Navigation is shown only for top-level destinations
 */
@Composable
fun rememberShowNavigation(navController: androidx.navigation.NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(null)
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val showNavigation = remember(currentRoute) {
        currentRoute in listOf(
            Routes.HOME,
            Routes.EXAMPLE,
            Routes.SETTINGS,
            Routes.ABOUT
        )
    }

    return showNavigation
}
