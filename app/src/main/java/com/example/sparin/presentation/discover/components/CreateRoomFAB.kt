package com.example.sparin.presentation.discover.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*

/**
 * Create Room FAB Component
 * Glowing floating action button with premium animations
 */

@Composable
fun CreateRoomFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fab_glow")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fab_glow_alpha"
    )

    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fab_float"
    )

    Box(
        modifier = modifier.offset(y = floatY.dp)
    ) {
        // Outer glow effect
        Box(
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.Center)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Crunch.copy(alpha = glowAlpha),
                            Crunch.copy(alpha = glowAlpha * 0.5f),
                            Crunch.copy(alpha = 0f)
                        )
                    ),
                    shape = CircleShape
                )
                .blur(16.dp)
        )

        // FAB Button
        Surface(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.Center)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = Crunch.copy(alpha = 0.5f),
                    spotColor = Crunch.copy(alpha = 0.5f)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            shape = CircleShape,
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Crunch,
                                SunsetOrange
                            ),
                            start = Offset.Zero,
                            end = Offset(100f, 100f)
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                NeumorphLight.copy(alpha = 0.5f),
                                Crunch.copy(alpha = 0.3f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create Room",
                    modifier = Modifier.size(28.dp),
                    tint = Lead
                )
            }
        }
    }
}

/**
 * Extended FAB with text - For larger screens or alternate layouts
 */
@Composable
fun ExtendedCreateRoomFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Create Room"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "extended_fab")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ext_fab_glow"
    )

    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ext_fab_float"
    )

    Box(
        modifier = modifier.offset(y = floatY.dp)
    ) {
        // Glow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 4.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Crunch.copy(alpha = glowAlpha),
                            Crunch.copy(alpha = 0f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .blur(20.dp)
        )

        Surface(
            modifier = Modifier
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Crunch.copy(alpha = 0.4f),
                    spotColor = Crunch.copy(alpha = 0.4f)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            shape = RoundedCornerShape(28.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Crunch,
                                SunsetOrange
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                NeumorphLight.copy(alpha = 0.4f),
                                Crunch.copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = Lead
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = Lead
                    )
                }
            }
        }
    }
}

/**
 * Mini FAB - Smaller version for tight spaces
 */
@Composable
fun MiniCreateRoomFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mini_fab")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mini_fab_glow"
    )

    Box(modifier = modifier) {
        // Subtle glow
        Box(
            modifier = Modifier
                .size(52.dp)
                .align(Alignment.Center)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Crunch.copy(alpha = glowAlpha),
                            Crunch.copy(alpha = 0f)
                        )
                    ),
                    shape = CircleShape
                )
                .blur(10.dp)
        )

        Surface(
            modifier = Modifier
                .size(44.dp)
                .align(Alignment.Center)
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    ambientColor = Crunch.copy(alpha = 0.4f)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            shape = CircleShape,
            color = Crunch
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create Room",
                    modifier = Modifier.size(22.dp),
                    tint = Lead
                )
            }
        }
    }
}

/**
 * Pulsing Create Room FAB - More attention-grabbing version
 */
@Composable
fun PulsingCreateRoomFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulsing_fab")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseOut),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fab_float"
    )

    Box(
        modifier = modifier.offset(y = floatY.dp),
        contentAlignment = Alignment.Center
    ) {
        // Pulse ring
        Box(
            modifier = Modifier
                .size((60 * pulseScale).dp)
                .background(
                    color = Crunch.copy(alpha = pulseAlpha),
                    shape = CircleShape
                )
        )

        // Main FAB
        Surface(
            modifier = Modifier
                .size(60.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = CircleShape,
                    ambientColor = Crunch.copy(alpha = 0.5f)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            shape = CircleShape,
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Crunch, SunsetOrange)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create Room",
                    modifier = Modifier.size(28.dp),
                    tint = Lead
                )
            }
        }
    }
}
