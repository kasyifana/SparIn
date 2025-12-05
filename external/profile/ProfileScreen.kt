package com.example.sparinprofile.presentation.profile

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparinprofile.presentation.profile.components.*
import com.example.sparinprofile.ui.theme.*

/**
 * ProfileScreen - Main Profile Display
 * 
 * Features:
 * - Dark mode optimized design
 * - Interactive statistics with expandable details
 * - Match history
 * - Badges and achievements
 * - AI-powered insights
 */
@Composable
fun ProfileScreen(
    onEditClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    
    // Mock data for preview - replace with ViewModel in production
    val userProfile = remember {
        UserProfile(
            name = "Alex Thompson",
            bio = "Passionate badminton player | Weekend warrior",
            profileImageUrl = null,
            city = "Jakarta",
            age = 25,
            gender = "Male"
        )
    }
    
    val userStats = remember {
        UserStats(
            winrate = 75.5f,
            totalMatches = 48,
            totalWins = 36,
            rank = "Gold III",
            elo = 1850
        )
    }
    
    val badges = remember {
        listOf(
            Badge("🏆", "Champion", "Won 10 matches"),
            Badge("⚡", "Speed Demon", "Fastest player"),
            Badge("🎯", "Sharpshooter", "95% accuracy"),
            Badge("🔥", "Hot Streak", "5 wins in a row")
        )
    }
    
    val matchHistory = remember {
        listOf(
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
    }
    
    val aiInsights = remember {
        AIInsights(
            performanceTrend = "📈 Up 15% this week",
            recommendations = listOf(
                "Practice your backhand shots",
                "Join more competitive matches",
                "Focus on stamina training"
            ),
            suggestedRooms = listOf(
                "Advanced Badminton - 7PM",
                "Competitive Futsal - Weekend"
            )
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkColors.BgPrimary,
                        DarkColors.BgSecondary,
                        DarkColors.BgPrimary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            // Profile Header
            ProfileHeader(
                userProfile = userProfile,
                onEditClick = onEditClick
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Stats Section
            StatsSection(
                stats = userStats
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Badges
            BadgeDisplay(badges = badges)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // AI Insights
            AIInsightCard(insights = aiInsights)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Match History
            MatchHistoryList(matchHistory = matchHistory)
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// Data Models
data class UserProfile(
    val name: String,
    val bio: String,
    val profileImageUrl: String?,
    val city: String,
    val age: Int,
    val gender: String
)

data class UserStats(
    val winrate: Float,
    val totalMatches: Int,
    val totalWins: Int,
    val rank: String,
    val elo: Int
)

data class Badge(
    val icon: String,
    val title: String,
    val description: String
)

data class AIInsights(
    val performanceTrend: String,
    val recommendations: List<String>,
    val suggestedRooms: List<String>
)

data class MatchHistoryItem(
    val id: String,
    val sport: String,
    val sportName: String,
    val opponent: String,
    val date: String,
    val score: String,
    val result: MatchResult
)

enum class MatchResult {
    WIN, LOSS, DRAW
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
