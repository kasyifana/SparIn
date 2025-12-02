package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.data.model.UserStats
import com.example.sparin.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Premium Breakdown Details for Total Matches with animations
 */
@Composable
fun TotalMatchesBreakdown(
    stats: UserStats,
    modifier: Modifier = Modifier
) {
    val winMatches = stats.totalWins
    val lossMatches = (stats.totalMatches * (1 - stats.winrate / 100)).toInt()
    val drawMatches = stats.totalMatches - winMatches - lossMatches

    // Animate entry
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(50)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                NeumorphLight.copy(alpha = 0.4f),
                                NeumorphLight.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MATCH BREAKDOWN",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            ),
                            color = NeumorphDark.copy(alpha = 0.85f)
                        )
                        Icon(
                            imageVector = Icons.Rounded.Analytics,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Crunch.copy(alpha = 0.6f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Breakdown items with animations
                    PremiumBreakdownItem(
                        icon = "🏆",
                        label = "Wins",
                        value = "$winMatches",
                        color = Crunch,
                        percentage = winMatches.toFloat() / stats.totalMatches,
                        index = 0
                    )
                    PremiumBreakdownItem(
                        icon = "💔",
                        label = "Losses",
                        value = "$lossMatches",
                        color = RoseDust,
                        percentage = lossMatches.toFloat() / stats.totalMatches,
                        index = 1
                    )
                    PremiumBreakdownItem(
                        icon = "🤝",
                        label = "Draws",
                        value = "$drawMatches",
                        color = ChineseSilver,
                        percentage = drawMatches.toFloat() / stats.totalMatches,
                        index = 2
                    )
                }
            }
        }
    }
}

/**
 * Premium Breakdown Details for Total Wins by Sport
 */
@Composable
fun TotalWinsBreakdown(
    stats: UserStats,
    modifier: Modifier = Modifier
) {
    val badmintonWins = (stats.totalWins * 0.45).toInt()
    val futsalWins = (stats.totalWins * 0.30).toInt()
    val basketballWins = stats.totalWins - badmintonWins - futsalWins
    
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(50)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                NeumorphLight.copy(alpha = 0.4f),
                                NeumorphLight.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "WINS BY SPORT",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            ),
                            color = Lead.copy(alpha = 0.7f)
                        )
                        Icon(
                            imageVector = Icons.Rounded.Sports,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = PeachGlow.copy(alpha = 0.6f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    PremiumBreakdownItem(
                        icon = "🏸",
                        label = "Badminton",
                        value = "$badmintonWins",
                        color = PeachGlow,
                        percentage = badmintonWins.toFloat() / stats.totalWins,
                        index = 0
                    )
                    PremiumBreakdownItem(
                        icon = "⚽",
                        label = "Futsal",
                        value = "$futsalWins",
                        color = MintBreeze,
                        percentage = futsalWins.toFloat() / stats.totalWins,
                        index = 1
                    )
                    PremiumBreakdownItem(
                        icon = "🏀",
                        label = "Basketball",
                        value = "$basketballWins",
                        color = SkyMist,
                        percentage = basketballWins.toFloat() / stats.totalWins,
                        index = 2
                    )
                }
            }
        }
    }
}

/**
 * Premium Breakdown Details for Rank with Progress
 */
