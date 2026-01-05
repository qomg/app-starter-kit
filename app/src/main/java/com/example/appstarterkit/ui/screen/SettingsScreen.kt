package com.example.appstarterkit.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.appstarterkit.R
import com.example.appstarterkit.ui.components.selection.AnimatedToggle

/**
 * Settings Screen - App configuration and preferences
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToAbout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            var darkMode by remember { mutableStateOf(false) }
            var notificationsEnabled by remember { mutableStateOf(true) }
            var autoSync by remember { mutableStateOf(false) }

            // Appearance Section
            SettingsSection(title = "Appearance") {
                SettingsToggle(
                    title = "Dark Mode",
                    description = "Enable dark theme",
                    checked = darkMode,
                    onCheckedChange = { darkMode = it },
                    icon = Icons.Default.DarkMode
                )
                SettingsToggle(
                    title = "Dynamic Colors",
                    description = "Use Material You colors",
                    checked = false,
                    onCheckedChange = { /* TODO: Implement */ },
                    icon = Icons.Default.Palette
                )
            }

            // Notifications Section
            SettingsSection(title = "Notifications") {
                SettingsToggle(
                    title = "Enable Notifications",
                    description = "Receive push notifications",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it },
                    icon = Icons.Default.Notifications
                )
            }

            // Data Section
            SettingsSection(title = "Data") {
                SettingsToggle(
                    title = "Auto Sync",
                    description = "Sync data automatically",
                    checked = autoSync,
                    onCheckedChange = { autoSync = it },
                    icon = Icons.Default.Sync
                )
                SettingsItem(
                    title = "Clear Cache",
                    description = "Free up storage space",
                    icon = Icons.Default.Delete,
                    onClick = { /* TODO: Implement */ }
                )
            }

            // About Section
            SettingsSection(title = "About") {
                SettingsItem(
                    title = "About App",
                    description = "Version 1.0.0",
                    icon = Icons.Default.Info,
                    onClick = onNavigateToAbout
                )
                SettingsItem(
                    title = "Privacy Policy",
                    description = "Read our privacy policy",
                    icon = Icons.Default.Security,
                    onClick = { /* TODO: Implement */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsToggle(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(description) },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        trailingContent = {
            AnimatedToggle(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
    Divider()
}

@Composable
private fun SettingsItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(description) },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate"
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    )
    Divider()
}
