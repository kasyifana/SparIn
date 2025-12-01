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
                        SoftLavender.copy(alpha = 0.12f),
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
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Decorative badge
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(12.dp, CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF74C9FF).copy(alpha = 0.25f),
                                    Color(0xFFEF4444).copy(alpha = 0.25f)
                                )
                            ),
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF74C9FF).copy(alpha = 0.4f),
                                    Color(0xFFEF4444).copy(alpha = 0.4f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎯", fontSize = 26.sp)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Lead
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Choose your match style",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Cards section - Compact fixed height
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Casual Card
                CompactModeCard(
                    title = "CASUAL",
                    subtitle = "Play for fun, no pressure",
                    emoji = "🎮",
                    accentColor = Color(0xFF74C9FF),
                    onClick = { onModeSelected(RoomMode.CASUAL) }
                )

                // Divider with "OR" text
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
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
                                        WarmHaze.copy(alpha = 0.25f)
                                    )
                                )
                            )
                    )
                    
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .size(32.dp)
                            .background(
                                color = CascadingWhite,
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = WarmHaze.copy(alpha = 0.15f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "OR",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp
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
                                        WarmHaze.copy(alpha = 0.25f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                // Competitive Card
                CompactModeCard(
                    title = "COMPETITIVE",
                    subtitle = "Serious matches, track wins",
                    emoji = "🏆",
                    accentColor = Color(0xFFEF4444),
                    onClick = { onModeSelected(RoomMode.COMPETITIVE) }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom helper text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.SwapHoriz,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = WarmHaze.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Switch modes anytime",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = WarmHaze.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// ==================== COMPACT MODE CARD ====================

@Composable
private fun CompactModeCard(
    title: String,
    subtitle: String,
    emoji: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = accentColor.copy(alpha = 0.2f),
                spotColor = accentColor.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.08f),
                            Color.White
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.3f),
                            accentColor.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Emoji icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.2f),
                                    accentColor.copy(alpha = 0.08f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = accentColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Text content
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = Lead
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 13.sp
                        ),
                        color = WarmHaze
                    )
                }

                // Arrow icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = accentColor.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = accentColor
                    )
                }
            }
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
