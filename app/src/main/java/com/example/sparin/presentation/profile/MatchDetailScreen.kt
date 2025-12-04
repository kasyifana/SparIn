package com.example.sparin.presentation.profile

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.ui.theme.*
import org.koin.androidx.compose.koinViewModel

/**
 * MatchDetailScreen - Premium Detail View for Match
 * Displays comprehensive match information with Gen-Z aesthetic
 */
@Composable
fun MatchDetailScreen(
    navController: NavHostController,
    matchId: String?,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Find match from uiState
    val match = remember(uiState, matchId) {
        when (val state = uiState) {
            is ProfileUiState.Success -> {
                state.matchHistory.find { it.id == matchId }
            }
            else -> null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CascadingWhite,
                        SoftLavender.copy(alpha = 0.2f),
                        CascadingWhite
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Top Bar
            MatchDetailTopBar(
                onBackClick = { navController.navigateUp() },
                match = match
            )

            if (match != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Match Hero Card
                    MatchHeroCard(match = match)

                    Spacer(modifier = Modifier.height(20.dp))

                    // Match Stats Grid
                    MatchStatsGrid(match = match)

                    Spacer(modifier = Modifier.height(20.dp))

                    // Match Details Section
                    MatchDetailsSection(match = match)

                    Spacer(modifier = Modifier.height(20.dp))

                    // Performance Highlights
                    PerformanceHighlights(match = match)

                    Spacer(modifier = Modifier.height(100.dp))
                }
            } else {
                // Not Found State
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "🔍", fontSize = 64.sp)
                        Text(
                            text = "Match not found",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Lead
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchDetailTopBar(
    onBackClick: () -> Unit,
    match: MatchHistoryItem?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CascadingWhite,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        ambientColor = ChineseSilver.copy(alpha = 0.3f)
                    )
                    .background(
                        color = ChineseSilver.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Lead
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Match Details",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = Lead
                )
                match?.let {
                    Text(
                        text = it.sportName,
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmHaze,
                        fontSize = 12.sp
                    )
                }
            }

            // Share Button (Optional)
            IconButton(
                onClick = { /* Share functionality */ },
                modifier = Modifier
                    .size(40.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        ambientColor = Crunch.copy(alpha = 0.2f)
                    )
                    .background(
                        color = Crunch.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = "Share",
                    tint = Crunch,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun MatchHeroCard(match: MatchHistoryItem) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_anim")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    val resultColor = when (match.result) {
        MatchResult.WIN -> MintBreeze
        MatchResult.LOSS -> RoseDust
        MatchResult.DRAW -> PeachGlow
    }

    val resultText = when (match.result) {
        MatchResult.WIN -> "VICTORY"
        MatchResult.LOSS -> "DEFEAT"
        MatchResult.DRAW -> "DRAW"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = resultColor.copy(alpha = 0.3f),
                spotColor = resultColor.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            ChineseSilver.copy(alpha = 0.7f),
                            resultColor.copy(alpha = 0.3f),
                            SoftLavender.copy(alpha = 0.5f)
                        )
                    )
                )
        ) {
            // Floating blobs
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-20 + float1).dp)
                    .background(
                        color = resultColor.copy(alpha = 0.25f),
                        shape = CircleShape
                    )
                    .blur(40.dp)
            )

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-25).dp, y = (25 + float2).dp)
                    .background(
                        color = Crunch.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
                    .blur(35.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sport Emoji Badge
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = resultColor.copy(alpha = 0.3f),
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        resultColor.copy(alpha = 0.5f)
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = match.sport, fontSize = 38.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Result Text
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp
                    ),
                    color = Lead
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Opponent
                Text(
                    text = match.opponent,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    color = WarmHaze
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Score Display
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = NeumorphLight.copy(alpha = 0.9f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        resultColor.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = match.score,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = Lead
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchStatsGrid(match: MatchHistoryItem) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Match Info",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = Lead
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Rounded.CalendarToday,
                label = "Date",
                value = match.date,
                color = Crunch,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                icon = Icons.Rounded.Category,
                label = "Sport",
                value = match.sportName,
                color = MintBreeze,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Rounded.Person,
                label = "Opponent",
                value = match.opponent.replace("vs. ", ""),
                color = PeachGlow,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                icon = Icons.Rounded.EmojiEvents,
                label = "Result",
                value = when (match.result) {
                    MatchResult.WIN -> "Win"
                    MatchResult.LOSS -> "Loss"
                    MatchResult.DRAW -> "Draw"
                },
                color = when (match.result) {
                    MatchResult.WIN -> MintBreeze
                    MatchResult.LOSS -> RoseDust
                    MatchResult.DRAW -> PeachGlow
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(95.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = color.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(18.dp),
        color = NeumorphLight
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = color.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color.copy(alpha = 0.9f),
                    modifier = Modifier.size(22.dp)
                )

                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmHaze,
                        fontSize = 11.sp
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = Lead,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchDetailsSection(match: MatchHistoryItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = ChineseSilver.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Match Summary",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = Lead
            )

            DetailRow(
                label = "Match ID",
                value = match.id.take(8).uppercase()
            )

            DetailRow(
                label = "Final Score",
                value = match.score
            )

            DetailRow(
                label = "Competition",
                value = match.sportName
            )

            DetailRow(
                label = "Match Type",
                value = "Competitive"
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = WarmHaze,
            fontSize = 13.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Lead,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun PerformanceHighlights(match: MatchHistoryItem) {
    val highlights = when (match.result) {
        MatchResult.WIN -> listOf(
            "Great teamwork! 🤝",
            "Solid performance overall",
            "Keep up the winning streak!"
        )
        MatchResult.LOSS -> listOf(
            "Good effort! 💪",
            "Learn and improve",
            "Every match makes you stronger"
        )
        MatchResult.DRAW -> listOf(
            "Evenly matched! ⚖️",
            "Competitive gameplay",
            "Close call - almost had it!"
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Performance Notes",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = Lead
        )

        highlights.forEach { highlight ->
            HighlightItem(text = highlight)
        }
    }
}

@Composable
private fun HighlightItem(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = ChineseSilver.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Dreamland.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(6.dp),
                shape = CircleShape,
                color = Crunch
            ) {}

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Lead,
                fontSize = 13.sp
            )
        }
    }
}
