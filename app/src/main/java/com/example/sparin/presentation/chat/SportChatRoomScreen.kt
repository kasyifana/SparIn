package com.example.sparin.presentation.chat

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Sport Chat Room Screen
 * Supports both Casual and Competitive modes with different aesthetics
 * 
 * Casual: Pastel colors, soft shadows, friendly vibe
 * Competitive: Dark theme, neon red accents, intense vibe
 */

// ==================== COLOR PALETTES ====================

// Casual Colors
private val CasualBabyBlue = Color(0xFF74C9FF)
private val CasualPastelPurple = Color(0xFFA78BFA)
private val CasualPastelGreen = Color(0xFF7EE5B2)
private val CasualOffWhite = Color(0xFFF8F9FC)
private val CasualGlassWhite = Color(0xFFFFFFFF)

// Competitive Colors
private val CompDeepRed = Color(0xFFEF4444)
private val CompCrimson = Color(0xFFDC2626)
private val CompCharcoalBlack = Color(0xFF0E0E0E)
private val CompDarkGrey = Color(0xFF1A1A1A)
private val CompNeonRed = Color(0xFFFF3B3B)
private val CompMetallicDark = Color(0xFF2A2A2A)

// Common Colors
private val Lead = Color(0xFF202022)
private val WarmHaze = Color(0xFF736A6A)

// ==================== DATA CLASSES ====================

data class ChatMember(
    val id: String,
    val name: String,
    val avatar: String? = null,
    val isHost: Boolean = false,
    val isReady: Boolean = false,
    val isOnline: Boolean = true,
    val skillLevel: String = "Beginner"
)

data class SportChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: Long,
    val isCurrentUser: Boolean,
    val isSystemMessage: Boolean = false,
    val messageType: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT,
    SYSTEM,
    LOCATION,
    READY_STATUS,
    MATCH_REMINDER
}

enum class ReadyStatus {
    READY,
    NOT_READY,
    PENDING
}

