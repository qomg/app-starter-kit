package com.example.appstarterkit.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appstarterkit.R
import com.example.appstarterkit.core.util.ToastController
import com.example.appstarterkit.data.offline.OfflineManager
import com.example.appstarterkit.data.repository.SncRepository
import com.example.appstarterkit.data.repository.ExampleRepository
import com.example.appstarterkit.domain.usecase.FetchExamplesUseCase
import com.example.appstarterkit.domain.usecase.SynchronizeDataUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Settings Screen with offline and sync support
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToAbout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    syncRepository: SyncRepository = hiltViewModel(),
    offlineManager: OfflineManager = hiltViewModel()
    context: android.content.Context = androidx.compose.ui.platform.LocalContext.current
) {
    val darkTheme by viewModel.darkTheme.collectAsState()
    val dynamicColors by viewModel.dynamicColors.collectAsState()
    var autoSync by remember { mutableStateOf(false) }
    var showSyncDialog by remember { mutableStateOf(false) }

    // Sync status
    val syncJob = viewModel.syncJob.collectAsState()
    val isSyncing = syncJob?.value?.isActive == true

    LaunchedEffect(Unit) {
        viewModel.loadSettings(context)
    }

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
            // Appearance Section
            SettingsSection(title = "Appearance") {
                SettingsToggle(
                    title = "Dark Mode",
                    description = "Enable dark theme",
                    checked = darkTheme,
                    onCheckedChange = { viewModel.toggleDarkTheme() },
                    icon = Icons.Default.DarkMode
                )
                SettingsToggle(
                    title = "Dynamic Colors",
                    description = "Use Material You colors",
                    checked = dynamicColors,
                    onCheckedChange = { viewModel.toggleDynamicColors() },
                    icon = Icons.Default.Palette
                )
            }

            // Notifications Section
            SettingsSection(title = "Notifications") {
                SettingsToggle(
                    title = "Enable Notifications",
                    description = "Receive push notifications",
                    checked = true,
                    onCheckedChange = { /* TODO: Implement notifications */ },
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
                    title = "Last Sync",
                    description = "Never",
                    icon = Icons.Default.History,
                    onClick = { /* TODO: Show sync history */ }
                )
                SettingsItem(
                    title = "Sync Now",
                    description = "Sync data immediately",
                    icon = Icons.Default.CloudSync,
                    enabled = !isSyncing,
                    onClick = {
                        viewModel.startSyncJob()
                    viewModel.launch {
                        val result = syncRepository.syncIfNeeded()
                        result.onSuccess {
                            ToastController.success(context, "Sync completed")
                        }.onFailure { e ->
                            ToastController.error(context, "Sync failed")
                        }
                    }
                )
                SettingsItem(
                    title = "Clear Cache",
                    description = "Clear offline cache",
                    icon = Icons.Default.CleaningServices,
                    onClick = {
                        viewModel.clearCache()
                        ToastController.success(context, "Cache cleared")
                    }
                )
            }

            // Offline Section
            SettingsSection(title = "Offline") {
                val isOffline by offlineManager.isOffline.collectAsState()
                SettingsToggle(
                    title = "Offline Mode",
                    description = "Disable network requests",
                    checked = isOffline,
                    onCheckedChange = { /* TODO: Implement offline mode */ },
                    icon = Icons.Default.CloudOff
                )
                SettingsItem(
                    title = "Cache Size",
                    description = "${offlineManager.getCacheSize()} items",
                    icon = Icons.Default.Storage,
                    onClick = { /* TODO: Show cache details */ }
                )
                SettingsItem(
                    title = "Clear Cache",
                    description = "Clear all cached data",
                    icon = Icons.Default.Delete,
                    onClick = {
                        viewModel.launch {
                            offlineManager.clearCache()
                        }
                        ToastController.success(context, "Cache cleared")
                    }
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
                    onClick = { /* TODO: Open privacy policy */ }
                )
            }
        }
    }

// Sync Dialog
if (showSyncDialog) {
    AlertDialog(
        onDismissRequest = { showSyncDialog = false },
        title = "Sync Data",
        text = "This will sync your data with the server. Continue?",
        confirmButton = "Sync",
        onConfirm = {
            showSyncDialog = false
            viewModel.startSyncJob()
        },
        dismissButton = "Cancel",
        onDismiss = {
            showSyncDialog = false
        },
        icon = Icons.Default.Sync
    )
}
