package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.example.sparin.presentation.profile.StatCardType
import com.example.sparin.ui.theme.*

/**
 * Bottom Sheet for detailed stat breakdown
 * Shows mini charts, AI suggestions, and detailed breakdown
 */
@Composable
fun StatCardBottomSheet(
    statType: StatCardType,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sheet_pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                ambientColor = Crunch.copy(alpha = glowAlpha)
            ),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
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
        ) {
            // Handle bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Dreamland.copy(alpha = 0.5f),
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                ) {}
            }

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getStatTitle(statType),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = Crunch
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = WarmHaze
                    )
                }
            }

            Divider(
                color = Dreamland.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    // Detailed breakdown
                    StatBreakdownSection(statType = statType)
                }
                
                item {
                    // Mini chart
                    StatMiniChart(statType = statType)
                }
                
                item {
                    // AI suggestions
                    AISuggestionsSection(statType = statType)
                }
            }
        }
    }
}

@Composable
private fun StatBreakdownSection(statType: StatCardType) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Detailed Breakdown",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Lead
        )
        
        when (statType) {
            StatCardType.WINRATE -> {
                BreakdownItem("Win Rate", "68.5%", Crunch)
                BreakdownItem("Loss Rate", "28.3%", Dreamland.copy(alpha = 0.7f))
                BreakdownItem("Draw Rate", "3.2%", WarmHaze.copy(alpha = 0.7f))
            }
            StatCardType.TOTAL_MATCHES -> {
                BreakdownItem("This Month", "12 matches", Crunch)
                BreakdownItem("Last Month", "10 matches", Dreamland.copy(alpha = 0.7f))
                BreakdownItem("Total Average", "11 matches/month", WarmHaze.copy(alpha = 0.7f))
            }
            StatCardType.TOTAL_WINS -> {
                BreakdownItem("Current Streak", "5 wins", Crunch)
                BreakdownItem("Best Streak", "10 wins", Dreamland.copy(alpha = 0.7f))
                BreakdownItem("Average Win Rate", "68.5%", WarmHaze.copy(alpha = 0.7f))
            }
            StatCardType.RANK -> {
                BreakdownItem("Current Rank", "Gold III", Crunch)
                BreakdownItem("Next Rank", "Platinum I", Dreamland.copy(alpha = 0.7f))
                BreakdownItem("Progress", "75% to next rank", WarmHaze.copy(alpha = 0.7f))
            }
            StatCardType.ELO -> {
                BreakdownItem("Current ELO", "1850", Crunch)
                BreakdownItem("ELO Change", "+45 this month", Dreamland.copy(alpha = 0.7f))
                BreakdownItem("Top Percentile", "Top 15%", WarmHaze.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
private fun BreakdownItem(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = WarmHaze
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
    }
}

@Composable
private fun StatMiniChart(statType: StatCardType) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Crunch.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = ChineseSilver.copy(alpha = 0.3f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for mini chart
            Text(
                text = "📊 Trend Chart\n(Visual representation)",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmHaze,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun AISuggestionsSection(statType: StatCardType) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "AI Insights & Suggestions",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Crunch
        )
        
        val suggestions = getAISuggestions(statType)
        suggestions.forEach { suggestion ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Crunch.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "💡 $suggestion",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze
                )
            }
        }
    }
}

private fun getStatTitle(type: StatCardType): String {
    return when (type) {
        StatCardType.WINRATE -> "Winrate Details"
        StatCardType.TOTAL_MATCHES -> "Match Statistics"
        StatCardType.TOTAL_WINS -> "Win Statistics"
        StatCardType.RANK -> "Rank & Progress"
        StatCardType.ELO -> "ELO Rating Details"
    }
}

private fun getAISuggestions(type: StatCardType): List<String> {
    return when (type) {
        StatCardType.WINRATE -> listOf(
            "Your winrate improved 5% this week! Keep up the momentum.",
            "Focus on morning sessions for better performance."
        )
        StatCardType.TOTAL_MATCHES -> listOf(
            "You're playing 20% more matches this month. Great consistency!",
            "Try joining competitive rooms to challenge yourself."
        )
        StatCardType.TOTAL_WINS -> listOf(
            "You're on a winning streak! Maintain this energy.",
            "Consider playing with higher-ranked players to improve."
        )
        StatCardType.RANK -> listOf(
            "You're 75% closer to Platinum I. Keep playing!",
            "Win 3 more matches to level up."
        )
        StatCardType.ELO -> listOf(
            "Your ELO increased by 45 points this month. Excellent progress!",
            "You're in the top 15% of players. Aim for top 10%!"
        )
    }
}

