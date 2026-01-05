package com.example.appstarterkit.ui.components.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Data class representing a bottom navigation item.
 *
 * @param route The navigation route
 * @param icon The icon to display
 * @param label The label text
 */
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

/**
 * Animated Bottom Navigation Bar with smooth transitions.
 *
 * @param navController The navigation controller
 * @param items List of bottom navigation items
 * @param modifier Modifier for the navigation bar
 */
@Composable
fun AnimatedBottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(
        initial = navController.currentBackStackEntry
    )
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any {
                it.route == item.route
            } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    AnimatedVisibility(
                        visible = selected,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Text(text = item.label)
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}

/**
 * Animated Navigation Drawer (Side Navigation).
 *
 * @param isOpen Whether the drawer is open
 * @param onClose Callback when drawer should close
 * @param items List of navigation items
 * @param modifier Modifier for the drawer
 * @param content The content to display when drawer is closed
 */
@Composable
fun AnimatedNavigationDrawer(
    isOpen: Boolean,
    onClose: () -> Unit,
    items: List<BottomNavItem>,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    // This would be implemented with ModalNavigationDrawer in actual use
    // For now, keeping it as a placeholder
    content(PaddingValues(0.dp))
}

/**
 * Common navigation transitions
 */
val EnterTransition = EnterTransitionSpec
val ExitTransition = ExitTransitionSpec
