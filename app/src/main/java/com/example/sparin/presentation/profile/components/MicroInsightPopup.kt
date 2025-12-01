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
import androidx.compose.ui.graphics.StrokeCap
import com.example.sparin.ui.theme.*

/**
 * Micro Insights Popup for Match Statistics
 * Shows most active play time, weekly activity, sport distribution
 */
@Composable
fun MicroInsightPopup(
    mostActiveTime: String = "Evenings (6-9 PM)",
    weeklyActivity: List<Float> = listOf(0.8f, 0.6f, 0.9f, 0.7f, 0.85f, 0.95f, 0.5f),
    sportDistribution: Map<String, Float> = mapOf(
        "Badminton" to 0.45f,
        "Futsal" to 0.30f,
        "Basketball" to 0.25f
    ),
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                .widthIn(max = 300.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Crunch.copy(alpha = 0.3f)
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
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "📊 Match Insights",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Crunch
                )
                
                // Most Active Time
                InsightCardText(
                    title = "Most Active Time",
                    content = mostActiveTime,
                    icon = "⏰"
                )
                
                // Weekly Activity Heat
                InsightCard(
                    title = "Weekly Activity",
                    content = {
                        WeeklyActivityHeatIndicator(activity = weeklyActivity)
                    },
                    icon = "📅"
                )
                
                // Sport Distribution
                InsightCard(
                    title = "Sport Distribution",
                    content = {
                        SportDistributionBar(distribution = sportDistribution)
                    },
                    icon = "🏅"
                )
            }
        }
    }
}

@Composable
private fun InsightCard(
    title: String,
    content: @Composable () -> Unit,
    icon: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Crunch.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = icon, fontSize = 20.sp)
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead
                )
            }
            content()
        }
    }
}

@Composable
private fun InsightCardText(
    title: String,
    content: String,
    icon: String
) {
    InsightCard(
        title = title,
        icon = icon,
        content = {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = WarmHaze
            )
        }
    )
}

@Composable
private fun WeeklyActivityHeatIndicator(activity: List<Float>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val days = listOf("M", "T", "W", "T", "F", "S", "S")
        activity.forEachIndexed { index, value ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height((value * 40).dp)
                        .background(
                            color = when {
                                value > 0.8f -> Crunch
                                value > 0.5f -> Crunch.copy(alpha = 0.6f)
                                else -> Dreamland.copy(alpha = 0.4f)
                            },
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Text(
                    text = days[index],
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun SportDistributionBar(distribution: Map<String, Float>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        distribution.forEach { (sport, percentage) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sport,
                    style = MaterialTheme.typography.bodySmall,
                    color = WarmHaze,
                    modifier = Modifier.widthIn(min = 80.dp)
                )
                LinearProgressIndicator(
                    progress = { percentage },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    color = Crunch,
                    trackColor = Dreamland.copy(alpha = 0.3f),
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "${(percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze
                )
            }
        }
    }
}

