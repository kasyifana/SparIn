package com.example.sparin.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * MessageBubble - Chat message bubble component
 * Following SparIN Design System
 * 
 * Design Rules:
 * - Current user messages: RIGHT aligned, Crunch background
 * - Other user messages: LEFT aligned, Cascading White background with shadow
 * - Rounded corners: 20-24dp
 * - Timestamp in Warm Haze color
 */
@Composable
fun MessageBubble(
    text: String,
    senderName: String,
    timestamp: Long,
    isCurrentUser: Boolean,
    senderAvatar: String? = null,
    modifier: Modifier = Modifier
) {
    // Define colors based on SparIN Design System
    val cascadingWhite = Color(0xFFF6F6F6)
    val chineseSilver = Color(0xFFE0DBF3)
    val crunch = Color(0xFFF3BA60)
    val dreamland = Color(0xFFB6B1C0)
    val warmHaze = Color(0xFF736A6A)
    val lead = Color(0xFF202022)

    val bubbleColor = if (isCurrentUser) crunch else cascadingWhite
    val textColor = if (isCurrentUser) lead else warmHaze
    val alignment = if (isCurrentUser) Alignment.End else Alignment.Start

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalAlignment = alignment
    ) {
        // Show sender name for other users
        if (!isCurrentUser) {
            Text(
                text = senderName,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                ),
                color = warmHaze,
                modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
            )
        }

        // Message bubble
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .shadow(
                    elevation = if (isCurrentUser) 2.dp else 4.dp,
                    shape = RoundedCornerShape(
                        topStart = if (isCurrentUser) 22.dp else 6.dp,
                        topEnd = if (isCurrentUser) 6.dp else 22.dp,
                        bottomStart = 22.dp,
                        bottomEnd = 22.dp
                    ),
                    ambientColor = dreamland.copy(alpha = 0.15f),
                    spotColor = dreamland.copy(alpha = 0.15f)
                )
                .background(
                    color = bubbleColor,
                    shape = RoundedCornerShape(
                        topStart = if (isCurrentUser) 22.dp else 6.dp,
                        topEnd = if (isCurrentUser) 6.dp else 22.dp,
                        bottomStart = 22.dp,
                        bottomEnd = 22.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                // Message text
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    ),
                    color = textColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Timestamp
                Text(
                    text = formatTimestamp(timestamp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp
                    ),
                    color = warmHaze.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

/**
 * Format timestamp to readable time
 */
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60000 -> "Just now" // Less than 1 minute
        diff < 3600000 -> "${diff / 60000}m ago" // Less than 1 hour
        diff < 86400000 -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp)) // Today
        else -> SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(timestamp)) // Older
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun MessageBubbleCurrentUserPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .padding(16.dp)
        ) {
            MessageBubble(
                text = "Perfect! See you tomorrow guys! 🏸",
                senderName = "You",
                timestamp = System.currentTimeMillis() - 300000,
                isCurrentUser = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageBubbleOtherUserPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .padding(16.dp)
        ) {
            MessageBubble(
                text = "Halo semua! Siap main badminton besok? Jangan lupa bawa raket ya!",
                senderName = "Reza Pratama",
                timestamp = System.currentTimeMillis() - 3600000,
                isCurrentUser = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageBubbleConversationPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .padding(vertical = 16.dp)
        ) {
            MessageBubble(
                text = "Halo! Siap main badminton besok?",
                senderName = "Reza Pratama",
                timestamp = System.currentTimeMillis() - 3600000,
                isCurrentUser = false
            )
            MessageBubble(
                text = "Siap! Jam berapa kita mulai?",
                senderName = "You",
                timestamp = System.currentTimeMillis() - 3500000,
                isCurrentUser = true
            )
            MessageBubble(
                text = "Jam 8 pagi ya, jangan telat!",
                senderName = "Dina Marlina",
                timestamp = System.currentTimeMillis() - 3400000,
                isCurrentUser = false
            )
            MessageBubble(
                text = "Oke noted! 👍",
                senderName = "You",
                timestamp = System.currentTimeMillis() - 300000,
                isCurrentUser = true
            )
        }
    }
}
