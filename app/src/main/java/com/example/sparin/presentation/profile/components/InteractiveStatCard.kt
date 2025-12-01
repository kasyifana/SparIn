package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.sparin.presentation.profile.StatCardType
import com.example.sparin.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Interactive Stat Card with tap, long press, flip gestures
 * Following SparIN Design System
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
    isExpanded: Boolean = false,
    onTap: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onTooltipClick: () -> Unit = {},
    onDismissTooltip: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    val density = LocalDensity.current.density
    
    // Scale animation for long press
    val scaleAnimation by animateFloatAsState(
        targetValue = if (isComparisonMode) 1.03f else if (isExpanded) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale_animation"
    )
    
    // Shadow animation for expansion
    val shadowElevation by animateDpAsState(
        targetValue = if (isExpanded || isSelected) 12.dp else 6.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "shadow_elevation"
    )
    
    // Achievement glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "achievement_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    // Press animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            scale = 0.95f
            delay(150)
            scale = 1f
            isPressed = false
        }
    }

    Box(
        modifier = modifier
            .scale(scale * scaleAnimation)
            .zIndex(if (isSelected || isComparisonMode || isExpanded) 10f else 1f)
    ) {
        // Achievement glow effect
        if (showAchievement) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                accentColor.copy(alpha = glowAlpha),
                                accentColor.copy(alpha = 0f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .zIndex(0f)
            )
        }
        
        // Comparison mode overlay
        if (isComparisonMode) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Dreamland.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .zIndex(1f)
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
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
                .shadow(
                    elevation = shadowElevation,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = accentColor.copy(alpha = if (isSelected || isExpanded) 0.3f else 0.15f)
                ),
            shape = RoundedCornerShape(16.dp),
            color = NeumorphLight.copy(alpha = 0.9f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.1f),
                                accentColor.copy(alpha = 0.02f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                StatCardFront(
                    icon = icon,
                    label = label,
                    value = value,
                    accentColor = accentColor,
                    modifier = Modifier.fillMaxSize(),
                    showTooltip = showTooltip,
                    onTooltipClick = onTooltipClick,
                    onDismissTooltip = onDismissTooltip
                )
            }
        }
    }
}

@Composable
private fun StatCardFront(
    icon: String,
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    showTooltip: Boolean,
    onTooltipClick: () -> Unit,
    onDismissTooltip: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            // Tooltip icon
            IconButton(
                onClick = onTooltipClick,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Help,
                    contentDescription = "Info",
                    modifier = Modifier.size(16.dp),
                    tint = WarmHaze.copy(alpha = 0.6f)
                )
            }
        }
        
        Text(
            text = icon,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = Crunch
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = WarmHaze
        )
    }
}

