package com.example.sparin.data.repository

import com.example.sparin.data.model.Campaign
import com.example.sparin.data.remote.FirestoreService
import com.example.sparin.domain.util.Resource
import com.example.sparin.util.Constants

/**
 * Repository untuk Campaign operations
 */
class CampaignRepository(
    private val firestoreService: FirestoreService
) {
    
    /**
     * Get all active campaigns
     */
    suspend fun getActiveCampaigns(): Resource<List<Campaign>> {
        return try {
            val campaigns = firestoreService.queryCollection(
                Constants.Collections.CAMPAIGNS,
                "status",
                "active",
                Campaign::class.java
            )
            Resource.Success(campaigns)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get campaigns")
        }
    }
    
    /**
     * Get campaign by ID
     */
    suspend fun getCampaign(campaignId: String): Resource<Campaign> {
        return try {
            val campaign = firestoreService.getDocument(
                Constants.Collections.CAMPAIGNS,
                campaignId,
                Campaign::class.java
            )
            
            if (campaign != null) {
                Resource.Success(campaign)
            } else {
                Resource.Error("Campaign not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get campaign")
        }
    }
    
    /**
     * Register for campaign
     */
    suspend fun registerForCampaign(campaignId: String, userId: String): Resource<Unit> {
        return try {
            val campaign = firestoreService.getDocument(
                Constants.Collections.CAMPAIGNS,
                campaignId,
                Campaign::class.java
            ) ?: return Resource.Error("Campaign not found")
            
            if (campaign.participants.contains(userId)) {
                return Resource.Error("Already registered")
            }
            
            if (campaign.maxParticipants != null && 
                campaign.participants.size >= campaign.maxParticipants) {
                return Resource.Error("Campaign is full")
            }
            
            val newParticipants = campaign.participants + userId
            val updates = mapOf("participants" to newParticipants)
            
            firestoreService.updateDocument(
                Constants.Collections.CAMPAIGNS,
                campaignId,
                updates
            )
            
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to register")
        }
    }
}
