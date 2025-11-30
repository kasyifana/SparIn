package com.example.sparin.presentation.auth

import androidx.credentials.CustomCredential
import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.repository.AuthRepository
import com.example.sparin.domain.util.Resource
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

/**
 * ViewModel untuk Sign In Screen
 * Handles Google Sign-In dengan Credential Manager API
 */
class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState: StateFlow<SignInState> = _signInState.asStateFlow()
    
    /**
     * Handle credential dari Credential Manager
     */
    fun handleSignIn(credential: Credential) {
        Log.d(TAG, "handleSignIn called")
        viewModelScope.launch {
            _signInState.value = SignInState.Loading
            Log.d(TAG, "State changed to Loading")
            
            try {
                if (credential is CustomCredential) {
                    when (credential.type) {
                        GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                            // Extract Google ID Token
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)
                            
                            val googleIdToken = googleIdTokenCredential.idToken
                            Log.d(TAG, "Google ID Token extracted successfully")
                            
                            // Sign in dengan Firebase
                            Log.d(TAG, "Starting Firebase sign in...")
                            val result = authRepository.signInWithGoogleIdToken(googleIdToken)
                            Log.d(TAG, "Firebase sign in result: ${result::class.simpleName}")
                            
                            when (result) {
                                is Resource.Success -> {
                                    val (user, isNewUser) = result.data!!
                                    Log.d(TAG, "Sign in SUCCESS - User: ${user.uid}, isNewUser: $isNewUser")
                                    _signInState.value = SignInState.Success(user.uid, isNewUser)
                                }
                                is Resource.Error -> {
                                    val errorMsg = result.message ?: "Sign in failed"
                                    Log.e(TAG, "Sign in ERROR: $errorMsg")
                                    _signInState.value = SignInState.Error(errorMsg)
                                }
                                else -> {
                                    Log.e(TAG, "Unexpected result type")
                                    _signInState.value = SignInState.Error("Unexpected error")
                                }
                            }
                        }
                        else -> {
                            Log.e(TAG, "Unexpected credential type: ${credential.type}")
                            _signInState.value = SignInState.Error("Unexpected credential type")
                        }
                    }
                } else {
                    Log.e(TAG, "Invalid credential - not CustomCredential")
                    _signInState.value = SignInState.Error("Invalid credential")
                }
            } catch (e: GoogleIdTokenParsingException) {
                Log.e(TAG, "GoogleIdTokenParsingException: ${e.message}", e)
                _signInState.value = SignInState.Error("Invalid Google ID token: ${e.message}")
            } catch (e: Exception) {
                Log.e(TAG, "Sign in exception: ${e.message}", e)
                _signInState.value = SignInState.Error(e.message ?: "Sign in failed")
            }
        }
    }
    
    /**
     * Handle sign in error
     */
    fun handleSignInError(errorMessage: String) {
        Log.e(TAG, "handleSignInError: $errorMessage")
        _signInState.value = SignInState.Error(errorMessage)
    }
    
    /**
     * Reset state
     */
    fun resetState() {
        Log.d(TAG, "State reset to Idle")
        _signInState.value = SignInState.Idle
    }
    
    companion object {
        private const val TAG = "SignInViewModel"
    }
}

/**
 * Sign In State
 */
sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    data class Success(val userId: String, val isNewUser: Boolean) : SignInState()
    data class Error(val message: String) : SignInState()
}
