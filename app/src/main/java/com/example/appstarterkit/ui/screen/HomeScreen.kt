package com.example.appstarterkit.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appstarterkit.R
import com.example.appstarterkit.ui.components.AdaptiveLayout
import com.example.appstarterkit.ui.components.adaptivePadding
import com.example.appstarterkit.ui.components.buttons.AnimatedButton
import com.example.appstarterkit.ui.components.cards.ExpandableCard

/**
 * Home Screen - The main landing screen of the app
 * Uses adaptive layouts for different screen sizes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToExample: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp >= 600f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .then(if (!isWideScreen) Modifier.padding(16.dp) else Modifier)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Card - spans full width on wide screens
            Card(
                modifier = if (isWideScreen) Modifier.fillMaxWidth(0.5f) else Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Welcome to AppStarterKit",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "A modern Android starter template with Jetpack Compose",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            // Features Section
            ExpandableCard(
                title = "Features",
                expanded = true
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeatureItem(
                        icon = "✨",
                        title = "Material Design 3",
                        description = "Beautiful, consistent UI components"
                    )
                    FeatureItem(
                        icon = "🎨",
                        title = "Dynamic Theme",
                        description = "Light/Dark mode with Material You"
                    )
                    FeatureItem(
                        icon = "⚡",
                        title = "Animated Components",
                        description = "Smooth, polished interactions"
                    )
                    FeatureItem(
                        icon = "🏗️",
                        title = "Clean Architecture",
                        description = "Scalable, testable codebase"
                    )
                }
            }

            // Quick Actions - Use Row on wide screens
            ExpandableCard(
                title = "Quick Actions",
                expanded = false
            ) {
                if (isWideScreen) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AnimatedButton(
                            text = "View Examples",
                            onClick = onNavigateToExample,
                            modifier = Modifier.weight(1f)
                        )
                        AnimatedButton(
                            text = "Open Settings",
                            onClick = onNavigateToSettings,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AnimatedButton(
                            text = "View Examples",
                            onClick = onNavigateToExample,
                            modifier = Modifier.fillMaxWidth()
                        )
                        AnimatedButton(
                            text = "Open Settings",
                            onClick = onNavigateToSettings,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FeatureItem(
    icon: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineMedium
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
