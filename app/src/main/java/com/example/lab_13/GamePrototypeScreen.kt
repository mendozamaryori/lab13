package com.example.lab_13

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab_13.ui.theme.Lab_13Theme
import kotlin.random.Random

@Composable
private fun EnemySprite(visible: Boolean, offsetX: Dp) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) + scaleIn(initialScale = 0.3f, animationSpec = tween(400)),
        exit = fadeOut(tween(300)) + scaleOut(targetScale = 0.2f, animationSpec = tween(300)),
        modifier = Modifier.offset(x = offsetX)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(EnemyColor),
            contentAlignment = Alignment.Center
        ) {
            Text("👾", fontSize = 24.sp)
        }
    }
}

@Composable
private fun HeroSprite(
    laneOffset: Dp,
    heroSize: Dp,
    heroColor: Color,
    poweredUp: Boolean
) {
    Box(
        modifier = Modifier
            .offset(x = laneOffset)
            .size(heroSize)
            .clip(CircleShape)
            .background(heroColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (poweredUp) "⚡" else "🧙",
            fontSize = 22.sp
        )
    }
}

@Composable
private fun PowerSparkle(visible: Boolean, laneOffset: Dp) {
    Box(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(300)),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = laneOffset + 30.dp, y = (-20).dp)
        ) {
            Text("✨", fontSize = 20.sp)
        }
    }
}

private enum class GamePhase {
    Menu,
    Playing,
    Victory,
    Defeat
}

private val HeroHealthy = Color(0xFF00E676)
private val HeroWarn = Color(0xFFFFD600)
private val HeroCritical = Color(0xFFFF5252)
private val HeroPowered = Color(0xFFEA80FC)
private val ArenaDark = Color(0xFF0D1B2A)
private val ArenaMid = Color(0xFF1B263B)
private val EnemyColor = Color(0xFFFF6B6B)
private val CrystalColor = Color(0xFF4FC3F7)

@Composable
fun GamePrototypeScreen(modifier: Modifier = Modifier) {
    var phase by remember { mutableStateOf(GamePhase.Menu) }
    var playerLane by remember { mutableIntStateOf(1) }
    var enemyLane by remember { mutableIntStateOf(0) }
    var enemyVisible by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var hp by remember { mutableIntStateOf(100) }
    var poweredUp by remember { mutableStateOf(false) }
    var isAttacking by remember { mutableStateOf(false) }
    var showBossWarning by remember { mutableStateOf(false) }
    var wave by remember { mutableIntStateOf(1) }

    val winScore = 5

    fun resetGame() {
        playerLane = 1
        enemyLane = Random.nextInt(3)
        enemyVisible = false
        score = 0
        hp = 100
        poweredUp = false
        isAttacking = false
        showBossWarning = false
        wave = 1
        phase = GamePhase.Playing
    }

    fun spawnEnemy() {
        showBossWarning = wave % 3 == 0
        enemyLane = Random.nextInt(3)
        enemyVisible = true
        wave++
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Ejercicio Final — Crystal Quest",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        AnimatedContent(
            targetState = phase,
            transitionSpec = {
                (fadeIn(tween(500)) + scaleIn(initialScale = 0.92f, animationSpec = tween(500)))
                    .togetherWith(fadeOut(tween(250)) + scaleOut(targetScale = 1.05f, animationSpec = tween(250)))
            },
            modifier = Modifier.fillMaxWidth(),
            label = "gamePhase"
        ) { currentPhase ->
            when (currentPhase) {
                GamePhase.Menu -> MenuPanel(onStart = { resetGame() })

                GamePhase.Playing -> PlayingPanel(
                    playerLane = playerLane,
                    enemyLane = enemyLane,
                    enemyVisible = enemyVisible,
                    score = score,
                    hp = hp,
                    poweredUp = poweredUp,
                    isAttacking = isAttacking,
                    showBossWarning = showBossWarning,
                    onMoveLeft = { if (playerLane > 0) playerLane-- },
                    onMoveRight = { if (playerLane < 2) playerLane++ },
                    onTogglePower = { poweredUp = !poweredUp },
                    onSpawn = { spawnEnemy() },
                    onAttack = {
                        if (!enemyVisible) return@PlayingPanel
                        isAttacking = true
                        if (playerLane == enemyLane) {
                            score += if (poweredUp) 2 else 1
                            enemyVisible = false
                            showBossWarning = false
                            if (score >= winScore) phase = GamePhase.Victory
                        } else {
                            hp = (hp - 20).coerceAtLeast(0)
                            if (hp <= 0) phase = GamePhase.Defeat
                        }
                        isAttacking = false
                    }
                )

                GamePhase.Victory -> EndPanel(
                    title = "¡Victoria!",
                    emoji = "🏆",
                    message = "Recogiste $score cristales.\n¡Eres un héroe legendario!",
                    onRetry = { phase = GamePhase.Menu }
                )

                GamePhase.Defeat -> EndPanel(
                    title = "Game Over",
                    emoji = "💀",
                    message = "Los enemigos vencieron...\nPuntuación: $score cristales",
                    onRetry = { phase = GamePhase.Menu }
                )
            }
        }
    }
}

