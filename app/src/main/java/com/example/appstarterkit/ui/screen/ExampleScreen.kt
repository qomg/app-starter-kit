package com.example.appstarterkit.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appstarterkit.domain.model.Example
import com.example.appstarterkit.ui.components.AdaptiveCards
import com.example.appstarterkit.ui.components.CardItem
import com.example.appstarterkit.ui.components.ListDetailLayout
import com.example.appstarterkit.ui.components.buttons.AnimatedButton

/**
 * Example Screen demonstrating adaptive layouts
 * Shows different layouts based on screen size
 */
@Composable
fun ExampleScreen(
    onNavigateToDetail: (String) -> Unit = {},
    viewModel: ExampleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp >= 600f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Examples") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is ExampleUiState.Loading -> {
                    LoadingContent()
                }
                is ExampleUiState.Success -> {
                    SuccessContent(
                        examples = state.examples,
                        onRefresh = viewModel::refreshExamples,
                        onNavigateToDetail = onNavigateToDetail,
                        isWideScreen = isWideScreen
                    )
                }
                is ExampleUiState.Error -> {
                    ErrorContent(
                        message = state.message,
                        onRetry = viewModel::refreshExamples
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SuccessContent(
    examples: List<Example>,
    onRefresh: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    isWideScreen: Boolean
) {
    // For wide screens, use list-detail layout
    var selectedId by remember { mutableStateOf<String?>(null) }

    if (isWideScreen && examples.isNotEmpty()) {
        // List-Detail Layout for tablets
        Column(modifier = Modifier.fillMaxSize()) {
            AnimatedButton(
                text = "Refresh",
                onClick = onRefresh,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            ListDetailLayout(
                items = examples,
                selectedItemId = selectedId,
                onItemSelected = { id -> selectedId = id },
                modifier = Modifier.fillMaxSize(),
                listItemContent = { example, isSelected ->
                    ListItem(
                        headlineContent = { Text(example.name) },
                        supportingContent = example.description?.let { Text(it) },
                        selected = isSelected,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToDetail(example.id) }
                    )
                },
                detailContent = { example ->
                    if (example != null) {
                        DetailContent(example = example)
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Select an item",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        }
    } else {
        // Single column layout for phones
        Column(modifier = Modifier.fillMaxSize()) {
            AnimatedButton(
                text = "Refresh",
                onClick = onRefresh,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(examples) { example ->
                    ExpandableCard(
                        title = example.name,
                        expanded = false
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (example.description != null) {
                                Text(
                                    text = example.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "ID: ${example.id}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                androidx.compose.material3.TextButton(
                                    onClick = { onNavigateToDetail(example.id) }
                                ) {
                                    Text("View Details")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailContent(example: Example) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = example.name,
                style = MaterialTheme.typography.headlineMedium
            )
            if (example.description != null) {
                Text(
                    text = example.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text("ID: ${example.id}") }
                )
                AssistChip(
                    onClick = {},
                    label = { Text("Active") }
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            AnimatedButton(
                text = "Retry",
                onClick = onRetry
            )
        }
    }
}
