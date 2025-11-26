package com.example.sparin.data.repository

import com.example.sparin.data.model.Room
import com.example.sparin.data.remote.FirebaseAuthService
import com.example.sparin.data.remote.FirestoreService
import com.example.sparin.domain.util.Resource
import com.example.sparin.util.Constants
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.flow.Flow

/**
 * Repository untuk Room operations
 */
class RoomRepository(
    private val firestoreService: FirestoreService,
    private val authService: FirebaseAuthService
) {
    
    /**
     * Create new room
     */
    suspend fun createRoom(
        name: String,
        category: String,
        mode: String,
        location: GeoPoint?,
        locationName: String,
        maxPlayers: Int,
        price: Double?,
        dateTime: Long,
        description: String
    ): Resource<String> {
        return try {
            val userId = authService.getCurrentUserId()
                ?: return Resource.Error("User not authenticated")
            
            val room = Room(
                name = name,
                category = category,
                mode = mode,
                location = location,
                locationName = locationName,
                maxPlayers = maxPlayers,
                currentPlayers = 1,
                members = listOf(userId),
                price = price,
                dateTime = dateTime,
                status = "open",
                createdBy = userId,
                description = description
            )
            
            val roomId = firestoreService.createDocument(Constants.Collections.ROOMS, room)
            
            // Update room dengan ID-nya
            firestoreService.updateDocument(
                Constants.Collections.ROOMS,
                roomId,
                mapOf("id" to roomId)
            )
            
            Resource.Success(roomId)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to create room")
        }
    }
    
    /**
     * Get all rooms
     */
    suspend fun getRooms(): Resource<List<Room>> {
        return try {
            val rooms = firestoreService.getCollection(Constants.Collections.ROOMS, Room::class.java)
            Resource.Success(rooms.filter { it.status == "open" })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get rooms")
        }
    }
    
    /**
     * Get rooms by category
     */
    suspend fun getRoomsByCategory(category: String): Resource<List<Room>> {
        return try {
            val rooms = firestoreService.queryCollection(
                Constants.Collections.ROOMS,
                "category",
                category,
                Room::class.java
            )
            Resource.Success(rooms.filter { it.status == "open" })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get rooms")
        }
    }
    
    /**
     * Get rooms by category and mode
     */
    suspend fun getRoomsByCategoryAndMode(category: String, mode: String): Resource<List<Room>> {
        return try {
            val filters = mapOf(
                "category" to category,
                "mode" to mode,
                "status" to "open"
            )
            val rooms = firestoreService.queryCollectionMultiple(
                Constants.Collections.ROOMS,
                filters,
                Room::class.java
            )
            Resource.Success(rooms)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get rooms")
        }
    }
    
    /**
     * Get room by ID
     */
    suspend fun getRoom(roomId: String): Resource<Room> {
        return try {
            val room = firestoreService.getDocument(
                Constants.Collections.ROOMS,
                roomId,
                Room::class.java
            )
            
            if (room != null) {
                Resource.Success(room)
            } else {
                Resource.Error("Room not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get room")
        }
    }
    
    /**
     * Join room
     */
    suspend fun joinRoom(roomId: String): Resource<Unit> {
        return try {
            val userId = authService.getCurrentUserId()
                ?: return Resource.Error("User not authenticated")
            
            val room = firestoreService.getDocument(
                Constants.Collections.ROOMS,
                roomId,
                Room::class.java
            ) ?: return Resource.Error("Room not found")
            
            // Check capacity
            if (room.currentPlayers >= room.maxPlayers) {
                return Resource.Error("Room is full")
            }
            
            // Check if already joined
            if (room.members.contains(userId)) {
                return Resource.Error("Already joined this room")
            }
            
            // Update room
            val newMembers = room.members + userId
            val newCurrentPlayers = room.currentPlayers + 1
            val newStatus = if (newCurrentPlayers >= room.maxPlayers) "full" else "open"
            
            val updates = mapOf(
                "members" to newMembers,
                "currentPlayers" to newCurrentPlayers,
                "status" to newStatus
            )
            
            firestoreService.updateDocument(
                Constants.Collections.ROOMS,
                roomId,
                updates
            )
            
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to join room")
        }
    }
    
    /**
     * Leave room
     */
    suspend fun leaveRoom(roomId: String): Resource<Unit> {
        return try {
            val userId = authService.getCurrentUserId()
                ?: return Resource.Error("User not authenticated")
            
            val room = firestoreService.getDocument(
                Constants.Collections.ROOMS,
                roomId,
                Room::class.java
            ) ?: return Resource.Error("Room not found")
            
            val newMembers = room.members.filter { it != userId }
            val newCurrentPlayers = room.currentPlayers - 1
            
            val updates = mapOf(
                "members" to newMembers,
                "currentPlayers" to newCurrentPlayers,
                "status" to "open"
            )
            
            firestoreService.updateDocument(
                Constants.Collections.ROOMS,
                roomId,
                updates
            )
            
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to leave room")
        }
    }
    
    /**
     * Observe rooms real-time
     */
    fun observeRooms(): Flow<List<Room>> {
        return firestoreService.observeCollection(
            Constants.Collections.ROOMS,
            Room::class.java
        )
    }
}
