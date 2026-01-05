package com.example.appstarterkit.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

/**
 * Adaptive Layout Types
 */
enum class LayoutType {
    SINGLE_COLUMN,       // Phone portrait
    TWO_COLUMN,          // Phone landscape / small tablet
    THREE_COLUMN         // Large tablet
}

/**
 * Determine layout type based on screen width
 */
@Composable
fun rememberLayoutType(): LayoutType {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    return remember(screenWidthDp) {
        when {
            screenWidthDp < 600f -> LayoutType.SINGLE_COLUMN
            screenWidthDp < 840f -> LayoutType.TWO_COLUMN
            else -> LayoutType.THREE_COLUMN
        }
    }
}

/**
 * Adaptive Column/Row Layout
 * Automatically switches between Column and Row based on screen size
 */
@Composable
fun AdaptiveLayout(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    val layoutType = rememberLayoutType()

    if (layoutType == LayoutType.SINGLE_COLUMN) {
        Column(
            modifier = modifier,
            verticalArrangement = verticalArrangement
        ) {
            content()
        }
    } else {
        Row(
            modifier = modifier,
            horizontalArrangement = horizontalArrangement
        ) {
            content()
        }
    }
}

/**
 * Adaptive Grid Layout
 * Automatically adjusts number of columns based on screen size
 *
 * @param columns Number of columns to display
 * @param modifier Modifier for the layout
 * @param content Content to display in each cell
 */
@Composable
fun <T> AdaptiveGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    minColumnWidth: Int = 300,
    horizontalSpacing: Int = 16,
    verticalSpacing: Int = 16,
    content: @Composable (T) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    val columns = remember(screenWidthDp, minColumnWidth) {
        val maxColumns = (screenWidthDp / minColumnWidth).toInt()
        maxColumns.coerceAtLeast(1)
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing.dp)
    ) {
        items(items.chunked(columns)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing.dp)
            ) {
                rowItems.forEach { item ->
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        content(item)
                    }
                }
                // Fill remaining space if needed
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * Adaptive Card Layout
 * Shows cards in different layouts based on screen size
 */
@Composable
fun AdaptiveCards(
    items: List<CardItem>,
    modifier: Modifier = Modifier
) {
    val layoutType = rememberLayoutType()

    when (layoutType) {
        LayoutType.SINGLE_COLUMN -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items.forEach { item ->
                    AdaptiveCardItem(item)
                }
            }
        }
        LayoutType.TWO_COLUMN -> {
            LazyRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items) { item ->
                    AdaptiveCardItem(
                        item = item,
                        modifier = Modifier.width(300.dp)
                    )
                }
            }
        }
        LayoutType.THREE_COLUMN -> {
            AdaptiveGrid(
                items = items,
                modifier = modifier,
                minColumnWidth = 300
            ) { item ->
                AdaptiveCardItem(item)
            }
        }
    }
}

@Composable
private fun AdaptiveCardItem(
    item: CardItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * List-Detail Layout
 * Shows list and detail side by side on large screens
 *
 * @param items List of items to display
 * @param selectedItemId Currently selected item ID
 * @param onItemSelected Callback when an item is selected
 * @param listItemContent Content to display for each list item
 * @param detailContent Content to display for detail view
 */
@Composable
fun <T> ListDetailLayout(
    items: List<T>,
    selectedItemId: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    listItemContent: @Composable (T, Boolean) -> Unit,
    detailContent: @Composable (T?) -> Unit
) {
    val layoutType = rememberLayoutType()

    if (layoutType == LayoutType.SINGLE_COLUMN) {
        // Single column layout - show only list
        Column(modifier = modifier) {
            items.forEach { item ->
                val itemId = getItemId(item)
                listItemContent(item, itemId == selectedItemId)
            }
        }
    } else {
        // Two/three column layout - show list and detail side by side
        Row(modifier = modifier.fillMaxHeight()) {
            // List panel
            Surface(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight(),
                tonalElevation = 2.dp
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        val itemId = getItemId(item)
                        listItemContent(item, itemId == selectedItemId)
                    }
                }
            }

            // Detail panel
            Surface(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                tonalElevation = 4.dp
            ) {
                val selectedItem = items.find { getItemId(it) == selectedItemId }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    detailContent(selectedItem)
                }
            }
        }
    }
}

/**
 * Adaptive Spacing
 * Returns appropriate spacing based on screen size
 */
@Composable
fun adaptivePadding(): PaddingValues {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    val padding = when {
        screenWidthDp < 600f -> 16.dp
        screenWidthDp < 840f -> 24.dp
        else -> 32.dp
    }

    return PaddingValues(padding)
}

/**
 * Data class for card items
 */
data class CardItem(
    val id: String,
    val title: String,
    val description: String
)

/**
 * Get ID from item (simplified version)
 */
private fun <T> getItemId(item: T): String {
    return when (item) {
        is CardItem -> item.id
        else -> item.hashCode().toString()
    }
}
