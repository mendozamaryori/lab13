package com.example.lab_13

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lab_13.ui.theme.Lab_13Theme

@Composable
fun AnimateSizePositionExercise(modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }
    var offsetBeforeSize by remember { mutableStateOf(false) }

    val animationSpec = spring<Dp>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    val boxSize by animateDpAsState(
        targetValue = if (isExpanded) 120.dp else 60.dp,
        animationSpec = animationSpec,
        label = "boxSize"
    )
    val offsetX by animateDpAsState(
        targetValue = if (isExpanded) 100.dp else 0.dp,
        animationSpec = animationSpec,
        label = "offsetX"
    )
    val offsetY by animateDpAsState(
        targetValue = if (isExpanded) 50.dp else 0.dp,
        animationSpec = animationSpec,
        label = "offsetY"
    )

    val boxColor = MaterialTheme.colorScheme.tertiary
    val boxShape = RoundedCornerShape(12.dp)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Ejercicio 3: Tamaño y posición",
            style = MaterialTheme.typography.titleMedium
        )

        Button(onClick = { isExpanded = !isExpanded }) {
            Text(
                text = if (isExpanded) "Posición y tamaño inicial" else "Mover y agrandar"
            )
        }

        OutlinedButton(onClick = { offsetBeforeSize = !offsetBeforeSize }) {
            Text(
                text = if (offsetBeforeSize) {
                    "Orden: offset → size"
                } else {
                    "Orden: size → offset"
                }
            )
        }

        Text(
            text = if (offsetBeforeSize) {
                "El desplazamiento se aplica antes del tamaño: el espacio reservado en el layout puede diferir."
            } else {
                "El tamaño se fija primero y luego se desplaza: el layout reserva el tamaño animado y el cuadro se mueve visualmente."
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            val boxModifier = if (offsetBeforeSize) {
                Modifier
                    .offset(x = offsetX, y = offsetY)
                    .size(boxSize)
                    .clip(boxShape)
                    .background(boxColor)
            } else {
                Modifier
                    .size(boxSize)
                    .offset(x = offsetX, y = offsetY)
                    .clip(boxShape)
                    .background(boxColor)
            }

            Box(
                modifier = boxModifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${boxSize.value.toInt()}dp",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimateSizePositionExercisePreview() {
    Lab_13Theme {
        AnimateSizePositionExercise(modifier = Modifier.padding(24.dp))
    }
}
