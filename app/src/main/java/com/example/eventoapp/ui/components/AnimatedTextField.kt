package com.example.eventoapp.ui.components


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventoapp.ui.animations.rememberShakeOffset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    validator: ((String) -> String?)? = null,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    var error by remember { mutableStateOf<String?>(null) }
    var shakeTrigger by remember { mutableStateOf(0) }

    // validate on change
    LaunchedEffect(value) {
        validator?.let {
            error = it(value)
        }
    }

    // when error becomes non-null, trigger shake animation once
    LaunchedEffect(error) {
        if (error != null) {
            // increment trigger to animate
            shakeTrigger++
        }
    }

    val offsetPx = rememberShakeOffset(shakeTrigger)
    val offsetDp = with(LocalDensity.current) { offsetPx.toDp() }

    val outlineColor by animateColorAsState(
        targetValue = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
        animationSpec = tween(220)
    )

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = label,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            leadingIcon = leadingIcon,
            isError = error != null,
            modifier = Modifier
                .offset(x = offsetDp)
        )
        AnimatedVisibility(visible = error != null) {
            Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 6.dp)
            )
        }
    }
}
