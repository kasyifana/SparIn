package com.example.sparin.presentation.community

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.model.Community
import com.example.sparin.data.model.User
import com.example.sparin.data.repository.CommunityRepository
import com.example.sparin.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import com.example.sparin.data.remote.FirestoreService
import com.example.sparin.util.Constants

/**
 * ViewModel untuk Community Screen
 * Handles loading communities and join/leave functionality
 */
class CommunityViewModel(
    private val communityRepository: CommunityRepository,
    private val firestoreService: FirestoreService
) : ViewModel() {
    
    // All Communities State
    private val _allCommunitiesState = MutableStateFlow<CommunitiesState>(CommunitiesState.Loading)
    val allCommunitiesState: StateFlow<CommunitiesState> = _allCommunitiesState.asStateFlow()
    
    // User Communities State
    private val _userCommunitiesState = MutableStateFlow<CommunitiesState>(CommunitiesState.Loading)
    val userCommunitiesState: StateFlow<CommunitiesState> = _userCommunitiesState.asStateFlow()
    
    // User Names Cache - maps userId to userName
    private val _userNamesCache = MutableStateFlow<Map<String, String>>(emptyMap())
    val userNamesCache: StateFlow<Map<String, String>> = _userNamesCache.asStateFlow()
    
    init {
        loadAllCommunities()
        loadUserCommunities()
    }
    
    /**
     * Load all communities
     */
    fun loadAllCommunities() {
        Log.d(TAG, "loadAllCommunities called")
        viewModelScope.launch {
            _allCommunitiesState.value = CommunitiesState.Loading
            
            val result = communityRepository.getCommunities()
            
            when (result) {
                is Resource.Success -> {
                    val communities = result.data ?: emptyList()
                    Log.d(TAG, "Loaded ${communities.size} communities")
                    
                    // Fetch user names for all creators
                    val creatorIds = communities.map { it.createdBy }.distinct()
                    fetchUserNames(creatorIds)
                    
                    _allCommunitiesState.value = CommunitiesState.Success(communities)
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to load communities: ${result.message}")
                    _allCommunitiesState.value = CommunitiesState.Error(result.message ?: "Failed to load communities")
                }
                else -> {
                    _allCommunitiesState.value = CommunitiesState.Error("Unexpected error")
                }
            }
        }
    }
    
    /**
     * Fetch user names for a list of user IDs
     */
    private suspend fun fetchUserNames(userIds: List<String>) {
        val newCache = _userNamesCache.value.toMutableMap()
        
        userIds.filter { it.isNotEmpty() && !newCache.containsKey(it) }.forEach { userId ->
            try {
                val user = firestoreService.getDocument(
                    Constants.Collections.USERS,
                    userId,
                    User::class.java
                )
                if (user != null && user.name.isNotEmpty()) {
                    newCache[userId] = user.name
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch user name for $userId: ${e.message}")
            }
        }
        
        _userNamesCache.value = newCache
    }
    
    /**
     * Get user display name from cache or return truncated UID
     */
    fun getUserDisplayName(userId: String): String {
        return _userNamesCache.value[userId] ?: run {
            // Return formatted short version if name not cached
            if (userId.length > 8) userId.take(8) + "..." else userId
        }
    }
    
    /**
     * Load user's joined communities
     */
    fun loadUserCommunities() {
        Log.d(TAG, "loadUserCommunities called")
        viewModelScope.launch {
            _userCommunitiesState.value = CommunitiesState.Loading
            
            val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
            val result = communityRepository.getCommunities()
            
            when (result) {
                is Resource.Success -> {
                    val allCommunities = result.data ?: emptyList()
                    // Filter to only communities where user is a member
                    val userCommunities = if (currentUserId != null) {
                        allCommunities.filter { it.members.contains(currentUserId) }
                    } else {
                        emptyList()
                    }
                    
                    Log.d(TAG, "Loaded ${userCommunities.size} user communities")
                    _userCommunitiesState.value = CommunitiesState.Success(userCommunities)
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to load user communities: ${result.message}")
                    _userCommunitiesState.value = CommunitiesState.Error(result.message ?: "Failed to load communities")
                }
                else -> {
                    _userCommunitiesState.value = CommunitiesState.Error("Unexpected error")
                }
            }
        }
    }
    
    /**
     * Join community
     */
    fun joinCommunity(communityId: String, userId: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            val result = communityRepository.joinCommunity(communityId, userId) // Fixed: added userId
            
            when (result) {
                is Resource.Success -> {
                    Log.d(TAG, "Successfully joined community: $communityId")
                    onSuccess()
                    // Refresh both lists
                    loadAllCommunities()
                    loadUserCommunities()
                }
                is Resource.Error -> {
                    Log.e(TAG, "Failed to join community: ${result.message}")
                    onError(result.message ?: "Failed to join community")
                }
                else -> {
                    onError("Unexpected error")
                }
            }
        }
    }
    
    /**
     * Leave community
     */
    fun leaveCommunity(communityId: String, userId: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            val result = communityRepository.leaveCommunity(communityId, userId)
            
            when (result) {
                is Resource.Success -> {
                    Log.d(TAG, "Successfully left community: $communityId")
                    onSuccess()
                    // Refresh both lists
                    loadAllCommunities()
                    loadUserCommunities()
                }
                is Resource.Error -> {
                    val errorMsg = result.message ?: "Failed to leave community"
                    Log.e(TAG, "Error leaving community: $errorMsg")
                    onError(errorMsg)
                }
                is Resource.Loading -> {
                    // Handle loading if needed
                }
            }
        }
    }

    /**
     * Create a new community
     */
    fun createCommunity(community: Community, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Creating community: ${community.name}")
                
                // Use FirestoreService to create community
                val docId = firestoreService.createDocument("communities", community)
                
                if (docId.isNotEmpty()) {
                    Log.d(TAG, "Successfully created community with ID: $docId")
                    onSuccess()
                    // Refresh communities list
                    loadAllCommunities()
                    loadUserCommunities()
                } else {
                    val errorMsg = "Failed to create community"
                    Log.e(TAG, errorMsg)
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Unknown error creating community"
                Log.e(TAG, "Exception creating community: $errorMsg", e)
                onError(errorMsg)
            }
        }
    }
    
    companion object {
        private const val TAG = "CommunityViewModel"
    }
}

// ==================== STATE CLASSES ====================

/**
 * Communities State
 */
sealed class CommunitiesState {
    object Loading : CommunitiesState()
    data class Success(val communities: List<Community>) : CommunitiesState()
    data class Error(val message: String) : CommunitiesState()
}
