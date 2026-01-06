package com.example.appstarterkit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        startDestination = Routes.HOME,
        modifier = modifier,
        enterTransition = { slideInFromRight },
        exitTransition = { slideOutToLeft },
        popEnterTransition = { slideInFromRight },
        popExitTransition = { slideOutToLeft }
    ) {
        // Home Screen
        composable(route = Routes.HOME) {
            HomeScreen(
                onNavigateToExample = {
                    navController.navigate(Routes.EXAMPLE)
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        // Example Screen
        composable(route = Routes.EXAMPLE) {
            ExampleScreen(
                onNavigateToDetail = { exampleId ->
                    navController.navigate(Routes.EXAMPLE_DETAIL.replace("{exampleId}", exampleId))
                }
            )
        }

        // Example Detail Screen
        composable(
            route = Routes.EXAMPLE_DETAIL,
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
        composable(route = Routes.SETTINGS) {
            SettingsScreen(
                onNavigateToAbout = {
                    navController.navigate(Routes.ABOUT)
                }
            )
        }

        // About Screen
        composable(route = Routes.ABOUT) {
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
    navigate(Routes.EXAMPLE_DETAIL.replace("{exampleId}", exampleId))
}
