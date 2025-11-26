package com.example.sparin.data.repository

import com.example.sparin.data.model.ChatMessage
import com.example.sparin.data.model.ChatRoom
import com.example.sparin.data.remote.FirebaseAuthService
import com.example.sparin.data.remote.FirestoreService
import com.example.sparin.domain.util.Resource
import com.example.sparin.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * Repository untuk Chat operations
 */
class ChatRepository(
    private val firestoreService: FirestoreService,
    private val authService: FirebaseAuthService
) {
    
    /**
     * Send message
     */
    suspend fun sendMessage(
        roomId: String,
        text: String,
        senderName: String,
        senderPhoto: String
    ): Resource<Unit> {
        return try {
            val userId = authService.getCurrentUserId()
                ?: return Resource.Error("User not authenticated")
            
            val message = ChatMessage(
                roomId = roomId,
                senderId = userId,
                senderName = senderName,
                senderPhoto = senderPhoto,
                text = text,
                timestamp = System.currentTimeMillis(),
                type = "text"
            )
            
            val messageId = firestoreService.createDocument(
                "${Constants.Collections.CHATS}/$roomId/${Constants.Collections.MESSAGES}",
                message
            )
            
            // Update message dengan ID-nya
            firestoreService.updateDocument(
                "${Constants.Collections.CHATS}/$roomId/${Constants.Collections.MESSAGES}",
                messageId,
                mapOf("id" to messageId)
            )
            
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to send message")
        }
    }
    
    /**
     * Observe messages for a room (real-time)
     */
    fun observeMessages(roomId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listener = FirebaseFirestore.getInstance()
            .collection(Constants.Collections.CHATS)
            .document(roomId)
            .collection(Constants.Collections.MESSAGES)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val messages = snapshot?.documents?.mapNotNull {
                    it.toObject(ChatMessage::class.java)
                } ?: emptyList()
                
                trySend(messages)
            }
        
        awaitClose { listener.remove() }
    }
    
    /**
     * Get chat rooms for user
     */
    suspend fun getChatRooms(userId: String): Resource<List<ChatRoom>> {
        return try {
            // TODO: Implement proper chat room listing
            // For now, return empty list
            Resource.Success(emptyList())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get chat rooms")
        }
    }
}
