package com.example.appstarterkit.ui.components.selection

import androidx.compose.animation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * App Switch with Material Design 3 styling
 * Animated thumb and ripple effect
 */
@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
    thumbIcon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val thumbScale by animateFloatAsState(
        targetValue = if (checked) 1.2f else 1f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "thumbScale"
    )

    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier
        enabled = enabled,
        interactionSource = interactionSource,
        thumbContent = {
            Icon(
                imageVector = thumbIcon,
                contentDescription = if (checked) "Enabled" else "Disabled",
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        scaleX = thumbScale
                        scaleY = thumbScale
                    },
                tint = if (checked)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.outlineVariant
            )
        }
    )
}

/**
 * Radio Button Group
 */
@Composable
fun <T> AppRadioGroup(
    options: List<AppRadioOption<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            AppRadioButton(
                text = option.text,
                selected = option.value == selectedOption,
                onClick = { onOptionSelected(option.value) }
            )
        }
    }
}

/**
 * Single Radio Button
 */
@Composable
fun <T> AppRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * Data class for radio options
 */
data class AppRadioOption<T>(
    val value: T,
    val text: String
)

/**
 * Segmented Control (Radio Button Group)
 */
@Composable
fun <T> AppSegmentedControl(
    items: List<AppRadioOption<T>>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items.forEach { item ->
            AppRadioItem(
                item = item,
                selected = item.value == selectedItem,
                onSelected = onSelected
            )
        }
    }
}

@Composable
private fun <T> AppRadioItem(
    item: AppRadioOption<T>,
    selected: Boolean,
    onSelected: (T) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) 
            MaterialTheme.colorScheme.primaryContainer 
        else 
            MaterialTheme.colorScheme.surfaceVariant
    )
    )

    Surface(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .clickable { onSelected(item.value) }
            .background(backgroundColor),
        border = BorderStroke(
            width = if (selected) 1.dp else 0.dp,
            color = backgroundColor
        )
    ) {
        Text(
            text = item.text,
            modifier = Modifier
                .padding(12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Dropdown Menu
 */
@Composable
fun <T> AppDropdown(
    label: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Select..."
) {
    var expanded by remember { mutableStateOf(false) }
    val items = remember(options)

    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = items.find { it.value == selectedOption }?.let { item -> item.text },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        imageVector = if (expanded)
                            Icons.Default.ExpandLess
                        else
                            Icons.Default.ExpandMore
                    )
                }
            }
        }
    }
}

/**
 * Bottom Sheet
 */
@Composable
fun AppBottomSheet(
    onDismissRequest: () -> Unit,
    sheetContent: @Composable ColumnScope.() -> Unit
) {
    val sheetState = androidx.compose.material3.rememberSheetState()
    val bottomSheetSkipHidden = BottomSheetDefaults.skipHidden()

    BottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            BottomSheetDefaults.DragHandle()
        ),
        content = {
            sheetContent()
        }
    )
}

/**
 * Custom Dropdown Menu Item
 */
@Composable
fun <T> DropdownMenuItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Surface(
        modifier = Modifier
            .clickable(enabled = enabled, onClick = onClick)
            .fillMaxWidth()
            .background(
                color = if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    Color.Transparent
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

/**
 * Multi-select Dropdown
 */
@Composable
fun <T> MultiSelectDropdown(
    label: String,
    options: List<T>,
    selectedOptions: Set<T> = emptySet(),
    onSelectionChanged: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val items = remember(options)

    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val selectedText = when {
                        selectedOptions.size -> selectedOptions.joinToString(", ")
                        else -> placeholder
                    }
                    Text(
                        text = selectedText,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expanded)
                            Icons.Default.ExpandLess
                        else
                            Icons.Default.expand_more
                    )
                }
            }
        }

        DropdownMenu(
            expanded = expanded,
            modifier = Modifier.fillMaxWidth(),
            offset = androidx.compose.material3.DropdownMenuItem(
                anchor = androidx.compose.material3.DropdownMenu(
                    offset = androidx.compose.material3.DpOffset(0.dp)
                )
            ),
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                val isSelected = selectedOptions.contains(item)
                DropdownMenuItem(
                    text = item.toString(),
                    selected = isSelected,
                    onClick = { }
                )
            }
        }
    }
}
