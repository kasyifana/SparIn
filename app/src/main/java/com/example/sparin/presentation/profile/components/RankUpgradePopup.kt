package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.graphics.StrokeCap
import com.example.sparin.ui.theme.*

/**
 * Rank Upgrade Info Popup
 * Shows current rank, XP to next rank, and improvement tips
 */
@Composable
fun RankUpgradePopup(
    currentRank: String,
    nextRank: String,
    xpProgress: Float, // 0.0 to 1.0
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "popup_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Popup(
        onDismissRequest = onDismiss,
        alignment = Alignment.Center,
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        // Glow background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ChineseSilver.copy(alpha = glowAlpha * 0.5f),
                            ChineseSilver.copy(alpha = 0f)
                        )
                    )
                )
        )
        
        Surface(
            modifier = modifier
                .widthIn(max = 320.dp)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Crunch.copy(alpha = glowAlpha)
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
                                ChineseSilver.copy(alpha = 0.3f),
                                CascadingWhite
                            )
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Rank Progress",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Crunch
                )
                
                // Current and Next Rank
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RankItem(currentRank, "Current")
                    Text(
                        text = "→",
                        style = MaterialTheme.typography.titleLarge,
                        color = WarmHaze
                    )
                    RankItem(nextRank, "Next")
                }
                
                // XP Progress Bar
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progress to Next Rank",
                            style = MaterialTheme.typography.labelMedium,
                            color = WarmHaze
                        )
                        Text(
                            text = "${(xpProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Crunch
                        )
                    }
                    
                    LinearProgressIndicator(
                        progress = { xpProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Crunch,
                        trackColor = Dreamland.copy(alpha = 0.3f),
                        strokeCap = StrokeCap.Round
                    )
                }
                
                Divider(
                    color = Dreamland.copy(alpha = 0.3f),
                    thickness = 1.dp
                )
                
                // Improvement Tips
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "💡 How to Improve",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Lead
                    )
                    TipItem("Win 3 more matches to level up")
                    TipItem("Play competitive matches for bonus XP")
                    TipItem("Maintain a win streak for extra points")
                }
            }
        }
    }
}

@Composable
private fun RankItem(rank: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Crunch.copy(alpha = 0.2f)
        ) {
            Text(
                text = rank,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Crunch
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = WarmHaze
        )
    }
}

@Composable
private fun TipItem(tip: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Crunch.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = tip,
            style = MaterialTheme.typography.bodySmall,
            color = WarmHaze
        )
    }
}

