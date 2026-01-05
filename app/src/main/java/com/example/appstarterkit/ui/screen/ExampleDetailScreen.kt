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
import androidx.compose.ui.unit.dp
import com.example.appstarterkit.ui.components.cards.AnimatedCard

/**
 * Example Detail Screen - Shows details of a specific example item
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExampleDetailScreen(
    exampleId: String,
    onNavigateBack: () -> Unit
) {
    // In a real app, this would come from a ViewModel
    var example by remember {
        mutableStateOf(
            ExampleDetail(
                id = exampleId,
                title = "Example $exampleId",
                description = "This is a detailed view of example item $exampleId. In a real application, this data would be fetched from a repository or database.",
                createdAt = System.currentTimeMillis()
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Example Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement edit */ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                    IconButton(onClick = { /* TODO: Implement delete */ }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Main Content Card
            AnimatedCard(
                onClick = { /* Expand card if needed */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ID Badge
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "ID: ${example.id}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    // Title
                    Text(
                        text = example.title,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    // Description
                    Text(
                        text = example.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Divider()

                    // Metadata
                    InfoRow(
                        icon = Icons.Default.CalendarToday,
                        label = "Created",
                        value = formatDate(example.createdAt)
                    )
                }
            }

            // Additional Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Additional Information",
                        style = MaterialTheme.typography.titleMedium
                    )

                    InfoRow(
                        icon = Icons.Default.Info,
                        label = "Status",
                        value = "Active"
                    )

                    InfoRow(
                        icon = Icons.Default.Person,
                        label = "Author",
                        value = "System"
                    )

                    InfoRow(
                        icon = Icons.Default.Category,
                        label = "Category",
                        value = "Example"
                    )
                }
            }

            // Actions Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Actions",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ListItem(
                        headlineContent = { Text("Share") },
                        leadingContent = {
                            Icon(Icons.Default.Share, contentDescription = null)
                        },
                        modifier = Modifier.clickable { /* TODO: Implement */ }
                    )

                    ListItem(
                        headlineContent = { Text("Export") },
                        leadingContent = {
                            Icon(Icons.Default.FileDownload, contentDescription = null)
                        },
                        modifier = Modifier.clickable { /* TODO: Implement */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
    return format.format(date)
}

/**
 * Data class for example details
 */
data class ExampleDetail(
    val id: String,
    val title: String,
    val description: String,
    val createdAt: Long
)
