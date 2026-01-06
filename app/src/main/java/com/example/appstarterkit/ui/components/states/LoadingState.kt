package com.example.appstarterkit.ui.components.states

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Loading State Component
 * Shows a circular progress indicator with optional message
 */
@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    message: String? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
        
        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Full Screen Loading State
 * Covers the entire screen with backdrop
 */
@Composable
fun FullScreenLoadingState(
    modifier: Modifier = Modifier,
    message: String? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingState(
            modifier = Modifier
        )
    }
}
