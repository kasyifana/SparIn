package com.example.sparinprofile.presentation.profile.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparinprofile.presentation.profile.UserStats
import com.example.sparinprofile.ui.theme.*

@Composable
fun StatsSection(
    stats: UserStats
) {
    var expandedStat by remember { mutableStateOf<StatType?>(null) }
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Section Header
        Text(
            text = "Performance Stats 🏆",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkColors.TextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Stats Grid
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Row 1: Winrate + Total Matches
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InteractiveStatCard(
                    modifier = Modifier.weight(1f),
                    type = StatType.WINRATE,
                    icon = "📊",
                    label = "Winrate",
                    value = "${stats.winrate}%",
                    accentColor = Crunch,
                    isExpanded = expandedStat == StatType.WINRATE,
                    onTap = {
                        expandedStat = if (expandedStat == StatType.WINRATE) null else StatType.WINRATE
                    }
                )
                
                InteractiveStatCard(
                    modifier = Modifier.weight(1f),
                    type = StatType.TOTAL_MATCHES,
                    icon = "🎮",
                    label = "Total Matches",
                    value = "${stats.totalMatches}",
                    accentColor = SportyCyan,
                    isExpanded = expandedStat == StatType.TOTAL_MATCHES,
                    onTap = {
                        expandedStat = if (expandedStat == StatType.TOTAL_MATCHES) null else StatType.TOTAL_MATCHES
                    }
                )
            }
            
            // Row 2: Total Wins + Rank
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InteractiveStatCard(
                    modifier = Modifier.weight(1f),
                    type = StatType.TOTAL_WINS,
                    icon = "🏆",
                    label = "Total Wins",
                    value = "${stats.totalWins}",
                    accentColor = SoftPeach,
                    isExpanded = expandedStat == StatType.TOTAL_WINS,
                    onTap = {
                        expandedStat = if (expandedStat == StatType.TOTAL_WINS) null else StatType.TOTAL_WINS
                    }
                )
                
                InteractiveStatCard(
                    modifier = Modifier.weight(1f),
                    type = StatType.RANK,
                    icon = "⭐",
                    label = "Rank",
                    value = stats.rank,
                    accentColor = VibrantPurple,
                    isExpanded = expandedStat == StatType.RANK,
                    onTap = {
                        expandedStat = if (expandedStat == StatType.RANK) null else StatType.RANK
                    }
                )
            }
            
            // Row 3: ELO (full width)
            InteractiveStatCard(
                modifier = Modifier.fillMaxWidth(),
                type = StatType.ELO,
                icon = "🎯",
                label = "ELO Rating",
                value = "${stats.elo}",
                accentColor = Crunch,
                isExpanded = expandedStat == StatType.ELO,
                onTap = {
                    expandedStat = if (expandedStat == StatType.ELO) null else StatType.ELO
                }
            )
        }
    }
}

enum class StatType {
    WINRATE,
    TOTAL_MATCHES,
    TOTAL_WINS,
    RANK,
    ELO
}

@Composable
fun InteractiveStatCard(
    modifier: Modifier = Modifier,
    type: StatType,
    icon: String,
    label: String,
    value: String,
    accentColor: Color,
    isExpanded: Boolean = false,
    onTap: () -> Unit = {}
) {
    val scale by animateFloatAsState(
        targetValue = if (isExpanded) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val infiniteTransition = rememberInfiniteTransition(label = "icon")
    val iconOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconFloat"
    )
    
    Surface(
        modifier = modifier
            .shadow(
                elevation = if (isExpanded) 12.dp else 8.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onTap),
        shape = RoundedCornerShape(20.dp),
        color = DarkColors.SurfaceDefault,
        border = if (isExpanded) {
            androidx.compose.foundation.BorderStroke(2.dp, accentColor)
        } else null
    ) {
        Box {
            // Animated background blobs
            AnimatedStatBlobs(accentColor)
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon with float animation
                Text(
                    text = icon,
                    fontSize = 32.sp,
                    modifier = Modifier.offset(y = iconOffset.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Label
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkColors.TextSecondary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Value
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkColors.TextPrimary
                )
                
                // Expanded details
                androidx.compose.animation.AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Divider(
                            color = DarkColors.BorderLight,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        
                        StatDetails(type)
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedStatBlobs(accentColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "blobs")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Blob 1
        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopEnd)
                .offset(x = 10.dp, y = (-10).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        // Blob 2
        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-15).dp, y = 15.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun StatDetails(type: StatType) {
    when (type) {
        StatType.WINRATE -> {
            Column {
                DetailRow("Last Month", "72.5%")
                DetailRow("This Month", "75.5%")
                DetailRow("Trend", "↑ 3.0%")
            }
        }
        StatType.TOTAL_MATCHES -> {
            Column {
                DetailRow("Badminton", "24 matches")
                DetailRow("Futsal", "16 matches")
                DetailRow("Basketball", "8 matches")
            }
        }
        StatType.TOTAL_WINS -> {
            Column {
                DetailRow("This Week", "5 wins")
                DetailRow("This Month", "18 wins")
                DetailRow("Best Streak", "7 wins")
            }
        }
        StatType.RANK -> {
            Column {
                DetailRow("Current XP", "2,450 / 3,000")
                DetailRow("Next Rank", "Platinum I")
                DetailRow("Progress", "82%")
            }
        }
        StatType.ELO -> {
            Column {
                DetailRow("Peak ELO", "1,950")
                DetailRow("Percentile", "Top 15%")
                DetailRow("Change", "↑ 50 this week")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = DarkColors.TextSecondary
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkColors.TextPrimary
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202022)
@Composable
fun StatsSectionPreview() {
    StatsSection(
        stats = UserStats(
            winrate = 75.5f,
            totalMatches = 48,
            totalWins = 36,
            rank = "Gold III",
            elo = 1850
        )
    )
}
