package com.example.sparin.data.repository

import com.example.sparin.data.model.Community
import com.example.sparin.data.remote.FirestoreService
import com.example.sparin.domain.util.Resource
import com.example.sparin.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

/**
 * Repository untuk Community operations
 */
class CommunityRepository(
    private val firestoreService: FirestoreService,
    private val firestore: com.google.firebase.firestore.FirebaseFirestore
) {
    
    /**
     * Get all communities
     */
    suspend fun getCommunities(): Resource<List<Community>> {
        return try {
            val snapshot = firestore.collection(Constants.Collections.COMMUNITIES)
                .get()
                .await()
                
            val communities = snapshot.documents.mapNotNull { doc ->
                val community = doc.toObject(Community::class.java)
                community?.copy(id = doc.id)
            }
            
            Resource.Success(communities.filter { it.isPublic })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get communities")
        }
    }
    
    /**
     * Get communities by sport category
     */
    suspend fun getCommunitiesByCategory(category: String): Resource<List<Community>> {
        return try {
            val snapshot = firestore.collection(Constants.Collections.COMMUNITIES)
                .whereEqualTo("sportCategory", category)
                .get()
                .await()
                
            val communities = snapshot.documents.mapNotNull { doc ->
                val community = doc.toObject(Community::class.java)
                community?.copy(id = doc.id)
            }
            
            Resource.Success(communities.filter { it.isPublic })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get communities")
        }
    }
    
    /**
     * Join community
     */
    suspend fun joinCommunity(communityId: String, userId: String): Resource<Unit> {
        return try {
            val community = firestoreService.getDocument(
                Constants.Collections.COMMUNITIES,
                communityId,
                Community::class.java
            ) ?: return Resource.Error("Community not found")
            
            if (community.members.contains(userId)) {
                return Resource.Error("Already a member")
            }
            
            val newMembers = community.members + userId
            val updates = mapOf(
                "members" to newMembers,
                "memberCount" to newMembers.size
            )
            
            firestoreService.updateDocument(
                Constants.Collections.COMMUNITIES,
                communityId,
                updates
            )
            
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to join community")
        }
    }
    
    /**
     * Leave community
     */
    suspend fun leaveCommunity(communityId: String, userId: String): Resource<Unit> {
        return try {
            val community = firestoreService.getDocument(
                Constants.Collections.COMMUNITIES,
                communityId,
                Community::class.java
            ) ?: return Resource.Error("Community not found")
            
            val newMembers = community.members.filter { it != userId }
            val updates = mapOf(
                "members" to newMembers,
                "memberCount" to newMembers.size
            )
            
            firestoreService.updateDocument(
                Constants.Collections.COMMUNITIES,
                communityId,
                updates
            )
            
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to leave community")
        }
    }
    
    /**
     * Observe communities real-time
     */
    fun observeCommunities(): Flow<List<Community>> {
        return firestoreService.observeCollection(
            Constants.Collections.COMMUNITIES,
            Community::class.java
        )
    }
}
