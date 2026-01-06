package com.example.appstarterkit.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Simple LazyColumn with common padding
 * Wraps LazyColumn with standard spacing
 */
@Composable
fun AppLazyColumn(
    modifier: Modifier = Modifier,
    content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

/**
 * LazyRow with common padding
 */
@Composable
fun AppLazyRow(
    modifier: Modifier = Modifier,
    content: androidx.compose.foundation.lazy.LazyRowScope.() -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 0.dp)
    ) {
        content()
    }
}

/**
 * LazyVerticalGrid for responsive grid layouts
 */
@Composable
fun <T> AppLazyVerticalGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    contentPadding: androidx.compose.foundation.layout.PaddingValues(16.dp),
    key: ((T) -> String)? = null,
    itemContent: @Composable androidx.compose.foundation.lazy.LazyItemScope.(item: T, index: Int) -> Unit
) {
    androidx.compose.foundation.lazy.LazyVerticalGrid(
        columns = columns,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.count()) { index ->
            LazyItem(
                key = key?.invoke(items[index])
            content = itemContent(items[index], index)
            )
        }
    }
}

/**
 * Refreshable content with pull-to-refresh
 */
@Composable
fun <T> RefreshableLazyColumn(
    items: List<T>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    key: ((T) -> String)? = null,
    itemContent: @Composable androidx.compose.foundation.lazy.LazyItemScope.(item: T, index: Int) -> Unit
) {
    androidx.compose.material3.pulltorefresh.PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.count()) { index ->
                LazyItem(
                    key = key?.invoke(items[index])
                    content = itemContent(items[index], index)
                )
            }
        }
    }
}