@Composable
fun RankBreakdown(
    stats: UserStats,
    modifier: Modifier = Modifier
) {
    val progressToNextRank = 0.65f
    val pointsToNextRank = 250
    val currentRankPoints = 1850
    
    var visible by remember { mutableStateOf(false) }
    var progressVisible by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        delay(50)
        visible = true
        delay(200)
        // Animate progress bar
        animate(
            initialValue = 0f,
            targetValue = progressToNextRank,
            animationSpec = tween(1000, easing = EaseOutCubic)
        ) { value, _ ->
            progressVisible = value
        }
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                NeumorphLight.copy(alpha = 0.4f),
                                NeumorphLight.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "RANK PROGRESS",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            ),
                            color = Lead.copy(alpha = 0.7f)
                        )
                        Icon(
                            imageVector = Icons.Rounded.TrendingUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Crunch.copy(alpha = 0.6f)
                        )
                    }
                    
                    // Progress bar with glow
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box {
                            // Glow effect
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progressVisible)
                                    .height(12.dp)
                                    .blur(8.dp)
                                    .background(
                                        color = Crunch.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                            )
                            
                            // Track
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .background(
                                        color = Dreamland.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                            )
                            
                            // Progress
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progressVisible)
                                    .height(12.dp)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Crunch,
                                                PeachGlow
                                            )
                                        ),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${(progressToNextRank * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Crunch
                            )
                            Text(
                                text = "$pointsToNextRank pts needed",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmHaze.copy(alpha = 0.7f)
                            )
                        }
                    }
                    
                    Divider(
                        color = ChineseSilver.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    
                    // Current points
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                SkyMist.copy(alpha = 0.3f),
                                                Color.Transparent
                                            )
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "⭐", fontSize = 16.sp)
                            }
                            Text(
                                text = "Current Points",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze
                            )
                        }
                        Text(
                            text = "$currentRankPoints",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = SkyMist
                        )
                    }
                }
            }
        }
    }
}

/**
 * Premium Breakdown Details for ELO Rating
 */
@Composable
fun EloBreakdown(
    stats: UserStats,
    modifier: Modifier = Modifier
) {
    val monthlyGain = 120
    val eloRank = "Top 5%"
    val peakElo = stats.elo + 15
    
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(50)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                NeumorphLight.copy(alpha = 0.4f),
                                NeumorphLight.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ELO STATISTICS",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            ),
                            color = Lead.copy(alpha = 0.7f)
                        )
                        Icon(
                            imageVector = Icons.Rounded.Whatshot,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Crunch.copy(alpha = 0.6f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    PremiumBreakdownItem(
                        icon = "📈",
                        label = "This Month",
                        value = "+$monthlyGain",
                        color = Crunch,
                        percentage = 0.8f,
                        index = 0
                    )
                    PremiumBreakdownItem(
                        icon = "🏅",
                        label = "Global Rank",
                        value = eloRank,
                        color = PeachGlow,
                        percentage = 0.95f,
                        index = 1
                    )
                    PremiumBreakdownItem(
                        icon = "⚡",
                        label = "Peak ELO",
                        value = "$peakElo",
                        color = SkyMist,
                        percentage = 1f,
                        index = 2
                    )
                }
            }
        }
    }
}

/**
 * Premium Breakdown Item with icon, progress bar, and animation
 */
@Composable
private fun PremiumBreakdownItem(
    icon: String,
    label: String,
    value: String,
    color: Color,
    percentage: Float,
    index: Int
) {
    var visible by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        delay(100L * index)
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(400, easing = EaseOutCubic)
        ) { value, _ ->
            visible = value
        }
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.18f * visible),
                        color.copy(alpha = 0.08f * visible)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.5.dp,
                color = color.copy(alpha = 0.3f * visible),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Icon with gradient background
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                color.copy(alpha = 0.35f * visible),
                                color.copy(alpha = 0.15f * visible)
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(
                        width = 1.5.dp,
                        color = color.copy(alpha = 0.4f * visible),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 20.sp)
            }
            
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = NeumorphDark.copy(alpha = 0.9f * visible)
                )
                
                // Mini progress bar
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color = color.copy(alpha = 0.25f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(percentage * visible)
                            .fillMaxHeight()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        color.copy(alpha = 0.9f),
                                        color
                                    )
                                )
                            )
                    )
                }
            }
        }
        
        // Value with better contrast
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                letterSpacing = (-0.3).sp
            ),
            color = color.copy(alpha = visible)
        )
    }
}
