package com.example.sparin.data.repository

import com.example.sparin.data.model.User
import com.example.sparin.data.remote.FirebaseAuthService
import com.example.sparin.data.remote.FirestoreService
import com.example.sparin.domain.util.Resource
import com.example.sparin.util.Constants
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 * Repository untuk Authentication operations
 */
class AuthRepository(
    private val authService: FirebaseAuthService,
    private val firestoreService: FirestoreService
) {
    
    /**
     * Sign in dengan Google
     */
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Resource<Pair<User, Boolean>> {
        return try {
            val result = authService.signInWithGoogle(account)
            
            when (result) {
                is Resource.Success -> {
                    val firebaseUser = result.data!!
                    val userId = firebaseUser.uid
                    
                    // Check if user exists in Firestore
                    val existingUser = firestoreService.getDocument(
                        Constants.Collections.USERS,
                        userId,
                        User::class.java
                    )
                    
                    if (existingUser != null) {
                        // Existing user
                        Resource.Success(Pair(existingUser, false))
                    } else {
                        // New user - create basic profile
                        val newUser = User(
                            uid = userId,
                            name = firebaseUser.displayName ?: "",
                            email = firebaseUser.email ?: "",
                            profilePhoto = firebaseUser.photoUrl?.toString() ?: ""
                        )
                        
                        firestoreService.createDocumentWithId(
                            Constants.Collections.USERS,
                            userId,
                            newUser
                        )
                        
                        Resource.Success(Pair(newUser, true))
                    }
                }
                is Resource.Error -> Resource.Error(result.message ?: "Authentication failed")
                else -> Resource.Error("Unexpected error")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Sign in failed")
        }
    }
    
    /**
     * Sign out
     */
    fun signOut() {
        authService.signOut()
    }
    
    /**
     * Check if user is signed in
     */
    fun isUserSignedIn(): Boolean {
        return authService.isUserSignedIn()
    }
    
    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return authService.getCurrentUserId()
    }
}
