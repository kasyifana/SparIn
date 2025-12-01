package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.sparin.ui.theme.*

/**
 * Achievement Reveal Animation
 * Shows burst glow, confetti drop, and toast notification
 */
@Composable
fun AchievementReveal(
    message: String = "New Achievement Unlocked!",
    show: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(show) {
        if (show) {
            visible = true
            // Auto dismiss after 3 seconds
            kotlinx.coroutines.delay(3000)
            visible = false
            onDismiss()
        }
    }
    
    if (visible && show) {
        Popup(
            onDismissRequest = { visible = false; onDismiss() },
            alignment = Alignment.Center,
            properties = PopupProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            AchievementContent(message = message)
        }
    }
}

@Composable
private fun AchievementContent(message: String) {
    // Confetti particles animation
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    
    // Burst glow animation
    val glowScale by animateFloatAsState(
        targetValue = 1.2f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "glow_scale"
    )
    
    // Toast scale animation
    val toastScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "toast_scale"
    )
    
    // Fade animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(500),
        label = "alpha"
    )
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Burst glow background
        Box(
            modifier = Modifier
                .size(300.dp)
                .scale(glowScale)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Crunch.copy(alpha = 0.6f * alpha),
                            Crunch.copy(alpha = 0.2f * alpha),
                            Crunch.copy(alpha = 0f)
                        )
                    ),
                    shape = RoundedCornerShape(50)
                )
        )
        
        // Toast notification
        Surface(
            modifier = Modifier
                .scale(toastScale)
                .alpha(alpha)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Crunch.copy(alpha = 0.5f)
                ),
            shape = RoundedCornerShape(20.dp),
            color = CascadingWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Crunch.copy(alpha = 0.2f),
                                ChineseSilver.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Confetti emoji
                Text(
                    text = "🎉",
                    fontSize = 64.sp,
                    modifier = Modifier.scale(animateFloatAsState(1.2f).value)
                )
                
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Crunch
                )
                
                Text(
                    text = "Keep up the great work! 🔥",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze
                )
            }
        }
        
        // Confetti particles (simplified as emojis)
        ConfettiParticles()
    }
}

@Composable
private fun ConfettiParticles() {
    val particles = remember { List(10) { it } }
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    
    particles.forEachIndexed { index, _ ->
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 600f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000 + index * 100,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "particle_$index"
        )
        
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1500 + index * 50,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation_$index"
        )
        
        Box(
            modifier = Modifier
                .offset(y = offsetY.dp)
                .offset(x = (index * 60 - 300).dp)
        ) {
            Text(
                text = listOf("✨", "🎊", "⭐", "💫", "🌟")[index % 5],
                fontSize = 24.sp,
                modifier = Modifier
                    .offset(x = rotation.dp)
            )
        }
    }
}

