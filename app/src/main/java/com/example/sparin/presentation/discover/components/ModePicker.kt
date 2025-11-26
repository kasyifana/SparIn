package com.example.sparin.presentation.discover.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*

/**
 * Mode Picker Component
 * Elegant segmented control for Casual / Competitive mode selection
 */

enum class MatchMode {
    CASUAL, COMPETITIVE
}

@Composable
fun ModePicker(
    selectedMode: MatchMode,
    onModeSelected: (MatchMode) -> Unit,
    modifier: Modifier = Modifier,
    showTitle: Boolean = true,
    title: String = "Match Mode"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        if (showTitle) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                color = Lead
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Mode toggle capsule
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = NeumorphDark.copy(alpha = 0.12f),
                    spotColor = NeumorphDark.copy(alpha = 0.12f)
                ),
            shape = RoundedCornerShape(28.dp),
            color = NeumorphLight
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = Dreamland.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Casual Mode
                    ModeButtonItem(
                        text = "CASUAL",
                        emoji = "🎮",
                        isSelected = selectedMode == MatchMode.CASUAL,
                        onClick = { onModeSelected(MatchMode.CASUAL) },
                        modifier = Modifier.weight(1f)
                    )

                    // Competitive Mode
                    ModeButtonItem(
                        text = "COMPETITIVE",
                        emoji = "🏆",
                        isSelected = selectedMode == MatchMode.COMPETITIVE,
                        onClick = { onModeSelected(MatchMode.COMPETITIVE) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeButtonItem(
    text: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else Color.Transparent,
        animationSpec = tween(300, easing = EaseInOutCubic),
        label = "mode_bg"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "mode_elevation"
    )

    Box(modifier = modifier) {
        // Glow effect for active state
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(
                                Crunch.copy(alpha = 0.3f),
                                Crunch.copy(alpha = 0f)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .blur(8.dp)
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = if (isSelected) Crunch.copy(alpha = 0.3f) else Color.Transparent,
                    spotColor = if (isSelected) Crunch.copy(alpha = 0.3f) else Color.Transparent
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            shape = RoundedCornerShape(24.dp),
            color = backgroundColor
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = emoji,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 1.sp,
                        fontSize = 12.sp
                    ),
                    color = if (isSelected) Lead else WarmHaze
                )
            }
        }
    }
}

/**
 * Compact Mode Picker - A smaller version for inline use
 */
@Composable
fun CompactModePicker(
    selectedMode: MatchMode,
    onModeSelected: (MatchMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(44.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(22.dp),
        color = NeumorphLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .border(
                    width = 1.dp,
                    color = Dreamland.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(3.dp)
        ) {
            CompactModeButton(
                text = "Casual",
                isSelected = selectedMode == MatchMode.CASUAL,
                onClick = { onModeSelected(MatchMode.CASUAL) }
            )
            CompactModeButton(
                text = "Competitive",
                isSelected = selectedMode == MatchMode.COMPETITIVE,
                onClick = { onModeSelected(MatchMode.COMPETITIVE) }
            )
        }
    }
}

@Composable
private fun CompactModeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else Color.Transparent,
        animationSpec = tween(250),
        label = "compact_bg"
    )

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(19.dp),
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected) Lead else WarmHaze
            )
        }
    }
}
