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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.sparin.presentation.profile.StatCardType
import com.example.sparin.ui.theme.*

/**
 * Tooltip Bubble - Shows explanation on stat calculation
 */
@Composable
fun TooltipBubble(
    statType: StatCardType,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "tooltip_glow")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
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
        Surface(
            modifier = modifier
                .widthIn(max = 280.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = ChineseSilver.copy(alpha = alpha)
                ),
            shape = RoundedCornerShape(16.dp),
            color = ChineseSilver
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ChineseSilver.copy(alpha = 0.9f),
                                ChineseSilver.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = getTooltipTitle(statType),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Crunch
                )
                
                Text(
                    text = getTooltipDescription(statType),
                    style = MaterialTheme.typography.bodySmall,
                    color = WarmHaze
                )
            }
        }
    }
}

private fun getTooltipTitle(type: StatCardType): String {
    return when (type) {
        StatCardType.WINRATE -> "Winrate Calculation"
        StatCardType.TOTAL_MATCHES -> "Total Matches"
        StatCardType.TOTAL_WINS -> "Total Wins"
        StatCardType.RANK -> "Rank System"
        StatCardType.ELO -> "ELO Rating"
    }
}

private fun getTooltipDescription(type: StatCardType): String {
    return when (type) {
        StatCardType.WINRATE -> 
            "Winrate is calculated as (Total Wins / Total Matches) × 100%. " +
            "This percentage shows your success rate across all matches."
        StatCardType.TOTAL_MATCHES -> 
            "Total number of matches you've participated in since joining SparIN. " +
            "Includes both casual and competitive matches."
        StatCardType.TOTAL_WINS -> 
            "Total number of matches you've won. " +
            "This count increases every time you win a match."
        StatCardType.RANK -> 
            "Your current rank is based on your overall performance and ELO rating. " +
            "Ranks range from Bronze to Diamond with multiple tiers."
        StatCardType.ELO -> 
            "ELO rating is a skill-based ranking system. " +
            "It increases when you win and decreases when you lose, " +
            "reflecting your skill level compared to other players."
    }
}

