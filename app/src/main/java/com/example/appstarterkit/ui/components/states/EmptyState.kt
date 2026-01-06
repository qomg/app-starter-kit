package com.example.appstarterkit.ui.components.states

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Empty State Component
 * Shows when there's no data to display
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    message: String? = "No data available",
    icon: ImageVector? = Icons.Default.Info,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = message,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        if (message != null) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        
        if (actionLabel != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onActionClick,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(actionLabel)
            }
        }
    }
}

/**
 * Minimal Empty State
 */
@Composable
fun MinimalEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Empty",
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "No Data",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Image-based Empty State
 */
@Composable
fun ImageEmptyState(
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = androidx.compose.ui.res.painterResource(imageRes),
            contentDescription = "Empty State",
            modifier = Modifier.size(128.dp),
            alpha = 0.5f
        )
    }
}

/**
 * Lottie-based Empty State (optional, requires lottie-compose library)
 */
@Composable
fun LottieEmptyState(
    lottieAnimation: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Loading animation...",
            style = MaterialTheme.typography.bodyMedium
        )
        
        // Uncomment and add lottie dependency if needed
        // LottieAnimation(
        //     modifier = Modifier.size(200.dp),
        //     animation = lottieAnimation
        // )
    }
}
