package com.example.sparinprofile.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparinprofile.presentation.profile.MatchHistoryItem
import com.example.sparinprofile.presentation.profile.MatchResult
import com.example.sparinprofile.ui.theme.*

@Composable
fun MatchHistoryList(
    matchHistory: List<MatchHistoryItem>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Match History 📋",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkColors.TextPrimary
            )
            
            TextButton(onClick = { /* View all */ }) {
                Text(
                    text = "View All",
                    fontSize = 12.sp,
                    color = Crunch
                )
            }
        }
        
        // Match cards
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            matchHistory.forEach { match ->
                MatchCard(match = match)
            }
        }
    }
}

@Composable
fun MatchCard(
    match: MatchHistoryItem
) {
    val borderColor = when (match.result) {
        MatchResult.WIN -> DarkColors.StatusSuccess
        MatchResult.LOSS -> DarkColors.StatusDanger
        MatchResult.DRAW -> DarkColors.StatusWarning
    }
    
    val bgColor = when (match.result) {
        MatchResult.WIN -> DarkColors.StatusSuccessBg
        MatchResult.LOSS -> DarkColors.StatusDangerBg
        MatchResult.DRAW -> DarkColors.StatusWarningBg
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = DarkColors.SurfaceDefault,
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sport Icon
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DarkColors.SurfaceVariant,
                modifier = Modifier.size(56.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getSportEmoji(match.sport),
                        fontSize = 32.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Match Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = match.sportName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkColors.TextPrimary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "vs ${match.opponent}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkColors.TextSecondary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = match.score,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = DarkColors.TextSecondary
                    )
                    
                    Text(text = "•", color = DarkColors.TextSecondary)
                    
                    Text(
                        text = match.date,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = DarkColors.TextSecondary
                    )
                }
            }
            
            // Result Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = bgColor,
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = borderColor
                )
            ) {
                Text(
                    text = when (match.result) {
                        MatchResult.WIN -> "WIN"
                        MatchResult.LOSS -> "LOSS"
                        MatchResult.DRAW -> "DRAW"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = borderColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

fun getSportEmoji(sport: String): String {
    return when (sport.lowercase()) {
        "badminton" -> "🏸"
        "futsal" -> "⚽"
        "basketball" -> "🏀"
        "tennis" -> "🎾"
        "volleyball" -> "🏐"
        "swimming" -> "🏊"
        "running" -> "🏃"
        "cycling" -> "🚴"
        else -> "🎮"
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202022)
@Composable
fun MatchHistoryListPreview() {
    MatchHistoryList(
        matchHistory = listOf(
            MatchHistoryItem(
                id = "1",
                sport = "badminton",
                sportName = "Badminton",
                opponent = "John Doe",
                date = "2 days ago",
                score = "21-18, 21-19",
                result = MatchResult.WIN
            ),
            MatchHistoryItem(
                id = "2",
                sport = "futsal",
                sportName = "Futsal",
                opponent = "Team Alpha",
                date = "1 week ago",
                score = "3-2",
                result = MatchResult.WIN
            ),
            MatchHistoryItem(
                id = "3",
                sport = "basketball",
                sportName = "Basketball",
                opponent = "Mike Smith",
                date = "2 weeks ago",
                score = "45-52",
                result = MatchResult.LOSS
            )
        )
    )
}
