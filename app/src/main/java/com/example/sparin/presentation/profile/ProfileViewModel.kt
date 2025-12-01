package com.example.sparin.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.model.Room
import com.example.sparin.data.model.User
import com.example.sparin.data.model.UserStats
import com.example.sparin.data.repository.RoomRepository
import com.example.sparin.data.repository.UserRepository
import com.example.sparin.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ProfileViewModel - Manages profile screen state
 * Following MVVM architecture pattern
 */
class ProfileViewModel(
    private val userRepository: UserRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Interactive state management
    private val _selectedStatCard = MutableStateFlow<StatCardType?>(null)
    val selectedStatCard: StateFlow<StatCardType?> = _selectedStatCard.asStateFlow()

    private val _isComparisonMode = MutableStateFlow(false)
    val isComparisonMode: StateFlow<Boolean> = _isComparisonMode.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _showPopup = MutableStateFlow<PopupType?>(null)
    val showPopup: StateFlow<PopupType?> = _showPopup.asStateFlow()

    private val _animatedStatIncrease = MutableStateFlow<StatCardType?>(null)
    val animatedStatIncrease: StateFlow<StatCardType?> = _animatedStatIncrease.asStateFlow()

    init {
        loadProfileData()
    }

    // Interactive state functions
    fun selectStatCard(type: StatCardType) {
        _selectedStatCard.value = type
        _showBottomSheet.value = true
    }

    fun deselectStatCard() {
        _selectedStatCard.value = null
        _showBottomSheet.value = false
    }

    fun toggleComparisonMode() {
        _isComparisonMode.value = !_isComparisonMode.value
    }

    fun exitComparisonMode() {
        _isComparisonMode.value = false
    }

    fun showPopup(type: PopupType) {
        _showPopup.value = type
    }

    fun hidePopup() {
        _showPopup.value = null
    }

    fun triggerStatIncrease(type: StatCardType) {
        _animatedStatIncrease.value = type
        // Reset after animation completes (will be handled in UI)
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            _animatedStatIncrease.value = null
        }
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            try {
                // 1. Get User Profile
                val userResult = userRepository.getCurrentUserProfile()
                if (userResult is Resource.Error) {
                    _uiState.value = ProfileUiState.Error(userResult.message ?: "Failed to load profile")
                    return@launch
                }
                val user = (userResult as Resource.Success).data!!

                // 2. Get Joined Rooms (Match History)
                val roomsResult = roomRepository.getRoomsByUser(user.uid)
                val rooms = if (roomsResult is Resource.Success) roomsResult.data ?: emptyList() else emptyList()

                // 3. Map Data
                val profile = UserProfile(
                    name = user.name,
                    bio = user.bio.ifEmpty { "No bio yet." },
                    profileImageUrl = user.profilePhoto.ifEmpty { null },
                    city = user.city,
                    age = user.age ?: 0,
                    gender = user.gender
                )

                val stats = user.stats

                val matchHistory = rooms.map { room ->
                    MatchHistoryItem(
                        id = room.id,
                        sport = getSportEmoji(room.category),
                        sportName = room.category,
                        opponent = "vs. ${room.maxPlayers - 1} others", // Simplified for now
                        date = formatDate(room.dateTime),
                        score = "Played", // Placeholder as we don't have score data yet
                        result = MatchResult.WIN // Placeholder
                    )
                }

                // Mock badges for now (can be dynamic later)
                val badges = listOf(
                    Badge("🏆", "Champion", "Won 5 tournaments"),
                    Badge("🔥", "Hot Streak", "10 wins in a row"),
                    Badge("⭐", "Rising Star", "Fastest climber")
                )

                // Mock AI insights for now
                val aiInsights = AIInsights(
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

    private fun getSportEmoji(category: String): String {
        return when (category.lowercase()) {
            "badminton" -> "🏸"
            "futsal" -> "⚽"
            "basketball" -> "🏀"
            "tennis" -> "🎾"
            "running" -> "🏃"
            "cycling" -> "🚴"
            "gym" -> "💪"
            else -> "🏅"
        }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
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

// ==================== Interactive State Types ====================

enum class StatCardType {
    WINRATE,
    TOTAL_MATCHES,
    TOTAL_WINS,
    RANK,
    ELO;
    
    val displayName: String
        get() = when (this) {
            WINRATE -> "Winrate"
            TOTAL_MATCHES -> "Total Matches"
            TOTAL_WINS -> "Total Wins"
            RANK -> "Rank"
            ELO -> "ELO Rating"
        }
}

enum class PopupType {
    RANK_UPGRADE,
    MICRO_INSIGHT,
    TOOLTIP
}

data class ComparisonData(
    val currentMonth: Double,
    val lastMonth: Double,
    val sportCategories: Map<String, Double>
)
