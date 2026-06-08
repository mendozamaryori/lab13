package com.example.lab_13

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_13.ui.theme.Lab_13Theme

private enum class ContentState {
    Loading,
    Content,
    Error
}

@Composable
fun AnimatedContentExercise(modifier: Modifier = Modifier) {
    var state by remember { mutableStateOf(ContentState.Loading) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Ejercicio 4: AnimatedContent",
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { state = ContentState.Loading }) {
                Text("Cargando")
            }
            Button(onClick = { state = ContentState.Content }) {
                Text("Contenido")
            }
            OutlinedButton(onClick = { state = ContentState.Error }) {
                Text("Error")
            }
        }

        AnimatedContent(
            targetState = state,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 600)) togetherWith
                    fadeOut(animationSpec = tween(durationMillis = 300))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            label = "contentStateTransition"
        ) { targetState ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (targetState) {
                    ContentState.Loading -> LoadingMessage()
                    ContentState.Content -> SuccessMessage()
                    ContentState.Error -> ErrorMessage()
                }
            }
        }
    }
}

@Composable
private fun LoadingMessage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Cargando datos...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SuccessMessage() {
    Text(
        text = "¡Contenido cargado correctamente!",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
private fun ErrorMessage() {
    Text(
        text = "Error al cargar.\nInténtalo de nuevo más tarde.",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun AnimatedContentExercisePreview() {
    Lab_13Theme {
        AnimatedContentExercise(modifier = Modifier.padding(24.dp))
    }
}
