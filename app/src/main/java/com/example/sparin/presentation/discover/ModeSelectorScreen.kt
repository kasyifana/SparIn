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
                        SoftLavender.copy(alpha = 0.15f),
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
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Compact header section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Discover",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        color = Lead
                    )
                    Text(
                        text = "Choose your match style",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze
                    )
                }
                
                // Floating decorative badge
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(8.dp, CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF74C9FF).copy(alpha = 0.3f),
                                    Color(0xFFEF4444).copy(alpha = 0.3f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎯", fontSize = 22.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cards take remaining space
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Casual Card - Takes more space
                ModeSelectionCard(
                    mode = RoomMode.CASUAL,
                    title = "CASUAL",
                    subtitle = "Play for fun, no pressure\nJust enjoy the game! 🎉",
                    emoji = "🎮",
                    gradient = listOf(
                        Color(0xFF74C9FF).copy(alpha = 0.35f),
                        Color(0xFF5BA8E0).copy(alpha = 0.3f),
                        Color(0xFFA78BFA).copy(alpha = 0.25f)
                    ),
                    accentColor = Color(0xFF74C9FF),
                    onClick = { onModeSelected(RoomMode.CASUAL) },
                    modifier = Modifier.weight(1f)
                )

                // Divider with "OR" text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        WarmHaze.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )
                    
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .size(40.dp)
                            .background(
                                color = CascadingWhite,
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = WarmHaze.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "OR",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = WarmHaze
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        WarmHaze.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                // Competitive Card - Takes more space
                ModeSelectionCard(
                    mode = RoomMode.COMPETITIVE,
                    title = "COMPETITIVE",
                    subtitle = "Serious matches, track wins\nClimb the leaderboard! 🔥",
                    emoji = "🏆",
                    gradient = listOf(
                        Color(0xFFEF4444).copy(alpha = 0.3f),
                        Color(0xFFDC2626).copy(alpha = 0.25f),
                        Color(0xFFB91C1C).copy(alpha = 0.2f)
                    ),
                    accentColor = Color(0xFFEF4444),
                    onClick = { onModeSelected(RoomMode.COMPETITIVE) },
                    isPreview = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom helper text with icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.SwapHoriz,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = WarmHaze.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Switch modes anytime",
                    style = MaterialTheme.typography.bodySmall,
                    color = WarmHaze.copy(alpha = 0.6f)
                )
            }
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
    modifier: Modifier = Modifier,
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
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
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
                    shape = RoundedCornerShape(28.dp)
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
                    elevation = 20.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = accentColor.copy(alpha = 0.35f),
                    spotColor = accentColor.copy(alpha = 0.35f)
                ),
            shape = RoundedCornerShape(28.dp),
            color = Color.Transparent,
            enabled = !isPreview
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = gradient + listOf(NeumorphLight.copy(alpha = 0.95f))
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.6f),
                                accentColor.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
            ) {
                // Floating sparkles
                FloatingSparkles(accentColor)

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji icon - bigger
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .shadow(
                                elevation = 16.dp,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = accentColor.copy(alpha = 0.5f)
                            )
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.4f),
                                        accentColor.copy(alpha = 0.15f)
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = accentColor.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(24.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 40.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Text content
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                letterSpacing = 1.sp
                            ),
                            color = Lead
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 20.sp
                            ),
                            color = WarmHaze
                        )

                        if (isPreview) {
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFDC143C).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "Coming Soon",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Color(0xFFDC143C)
                                )
                            }
                        }
                    }

                    // Arrow icon - styled
                    if (!isPreview) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    color = accentColor.copy(alpha = 0.15f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = accentColor
                            )
                        }
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
