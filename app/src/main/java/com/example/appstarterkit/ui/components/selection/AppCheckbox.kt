package com.example.appstarterkit.ui.components.selection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * App Checkbox with custom styling
 */
@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = ""
) {
    val interactionSource = remember { MutableInteractionSource() }
    val checkedState by remember { mutableStateOf(checked) }

    Row(
        modifier = modifier
            .size(20.dp)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                onClick = { onCheckedChange(!checkedState) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = {
                checkedState = it
                onCheckedChange(it)
            },
            enabled = enabled,
            interactionSource = interactionSource,
            modifier = Modifier.padding(8.dp)
        )
        if (text.isNotEmpty()) {
            Text(
                text = text,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

/**
 * Checkbox with label support
 */
@Composable
fun LabeledCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppCheckbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = true
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
