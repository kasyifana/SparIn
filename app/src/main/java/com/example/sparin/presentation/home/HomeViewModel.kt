package com.example.sparin.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.model.User
import com.example.sparin.data.repository.UserRepository
import com.example.sparin.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Home Screen
 * Handles fetching user profile data
 */
class HomeViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState.asStateFlow()
    
    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()
    
    init {
        loadUserProfile()
    }
    
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
    
    fun onNavigationHandled() {
        _navigationEvent.value = null
    }
    
    companion object {
        private const val TAG = "HomeViewModel"
    }
}

/**
 * User State
 */
sealed class UserState {
    object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}

/**
 * Navigation Event
 */
sealed class NavigationEvent {
    object NavigateToPersonalization : NavigationEvent()
}
