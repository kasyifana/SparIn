package com.example.sparin.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*

/**
 * ChatItem - Chat room item for chat list
 * Following SparIN Design System
 * 
 * Design Rules:
 * - Rounded card with shadow
 * - Sport emoji as avatar
 * - Room name in Lead color
 * - Last message preview in Warm Haze
 * - Unread badge in Crunch color
 */
@Composable
fun ChatItem(
    roomName: String,
    lastMessage: String,
    lastMessageTime: String,
    sportEmoji: String,
    unreadCount: Int = 0,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Define colors based on SparIN Design System
    val cascadingWhite = Color(0xFFF6F6F6)
    val chineseSilver = Color(0xFFE0DBF3)
    val crunch = Color(0xFFF3BA60)
    val dreamland = Color(0xFFB6B1C0)
    val warmHaze = Color(0xFF736A6A)
    val lead = Color(0xFF202022)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = dreamland.copy(alpha = 0.15f),
                spotColor = dreamland.copy(alpha = 0.15f)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sport emoji avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = CircleShape,
                        ambientColor = chineseSilver.copy(alpha = 0.3f)
                    )
                    .background(
                        color = chineseSilver.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = sportEmoji,
                    fontSize = 28.sp
                )
            }

            // Chat info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Room name
                Text(
                    text = roomName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = lead,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Last message
                Text(
                    text = lastMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                    color = warmHaze,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Right side: time and unread badge
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Time
                Text(
                    text = lastMessageTime,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp
                    ),
                    color = warmHaze.copy(alpha = 0.7f)
                )

                // Unread badge
                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .background(
                                color = crunch,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = lead
                        )
                    }
                }
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun ChatItemPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .padding(16.dp)
        ) {
            ChatItem(
                roomName = "Badminton Weekend Warriors",
                lastMessage = "Perfect! See you tomorrow guys! 🏸",
                lastMessageTime = "10:30",
                sportEmoji = "🏸",
                unreadCount = 3,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatItemNoUnreadPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .padding(16.dp)
        ) {
            ChatItem(
                roomName = "Futsal Champions League",
                lastMessage = "Jangan lupa bawa sepatu futsal ya!",
                lastMessageTime = "Yesterday",
                sportEmoji = "⚽",
                unreadCount = 0,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatItemListPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .padding(vertical = 16.dp)
        ) {
            ChatItem(
                roomName = "Badminton Weekend Warriors",
                lastMessage = "Perfect! See you tomorrow guys! 🏸",
                lastMessageTime = "10:30",
                sportEmoji = "🏸",
                unreadCount = 3,
                onClick = {}
            )
            ChatItem(
                roomName = "Futsal Champions League",
                lastMessage = "Jangan lupa bawa sepatu futsal ya!",
                lastMessageTime = "Yesterday",
                sportEmoji = "⚽",
                unreadCount = 0,
                onClick = {}
            )
            ChatItem(
                roomName = "Basketball Pro League",
                lastMessage = "Game dimulai jam 7 malam",
                lastMessageTime = "2 days ago",
                sportEmoji = "🏀",
                unreadCount = 15,
                onClick = {}
            )
        }
    }
}
