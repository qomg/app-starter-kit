package com.example.appstarterkit.ui.components.states

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

/**
 * Error State Component
 * Displays error message with retry action
 */
@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier,
    icon: ImageVector? = Icons.Default.Error
    actionLabel: String = "Retry",
    title: String? = "Error",
    backgroundColor: Color = MaterialTheme.colorScheme.errorContainer
) {
    val scale by remember { mutableFloatStateOf(0.85f) }
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 200),
        label = "errorFadeIn"
    )

    Card(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Error Icon with scale animation
                androidx.compose.foundation.Image(
                    imageVector = icon,
                    contentDescription = "Error Icon",
                    modifier = Modifier
                        .size(48.dp)
                        .scale(scale),
                    alpha = alpha,
                    colorFilter = ColorFilter.tint(Color(0xFFD32F2F))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFD32F2F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Retry Button
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = actionLabel,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

/**
 * Full Screen Error State
 */
@Composable
fun FullScreenErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        ErrorState(
            message = message,
            onRetry = onRetry
        )
    }
}

/**
 * Dismissible Error Toast
 */
@Composable
fun ErrorToast(
    message: String,
    durationMillis: Long = 3000L
) {
    val toastController = rememberToastController(state = rememberErrorState(message, ToastType.Error))
}

/**
 * Remember error state for toast
 */
@Composable
private fun rememberErrorState(
    message: String,
    toastType: ToastType
): ToastState {
    val controller = rememberToastController(toastType)
    
    LaunchedEffect(message) {
        controller.show(message, durationMillis)
    }
    
    controller
}

/**
 * Error Dialog
 */
@Composable
fun ErrorDialog(
    title: String,
    message: String,
    errorMessage: String? = null,
    onDismiss: () -> Unit = {},
    onRetry: () -> Unit = {},
    onDismissError: () -> Unit = {}
) {
    ErrorDialog(
        title = title,
        message = message,
        errorMessage = errorMessage,
        confirmText = "Retry",
        onConfirm = onRetry,
        onDismiss = onDismiss,
        icon = Icons.Default.Error
    )
}

/**
 * Detailed Error Dialog with stack trace
 */
@Composable
fun ErrorDialog(
    title: String,
    message: String,
    errorMessage: String?,
    errorDetails: List<String>? = null,
    onDismiss: () -> Unit = {},
    onRetry: () -> Unit = {},
    onDismissError: () -> Unit = {}
) {
    val showDialog by remember { mutableStateOf(false) }
    
    if (showDialog || errorMessage != null || errorDetails != null) {
        AppAlertDialog(
            onDismissRequest = {
                onDismiss()
                onDismissError()
            },
            title = title,
            message = message,
            confirmButton = if (errorMessage != null || errorDetails != null) "Show Details" else null,
            onConfirm = {
                // Show error details
            },
            icon = Icons.Default.Error
        )
    } else {
        ErrorState(
            message = message,
            onRetry = onRetry
        )
    }
}

/**
 * Warning State Component
 */
@Composable
fun WarningState(
    message: String,
    onDismiss: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            androidx.compose.foundation.Image(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                modifier = Modifier.size(64.dp),
                colorFilter = ColorFilter.tint(Color(0xFFFF9800))
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(0.7f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "OK",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
