package com.example.sparin.data.model

/**
 * ChatMessage data class untuk pesan chat
 */
data class ChatMessage(
    val id: String = "",
    val roomId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderPhoto: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "text", // text, system, image
    val imageUrl: String? = null
)

/**
 * ChatRoom data class untuk list chat rooms
 */
data class ChatRoom(
    val id: String = "",
    val roomName: String = "",
    val roomCategory: String = "",
    val members: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Long = 0,
    val unreadCount: Int = 0
)
