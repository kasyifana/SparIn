package com.example.sparin.presentation.personalization

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.repository.AuthRepository
import com.example.sparin.data.repository.UserRepository
import com.example.sparin.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Personalization Screen
 * Handles submitting personalization data to Firestore
 */
class PersonalizationViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _personalizationState = MutableStateFlow<PersonalizationState>(PersonalizationState.Idle)
    val personalizationState: StateFlow<PersonalizationState> = _personalizationState.asStateFlow()
    
    fun submitPersonalization(
        name: String,
        age: Int,
        gender: String,
        city: String,
        sportInterests: List<String>,
        skillLevel: String,
        playFrequency: String,
        bio: String = ""
    ) {
        Log.d(TAG, "submitPersonalization called")
        Log.d(TAG, "Data - name: $name, age: $age, gender: $gender, city: $city")
        Log.d(TAG, "Sports: $sportInterests, skill: $skillLevel, frequency: $playFrequency")
        
        viewModelScope.launch {
            _personalizationState.value = PersonalizationState.Loading
            
            val userId = authRepository.getCurrentUserId()
            if (userId == null) {
                Log.e(TAG, "User not authenticated")
                _personalizationState.value = PersonalizationState.Error("User not authenticated")
                return@launch
            }
            
            Log.d(TAG, "Submitting for userId: $userId")
            
            val result = userRepository.completePersonalization(
                userId = userId,
                name = name,
                city = city,
                gender = gender,
                age = age,
                sportInterests = sportInterests,
                skillLevel = skillLevel,
                bio = bio,
                playFrequency = playFrequency
            )
            
            when (result) {
                is Resource.Success -> {
                    Log.d(TAG, "Personalization completed successfully")
                    _personalizationState.value = PersonalizationState.Success
                }
                is Resource.Error -> {
                    Log.e(TAG, "Personalization failed: ${result.message}")
                    _personalizationState.value = PersonalizationState.Error(
                        result.message ?: "Failed to complete personalization"
                    )
                }
                else -> {
                    Log.e(TAG, "Unexpected result type")
                    _personalizationState.value = PersonalizationState.Error("Unexpected error")
                }
            }
        }
    }
    
    companion object {
        private const val TAG = "PersonalizationVM"
    }
}

/**
 * Personalization State
 */
sealed class PersonalizationState {
    object Idle : PersonalizationState()
    object Loading : PersonalizationState()
    object Success : PersonalizationState()
    data class Error(val message: String) : PersonalizationState()
}
