package com.example.sparin.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.presentation.profile.components.MatchHistoryList
import com.example.sparin.ui.theme.*
import org.koin.androidx.compose.koinViewModel

/**
 * MatchHistoryScreen - Full Match History View
 * Displays all matches played by the user
 */
@Composable
fun MatchHistoryScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LightColors.BgPrimary,
                        LightColors.BgSecondary,
                        LightColors.BgPrimary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Top Bar with Back Button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = LightColors.BgPrimary,
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
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = LightColors.SurfaceVariant,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = LightColors.TextPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Title
                    Column {
                        Text(
                            text = "Match History",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            ),
                            color = LightColors.TextPrimary
                        )
                        
                        when (val state = uiState) {
                            is ProfileUiState.Success -> {
                                Text(
                                    text = "${state.matchHistory.size} matches played",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LightColors.TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Content
            when (val state = uiState) {
                is ProfileUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Crunch,
                            strokeWidth = 4.dp
                        )
                    }
                }
                
                is ProfileUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        if (state.matchHistory.isEmpty()) {
                            // Empty State
                            EmptyMatchHistoryState()
                        } else {
                            // Stats Summary Card
                            MatchHistorySummaryCard(
                                matchHistory = state.matchHistory
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Full Match History List
                            MatchHistoryList(
                                matchHistory = state.matchHistory,
                                maxItems = null, // Show all
                                onViewAllClick = null, // No view all button here
                                onMatchClick = { match ->
                                    navController.navigate(Screen.MatchDetail.createRoute(match.id))
                                }
                            )
                        }
                    }
                }
                
                is ProfileUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = "😕", fontSize = 48.sp)
                            Text(
                                text = state.message,
                                color = LightColors.TextSecondary
                            )
                            Button(
                                onClick = { viewModel.retry() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Crunch
                                )
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun MatchHistorySummaryCard(
    matchHistory: List<MatchHistoryItem>
) {
    val wins = matchHistory.count { it.result == MatchResult.WIN }
    val losses = matchHistory.count { it.result == MatchResult.LOSS }
    val winRate = if (matchHistory.isNotEmpty()) {
        (wins.toFloat() / matchHistory.size * 100)
    } else 0f
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = LightColors.SurfaceVariant,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Total Matches
            SummaryItem(
                value = "${matchHistory.size}",
                label = "Total",
                color = Crunch
            )
            
            Divider(
                modifier = Modifier
                    .height(48.dp)
                    .width(1.dp),
                color = LightColors.BorderLight
            )
            
            // Wins
            SummaryItem(
                value = "$wins",
                label = "Wins",
                color = SportyCyan
            )
            
            Divider(
                modifier = Modifier
                    .height(48.dp)
                    .width(1.dp),
                color = LightColors.BorderLight
            )
            
            // Losses
            SummaryItem(
                value = "$losses",
                label = "Losses",
                color = SoftPeach
            )
            
            Divider(
                modifier = Modifier
                    .height(48.dp)
                    .width(1.dp),
                color = LightColors.BorderLight
            )
            
            // Win Rate
            SummaryItem(
                value = "${String.format("%.0f", winRate)}%",
                label = "Win Rate",
                color = VibrantPurple
            )
        }
    }
}

@Composable
private fun SummaryItem(
    value: String,
    label: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = LightColors.TextSecondary
        )
    }
}

@Composable
private fun EmptyMatchHistoryState() {
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
            Text(text = "🎮", fontSize = 64.sp)
            Text(
                text = "No matches yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LightColors.TextPrimary
            )
            Text(
                text = "Join a room to start playing!",
                fontSize = 14.sp,
                color = LightColors.TextSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchHistoryScreenPreview() {
    SparInTheme(darkTheme = false) {
        MatchHistoryScreen(navController = rememberNavController())
    }
}