// ==================== MAIN SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportChatRoomScreen(
    navController: NavHostController,
    roomId: String?,
    mode: String = "casual", // "casual" or "competitive"
    roomTitle: String = "Chat Room",
    sport: String = "Sport",
    sportEmoji: String = "🏃"
) {
    val isCompetitive = mode == "competitive"
    
    // State
    var inputText by remember { mutableStateOf("") }
    var showMembersModal by remember { mutableStateOf(false) }
    var showLocationModal by remember { mutableStateOf(false) }
    var myReadyStatus by remember { mutableStateOf(ReadyStatus.PENDING) }
    
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    // Sample data - Members list reacts to myReadyStatus changes
    val members by remember(myReadyStatus) {
        derivedStateOf {
            listOf(
                ChatMember("1", "ProPlayer99", isHost = true, isReady = true, skillLevel = "Advanced"),
                ChatMember("2", "Anya", isReady = true, skillLevel = "Intermediate"),
                ChatMember("3", "Budi", isReady = false, skillLevel = "Beginner"),
                ChatMember("4", "You", isReady = myReadyStatus == ReadyStatus.READY, skillLevel = "Intermediate")
            )
        }
    }
    
    val messages = remember {
        mutableStateListOf(
            SportChatMessage(
                id = "sys1",
                senderId = "system",
                senderName = "System",
                text = "Welcome to the room! 🎉",
                timestamp = System.currentTimeMillis() - 3600000,
                isCurrentUser = false,
                isSystemMessage = true,
                messageType = MessageType.SYSTEM
            ),
            SportChatMessage(
                id = "1",
                senderId = "user1",
                senderName = "ProPlayer99",
                text = "Hey everyone! Ready for the match?",
                timestamp = System.currentTimeMillis() - 3500000,
                isCurrentUser = false
            ),
            SportChatMessage(
                id = "2",
                senderId = "user2",
                senderName = "Anya",
                text = "Yes! Can't wait 🔥",
                timestamp = System.currentTimeMillis() - 3400000,
                isCurrentUser = false
            ),
            SportChatMessage(
                id = "sys2",
                senderId = "system",
                senderName = "System",
                text = "📍 Location has been shared",
                timestamp = System.currentTimeMillis() - 3300000,
                isCurrentUser = false,
                isSystemMessage = true,
                messageType = MessageType.LOCATION
            ),
            SportChatMessage(
                id = "3",
                senderId = "currentUser",
                senderName = "You",
                text = "On my way! See you in 30 mins",
                timestamp = System.currentTimeMillis() - 3200000,
                isCurrentUser = true
            )
        )
    }
    
    // Auto scroll
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isCompetitive) CompCharcoalBlack else CasualOffWhite)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            SportChatHeader(
                isCompetitive = isCompetitive,
                roomTitle = roomTitle,
                sport = sport,
                sportEmoji = sportEmoji,
                memberCount = members.size,
                onBackClick = { navController.navigateUp() },
                onMembersClick = { showMembersModal = true }
            )
            
            // Quick Action Bar
            QuickActionBar(
                isCompetitive = isCompetitive,
                myReadyStatus = myReadyStatus,
                onReadyToggle = {
                    myReadyStatus = if (myReadyStatus == ReadyStatus.READY) {
                        ReadyStatus.NOT_READY
                    } else {
                        ReadyStatus.READY
                    }
                    // Add system message
                    messages.add(
                        SportChatMessage(
                            id = UUID.randomUUID().toString(),
                            senderId = "system",
                            senderName = "System",
                            text = if (myReadyStatus == ReadyStatus.READY) "✅ You are now READY" else "⏳ You changed status to NOT READY",
                            timestamp = System.currentTimeMillis(),
                            isCurrentUser = false,
                            isSystemMessage = true,
                            messageType = MessageType.READY_STATUS
                        )
                    )
                },
                onLocationClick = { showLocationModal = true },
                onMembersClick = { showMembersModal = true }
            )
            
            // Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    SportMessageBubble(
                        message = message,
                        isCompetitive = isCompetitive
                    )
                }
            }
            
            // Input Bar
            SportChatInput(
                isCompetitive = isCompetitive,
                value = inputText,
                onValueChange = { inputText = it },
                onSendClick = {
                    if (inputText.isNotBlank()) {
                        messages.add(
                            SportChatMessage(
                                id = UUID.randomUUID().toString(),
                                senderId = "currentUser",
                                senderName = "You",
                                text = inputText,
                                timestamp = System.currentTimeMillis(),
                                isCurrentUser = true
                            )
                        )
                        inputText = ""
                    }
                }
            )
        }
        
        // Members Modal
        if (showMembersModal) {
            MembersModal(
                isCompetitive = isCompetitive,
                members = members,
                onDismiss = { showMembersModal = false }
            )
        }
        
        // Location Modal
        if (showLocationModal) {
            LocationConfirmModal(
                isCompetitive = isCompetitive,
                location = "Elite Sports Center, Jakarta",
                onDismiss = { showLocationModal = false },
                onConfirm = {
                    messages.add(
                        SportChatMessage(
                            id = UUID.randomUUID().toString(),
                            senderId = "system",
                            senderName = "System",
                            text = "📍 You confirmed the location",
                            timestamp = System.currentTimeMillis(),
                            isCurrentUser = false,
                            isSystemMessage = true,
                            messageType = MessageType.LOCATION
                        )
                    )
                    showLocationModal = false
                }
            )
        }
    }
}

// ==================== HEADER ====================

@Composable
private fun SportChatHeader(
    isCompetitive: Boolean,
    roomTitle: String,
    sport: String,
    sportEmoji: String,
    memberCount: Int,
    onBackClick: () -> Unit,
    onMembersClick: () -> Unit
) {
    val backgroundColor = if (isCompetitive) CompDarkGrey else CasualGlassWhite.copy(alpha = 0.95f)
    val textColor = if (isCompetitive) Color.White else Lead
    val accentColor = if (isCompetitive) CompNeonRed else CasualBabyBlue
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                ambientColor = if (isCompetitive) CompDeepRed.copy(alpha = 0.2f) else CasualBabyBlue.copy(alpha = 0.15f)
            ),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = textColor
                )
            }
            
            // Sport icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        color = accentColor.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(sportEmoji, fontSize = 22.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Title & info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = roomTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Sport badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = accentColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = sport,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp
                            ),
                            color = accentColor
                        )
                    }
                    Text(
                        text = "•",
                        color = textColor.copy(alpha = 0.5f),
                        fontSize = 10.sp
                    )
                    Text(
                        text = "$memberCount members",
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.6f)
                    )
                }
            }
            
            // Members button
            IconButton(onClick = onMembersClick) {
                Icon(
                    imageVector = Icons.Rounded.Group,
                    contentDescription = "Members",
                    tint = accentColor
                )
            }
        }
    }
}

