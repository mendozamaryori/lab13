package com.example.lab_13

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_13.ui.theme.Lab_13Theme

private val BoxBlue = Color(0xFF2196F3)
private val BoxGreen = Color(0xFF4CAF50)

@Composable
fun AnimateColorExercise(modifier: Modifier = Modifier) {
    var isBlue by remember { mutableStateOf(true) }
    var useSpring by remember { mutableStateOf(true) }

    val animationSpec: AnimationSpec<Color> = if (useSpring) {
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    } else {
        tween(durationMillis = 800, easing = FastOutSlowInEasing)
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isBlue) BoxBlue else BoxGreen,
        animationSpec = animationSpec,
        label = "boxBackgroundColor"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Ejercicio 2: animateColorAsState",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedButton(onClick = { useSpring = !useSpring }) {
            Text(
                text = "Animación: ${if (useSpring) "Spring" else "Tween"} (tocar para cambiar)"
            )
        }

        Button(onClick = { isBlue = !isBlue }) {
            Text(
                text = if (isBlue) "Cambiar a verde" else "Cambiar a azul"
            )
        }

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isBlue) "Azul" else "Verde",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimateColorExercisePreview() {
    Lab_13Theme {
        AnimateColorExercise(modifier = Modifier.padding(24.dp))
    }
}
