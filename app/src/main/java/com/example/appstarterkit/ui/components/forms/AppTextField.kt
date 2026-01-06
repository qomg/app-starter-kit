package com.example.appstarterkit.ui.components.forms

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecoration
import androidx.compose.foundation.text.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation

/**
 * App TextField with Material Design 3 styling
 * Supports outline, error, disabled states
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    visualTransformation: VisualTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
    keyboardType = KeyboardType.Text,
    imeAction = androidx.compose.ui.text.input.ImeAction.Default
    )
) {
    val textInteractionState = androidx.compose.material3.TextFieldDefaults.colors(
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        errorIndicatorColor = MaterialTheme.colorScheme.error,
        disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
        errorContainerColor = MaterialTheme.colorScheme.errorContainer,
        errorTextColor = MaterialTheme.colorScheme.error
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        isError = isError,
        singleLine = singleLine,
        maxLines = maxLines,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        label = label,
        placeholder = placeholder,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        colors = textInteractionState,
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

/**
 * Password TextField with visibility toggle
 */
@Composable
fun AppPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true
    maxLines: Int = 1,
    leadingIcon: ImageVector? = Icons.Default.Lock,
    trailingIcon: ImageVector? = Icons.Default.Check
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val visualTransformation = if (passwordVisible) {
        PasswordVisualTransformation()
    } else {
        null
    }

    val trailingIcon = if (passwordVisible) {
        androidx.compose.material3.IconToggleButton(
            checked = passwordVisible,
            onCheckedChange = { passwordVisible = it }
        )
    } else {
        Icons.Default.Check
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        isError = isError,
        singleLine = singleLine,
        maxLines = maxLines,
        leadingIcon = leadingIcon,
        trailingIcon = {
            Box(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                trailingIcon
            }
        },
        label = label,
        placeholder = placeholder,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = androidx.compose.ui.text.input.ImeAction.Done
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorIndicatorColor = MaterialTheme.colorScheme.error
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

/**
 * Number TextField
 */
@Composable
fun AppNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = false
    prefix: String = "",
    suffix: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        isError = isError,
        singleLine = singleLine,
        prefix = prefix,
        suffix = suffix,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = androidx.compose.ui.text.input.ImeAction.Done
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

/**
 * Email TextField
 */
@Composable
fun AppEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: MaterialTheme.typography.bodyLarge,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = true
    keyboardOptions: KeyboardOptions(
        keyboardType = KeyboardType.Email,
        imeAction = androidx.compose.ui.text.input.ImeAction.Done
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        isError = isError,
        singleLine = singleLine,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        keyboardOptions = keyboardOptions,
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

/**
 * Search TextField
 */
@Composable
fun AppSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Search...",
    enabled: Boolean = true
    leadingIcon: ImageVector? = Icons.Default.Search
    trailingIcon: ImageVector? = null,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        isError = false,
        singleLine = singleLine,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = androidx.compose.ui.text.input.ImeAction.Search
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}