// ==================== QUICK ACTION BAR ====================

@Composable
private fun QuickActionBar(
    isCompetitive: Boolean,
    myReadyStatus: ReadyStatus,
    onReadyToggle: () -> Unit,
    onLocationClick: () -> Unit,
    onMembersClick: () -> Unit
) {
    val backgroundColor = if (isCompetitive) CompMetallicDark else CasualGlassWhite.copy(alpha = 0.9f)
    val accentColor = if (isCompetitive) CompNeonRed else CasualBabyBlue
    val textColor = if (isCompetitive) Color.White else Lead
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Ready Toggle
            QuickActionButton(
                modifier = Modifier.weight(1f),
                isCompetitive = isCompetitive,
                isActive = myReadyStatus == ReadyStatus.READY,
                icon = if (myReadyStatus == ReadyStatus.READY) "✅" else "⏳",
                text = if (myReadyStatus == ReadyStatus.READY) "Ready" else "Not Ready",
                onClick = onReadyToggle
            )
            
            // Location
            QuickActionButton(
                modifier = Modifier.weight(1f),
                isCompetitive = isCompetitive,
                isActive = false,
                icon = "📍",
                text = "Location",
                onClick = onLocationClick
            )
            
            // Members
            QuickActionButton(
                modifier = Modifier.weight(1f),
                isCompetitive = isCompetitive,
                isActive = false,
                icon = "👥",
                text = "Members",
                onClick = onMembersClick
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    modifier: Modifier = Modifier,
    isCompetitive: Boolean,
    isActive: Boolean,
    icon: String,
    text: String,
    onClick: () -> Unit
) {
    val accentColor = if (isCompetitive) CompNeonRed else CasualBabyBlue
    val backgroundColor = when {
        isActive && isCompetitive -> CompDeepRed.copy(alpha = 0.2f)
        isActive -> CasualPastelGreen.copy(alpha = 0.3f)
        isCompetitive -> CompDarkGrey
        else -> CasualOffWhite
    }
    val borderColor = if (isActive) accentColor else Color.Transparent
    val textColor = if (isCompetitive) Color.White else Lead
    
    Surface(
        onClick = onClick,
        modifier = modifier
            .height(42.dp),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = if (isActive) {
            androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                ),
                color = textColor.copy(alpha = 0.9f),
                maxLines = 1
            )
        }
    }
}

// ==================== MESSAGE BUBBLE ====================

@Composable
private fun SportMessageBubble(
    message: SportChatMessage,
    isCompetitive: Boolean
) {
    val accentColor = if (isCompetitive) CompNeonRed else CasualBabyBlue
    
    when {
        message.isSystemMessage -> {
            // System message - centered
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isCompetitive) CompMetallicDark else CasualBabyBlue.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = message.text,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        ),
                        color = if (isCompetitive) Color.White.copy(alpha = 0.7f) else WarmHaze,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        message.isCurrentUser -> {
            // Current user - right aligned
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Surface(
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 4.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    color = accentColor,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = message.text,
                        modifier = Modifier
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .widthIn(max = 280.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
                Text(
                    text = formatTimestamp(message.timestamp),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isCompetitive) Color.White.copy(alpha = 0.4f) else WarmHaze.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp, end = 4.dp)
                )
            }
        }
        else -> {
            // Other user - left aligned
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // Sender name
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    ),
                    color = accentColor,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
                
                Surface(
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    color = if (isCompetitive) CompMetallicDark else CasualGlassWhite,
                    shadowElevation = 2.dp
                ) {
                    Text(
                        text = message.text,
                        modifier = Modifier
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .widthIn(max = 280.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isCompetitive) Color.White else Lead
                    )
                }
                Text(
                    text = formatTimestamp(message.timestamp),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isCompetitive) Color.White.copy(alpha = 0.4f) else WarmHaze.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
            }
        }
    }
}

