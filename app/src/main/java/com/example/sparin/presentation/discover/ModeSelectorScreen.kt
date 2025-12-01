package com.example.sparin.presentation.discover

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Mode Selector Screen - Choose between Casual and Competitive
 * Ultra-aesthetic Gen-Z design with premium gradients and animations
 */
@Composable
fun ModeSelectorScreen(
    navController: NavHostController,
    onModeSelected: (RoomMode) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CascadingWhite,
                        SoftLavender.copy(alpha = 0.2f),
                        CascadingWhite
                    )
                )
            )
    ) {
        // Animated background blobs
        ModeSelectorBackgroundBlobs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(bottom = 100.dp), // Add bottom padding to avoid navbar
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Floating decorative elements
            FloatingModeIcons()

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = "Choose Your\nMatch Style",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 38.sp,
                    letterSpacing = (-1).sp
                ),
                color = Lead,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Casual for fun · Competitive for serious challenges",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp
                ),
                color = WarmHaze,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Casual Card
            ModeSelectionCard(
                mode = RoomMode.CASUAL,
                title = "CASUAL",
                subtitle = "Play for fun, no pressure",
                emoji = "🎮",
                gradient = listOf(
                    Color(0xFF74C9FF).copy(alpha = 0.3f),
                    Color(0xFF5BA8E0).copy(alpha = 0.25f),
                    Color(0xFFA78BFA).copy(alpha = 0.2f)
                ),
                accentColor = Color(0xFF74C9FF),
                onClick = { onModeSelected(RoomMode.CASUAL) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Competitive Card
            ModeSelectionCard(
                mode = RoomMode.COMPETITIVE,
                title = "COMPETITIVE",
                subtitle = "Serious matches, track your wins",
                emoji = "🏆",
                gradient = listOf(
                    Color(0xFFEF4444).copy(alpha = 0.25f),
                    Color(0xFFDC2626).copy(alpha = 0.2f),
                    Color(0xFFB91C1C).copy(alpha = 0.15f)
                ),
                accentColor = Color(0xFFEF4444),
                onClick = { onModeSelected(RoomMode.COMPETITIVE) },
                isPreview = false
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Helper text
            Text(
                text = "You can switch modes anytime",
                style = MaterialTheme.typography.bodySmall,
                color = WarmHaze.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==================== MODE SELECTION CARD ====================

@Composable
private fun ModeSelectionCard(
    mode: RoomMode,
    title: String,
    subtitle: String,
    emoji: String,
    gradient: List<Color>,
    accentColor: Color,
    onClick: () -> Unit,
    isPreview: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mode_card_$title")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        // Animated glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = floatOffset.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            accentColor.copy(alpha = glowAlpha * 0.4f),
                            accentColor.copy(alpha = 0f)
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .blur(20.dp)
        )

        // Main card
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = floatOffset.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = accentColor.copy(alpha = 0.3f),
                    spotColor = accentColor.copy(alpha = 0.3f)
                ),
            shape = RoundedCornerShape(32.dp),
            color = Color.Transparent,
            enabled = !isPreview
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = gradient + listOf(NeumorphLight.copy(alpha = 0.9f))
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(32.dp)
                    )
            ) {
                // Floating sparkles
                FloatingSparkles(accentColor)

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji icon
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = CircleShape,
                                ambientColor = accentColor.copy(alpha = 0.4f)
                            )
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.3f),
                                        accentColor.copy(alpha = 0.1f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Text content
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                letterSpacing = 0.5.sp
                            ),
                            color = Lead
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmHaze
                        )

                        if (isPreview) {
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFDC143C).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "Coming Soon",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Color(0xFFDC143C)
                                )
                            }
                        }
                    }

                    // Arrow icon
                    if (!isPreview) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = accentColor
                        )
                    }
                }
            }
        }
    }
}

// ==================== FLOATING SPARKLES ====================

@Composable
private fun BoxScope.FloatingSparkles(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "sparkles")

    val sparkle1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle1"
    )

    val sparkle2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle2"
    )

    // Sparkle 1
    Box(
        modifier = Modifier
            .size(12.dp)
            .align(Alignment.TopEnd)
            .offset(x = (-30).dp, y = (20 + sparkle1).dp)
            .background(
                color = color.copy(alpha = 0.6f),
                shape = CircleShape
            )
            .blur(4.dp)
    )

    // Sparkle 2
    Box(
        modifier = Modifier
            .size(8.dp)
            .align(Alignment.BottomStart)
            .offset(x = 40.dp, y = (-25 + sparkle2).dp)
            .background(
                color = color.copy(alpha = 0.5f),
                shape = CircleShape
            )
            .blur(3.dp)
    )
}

// ==================== FLOATING MODE ICONS ====================

@Composable
private fun FloatingModeIcons() {
    val infiniteTransition = rememberInfiniteTransition(label = "mode_icons")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        // Icon 1 - Blue themed
        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.TopStart)
                .offset(x = 40.dp, y = float1.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = Color(0xFF74C9FF).copy(alpha = 0.4f)
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF74C9FF).copy(alpha = 0.6f),
                            Color(0xFF74C9FF).copy(alpha = 0.2f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("⭐", fontSize = 24.sp)
        }

        // Icon 2 - Red themed
        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-50).dp, y = float2.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape,
                    ambientColor = Color(0xFFEF4444).copy(alpha = 0.4f)
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFEF4444).copy(alpha = 0.6f),
                            Color(0xFFEF4444).copy(alpha = 0.2f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("🔥", fontSize = 18.sp)
        }
    }
}

// ==================== BACKGROUND BLOBS ====================

@Composable
private fun ModeSelectorBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "mode_selector_blobs")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(80.dp)
    ) {
        // Blue blob for Casual
        drawCircle(
            color = Color(0xFF74C9FF).copy(alpha = 0.2f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.2f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 40f,
                y = size.height * 0.15f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )

        // Red blob for Competitive
        drawCircle(
            color = Color(0xFFEF4444).copy(alpha = 0.15f),
            radius = 160f,
            center = Offset(
                x = size.width * 0.8f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 35f,
                y = size.height * 0.25f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 40f
            )
        )

        // Light blue accent blob
        drawCircle(
            color = Color(0xFFA78BFA).copy(alpha = 0.15f),
            radius = 140f,
            center = Offset(
                x = size.width * 0.5f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f,
                y = size.height * 0.7f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 35f
            )
        )
    }
}
