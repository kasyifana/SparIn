package com.example.sparin.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.model.Campaign
import com.example.sparin.data.model.Match
import com.example.sparin.data.model.Room
import com.example.sparin.data.model.User
import com.example.sparin.data.repository.CampaignRepository
import com.example.sparin.data.repository.MatchRepository
import com.example.sparin.data.repository.RoomRepository
import com.example.sparin.data.repository.UserRepository
import com.example.sparin.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Home Screen
 * Handles fetching user profile, recommended rooms, last opponents, upcoming matches, and campaigns
 */
class HomeViewModel(
    private val userRepository: UserRepository,
    private val roomRepository: RoomRepository,
    private val matchRepository: MatchRepository,
    private val campaignRepository: CampaignRepository
) : ViewModel() {
    
    // User Profile State
    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState.asStateFlow()
    
    // Recommended Rooms State
    private val _recommendedRoomsState = MutableStateFlow<RoomsState>(RoomsState.Loading)
    val recommendedRoomsState: StateFlow<RoomsState> = _recommendedRoomsState.asStateFlow()
    
    // Last Opponents State
    private val _lastOpponentsState = MutableStateFlow<OpponentsState>(OpponentsState.Loading)
    val lastOpponentsState: StateFlow<OpponentsState> = _lastOpponentsState.asStateFlow()
    
    // Upcoming Matches State
    private val _upcomingMatchesState = MutableStateFlow<MatchesState>(MatchesState.Loading)
    val upcomingMatchesState: StateFlow<MatchesState> = _upcomingMatchesState.asStateFlow()
    
    // Active Campaign State (single campaign banner)
    private val _activeCampaignState = MutableStateFlow<CampaignState>(CampaignState.Loading)
    val activeCampaignState: StateFlow<CampaignState> = _activeCampaignState.asStateFlow()
    
    // Navigation Event
    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()
    
    init {
        loadUserProfile()
        loadAllSections()
    }
    
    /**
     * Load user profile
     */
    fun loadUserProfile() {
        Log.d(TAG, "loadUserProfile called")
        viewModelScope.launch {
            _userState.value = UserState.Loading
            
            val result = userRepository.getCurrentUserProfile()
            
            when (result) {
                is Resource.Success -> {
                    val user = result.data!!
                    Log.d(TAG, "User profile loaded: ${user.name}")
                    Log.d(TAG, "Has completed personalization: ${user.hasCompletedPersonalization}")
                    
                    // Check if user needs to complete personalization
                    if (!user.hasCompletedPersonalization) {
                        Log.d(TAG, "User needs personalization - triggering navigation")
                        _navigationEvent.value = NavigationEvent.NavigateToPersonalization
                    }
                    
                    _userState.value = UserState.Success(user)
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to load user profile: ${result.message}")
                    _userState.value = UserState.Error(result.message ?: "Failed to load profile")
                }
                else -> {
                    Log.e(TAG, "Unexpected result type")
                    _userState.value = UserState.Error("Unexpected error")
                }
            }
        }
    }
    
    /**
     * Load all home screen sections
     */
    private fun loadAllSections() {
        loadRecommendedRooms()
        loadLastOpponents()
        loadUpcomingMatches()
        loadActiveCampaign()
    }
    
    /**
     * Load recommended rooms based on user interests
     */
    fun loadRecommendedRooms() {
        Log.d(TAG, "loadRecommendedRooms called")
        viewModelScope.launch {
            _recommendedRoomsState.value = RoomsState.Loading
            
            val result = roomRepository.getRooms()
            
            when (result) {
                is Resource.Success -> {
                    val rooms = result.data ?: emptyList()
                    Log.d(TAG, "Loaded ${rooms.size} recommended rooms")
                    
                    // Filter open rooms and limit to 10
                    val recommendedRooms = rooms
                        .filter { it.status == "open" }
                        .take(10)
                    
                    _recommendedRoomsState.value = RoomsState.Success(recommendedRooms)
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to load rooms: ${result.message}")
                    _recommendedRoomsState.value = RoomsState.Error(result.message ?: "Failed to load rooms")
                }
                else -> {
                    _recommendedRoomsState.value = RoomsState.Error("Unexpected error")
                }
            }
        }
    }
    
    /**
     * Load last opponents from match history
     */
    fun loadLastOpponents() {
        Log.d(TAG, "loadLastOpponents called")
        viewModelScope.launch {
            _lastOpponentsState.value = OpponentsState.Loading
            
            // Get current user ID
            val userResult = userRepository.getCurrentUserProfile()
            if (userResult !is Resource.Success) {
                _lastOpponentsState.value = OpponentsState.Error("Failed to get user")
                return@launch
            }
            
            val userId = userResult.data!!.uid
            
            // Get match history
            val matchResult = matchRepository.getMatchHistory(userId)
            
            when (matchResult) {
                is Resource.Success -> {
                    val matches = matchResult.data ?: emptyList()
                    Log.d(TAG, "Loaded ${matches.size} matches for opponents")
                    
                    // Extract unique opponent IDs
                    val opponentIds = matches
                        .flatMap { it.participants }
                        .filter { it != userId }
                        .distinct()
                        .take(5) // Limit to last 5 opponents
                    
                    // Fetch opponent user profiles
                    val opponents = mutableListOf<User>()
                    opponentIds.forEach { opponentId ->
                        val userProfileResult = userRepository.getUserProfile(opponentId)
                        if (userProfileResult is Resource.Success && userProfileResult.data != null) {
                            opponents.add(userProfileResult.data)
                        }
                    }
                    
                    Log.d(TAG, "Loaded ${opponents.size} opponent profiles")
                    _lastOpponentsState.value = OpponentsState.Success(opponents)
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to load match history: ${matchResult.message}")
                    _lastOpponentsState.value = OpponentsState.Error(matchResult.message ?: "Failed to load opponents")
                }
                else -> {
                    _lastOpponentsState.value = OpponentsState.Error("Unexpected error")
                }
            }
        }
    }
    
    /**
     * Load upcoming matches
     */
    fun loadUpcomingMatches() {
        Log.d(TAG, "loadUpcomingMatches called")
        viewModelScope.launch {
            _upcomingMatchesState.value = MatchesState.Loading
            
            // Get current user ID
            val userResult = userRepository.getCurrentUserProfile()
            if (userResult !is Resource.Success) {
                _upcomingMatchesState.value = MatchesState.Error("Failed to get user")
                return@launch
            }
            
            val userId = userResult.data!!.uid
            
            // Get upcoming matches
            val result = matchRepository.getUpcomingMatches(userId)
            
            when (result) {
                is Resource.Success -> {
                    val matches = result.data ?: emptyList()
                    Log.d(TAG, "Loaded ${matches.size} upcoming matches")
                    
                    _upcomingMatchesState.value = MatchesState.Success(matches.take(5))
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to load upcoming matches: ${result.message}")
                    _upcomingMatchesState.value = MatchesState.Error(result.message ?: "Failed to load matches")
                }
                else -> {
                    _upcomingMatchesState.value = MatchesState.Error("Unexpected error")
                }
            }
        }
    }
    
    /**
     * Load active campaign for banner
     */
    fun loadActiveCampaign() {
        Log.d(TAG, "loadActiveCampaign called")
        viewModelScope.launch {
            _activeCampaignState.value = CampaignState.Loading
            
            val result = campaignRepository.getActiveCampaigns()
            
            when (result) {
                is Resource.Success -> {
                    val campaigns = result.data ?: emptyList()
                    Log.d(TAG, "Loaded ${campaigns.size} active campaigns")
                    
                    // Show first active campaign
                    val campaign = campaigns.firstOrNull()
                    
                    _activeCampaignState.value = if (campaign != null) {
                        CampaignState.Success(campaign)
                    } else {
                        CampaignState.Empty
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to load campaigns: ${result.message}")
                    _activeCampaignState.value = CampaignState.Error(result.message ?: "Failed to load campaign")
                }
                else -> {
                    _activeCampaignState.value = CampaignState.Error("Unexpected error")
                }
            }
        }
    }
    
    /**
     * Refresh all home screen data
     */
    fun refreshAll() {
        Log.d(TAG, "Refreshing all home screen data")
        loadUserProfile()
        loadAllSections()
    }
    
    fun onNavigationHandled() {
        _navigationEvent.value = null
    }
    
    companion object {
        private const val TAG = "HomeViewModel"
    }
}

// ==================== STATE CLASSES ====================

/**
 * User State
 */
sealed class UserState {
    object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}

/**
 * Rooms State
 */
sealed class RoomsState {
    object Loading : RoomsState()
    data class Success(val rooms: List<Room>) : RoomsState()
    data class Error(val message: String) : RoomsState()
}

/**
 * Opponents State
 */
sealed class OpponentsState {
    object Loading : OpponentsState()
    data class Success(val opponents: List<User>) : OpponentsState()
    data class Error(val message: String) : OpponentsState()
}

/**
 * Matches State
 */
sealed class MatchesState {
    object Loading : MatchesState()
    data class Success(val matches: List<Match>) : MatchesState()
    data class Error(val message: String) : MatchesState()
}

/**
 * Campaign State
 */
sealed class CampaignState {
    object Loading : CampaignState()
    data class Success(val campaign: Campaign) : CampaignState()
    object Empty : CampaignState()
    data class Error(val message: String) : CampaignState()
}

/**
 * Navigation Event
 */
sealed class NavigationEvent {
    object NavigateToPersonalization : NavigationEvent()
}