// ==================== INPUT BAR ====================

@Composable
private fun SportChatInput(
    isCompetitive: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val backgroundColor = if (isCompetitive) CompDarkGrey else CasualGlassWhite
    val inputBgColor = if (isCompetitive) CompMetallicDark else CasualOffWhite
    val accentColor = if (isCompetitive) CompNeonRed else CasualBabyBlue
    val textColor = if (isCompetitive) Color.White else Lead
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Emoji button
            IconButton(
                onClick = { /* TODO: Emoji picker */ },
                modifier = Modifier.size(40.dp)
            ) {
                Text("😊", fontSize = 22.sp)
            }
            
            // Input field
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                color = inputBgColor
            ) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Type a message...",
                            color = textColor.copy(alpha = 0.5f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = accentColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    singleLine = true
                )
            }
            
            // Send button
            val canSend = value.isNotBlank()
            Surface(
                onClick = { if (canSend) onSendClick() },
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = if (canSend) accentColor else accentColor.copy(alpha = 0.3f),
                shadowElevation = if (canSend) 6.dp else 0.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ==================== MEMBERS MODAL ====================

@Composable
private fun MembersModal(
    isCompetitive: Boolean,
    members: List<ChatMember>,
    onDismiss: () -> Unit
) {
    val backgroundColor = if (isCompetitive) CompDarkGrey else CasualGlassWhite
    val accentColor = if (isCompetitive) CompNeonRed else CasualBabyBlue
    val textColor = if (isCompetitive) Color.White else Lead
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { },
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = backgroundColor
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Handle
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                textColor.copy(alpha = 0.3f),
                                RoundedCornerShape(2.dp)
                            )
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Title
                    Text(
                        text = "Room Members",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = textColor
                    )
                    
                    Text(
                        text = "${members.size} participants",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor.copy(alpha = 0.6f)
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Members list
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(members) { member ->
                            MemberItem(
                                member = member,
                                isCompetitive = isCompetitive
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberItem(
    member: ChatMember,
    isCompetitive: Boolean
) {
    val accentColor = if (isCompetitive) CompNeonRed else CasualBabyBlue
    val backgroundColor = if (isCompetitive) CompMetallicDark else CasualOffWhite
    val textColor = if (isCompetitive) Color.White else Lead
    
    val statusColor = when {
        member.isReady -> CasualPastelGreen
        !member.isOnline -> Color.Gray
        else -> Color(0xFFFFB800)
    }
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        accentColor.copy(alpha = 0.2f),
                        CircleShape
                    )
                    .border(
                        width = if (member.isHost) 2.dp else 0.dp,
                        color = if (member.isHost) accentColor else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.name.take(2).uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = accentColor
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = member.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = textColor
                    )
                    if (member.isHost) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = accentColor
                        ) {
                            Text(
                                text = "HOST",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 8.sp
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
                Text(
                    text = member.skillLevel,
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor.copy(alpha = 0.6f)
                )
            }
            
            // Status indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(statusColor, CircleShape)
                )
                Text(
                    text = when {
                        member.isReady -> "Ready"
                        !member.isOnline -> "Offline"
                        else -> "Not Ready"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor
                )
            }
        }
    }
}

// ==================== LOCATION MODAL ====================

@Composable
private fun LocationConfirmModal(
    isCompetitive: Boolean,
    location: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val backgroundColor = if (isCompetitive) CompDarkGrey else CasualGlassWhite
    val accentColor = if (isCompetitive) CompNeonRed else CasualBabyBlue
    val textColor = if (isCompetitive) Color.White else Lead
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = backgroundColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("📍", fontSize = 48.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Confirm Location",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = textColor
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            textColor.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            "Cancel",
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }
                    
                    // Confirm
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor
                        )
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                }
            }
        }
    }
}

// ==================== UTILITIES ====================

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
