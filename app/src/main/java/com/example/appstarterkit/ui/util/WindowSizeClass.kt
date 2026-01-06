package com.example.appstarterkit.ui.util

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

/**
 * Window Size Classes
 * Defines breakpoints for different screen sizes
 *
 * Reference: https://m3.material.io/foundations/layout/applying-layout/window-size-classes
 */
object WindowSizeClasses {
    // Width breakpoints (dp)
    const val COMPACT_MAX_WIDTH = 600f
    const val MEDIUM_MAX_WIDTH = 840f

    // Height breakpoints (dp)
    const val COMPACT_MAX_HEIGHT = 480f
    const val MEDIUM_MAX_HEIGHT = 900f
}

/**
 * Get current window size class
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@ReadOnlyComposable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp

    val widthSizeClass = when {
        screenWidthDp < WindowSizeClasses.COMPACT_MAX_WIDTH -> WindowWidthSizeClass.Compact
        screenWidthDp < WindowSizeClasses.MEDIUM_MAX_WIDTH -> WindowWidthSizeClass.Medium
        else -> WindowWidthSizeClass.Expanded
    }

    val heightSizeClass = when {
        screenHeightDp < WindowSizeClasses.COMPACT_MAX_HEIGHT -> WindowHeightSizeClass.Compact
        screenHeightDp < WindowSizeClasses.MEDIUM_MAX_HEIGHT -> WindowHeightSizeClass.Medium
        else -> WindowHeightSizeClass.Expanded
    }

    return WindowSizeClass.calculateFromSize(DpSize(screenWidthDp.dp, screenHeightDp.dp))
}

/**
 * Check if the device is a tablet or has medium/expanded width
 */
@Composable
fun isTabletOrLandscape(widthSizeClass: WindowWidthSizeClass): Boolean {
    return widthSizeClass != WindowWidthSizeClass.Compact
}

/**
 * Check if the device is a phone (compact width)
 */
@Composable
fun isPhone(widthSizeClass: WindowWidthSizeClass): Boolean {
    return widthSizeClass == WindowWidthSizeClass.Compact
}

/**
 * Get appropriate navigation type based on window size
 */
enum class NavigationType {
    BOTTOM_NAVIGATION,    // Phone portrait
    NAVIGATION_RAIL,       // Tablet or phone landscape
    PERMANENT_DRAWER      // Large screen devices
}

/**
 * Determine navigation type based on window size class
 */
fun getNavigationType(widthSizeClass: WindowWidthSizeClass): NavigationType {
    return when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAVIGATION
        WindowWidthSizeClass.Medium -> NavigationType.NAVIGATION_RAIL
        WindowWidthSizeClass.Expanded -> NavigationType.PERMANENT_DRAWER
        else -> NavigationType.BOTTOM_NAVIGATION
    }
}

/**
 * Check if should use list-detail layout (for medium/expanded screens)
 */
fun shouldUseListDetailLayout(widthSizeClass: WindowWidthSizeClass): Boolean {
    return widthSizeClass == WindowWidthSizeClass.Medium ||
            widthSizeClass == WindowWidthSizeClass.Expanded
}
