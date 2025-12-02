package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.sparin.presentation.profile.StatCardType
import com.example.sparin.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * Premium Interactive Stat Card with Gen-Z Modern Sporty Aesthetic
 * Inspired by Community Screen design with glassmorphism and vibrant animations
 */
@Composable
fun InteractiveStatCard(
    type: StatCardType,
    icon: String,
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isComparisonMode: Boolean = false,
    showTooltip: Boolean = false,
    showAchievement: Boolean = false,
    onTap: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onTooltipClick: () -> Unit = {},
    onDismissTooltip: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }
    
    // Scale animation for interactions
    val scaleAnimation by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.94f
            isSelected -> 1.03f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale_animation"
    )
    
    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "card_animations")
    
    // Floating animation
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_y"
    )
    
    // Border pulse animation
    val borderPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_pulse"
    )
    
    // Shimmer animation
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -400f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )
    
    // Gradient rotation
    val gradientRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient_rotation"
    )
    
    // Press animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(120)
            isPressed = false
        }
    }

    Box(
        modifier = modifier
            .scale(scaleAnimation)
            .offset(y = if (isSelected) floatY.dp else 0.dp)
            .zIndex(if (isSelected || isComparisonMode) 10f else 1f)
    ) {
        // Animated background blobs
        AnimatedBackgroundBlobs(
            accentColor = accentColor,
            rotation = gradientRotation,
            isVisible = isSelected
        )
        
        // Main card with glassmorphism
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onTap()
                            isPressed = true
                        },
                        onLongPress = {
                            onLongPress()
                        }
                    )
                }
        ) {
            // Gradient background layer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.35f),
                                accentColor.copy(alpha = 0.2f),
                                accentColor.copy(alpha = 0.1f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
            )
            
            // Glassmorphism frosted layer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.25f),
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    )
            )
            
            // Shimmer effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.2f),
                                Color.Transparent
                            ),
                            start = Offset(shimmerOffset - 200f, shimmerOffset - 200f),
                            end = Offset(shimmerOffset, shimmerOffset)
                        )
                    )
            )
            
            // Animated border
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                accentColor.copy(alpha = if (isSelected) borderPulse * 0.8f else 0.4f),
                                accentColor.copy(alpha = if (isSelected) borderPulse * 0.5f else 0.2f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
            )
            
            // Inner shadow/depth
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                NeumorphDark.copy(alpha = 0.03f)
                            )
                        )
                    )
            )
            
            // Content
            StatCardContent(
                icon = icon,
                label = label,
                value = value,
                accentColor = accentColor,
                isSelected = isSelected,
                floatOffset = floatY
            )
        }
        
        // Achievement glow effect
        if (showAchievement) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.4f),
                                accentColor.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .blur(16.dp)
            )
        }
    }
}

@Composable
private fun AnimatedBackgroundBlobs(
    accentColor: Color,
    rotation: Float,
    isVisible: Boolean
) {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 0.6f else 0.3f,
        animationSpec = tween(500),
        label = "blob_alpha"
    )
    
    if (alpha > 0.1f) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(30.dp)
                .alpha(alpha)
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = 40f
            
            // Blob 1 - top right
            drawCircle(
                color = accentColor.copy(alpha = 0.3f),
                radius = radius,
                center = Offset(
                    x = centerX + cos(Math.toRadians(rotation.toDouble())).toFloat() * 50f,
                    y = centerY - radius + sin(Math.toRadians(rotation.toDouble())).toFloat() * 30f
                )
            )
            
            // Blob 2 - bottom left
            drawCircle(
                color = accentColor.copy(alpha = 0.25f),
                radius = radius * 0.8f,
                center = Offset(
                    x = centerX - cos(Math.toRadians(rotation.toDouble() + 180)).toFloat() * 40f,
                    y = centerY + radius - sin(Math.toRadians(rotation.toDouble() + 180)).toFloat() * 25f
                )
            )
        }
    }
}

@Composable
private fun StatCardContent(
    icon: String,
    label: String,
    value: String,
    accentColor: Color,
    isSelected: Boolean,
    floatOffset: Float
) {
    // Cool stat names mapping
    val coolLabel = when (label.lowercase()) {
        "winrate" -> "WIN RATE"
        "total matches" -> "BATTLES"
        "total wins" -> "VICTORIES"
        "rank" -> "TIER"
        "elo rating" -> "RATING"
        else -> label.uppercase()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Text label at top
        Text(
            text = coolLabel,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                letterSpacing = 1.8.sp
            ),
            color = Lead.copy(alpha = 0.65f)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Floating icon with decorative ring
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.offset(y = floatOffset.dp)
        ) {
            // Outer decorative ring
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                accentColor.copy(alpha = if (isSelected) 0.35f else 0.18f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
            
            // Icon container with gradient background
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.25f),
                                Color.White.copy(alpha = 0.08f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(
                        width = 1.5.dp,
                        color = accentColor.copy(alpha = if (isSelected) 0.5f else 0.25f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 26.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Value with premium typography and better contrast
        Text(
            text = value,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                letterSpacing = (-0.8).sp
            ),
            color = if (isSelected) accentColor else NeumorphDark
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Decorative dots row
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Decorative dot
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(
                        color = accentColor.copy(alpha = if (isSelected) 0.8f else 0.5f),
                        shape = CircleShape
                    )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Pulsing center dot (bigger)
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .background(
                        color = if (isSelected) accentColor else WarmHaze.copy(alpha = 0.6f),
                        shape = CircleShape
                    )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Decorative dot
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(
                        color = accentColor.copy(alpha = if (isSelected) 0.8f else 0.5f),
                        shape = CircleShape
                    )
            )
        }
    }
}
