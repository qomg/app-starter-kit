package com.example.appstarterkit.ui.components.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.appstarterkit.ui.navigation.AppBottomBar
import com.example.appstarterkit.ui.navigation.Routes

/**
 * Navigation Rail Item
 * Represents a single item in the navigation rail
 */
@Composable
fun NavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scale"
    )

    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.scale(scale)
            )
        },
        label = {
            Text(label)
        },
        modifier = modifier
    )
}

/**
 * Adaptive Navigation Rail
 * Side navigation bar for medium/expanded screens
 *
 * @param navController Navigation controller
 * @param items List of navigation items
 * @param modifier Modifier for the navigation rail
 */
@Composable
fun AdaptiveNavigationRail(
    navController: NavHostController,
    items: List<NavigationItem>,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(null)
    val currentDestination = navBackStackEntry?.destination

    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any {
                it.route == item.route
            } == true

            NavigationRailItem(
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
                icon = item.icon,
                label = item.label
            )
        }
    }
}

/**
 * Navigation Item Data Class
 */
data class NavigationItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)

/**
 * Standard navigation items
 */
val standardNavigationItems = listOf(
    NavigationItem(
        route = Routes.HOME,
        icon = androidx.compose.material.icons.Icons.Default.Home,
        label = "Home"
    ),
    NavigationItem(
        route = Routes.EXAMPLE,
        icon = androidx.compose.material.icons.Icons.Default.List,
        label = "Examples"
    ),
    NavigationItem(
        route = Routes.SETTINGS,
        icon = androidx.compose.material.icons.Icons.Default.Settings,
        label = "Settings"
    ),
    NavigationItem(
        route = Routes.ABOUT,
        icon = androidx.compose.material.icons.Icons.Default.Info,
        label = "About"
    )
)

/**
 * Permanent Navigation Drawer
 * For large screen devices (expanded width)
 */
@Composable
fun PermanentNavigationDrawer(
    navController: NavHostController,
    items: List<NavigationItem>,
    modifier: Modifier = Modifier,
    drawerContent: @Composable () -> Unit = {}
) {
    val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(null)
    val currentDestination = navBackStackEntry?.destination

    PermanentNavigationDrawer(
        drawerContent = {
            drawerContent()
            items.forEach { item ->
                val selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true

                NavigationDrawerItem(
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
                    label = { Text(item.label) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        },
        modifier = modifier
    ) {
        // Content will be provided by the caller
    }
}

/**
 * Adaptive Navigation Suite
 * Automatically selects appropriate navigation component based on window size
 *
 * @param widthSizeClass Window width size class
 * @param navController Navigation controller
 * @param content Main content to display
 */
@Composable
fun AdaptiveNavigationSuite(
    widthSizeClass: androidx.compose.material3.windowsizeclass.WindowWidthSizeClass,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val navigationType = when (widthSizeClass) {
        androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Compact -> {
            // Bottom Navigation
            androidx.compose.material3.Scaffold(
                bottomBar = {
                    AppBottomBar(
                        navController = navController,
                        items = standardNavigationItems.map {
                            BottomNavItem(it.route, it.icon, it.label)
                        }
                    )
                }
            ) { padding ->
                content(padding)
            }
        }
        androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Medium -> {
            // Navigation Rail
            Row {
                AdaptiveNavigationRail(
                    navController = navController,
                    items = standardNavigationItems
                )
                androidx.compose.material3.Surface(
                    modifier = Modifier.weight(1f)
                ) {
                    content(PaddingValues(0.dp))
                }
            }
        }
        androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Expanded -> {
            // Permanent Navigation Drawer
            PermanentNavigationDrawer(
                navController = navController,
                items = standardNavigationItems
            )
        }

        else -> {}
    }
}
