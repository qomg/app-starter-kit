package com.example.appstarterkit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appstarterkit.ui.navigation.Routes.*
import com.example.appstarterkit.ui.screen.*

/**
 * App Navigation
 * Configures the navigation graph for the application
 *
 * @param navController Navigation controller
 * @param modifier Modifier for the NavHost
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HOME,
        modifier = modifier,
        enterTransition = { slideInFromRight },
        exitTransition = { slideOutToLeft },
        popEnterTransition = { slideInFromRight },
        popExitTransition = { slideOutToLeft }
    ) {
        // Home Screen
        composable(route = HOME) {
            HomeScreen(
                onNavigateToExample = {
                    navController.navigate(EXAMPLE)
                },
                onNavigateToSettings = {
                    navController.navigate(SETTINGS)
                }
            )
        }

        // Example Screen
        composable(route = EXAMPLE) {
            ExampleScreen(
                onNavigateToDetail = { exampleId ->
                    navController.navigate(EXAMPLE_DETAIL.replace("{exampleId}", exampleId))
                }
            )
        }

        // Example Detail Screen
        composable(
            route = EXAMPLE_DETAIL,
            arguments = listOf(
                navArgument(NavArguments.EXAMPLE_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val exampleId = backStackEntry.arguments?.getString(NavArguments.EXAMPLE_ID) ?: ""
            ExampleDetailScreen(
                exampleId = exampleId,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        // Settings Screen
        composable(route = SETTINGS) {
            SettingsScreen(
                onNavigateToAbout = {
                    navController.navigate(ABOUT)
                }
            )
        }

        // About Screen
        composable(route = ABOUT) {
            AboutScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

/**
 * Navigate to example detail with arguments
 */
fun NavHostController.navigateToExampleDetail(exampleId: String) {
    navigate(EXAMPLE_DETAIL.replace("{exampleId}", exampleId))
}
