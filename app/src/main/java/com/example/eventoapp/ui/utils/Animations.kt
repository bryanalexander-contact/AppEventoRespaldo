package com.example.eventoapp.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * FadeInAnimation — envuelve cualquier contenido con aparición suave
 */
@Composable
fun FadeInAnimation(
    visible: Boolean = true,
    duration: Int = 320,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = duration)) + slideInVertically(
            animationSpec = tween(durationMillis = duration),
            initialOffsetY = { it / 8 }
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = duration))
    ) {
        content()
    }
}

/**
 * ClickScaleAnimation — composable helper para botones:
 * usage: ClickScaleAnimation(pressed) { scale -> Button(modifier=Modifier.scale(scale)) {...} }
 */
@Composable
fun ClickScaleAnimation(pressed: Boolean, content: @Composable (scale: Float) -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = tween(120)
    )
    content(scale)
}

/**
 * SlideDownAlert — para mensajes de error/éxito (aparece desde arriba)
 */
@Composable
fun SlideDownAlert(visible: Boolean, duration: Int = 300, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(duration)) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -40 }, animationSpec = tween(duration)) + fadeOut()
    ) {
        content()
    }
}

/**
 * CardAppearAnimation — animación para items en listados (con stagger)
 */
@Composable
fun CardAppearAnimation(index: Int, content: @Composable () -> Unit) {
    val delay = index * 45
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(380, delayMillis = delay)) + slideInVertically(
            initialOffsetY = { 20 },
            animationSpec = tween(380, delayMillis = delay)
        )
    ) {
        content()
    }
}

/**
 * ShakeOffset helper — retorna offset X (dp) animado para un shake.
 * Nota: se usa desde AnimatedTextField (no como extension global).
 */
@Composable
fun rememberShakeOffset(trigger: Int): Int {
    // trigger increments when we want to shake
    val anim = remember { Animatable(0f) }
    LaunchedEffect(trigger) {
        if (trigger <= 0) return@LaunchedEffect
        anim.snapTo(0f)
        anim.animateTo(
            targetValue = 1f,
            animationSpec = keyframes {
                durationMillis = 360
                // keyframes that simulate a shake
                0.0f at 0
                -8f at 40
                8f at 80
                -6f at 120
                6f at 160
                -3f at 200
                3f at 240
                0f at 300
            }
        )
        anim.snapTo(0f)
    }
    // convert to dp-int for Modifier offset
    val px = with(LocalDensity.current) { (anim.value).dp.toPx() } // value is dp already in keyframes
    return px.roundToInt()
}
