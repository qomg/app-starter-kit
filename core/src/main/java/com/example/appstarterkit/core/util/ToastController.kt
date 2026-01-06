package com.example.appstarterkit.core.util

import android.content.Context
import android.widget.Toast
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import timber.log.Timber

/**
 * Toast Controller
 * Manages toast messages with simple API
 */
object ToastController {

    /**
     * Show a toast message
     */
    fun show(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Show a long toast message
     */
    fun showLong(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Show an error toast
     */
    fun showError(context: Context, message: String) {
        Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
    }

    /**
     * Show a success toast
     */
    fun showSuccess(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Show an info toast
     */
    fun showInfo(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Toast State for Composable
 */
@Composable
fun rememberToastController(
    message: String,
    showToast: Boolean = false
): ToastController {
    val context = LocalContext.current
    var hasShownToast by remember { mutableStateOf(false) }

    // Only show toast if it hasn't been shown yet
    if (showToast && !hasShownToast) {
        ToastController.show(context, message)
        hasShownToast = true
    }

    return ToastController
}

/**
 * Toast Type
 */
enum class ToastType {
    Success,
    Info,
    Warning,
    Error
}

/**
 * Show a toast by type
 */
fun showToastByType(
    type: ToastType,
    message: String
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    when (type) {
        ToastType.Success -> ToastController.showSuccess(context, message)
        ToastType.Info -> ToastController.showInfo(context, message)
        ToastType.Warning -> ToastController.show(context, message)
        ToastType.Error -> ToastController.showError(context, message)
    }
}
