package com.example.sparin.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sparin.presentation.chat.components.MessageBubble
import com.example.sparin.presentation.chat.components.MessageInput
import com.example.sparin.ui.theme.*
import kotlinx.coroutines.launch

/**
 * ChatRoomScreen - Main chat room screen for SparIN
 * Following SparIN Design System with premium Gen-Z aesthetic
 * 
 * Design Rules:
 * - Background: Cascading White (#F6F6F6)
 * - TopBar: Chinese Silver background
 * - Messages: Auto-scroll to bottom
 * - Current user messages: RIGHT aligned with Crunch background
 * - Other user messages: LEFT aligned with shadow
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    navController: NavHostController,
    roomId: String? = null,
    viewModel: ChatRoomViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val inputText by viewModel.inputText.collectAsState()

    // Define colors based on SparIN Design System
    val cascadingWhite = Color(0xFFF6F6F6)
    val chineseSilver = Color(0xFFE0DBF3)
    val crunch = Color(0xFFF3BA60)
    val dreamland = Color(0xFFB6B1C0)
    val warmHaze = Color(0xFF736A6A)
    val lead = Color(0xFF202022)

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
        when (val state = uiState) {
            is ChatRoomUiState.Loading -> {
                LoadingState()
            }
            is ChatRoomUiState.Success -> {
                ChatRoomContent(
                    roomName = state.roomName,
                    participantsCount = state.participantsCount,
                    messages = state.messages,
                    inputText = inputText,
                    onInputChange = { viewModel.updateInputText(it) },
                    onSendClick = { viewModel.sendMessage() },
                    onBackClick = { navController.navigateUp() }
                )
            }
            is ChatRoomUiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { viewModel.retry() },
                    onBackClick = { navController.navigateUp() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatRoomContent(
    roomName: String,
    participantsCount: Int,
    messages: List<ChatMessage>,
    inputText: String,
    onInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit
) {
    // Define colors
    val cascadingWhite = Color(0xFFF6F6F6)
    val chineseSilver = Color(0xFFE0DBF3)
    val crunch = Color(0xFFF3BA60)
    val dreamland = Color(0xFFB6B1C0)
    val warmHaze = Color(0xFF736A6A)
    val lead = Color(0xFF202022)

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll to bottom when new message arrives
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = roomName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = lead
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Group,
                            contentDescription = null,
                            tint = warmHaze,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "$participantsCount participants",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp
                            ),
                            color = warmHaze
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = lead
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = chineseSilver.copy(alpha = 0.6f)
            ),
            modifier = Modifier.shadow(
                elevation = 4.dp,
                ambientColor = dreamland.copy(alpha = 0.15f)
            )
        )

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp),
            reverseLayout = false
        ) {
            items(messages, key = { it.id }) { message ->
                MessageBubble(
                    text = message.text,
                    senderName = message.senderName,
                    timestamp = message.timestamp,
                    isCurrentUser = message.isCurrentUser,
                    senderAvatar = message.senderAvatar
                )
            }
        }

        // Message Input Bar
        MessageInput(
            value = inputText,
            onValueChange = onInputChange,
            onSendClick = onSendClick,
            onEmojiClick = {
                // TODO: Implement emoji picker
            }
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Color(0xFFF3BA60), // Crunch
                strokeWidth = 4.dp
            )
            Text(
                text = "Loading chat...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF736A6A) // Warm Haze
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    val cascadingWhite = Color(0xFFF6F6F6)
    val chineseSilver = Color(0xFFE0DBF3)
    val crunch = Color(0xFFF3BA60)
    val warmHaze = Color(0xFF736A6A)
    val lead = Color(0xFF202022)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Chat Room",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = lead
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = lead
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = chineseSilver.copy(alpha = 0.6f)
            )
        )

        // Error content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "😕",
                    fontSize = 64.sp
                )
                Text(
                    text = "Oops!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = lead
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = warmHaze,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = crunch,
                        contentColor = lead
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = crunch.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "Try Again",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ChatRoomScreenPreview() {
    MaterialTheme {
        ChatRoomScreen(
            navController = rememberNavController(),
            roomId = "room123"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomContentPreview() {
    MaterialTheme {
        ChatRoomContent(
            roomName = "Badminton Weekend Warriors",
            participantsCount = 8,
            messages = listOf(
                ChatMessage(
                    id = "1",
                    senderId = "user2",
                    senderName = "Reza Pratama",
                    text = "Halo semua! Siap main badminton besok?",
                    timestamp = System.currentTimeMillis() - 3600000,
                    isCurrentUser = false
                ),
                ChatMessage(
                    id = "2",
                    senderId = "currentUser",
                    senderName = "You",
                    text = "Siap! Jam berapa kita mulai?",
                    timestamp = System.currentTimeMillis() - 3500000,
                    isCurrentUser = true
                ),
                ChatMessage(
                    id = "3",
                    senderId = "user3",
                    senderName = "Dina Marlina",
                    text = "Jam 8 pagi ya, jangan telat!",
                    timestamp = System.currentTimeMillis() - 3400000,
                    isCurrentUser = false
                )
            ),
            inputText = "",
            onInputChange = {},
            onSendClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomLoadingPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
        ) {
            LoadingState()
        }
    }
}
