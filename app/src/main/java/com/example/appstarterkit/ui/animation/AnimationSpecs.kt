package com.example.appstarterkit.ui.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*

// Easing functions
val FastOutSlowInEasing = FastOutSlowInEasing
val LinearOutSlowInEasing = LinearOutSlowInEasing
val FastOutLinearInEasing = FastOutLinearInEasing

// Duration constants
const val AnimationDurationShort = 200
const val AnimationDurationMedium = 300
const val AnimationDurationLong = 400

// Animation specs
val DefaultTween = tween<Float>(
    durationMillis = AnimationDurationMedium,
    easing = FastOutSlowInEasing
)

val ShortTween = tween<Float>(
    durationMillis = AnimationDurationShort,
    easing = FastOutSlowInEasing
)

val LongTween = tween<Float>(
    durationMillis = AnimationDurationLong,
    easing = FastOutSlowInEasing
)

val SpringSpec = spring<Float>(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = Spring.StiffnessLow
)

val SpringSpecBouncy = spring<Float>(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessLow
)

// Fade animation specs
val FadeInSpec = fadeIn(
    animationSpec = tween(
        durationMillis = AnimationDurationMedium,
        easing = LinearOutSlowInEasing
    )
)

val FadeOutSpec = fadeOut(
    animationSpec = tween(
        durationMillis = AnimationDurationShort,
        easing = FastOutLinearInEasing
    )
)

// Slide animation specs
val SlideInLeftSpec = slideInHorizontally(
    animationSpec = tween(AnimationDurationMedium, easing = FastOutSlowInEasing),
    initialOffsetX = { it }
)

val SlideInRightSpec = slideInHorizontally(
    animationSpec = tween(AnimationDurationMedium, easing = FastOutSlowInEasing),
    initialOffsetX = { -it }
)

val SlideOutLeftSpec = slideOutHorizontally(
    animationSpec = tween(AnimationDurationShort, easing = FastOutLinearInEasing),
    targetOffsetX = { -it }
)

val SlideOutRightSpec = slideOutHorizontally(
    animationSpec = tween(AnimationDurationShort, easing = FastOutLinearInEasing),
    targetOffsetX = { it }
)

// Scale animation specs
val ScaleInSpec = scaleIn(
    animationSpec = tween(AnimationDurationMedium, easing = FastOutSlowInEasing),
    initialScale = 0.8f
)

val ScaleOutSpec = scaleOut(
    animationSpec = tween(AnimationDurationShort, easing = FastOutLinearInEasing),
    targetScale = 0.8f
)

// Size animation specs
val ExpandVerticallySpec = expandVertically(
    animationSpec = tween(AnimationDurationMedium, easing = FastOutSlowInEasing),
    expandFrom = Alignment.Top
)

val ShrinkVerticallySpec = shrinkVertically(
    animationSpec = tween(AnimationDurationShort, easing = FastOutLinearInEasing),
    shrinkTowards = Alignment.Top
)

// Combined animation specs
val EnterTransitionSpec = fadeIn(
    animationSpec = tween(AnimationDurationMedium, easing = LinearOutSlowInEasing)
) + slideInHorizontally(
    animationSpec = tween(AnimationDurationMedium, easing = FastOutSlowInEasing)
) { it / 4 }

val ExitTransitionSpec = fadeOut(
    animationSpec = tween(AnimationDurationShort, easing = FastOutLinearInEasing)
) + slideOutHorizontally(
    animationSpec = tween(AnimationDurationShort, easing = FastOutLinearInEasing)
) { -it / 4 }
