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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sparin.data.model.UserStats
import com.example.sparin.ui.theme.*

/**
 * Breakdown Details for Total Matches
 */
@Composable
fun TotalMatchesBreakdown(
    stats: UserStats,
    modifier: Modifier = Modifier
) {
    val winMatches = stats.totalWins
    val lossMatches = (stats.totalMatches * (1 - stats.winrate / 100)).toInt()
    val drawMatches = stats.totalMatches - winMatches - lossMatches
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BreakdownItem("Wins", "$winMatches", Crunch)
        BreakdownItem("Losses", "$lossMatches", Dreamland.copy(alpha = 0.7f))
        BreakdownItem("Draws", "$drawMatches", WarmHaze.copy(alpha = 0.5f))
    }
}

/**
 * Breakdown Details for Total Wins
 */
@Composable
fun TotalWinsBreakdown(
    stats: UserStats,
    modifier: Modifier = Modifier
) {
    val badmintonWins = (stats.totalWins * 0.45).toInt()
    val futsalWins = (stats.totalWins * 0.30).toInt()
    val basketballWins = stats.totalWins - badmintonWins - futsalWins
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BreakdownItem("🏸 Badminton", "$badmintonWins wins", Crunch)
        BreakdownItem("⚽ Futsal", "$futsalWins wins", MintBreeze)
        BreakdownItem("🏀 Basketball", "$basketballWins wins", SkyMist)
    }
}

/**
 * Breakdown Details for Rank
 */
@Composable
fun RankBreakdown(
    stats: UserStats,
    modifier: Modifier = Modifier
) {
    val progressToNextRank = 0.65f // Mock data
    val pointsToNextRank = 250
    val currentRankPoints = 1850
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Rank Progress
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                "Progress to Next Rank",
                style = MaterialTheme.typography.labelMedium,
                color = WarmHaze
            )
            LinearProgressIndicator(
                progress = { progressToNextRank },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = PeachGlow,
                trackColor = Dreamland.copy(alpha = 0.2f),
                gapSize = 0.dp
            )
            Text(
                "$pointsToNextRank points needed",
                style = MaterialTheme.typography.bodySmall,
                color = WarmHaze
            )
        }
        
        // Current Rank Info
        BreakdownItem("Current Points", "$currentRankPoints", SkyMist)
    }
}

/**
 * Breakdown Details for ELO Rating
 */
@Composable
fun EloBreakdown(
    stats: UserStats,
    modifier: Modifier = Modifier
) {
    val eloTrend = 45 // Mock: +45 this month
    val eloRank = "Top 5%"
    val monthlyGain = 120
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BreakdownItem("This Month", "+$monthlyGain ELO", Crunch)
        BreakdownItem("Ranking", eloRank, SkyMist)
        BreakdownItem("Peak ELO", "${stats.elo + 15}", PeachGlow)
    }
}

/**
 * Reusable Breakdown Item (same as WinrateProgressRing's RingBreakdownItem)
 */
@Composable
private fun BreakdownItem(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color = color, shape = RoundedCornerShape(50))
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = WarmHaze
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
    }
}
