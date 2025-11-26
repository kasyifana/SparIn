package com.example.sparin.data.remote

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.example.sparin.domain.util.Resource
import kotlinx.coroutines.tasks.await

/**
 * Service untuk handle Firebase Authentication
 */
class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth
) {
    
    /**
     * Sign in dengan Google account
     */
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Resource<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user
            
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("Sign in failed: User is null")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Sign in failed")
        }
    }
    
    /**
     * Get current user
     */
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
    
    /**
     * Check if user is signed in
     */
    fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
    
    /**
     * Sign out
     */
    fun signOut() {
        firebaseAuth.signOut()
    }
    
    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}
