package com.example.appstarterkit.ui.components.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

/**
 * Simple Alert Dialog
 * Shows a title, message, and optional action button
 */
@Composable
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit = {},
    title: String,
    message: String,
    confirmButton: String? = null,
    dismissButton: String = "Cancel",
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismissRequest,
        title = title,
        text = message,
        icon = icon,
        confirmButton = confirmButton?.let { { Text(it) } },
        dismissButton = { Text(dismissButton) },
        modifier = Modifier.fillMaxWidth(0.9f)
    )
}

/**
 * App Dialog with customizable content
 */
@Composable
fun AppDialog(
    onDismissRequest: () -> Unit,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismissRequest,
        title = title,
        icon = icon,
        text = content,
        confirmButton = {
            Text("OK")
        },
        dismissButton = {
            Text("Close")
        },
        modifier = Modifier.fillMaxWidth(0.9f)
    )
    }
}

/**
 * Loading Dialog
 */
@Composable
fun LoadingDialog(
    onDismissRequest: () -> Unit,
    title: String = "Loading",
    message: String = "Please wait..."
) {
    AppDialog(
        onDismissRequest = onDismissRequest,
        title = title,
        icon = Icons.Default.Info
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Success Dialog
 */
@Composable
fun SuccessDialog(
    onDismissRequest: () -> Unit,
    title: String = "Success",
    message: String,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    if (actionLabel != null) {
        AppDialog(
            onDismissRequest = onDismissRequest,
            title = title,
            icon = Icons.Default.CheckCircle
        ) {
            Column {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = onActionClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(actionLabel)
                }
            }
        }
    } else {
        AppAlertDialog(
            onDismissRequest = onDismissRequest,
            title = title,
            icon = Icons.Default.CheckCircle,
            confirmButton = "OK"
            dismissButton = null
            message = message
        )
    }
    }
}

/**
 * Error Dialog
 */
@Composable
fun ErrorDialog(
    onDismissRequest: () -> Unit,
    title: String = "Error",
    message: String,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    if (actionLabel != null) {
        AppDialog(
            onDismissRequest = onDismissRequest,
            title = title,
            icon = Icons.Default.Error
        ) {
            Column {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = onActionClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.errorButtonColors()
                ) {
                    Text(actionLabel)
                }
            }
        }
    } else {
        AppAlertDialog(
            onDismissRequest = onDismissRequest,
            title = title,
            icon = Icons.Default.Error,
            confirmButton = "OK",
            dismissButton = null,
            message = message
        )
    }
    }
}

/**
 * Confirmation Dialog
 */
@Composable
fun ConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit = {},
    title: String,
    message: String,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    AppAlertDialog(
        onDismissRequest = onDismissRequest,
        title = title,
        icon = icon,
        confirmButton = confirmText,
        dismissButton = cancelText,
        message = message
    )
}
}
