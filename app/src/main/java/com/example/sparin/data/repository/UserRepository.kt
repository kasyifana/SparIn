package com.example.sparin.data.repository

import com.example.sparin.data.model.User
import com.example.sparin.data.model.UserStats
import com.example.sparin.data.remote.FirebaseAuthService
import com.example.sparin.data.remote.FirestoreService
import com.example.sparin.domain.util.Resource
import com.example.sparin.util.Constants
import kotlinx.coroutines.flow.Flow
import android.util.Log

/**
 * Repository untuk User operations
 */
class UserRepository(
    private val firestoreService: FirestoreService,
    private val authService: FirebaseAuthService
) {
    
    /**
     * Get user profile by ID
     */
    suspend fun getUserProfile(userId: String): Resource<User> {
        Log.d(TAG, "getUserProfile called for userId: $userId")
        return try {
            val user = firestoreService.getDocument(
                Constants.Collections.USERS,
                userId,
                User::class.java
            )
            
            if (user != null) {
                Log.d(TAG, "User profile found: ${user.name}")
                Resource.Success(user)
            } else {
                Log.e(TAG, "User document is NULL for userId: $userId")
                Resource.Error("User not found")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in getUserProfile: ${e.message}", e)
            Resource.Error(e.message ?: "Failed to get user profile")
        }
    }
    
    /**
     * Get current user profile
     */
    suspend fun getCurrentUserProfile(): Resource<User> {
        Log.d(TAG, "getCurrentUserProfile called")
        val userId = authService.getCurrentUserId()
        Log.d(TAG, "Current userId: $userId")
        
        return if (userId != null) {
            getUserProfile(userId)
        } else {
            Log.e(TAG, "User not authenticated - userId is NULL")
            Resource.Error("User not authenticated")
        }
    }
    
    /**
     * Update user profile
     */
    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Resource<Unit> {
        return try {
            firestoreService.updateDocument(
                Constants.Collections.USERS,
                userId,
                updates
            )
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update profile")
        }
    }
    
    /**
     * Complete personalization (untuk new users)
     */
    suspend fun completePersonalization(
        userId: String,
        name: String,
        city: String,
        gender: String,
        age: Int?,
        sportInterests: List<String>,
        skillLevel: String,
        bio: String
    ): Resource<Unit> {
        return try {
            val updates = mapOf(
                "name" to name,
                "city" to city,
                "gender" to gender,
                "age" to (age ?: 0),
                "sportInterests" to sportInterests,
                "skillLevel" to skillLevel,
                "bio" to bio
            )
            
            firestoreService.updateDocument(
                Constants.Collections.USERS,
                userId,
                updates
            )
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to complete personalization")
        }
    }
    
    /**
     * Update user stats
     */
    suspend fun updateUserStats(userId: String, stats: UserStats): Resource<Unit> {
        return try {
            val updates = mapOf("stats" to stats)
            firestoreService.updateDocument(
                Constants.Collections.USERS,
                userId,
                updates
            )
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update stats")
        }
    }
    
    /**
     * Observe user profile real-time
     */
    fun observeUserProfile(userId: String): Flow<User?> {
        return firestoreService.observeDocument(
            Constants.Collections.USERS,
            userId,
            User::class.java
        )
    }
    
    companion object {
        private const val TAG = "UserRepository"
    }
}
