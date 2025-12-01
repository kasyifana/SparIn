package com.example.sparin.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.model.Community
import com.example.sparin.data.repository.AuthRepository
import com.example.sparin.data.repository.CommunityRepository
import com.example.sparin.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Community Screen
 * Manages community list states and user interactions
 */
class CommunityViewModel(
    private val communityRepository: CommunityRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // State for all communities
    private val _communitiesState = MutableStateFlow<CommunitiesState>(CommunitiesState.Loading)
    val communitiesState: StateFlow<CommunitiesState> = _communitiesState.asStateFlow()

    // State for user's joined communities
    private val _myCommunitiesState = MutableStateFlow<CommunitiesState>(CommunitiesState.Loading)
    val myCommunitiesState: StateFlow<CommunitiesState> = _myCommunitiesState.asStateFlow()

    // State for recommended communities
    private val _recommendedState = MutableStateFlow<CommunitiesState>(CommunitiesState.Loading)
    val recommendedState: StateFlow<CommunitiesState> = _recommendedState.asStateFlow()

    // Selected category filter
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadCommunities()
    }

    fun loadCommunities() {
        viewModelScope.launch {
            _communitiesState.value = CommunitiesState.Loading
            
            when (val result = communityRepository.getCommunities()) {
                is Resource.Success -> {
                    val communities = result.data ?: emptyList()
                    _communitiesState.value = CommunitiesState.Success(communities)
                    
                    // Separate into my communities and recommended
                    // For now, use simple logic - can be enhanced with user preferences
                    val currentUserId = authRepository.getCurrentUserId()
                    
                    val myCommunities = communities.filter { it.members.contains(currentUserId) }
                    val recommended = communities.filter { !it.members.contains(currentUserId) }
                    
                    _myCommunitiesState.value = CommunitiesState.Success(myCommunities)
                    _recommendedState.value = CommunitiesState.Success(recommended)
                }
                is Resource.Error -> {
                    _communitiesState.value = CommunitiesState.Error(result.message ?: "Unknown error")
                    _myCommunitiesState.value = CommunitiesState.Error(result.message ?: "Unknown error")
                    _recommendedState.value = CommunitiesState.Error(result.message ?: "Unknown error")
                }
                is Resource.Loading -> {
                    _communitiesState.value = CommunitiesState.Loading
                }
            }
        }
    }

    fun loadCommunitiesByCategory(category: String) {
        viewModelScope.launch {
            _selectedCategory.value = category
            _communitiesState.value = CommunitiesState.Loading
            
            when (val result = communityRepository.getCommunitiesByCategory(category)) {
                is Resource.Success -> {
                    _communitiesState.value = CommunitiesState.Success(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _communitiesState.value = CommunitiesState.Error(result.message ?: "Unknown error")
                }
                is Resource.Loading -> {
                    _communitiesState.value = CommunitiesState.Loading
                }
            }
        }
    }

    fun clearCategoryFilter() {
        _selectedCategory.value = null
        loadCommunities()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        // Filter communities based on search query
        filterCommunities(query)
    }

    private fun filterCommunities(query: String) {
        val currentState = _communitiesState.value
        if (currentState is CommunitiesState.Success) {
            val filtered = if (query.isBlank()) {
                currentState.communities
            } else {
                currentState.communities.filter { community ->
                    community.name.contains(query, ignoreCase = true) ||
                    community.sportCategory.contains(query, ignoreCase = true) ||
                    community.description.contains(query, ignoreCase = true)
                }
            }
            // Note: This modifies the display list, original list is preserved in ViewModel
        }
    }

    fun joinCommunity(communityId: String) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            
            when (val result = communityRepository.joinCommunity(communityId, userId)) {
                is Resource.Success -> {
                    // Refresh communities to reflect the change
                    loadCommunities()
                }
                is Resource.Error -> {
                    // Handle error - could emit to a separate error state
                }
                is Resource.Loading -> { }
            }
        }
    }

    fun leaveCommunity(communityId: String) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            
            when (val result = communityRepository.leaveCommunity(communityId, userId)) {
                is Resource.Success -> {
                    loadCommunities()
                }
                is Resource.Error -> {
                    // Handle error
                }
                is Resource.Loading -> { }
            }
        }
    }
}

/**
 * State sealed class for communities loading
 */
sealed class CommunitiesState {
    object Loading : CommunitiesState()
    data class Success(val communities: List<Community>) : CommunitiesState()
    data class Error(val message: String) : CommunitiesState()
}
