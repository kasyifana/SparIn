package com.example.sparin.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.SentimentDissatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.presentation.profile.MatchHistoryItem
import com.example.sparin.presentation.profile.MatchResult
import com.example.sparin.ui.theme.*

/**
 * MatchHistoryList Component
 * Displays list of previous matches with results
 */
@Composable
fun MatchHistoryList(
    matchHistory: List<MatchHistoryItem>,
    modifier: Modifier = Modifier,
    maxItems: Int? = null,
    onViewAllClick: (() -> Unit)? = null,
    onMatchClick: ((MatchHistoryItem) -> Unit)? = null
) {
    val displayedMatches = if (maxItems != null) {
        matchHistory.take(maxItems)
    } else {
        matchHistory
    }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Match History",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = Lead
            )

            if (onViewAllClick != null && matchHistory.size > (maxItems ?: 0)) {
                TextButton(onClick = onViewAllClick) {
                    Text(
                        text = "View All",
                        color = Crunch,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Match Cards
        displayedMatches.forEach { match ->
            MatchHistoryCard(
                match = match,
                onClick = { onMatchClick?.invoke(match) }
            )
        }
    }
}

@Composable
private fun MatchHistoryCard(
    match: MatchHistoryItem,
    onClick: () -> Unit = {}
) {
    val resultColor = when (match.result) {
        MatchResult.WIN -> MintBreeze
        MatchResult.LOSS -> RoseDust
        MatchResult.DRAW -> PeachGlow
    }

    val resultIcon = when (match.result) {
        MatchResult.WIN -> Icons.Rounded.EmojiEvents
        MatchResult.LOSS -> Icons.Rounded.SentimentDissatisfied
        MatchResult.DRAW -> Icons.Rounded.EmojiEvents
    }

    val resultText = when (match.result) {
        MatchResult.WIN -> "Victory"
        MatchResult.LOSS -> "Defeat"
        MatchResult.DRAW -> "Draw"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Dreamland.copy(alpha = 0.15f),
                spotColor = Dreamland.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = CascadingWhite,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            resultColor.copy(alpha = 0.3f),
                            resultColor.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sport Icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    resultColor.copy(alpha = 0.3f),
                                    resultColor.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = match.sport, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Match Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = match.opponent,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        ),
                        color = Lead
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = match.sportName,
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmHaze
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ChineseSilver.copy(alpha = 0.4f)
                        ) {
                            Text(
                                text = match.score,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Lead,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = "• ${match.date}",
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmHaze,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Result Badge
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = resultColor.copy(alpha = 0.3f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            resultColor.copy(alpha = 0.5f)
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = resultIcon,
                                contentDescription = resultText,
                                modifier = Modifier.size(22.dp),
                                tint = when (match.result) {
                                    MatchResult.WIN -> Lead
                                    MatchResult.LOSS -> WarmHaze
                                    MatchResult.DRAW -> Lead
                                }
                            )
                        }
                    }

                    Text(
                        text = resultText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = when (match.result) {
                            MatchResult.WIN -> Lead
                            MatchResult.LOSS -> WarmHaze
                            MatchResult.DRAW -> Lead
                        },
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
