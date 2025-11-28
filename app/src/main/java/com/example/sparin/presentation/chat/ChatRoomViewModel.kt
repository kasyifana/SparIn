package com.example.sparin.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

/**
 * ChatRoomViewModel - Manages chat room state
 * Following MVVM architecture pattern for SparIN
 */
class ChatRoomViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ChatRoomUiState>(ChatRoomUiState.Loading)
    val uiState: StateFlow<ChatRoomUiState> = _uiState.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    init {
        loadChatRoom()
    }

    private fun loadChatRoom() {
        viewModelScope.launch {
            _uiState.value = ChatRoomUiState.Loading

            // Simulate data loading - Replace with actual repository calls
            try {
                // Mock data for demonstration
                val messages = listOf(
                    ChatMessage(
                        id = "1",
                        senderId = "user2",
                        senderName = "Reza Pratama",
                        text = "Halo semua! Siap main badminton besok?",
                        timestamp = System.currentTimeMillis() - 3600000,
                        isCurrentUser = false,
                        senderAvatar = null
                    ),
                    ChatMessage(
                        id = "2",
                        senderId = "currentUser",
                        senderName = "You",
                        text = "Siap! Jam berapa kita mulai?",
                        timestamp = System.currentTimeMillis() - 3500000,
                        isCurrentUser = true,
                        senderAvatar = null
                    ),
                    ChatMessage(
                        id = "3",
                        senderId = "user3",
                        senderName = "Dina Marlina",
                        text = "Jam 8 pagi ya, jangan telat!",
                        timestamp = System.currentTimeMillis() - 3400000,
                        isCurrentUser = false,
                        senderAvatar = null
                    ),
                    ChatMessage(
                        id = "4",
                        senderId = "currentUser",
                        senderName = "You",
                        text = "Oke noted! Lokasi di GOR Senayan kan?",
                        timestamp = System.currentTimeMillis() - 3300000,
                        isCurrentUser = true,
                        senderAvatar = null
                    ),
                    ChatMessage(
                        id = "5",
                        senderId = "user2",
                        senderName = "Reza Pratama",
                        text = "Betul, lapangan 3. Jangan lupa bawa raket sendiri ya!",
                        timestamp = System.currentTimeMillis() - 3200000,
                        isCurrentUser = false,
                        senderAvatar = null
                    ),
                    ChatMessage(
                        id = "6",
                        senderId = "user4",
                        senderName = "Aldo Wijaya",
                        text = "Gue bawa shuttlecock extra, siapa tau habis 😄",
                        timestamp = System.currentTimeMillis() - 3100000,
                        isCurrentUser = false,
                        senderAvatar = null
                    ),
                    ChatMessage(
                        id = "7",
                        senderId = "currentUser",
                        senderName = "You",
                        text = "Perfect! See you tomorrow guys! 🏸",
                        timestamp = System.currentTimeMillis() - 3000000,
                        isCurrentUser = true,
                        senderAvatar = null
                    )
                )

                _uiState.value = ChatRoomUiState.Success(
                    roomName = "Badminton Weekend Warriors",
                    participantsCount = 8,
                    messages = messages
                )
            } catch (e: Exception) {
                _uiState.value = ChatRoomUiState.Error(
                    message = e.message ?: "Failed to load chat room"
                )
            }
        }
    }

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun sendMessage() {
        val currentState = _uiState.value
        if (currentState !is ChatRoomUiState.Success) return

        val messageText = _inputText.value.trim()
        if (messageText.isEmpty()) return

        viewModelScope.launch {
            try {
                // Create new message
                val newMessage = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    senderId = "currentUser",
                    senderName = "You",
                    text = messageText,
                    timestamp = System.currentTimeMillis(),
                    isCurrentUser = true,
                    senderAvatar = null
                )

                // Update messages list
                val updatedMessages = currentState.messages + newMessage

                _uiState.value = currentState.copy(messages = updatedMessages)

                // Clear input
                _inputText.value = ""

                // TODO: Send to repository/Firebase
                // sendMessageUseCase(roomId, messageText)
            } catch (e: Exception) {
                // Handle error
                println("Error sending message: ${e.message}")
            }
        }
    }

    fun retry() {
        loadChatRoom()
    }
}

// ==================== UI State ====================

sealed class ChatRoomUiState {
    object Loading : ChatRoomUiState()
    data class Success(
        val roomName: String,
        val participantsCount: Int,
        val messages: List<ChatMessage>
    ) : ChatRoomUiState()
    data class Error(val message: String) : ChatRoomUiState()
}

// ==================== Data Models ====================

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: Long,
    val isCurrentUser: Boolean,
    val senderAvatar: String? = null
)
