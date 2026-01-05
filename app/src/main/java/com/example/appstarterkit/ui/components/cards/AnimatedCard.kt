package com.example.appstarterkit.ui.components.cards

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.appstarterkit.ui.theme.CardShape

/**
 * Animated Card component with smooth elevation and color transitions.
 * Provides visual feedback when clicked.
 *
 * @param onClick Callback when card is clicked
 * @param modifier Modifier for the card
 * @param elevation Card elevation (default: 4.dp)
 * @param border Optional border for the card
 * @param shape Card shape (default: CardShape)
 * @param content The content to display inside the card
 */
@Composable
fun AnimatedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Float = 4f,
    border: BorderStroke? = null,
    shape: Shape = CardShape,
    content: @Composable ColumnScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .clip(shape)
            .clickable(
                onClick = {
                    isPressed = true
                    onClick()
                },
                indication = null
            )
            .padding(if (isPressed) 8.dp else 12.dp),
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation.dp,
            pressedElevation = 8.dp.dp
        ),
        border = border,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        content()
    }
}

/**
 * Expandable Card that can be expanded/collapsed with animation.
 *
 * @param title The title of the card
 * @param expanded Whether the card is initially expanded
 * @param modifier Modifier for the card
 * @param content The content to display when expanded
 */
@Composable
fun ExpandableCard(
    title: String,
    expanded: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    var isExpanded by remember { mutableStateOf(expanded) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(durationMillis = 300)
            ),
        shape = CardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                AnimatedContent(
                    targetState = isExpanded,
                    transitionSpec = {
                        slideInVertically() + fadeIn() togetherWith slideOutVertically() + fadeOut()
                    },
                    label = "icon"
                ) { expanded ->
                    Icon(
                        imageVector = if (expanded)
                            androidx.compose.material.icons.Icons.Default.ExpandLess
                        else
                            androidx.compose.material.icons.Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween(durationMillis = 300)
                ) + fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                content()
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = CardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }
}

/**
 * Flip Card that can be flipped to show content on the back.
 *
 * @param frontContent Content to display on the front
 * @param backContent Content to display on the back
 * @param modifier Modifier for the card
 */
@Composable
fun FlipCard(
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clickable { isFlipped = !isFlipped },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AnimatedContent(
            targetState = isFlipped,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = tween(durationMillis = 400),
                    initialOffsetX = { if (targetState) it else -it }
                ) + fadeIn(animationSpec = tween(durationMillis = 400)) togetherWith
                        slideOutHorizontally(
                            animationSpec = tween(durationMillis = 400),
                            targetOffsetX = { if (targetState) -it else it }
                        ) + fadeOut(animationSpec = tween(durationMillis = 400))
            },
            label = "flip"
        ) { flipped ->
            if (flipped) {
                Box(modifier = Modifier.fillMaxSize()) {
                    backContent()
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    frontContent()
                }
            }
        }
    }
}
