package com.example.sparin.presentation.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.repository.UserRepository
import com.example.sparin.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * EditProfileViewModel - Manages edit profile screen state
 * Following MVVM architecture pattern
 */
class EditProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            when (val result = userRepository.getCurrentUserProfile()) {
                is Resource.Success -> {
                    val user = result.data
                    if (user != null) {
                        _uiState.value = EditProfileUiState(
                            name = user.name,
                            bio = user.bio,
                            city = user.city,
                            gender = try {
                                Gender.valueOf(user.gender.uppercase())
                            } catch (e: Exception) {
                                Gender.MALE // Default fallback
                            },
                            playFrequency = try {
                                PlayFrequency.values().find { it.displayName == user.playFrequency } 
                                    ?: PlayFrequency.ONE_TO_TWO
                            } catch (e: Exception) {
                                PlayFrequency.ONE_TO_TWO
                            },
                            profilePhotoUri = if (user.profilePhoto.isNotEmpty()) Uri.parse(user.profilePhoto) else null,
                            skillLevel = try {
                                SkillLevel.values().find { it.displayName == user.skillLevel } 
                                    ?: SkillLevel.BEGINNER
                            } catch (e: Exception) {
                                SkillLevel.BEGINNER
                            },
                            sportInterests = user.sportInterests,
                            isLoading = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "User data not found"
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
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
            
            val currentState = _uiState.value
            
            // Prepare updates map
            val updates = mutableMapOf<String, Any>(
                "name" to currentState.name,
                "bio" to currentState.bio,
                "city" to currentState.city,
                "gender" to currentState.gender.name, // Store enum name
                "playFrequency" to currentState.playFrequency.displayName,
                "skillLevel" to currentState.skillLevel.displayName,
                "sportInterests" to currentState.sportInterests
            )
            
            // Handle profile photo if changed (Note: This assumes we just store the URI string for now. 
            // In a real app, we would upload the image to Storage first and get a URL)
            currentState.profilePhotoUri?.let {
                updates["profilePhoto"] = it.toString()
            }
            
            // Get current user ID
            when (val userResult = userRepository.getCurrentUserProfile()) {
                is Resource.Success -> {
                    val userId = userResult.data?.uid
                    if (userId != null) {
                        when (val updateResult = userRepository.updateUserProfile(userId, updates)) {
                            is Resource.Success -> {
                                _uiState.value = _uiState.value.copy(isLoading = false)
                                onSuccess()
                            }
                            is Resource.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    errorMessage = updateResult.message ?: "Failed to update profile"
                                )
                            }
                            else -> {}
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "User ID not found"
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = userResult.message ?: "Failed to get user info"
                    )
                }
                else -> {}
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
