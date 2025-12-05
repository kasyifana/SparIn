package com.example.sparin.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.presentation.profile.components.*
import com.example.sparin.data.model.UserStats
import com.example.sparin.data.model.Badge
import com.example.sparin.ui.theme.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.koin.androidx.compose.koinViewModel

/**
 * ProfileScreen - Main Profile Screen for SparIN
 * Displays user profile, stats, match history, badges, and AI insights
 * Following SparIN Design System with premium Gen-Z aesthetic
 */
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadProfileData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CascadingWhite,
                        SoftLavender.copy(alpha = 0.15f)
                    )
                )
            )
    ) {
        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                LoadingState()
            }
            is ProfileUiState.Success -> {
                ProfileContent(
                    userProfile = state.userProfile,
                    stats = state.stats,
                    matchHistory = state.matchHistory,
                    badges = state.badges,
                    aiInsights = state.aiInsights,
                    onEditProfile = {
                        navController.navigate(Screen.EditProfile.route)
                    },
                    onViewMatchHistory = {
                        navController.navigate(Screen.MatchHistory.route)
                    },
                    onMatchClick = { match ->
                        navController.navigate(Screen.MatchDetail.createRoute(match.id))
                    },
                    onLogout = {
                        viewModel.logout {
                            // Navigate to sign in screen and clear back stack
                            navController.navigate(Screen.SignIn.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    viewModel = viewModel
                )
            }
            is ProfileUiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    userProfile: UserProfile,
    stats: UserStats,
    matchHistory: List<MatchHistoryItem>,
    badges: List<Badge>,
    aiInsights: AIInsights,
    onEditProfile: () -> Unit,
    onViewMatchHistory: () -> Unit,
    onMatchClick: (MatchHistoryItem) -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Profile Header
        ProfileHeader(
            userProfile = userProfile,
            onEditClick = onEditProfile
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Section
        StatsSection(
            stats = stats,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Badge Display
        BadgeDisplay(badges = badges)

        Spacer(modifier = Modifier.height(16.dp))

        // AI Insight Card
        AIInsightCard(aiInsights = aiInsights)

        Spacer(modifier = Modifier.height(16.dp))

        // Match History List (Limited to 3, View All for full list)
        MatchHistoryList(
            matchHistory = matchHistory,
            maxItems = 3,
            onViewAllClick = onViewMatchHistory,
            onMatchClick = onMatchClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Logout Section
        LogoutSection(
            onLogoutClick = onLogout
        )

        Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for nav bar
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Crunch,
                strokeWidth = 4.dp
            )
            Text(
                text = "Loading profile...",
                style = MaterialTheme.typography.bodyLarge,
                color = WarmHaze
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "😕",
                fontSize = 64.sp
            )
            Text(
                text = "Oops!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Lead
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = WarmHaze,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Crunch,
                    contentColor = Lead
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = Crunch.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = "Try Again",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ==================== PREVIEW ====================

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileHeaderPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CascadingWhite)
                .padding(24.dp)
        ) {
            ProfileHeader(
                userProfile = UserProfile(
                    name = "Avit Raharjo",
                    bio = "Passionate badminton player | Weekend warrior",
                    profileImageUrl = null,
                    city = "Jakarta",
                    age = 24,
                    gender = "Male"
                ),
                onEditClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsSectionPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CascadingWhite)
                .padding(24.dp)
        ) {
            // Preview disabled - requires ViewModel dependency
            // StatsSection preview is available in StatsSection.kt
            Text(
                text = "Stats Section Preview\n(Requires ViewModel)",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmHaze
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeDisplayPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CascadingWhite)
                .padding(24.dp)
        ) {
            BadgeDisplay(
                badges = listOf(
                    Badge("🏆", "Champion", "Won 5 tournaments"),
                    Badge("🔥", "Hot Streak", "10 wins in a row"),
                    Badge("⭐", "Rising Star", "Fastest climber")
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AIInsightCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CascadingWhite)
                .padding(24.dp)
        ) {
            AIInsightCard(
                aiInsights = AIInsights(
                    performanceTrend = "Your performance improved 15% this week! 🚀",
                    recommendations = listOf(
                        "Try joining advanced badminton rooms to challenge yourself",
                        "You're performing best in morning sessions (7-9 AM)"
                    ),
                    suggestedRooms = listOf(
                        "Pro Badminton League - Senayan",
                        "Elite Futsal Championship"
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchHistoryPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CascadingWhite)
                .padding(24.dp)
        ) {
            MatchHistoryList(
                matchHistory = listOf(
                    MatchHistoryItem(
                        id = "1",
                        sport = "🏸",
                        sportName = "Badminton",
                        opponent = "vs. Reza Pratama",
                        date = "Today",
                        score = "21-18, 21-19",
                        result = MatchResult.WIN
                    ),
                    MatchHistoryItem(
                        id = "2",
                        sport = "⚽",
                        sportName = "Futsal",
                        opponent = "vs. Team Alpha",
                        date = "Yesterday",
                        score = "5-3",
                        result = MatchResult.WIN
                    )
                )
            )
        }
    }
}
