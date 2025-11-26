package com.example.sparin.data.repository

import com.example.sparin.data.model.Match
import com.example.sparin.data.remote.FirestoreService
import com.example.sparin.domain.util.Resource
import com.example.sparin.util.Constants

/**
 * Repository untuk Match operations
 */
class MatchRepository(
    private val firestoreService: FirestoreService
) {
    
    /**
     * Create match record
     */
    suspend fun createMatch(
        roomId: String,
        participants: List<String>,
        winnerId: String?,
        loserId: String?,
        score: String,
        category: String,
        mode: String,
        notes: String,
        createdBy: String
    ): Resource<String> {
        return try {
            val match = Match(
                roomId = roomId,
                participants = participants,
                winnerId = winnerId,
                loserId = loserId,
                score = score,
                category = category,
                mode = mode,
                dateTime = System.currentTimeMillis(),
                notes = notes,
                createdBy = createdBy
            )
            
            val matchId = firestoreService.createDocument(Constants.Collections.MATCHES, match)
            
            // Update match dengan ID
            firestoreService.updateDocument(
                Constants.Collections.MATCHES,
                matchId,
                mapOf("id" to matchId)
            )
            
            Resource.Success(matchId)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to create match")
        }
    }
    
    /**
     * Get match history for user
     */
    suspend fun getMatchHistory(userId: String): Resource<List<Match>> {
        return try {
            val allMatches = firestoreService.getCollection(
                Constants.Collections.MATCHES,
                Match::class.java
            )
            
            val userMatches = allMatches.filter { it.participants.contains(userId) }
            Resource.Success(userMatches.sortedByDescending { it.dateTime })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get match history")
        }
    }
    
    /**
     * Get upcoming matches for user
     */
    suspend fun getUpcomingMatches(userId: String): Resource<List<Match>> {
        return try {
            val allMatches = firestoreService.getCollection(
                Constants.Collections.MATCHES,
                Match::class.java
            )
            
            val currentTime = System.currentTimeMillis()
            val upcomingMatches = allMatches.filter {
                it.participants.contains(userId) && it.dateTime > currentTime
            }
            
            Resource.Success(upcomingMatches.sortedBy { it.dateTime })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get upcoming matches")
        }
    }
}
