package com.example.sparin.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ProfileViewModel - Manages profile screen state
 * Following MVVM architecture pattern
 */
class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            // Simulate data loading - Replace with actual repository calls
            try {
                // Mock data for demonstration
                val profile = UserProfile(
                    name = "Avit Raharjo",
                    bio = "Passionate badminton player | Weekend warrior",
                    profileImageUrl = null,
                    city = "Jakarta",
                    age = 24,
                    gender = "Male"
                )

                val stats = UserStats(
                    winrate = 68.5,
                    totalMatches = 47,
                    totalWins = 32,
                    rank = "Gold III",
                    elo = 1850
                )

                val matchHistory = listOf(
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
                    ),
                    MatchHistoryItem(
                        id = "3",
                        sport = "🏸",
                        sportName = "Badminton",
                        opponent = "vs. Dina Marlina",
                        date = "2 days ago",
                        score = "19-21, 20-22",
                        result = MatchResult.LOSS
                    ),
                    MatchHistoryItem(
                        id = "4",
                        sport = "🏀",
                        sportName = "Basketball",
                        opponent = "vs. Aldo & Friends",
                        date = "3 days ago",
                        score = "42-38",
                        result = MatchResult.WIN
                    ),
                    MatchHistoryItem(
                        id = "5",
                        sport = "🎾",
                        sportName = "Tennis",
                        opponent = "vs. Maya Sari",
                        date = "5 days ago",
                        score = "6-4, 5-7, 6-3",
                        result = MatchResult.WIN
                    )
                )

                val badges = listOf(
                    Badge("🏆", "Champion", "Won 5 tournaments"),
                    Badge("🔥", "Hot Streak", "10 wins in a row"),
                    Badge("⭐", "Rising Star", "Fastest climber"),
                    Badge("💪", "Consistent", "30 days active"),
                    Badge("🎯", "Sharpshooter", "90% accuracy")
                )

                val aiInsights = AIInsights(
                    performanceTrend = "Your performance improved 15% this week! 🚀",
                    recommendations = listOf(
                        "Try joining advanced badminton rooms to challenge yourself",
                        "You're performing best in morning sessions (7-9 AM)",
                        "Consider practicing your backhand technique"
                    ),
                    suggestedRooms = listOf(
                        "Pro Badminton League - Senayan",
                        "Elite Futsal Championship",
                        "Weekend Tennis Masters"
                    )
                )

                _uiState.value = ProfileUiState.Success(
                    userProfile = profile,
                    stats = stats,
                    matchHistory = matchHistory,
                    badges = badges,
                    aiInsights = aiInsights
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(
                    message = e.message ?: "Failed to load profile"
                )
            }
        }
    }

    fun retry() {
        loadProfileData()
    }
}

// ==================== UI State ====================

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(
        val userProfile: UserProfile,
        val stats: UserStats,
        val matchHistory: List<MatchHistoryItem>,
        val badges: List<Badge>,
        val aiInsights: AIInsights
    ) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

// ==================== Data Models ====================

data class UserProfile(
    val name: String,
    val bio: String,
    val profileImageUrl: String?,
    val city: String,
    val age: Int,
    val gender: String
)

data class UserStats(
    val winrate: Double,
    val totalMatches: Int,
    val totalWins: Int,
    val rank: String,
    val elo: Int
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