@Composable
private fun MenuPanel(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(listOf(ArenaMid, ArenaDark))
            )
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("💎", fontSize = 64.sp)
        Text(
            text = "Crystal Quest",
            style = MaterialTheme.typography.headlineMedium,
            color = CrystalColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Muévete entre carriles, derrota enemigos\ny recolecta cristales mágicos.",
            textAlign = TextAlign.Center,
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = CrystalColor)
        ) {
            Text("▶  Jugar", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PlayingPanel(
    playerLane: Int,
    enemyLane: Int,
    enemyVisible: Boolean,
    score: Int,
    hp: Int,
    poweredUp: Boolean,
    isAttacking: Boolean,
    showBossWarning: Boolean,
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onTogglePower: () -> Unit,
    onSpawn: () -> Unit,
    onAttack: () -> Unit
) {
    val heroColor by animateColorAsState(
        targetValue = when {
            poweredUp -> HeroPowered
            hp <= 30 -> HeroCritical
            hp <= 60 -> HeroWarn
            else -> HeroHealthy
        },
        animationSpec = tween(400),
        label = "heroColor"
    )

    val laneOffset by animateDpAsState(
        targetValue = (playerLane * 90).dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "playerLane"
    )

    val heroSize by animateDpAsState(
        targetValue = when {
            isAttacking -> 56.dp
            poweredUp -> 52.dp
            else -> 44.dp
        },
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "heroSize"
    )

    val enemyOffset by animateDpAsState(
        targetValue = (enemyLane * 90).dp,
        animationSpec = tween(350),
        label = "enemyLane"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("💎 $score", color = CrystalColor, fontWeight = FontWeight.Bold)
            Text("❤ $hp", color = heroColor, fontWeight = FontWeight.Bold)
        }

        LinearProgressIndicator(
            progress = { hp / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = heroColor,
            trackColor = Color.White.copy(alpha = 0.15f)
        )

        AnimatedVisibility(
            visible = showBossWarning,
            enter = fadeIn(tween(300)) + scaleIn(initialScale = 0.5f),
            exit = fadeOut(tween(200)) + scaleOut()
        ) {
            Text(
                text = "⚠️  ¡OLEADA DE JEFE!",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFAB00).copy(alpha = 0.25f))
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = Color(0xFFFFAB00),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.verticalGradient(listOf(ArenaMid, ArenaDark)))
                .border(1.dp, CrystalColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    EnemySprite(visible = enemyVisible, offsetX = enemyOffset)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    HeroSprite(
                        laneOffset = laneOffset,
                        heroSize = heroSize,
                        heroColor = heroColor,
                        poweredUp = poweredUp
                    )
                    PowerSparkle(visible = poweredUp, laneOffset = laneOffset)
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(3) { Text("│", color = Color.White.copy(alpha = 0.15f)) }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onMoveLeft, modifier = Modifier.weight(1f)) {
                Text("◀")
            }
            Button(
                onClick = onAttack,
                modifier = Modifier.weight(1.2f),
                colors = ButtonDefaults.buttonColors(containerColor = EnemyColor)
            ) {
                Text("⚔", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(onClick = onMoveRight, modifier = Modifier.weight(1f)) {
                Text("▶")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onSpawn, modifier = Modifier.weight(1f)) {
                Text("Ola 👾")
            }
            Button(
                onClick = onTogglePower,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (poweredUp) HeroPowered else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(if (poweredUp) "⚡ ON" else "⚡ Power")
            }
        }
    }
}

@Composable
private fun EndPanel(
    title: String,
    emoji: String,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(listOf(ArenaMid, ArenaDark)))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(emoji, fontSize = 72.sp)
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.bodyLarge
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CrystalColor)
        ) {
            Text("Volver al menú")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1B2A)
@Composable
fun GamePrototypeScreenPreview() {
    Lab_13Theme {
        GamePrototypeScreen()
    }
}
