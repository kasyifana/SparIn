package com.example.sparin.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sparin.presentation.chat.components.ChatItem
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*

/**
 * Chat List Screen - Displays list of chat rooms
 * Following SparIN Design System
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavHostController) {
    // Define colors based on SparIN Design System
    val cascadingWhite = Color(0xFFF6F6F6)
    val chineseSilver = Color(0xFFE0DBF3)
    val crunch = Color(0xFFF3BA60)
    val dreamland = Color(0xFFB6B1C0)
    val warmHaze = Color(0xFF736A6A)
    val lead = Color(0xFF202022)

    // Mock data - Replace with ViewModel later
    val chatRooms = listOf(
        ChatRoomItem(
            id = "room1",
            name = "Badminton Weekend Warriors",
            lastMessage = "Perfect! See you tomorrow guys! 🏸",
            lastMessageTime = "10:30",
            sportEmoji = "🏸",
            unreadCount = 3
        ),
        ChatRoomItem(
            id = "room2",
            name = "Futsal Champions League",
            lastMessage = "Jangan lupa bawa sepatu futsal ya!",
            lastMessageTime = "Yesterday",
            sportEmoji = "⚽",
            unreadCount = 0
        ),
        ChatRoomItem(
            id = "room3",
            name = "Basketball Pro League",
            lastMessage = "Game dimulai jam 7 malam",
            lastMessageTime = "2 days ago",
            sportEmoji = "🏀",
            unreadCount = 15
        ),
        ChatRoomItem(
            id = "room4",
            name = "Tennis Masters",
            lastMessage = "Court sudah dibooking!",
            lastMessageTime = "3 days ago",
            sportEmoji = "🎾",
            unreadCount = 1
        ),
        ChatRoomItem(
            id = "room5",
            name = "Volleyball Beach Party",
            lastMessage = "Siapa yang bawa bola?",
            lastMessageTime = "1 week ago",
            sportEmoji = "🏐",
            unreadCount = 0
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        cascadingWhite,
                        chineseSilver.copy(alpha = 0.1f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = "Chats",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = lead
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            // Chat List
            if (chatRooms.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "💬",
                            fontSize = 64.sp
                        )
                        Text(
                            text = "No chats yet",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = lead
                        )
                        Text(
                            text = "Join a room to start chatting!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = warmHaze
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(chatRooms) { chatRoom ->
                        ChatItem(
                            roomName = chatRoom.name,
                            lastMessage = chatRoom.lastMessage,
                            lastMessageTime = chatRoom.lastMessageTime,
                            sportEmoji = chatRoom.sportEmoji,
                            unreadCount = chatRoom.unreadCount,
                            onClick = {
                                // Navigate to chat room
                                navController.navigate(Screen.ChatRoom.createRoute(chatRoom.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

// ==================== Data Model ====================

data class ChatRoomItem(
    val id: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val sportEmoji: String,
    val unreadCount: Int = 0
)

// ==================== PREVIEW ====================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ChatListScreenPreview() {
    MaterialTheme {
        ChatListScreen(navController = rememberNavController())
    }
}
