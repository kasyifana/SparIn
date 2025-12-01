package com.example.sparin.presentation.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * EditProfileViewModel - Manages edit profile screen state
 * Following MVVM architecture pattern
 */
class EditProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            // Simulate loading user data - Replace with actual repository call
            try {
                _uiState.value = EditProfileUiState(
                    name = "Avit Raharjo",
                    bio = "Passionate badminton player | Weekend warrior",
                    city = "Jakarta",
                    gender = Gender.MALE,
                    playFrequency = PlayFrequency.THREE_TO_FIVE,
                    profilePhotoUri = null,
                    skillLevel = SkillLevel.INTERMEDIATE,
                    sportInterests = listOf("Badminton", "Futsal", "Basketball"),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateBio(bio: String) {
        _uiState.value = _uiState.value.copy(bio = bio)
    }

    fun updateCity(city: String) {
        _uiState.value = _uiState.value.copy(city = city)
    }

    fun updateGender(gender: Gender) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    fun updatePlayFrequency(frequency: PlayFrequency) {
        _uiState.value = _uiState.value.copy(playFrequency = frequency)
    }

    fun updateProfilePhoto(uri: Uri?) {
        _uiState.value = _uiState.value.copy(profilePhotoUri = uri)
    }

    fun updateSkillLevel(level: SkillLevel) {
        _uiState.value = _uiState.value.copy(skillLevel = level)
    }

    fun toggleSportInterest(sport: String) {
        val currentInterests = _uiState.value.sportInterests.toMutableList()
        if (currentInterests.contains(sport)) {
            currentInterests.remove(sport)
        } else {
            currentInterests.add(sport)
        }
        _uiState.value = _uiState.value.copy(sportInterests = currentInterests)
    }

    fun saveProfile(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            try {
                // Simulate save operation - Replace with actual repository call
                kotlinx.coroutines.delay(1000)
                
                // TODO: Call repository to save profile
                // userRepository.updateProfile(_uiState.value.toUserProfile())
                
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to save profile"
                )
            }
        }
    }
}

// ==================== UI State ====================

data class EditProfileUiState(
    val name: String = "",
    val bio: String = "",
    val city: String = "",
    val gender: Gender = Gender.MALE,
    val playFrequency: PlayFrequency = PlayFrequency.ONE_TO_TWO,
    val profilePhotoUri: Uri? = null,
    val skillLevel: SkillLevel = SkillLevel.BEGINNER,
    val sportInterests: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

// ==================== Enums ====================

enum class Gender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
}

enum class PlayFrequency(val displayName: String) {
    ONE_TO_TWO("1–2x/week"),
    THREE_TO_FIVE("3–5x/week"),
    SEVEN("7x/week")
}

enum class SkillLevel(val displayName: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced")
}
