package com.example.appstarterkit.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.appstarterkit.ui.components.navigation.BottomNavItem
import com.example.appstarterkit.ui.navigation.Routes.*

/**
 * Bottom Navigation Items
 * Defines the items that appear in the bottom navigation bar
 */
val bottomNavItems = listOf(
    BottomNavItem(
        route = HOME,
        icon = Icons.Filled.Home,
        label = "Home"
    ),
    BottomNavItem(
        route = EXAMPLE,
        icon = Icons.Filled.List,
        label = "Examples"
    ),
    BottomNavItem(
        route = SETTINGS,
        icon = Icons.Filled.Settings,
        label = "Settings"
    ),
    BottomNavItem(
        route = ABOUT,
        icon = Icons.Filled.Info,
        label = "About"
    )
)

/**
 * Navigation Transitions
 * Defines custom enter and exit transitions for navigation
 */
val slideInFromRight = EnterTransition
val slideOutToLeft = ExitTransition

/**
 * Check if a route is currently selected
 */
fun isSelectedRoute(
    currentDestination: NavBackStackEntry?,
    route: String
): Boolean {
    return currentDestination?.destination?.hierarchy?.any {
        it.route == route
    } == true
}

/**
 * Bottom Navigation Bar Composable
 *
 * @param navController Navigation controller
 * @param items List of navigation items
 */
@Composable
fun AppBottomBar(
    navController: NavHostController,
    items: List<BottomNavItem> = bottomNavItems
) {
    val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(null)
    val currentDestination = navBackStackEntry?.destination

    androidx.compose.material3.NavigationBar(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
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
                    androidx.compose.animation.AnimatedVisibility(
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
 * Icon for navigation items
 */
@Composable
fun NavigationIcon(
    icon: ImageVector,
    contentDescription: String?,
    selected: Boolean
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = if (selected)
            androidx.compose.material3.MaterialTheme.colorScheme.primary
        else
            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
    )
}
