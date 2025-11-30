package com.example.sparin.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEmotions
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*

/**
 * MessageInput - Message input bar component
 * Following SparIN Design System
 * 
 * Design Rules:
 * - Floating card with rounded corners (22-28dp)
 * - Background: Cascading White (#F6F6F6)
 * - Send button with Crunch (#F3BA60)
 * - Subtle shadow using Dreamland
 * - Includes emoji button and send button
 */
@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onEmojiClick: () -> Unit = {},
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
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                ambientColor = dreamland.copy(alpha = 0.2f),
                spotColor = dreamland.copy(alpha = 0.2f)
            )
            .background(
                color = cascadingWhite,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Emoji button
            IconButton(
                onClick = onEmojiClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.EmojiEmotions,
                    contentDescription = "Emoji",
                    tint = warmHaze,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Text input field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = dreamland.copy(alpha = 0.1f)
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = "Type a message...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp
                        ),
                        color = warmHaze.copy(alpha = 0.5f)
                    )
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        color = lead,
                        fontWeight = FontWeight.Normal
                    ),
                    cursorBrush = SolidColor(crunch),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Send button
            IconButton(
                onClick = {
                    if (value.isNotBlank()) {
                        onSendClick()
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .shadow(
                        elevation = if (value.isNotBlank()) 6.dp else 0.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = crunch.copy(alpha = 0.3f),
                        spotColor = crunch.copy(alpha = 0.3f)
                    )
                    .background(
                        color = if (value.isNotBlank()) crunch else chineseSilver.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "Send",
                    tint = if (value.isNotBlank()) lead else warmHaze.copy(alpha = 0.5f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun MessageInputEmptyPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .padding(16.dp)
        ) {
            MessageInput(
                value = "",
                onValueChange = {},
                onSendClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageInputWithTextPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .padding(16.dp)
        ) {
            MessageInput(
                value = "Halo! Siap main badminton besok?",
                onValueChange = {},
                onSendClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageInputLongTextPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .padding(16.dp)
        ) {
            MessageInput(
                value = "Halo semua! Siap main badminton besok? Jangan lupa bawa raket dan shuttlecock ya!",
                onValueChange = {},
                onSendClick = {}
            )
        }
    }
}
