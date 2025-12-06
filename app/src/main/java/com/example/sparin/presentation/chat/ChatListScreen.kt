package com.example.sparin.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sparin.presentation.navigation.Screen

/**
 * Chat List Screen - Modern Sporty Design
 * Colorful gradient header with SparIN brand colors
 * Functional search and clickable sport categories
 */

// ==================== COLOR PALETTE (SparIN Brand) ====================

// Primary Brand Colors
private val Crunch = Color(0xFFF3BA60)           // CTA utama, highlight
private val ChineseSilver = Color(0xFFE0DBF3)   // Background playful
private val Dreamland = Color(0xFFB6B1C0)       // Border, secondary

// Neutral Colors
private val Lead = Color(0xFF202022)             // Text dark
private val CascadingWhite = Color(0xFFF6F6F6)  // Background light
private val WarmHaze = Color(0xFF736A6A)        // Subtitle

// Accent Colors - Vibrant & Bold
private val SportyCyan = Color(0xFF6FEDD6)      // Cards, badges
private val NeonLime = Color(0xFFC8FF00)        // High-energy CTA
private val VibrantPurple = Color(0xFF8B5CF6)   // Premium features
private val SoftPeach = Color(0xFFFFD4B8)       // Warm accents

// Additional Vibrant Colors
private val EnergyOrange = Color(0xFFFF6B35)    // Energetic accent
private val ElectricBlue = Color(0xFF00D4FF)    // Cool accent
private val HotPink = Color(0xFFFF2D78)         // Bold accent
private val OnlineGreen = Color(0xFF22C55E)     // Status online

// ==================== DATA MODELS ====================

data class ChatRoomItem(
    val id: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val sportEmoji: String,
    val sportType: String = "",
    val unreadCount: Int = 0,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false
)

data class SportCategory(
    val id: String,
    val name: String,
    val emoji: String,
    val roomId: String,
    val gradientColors: List<Color>,
    val isAddButton: Boolean = false
)

// ==================== MAIN SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    
    // Sport categories with direct navigation to rooms
    val sportCategories = listOf(
        SportCategory(
            id = "badminton", 
            name = "Badminton", 
            emoji = "🏸", 
            roomId = "room1",
            gradientColors = listOf(SportyCyan, ElectricBlue)
        ),
        SportCategory(
            id = "futsal", 
            name = "Futsal", 
            emoji = "⚽", 
            roomId = "room2",
            gradientColors = listOf(NeonLime, SportyCyan)
        ),
        SportCategory(
            id = "basket", 
            name = "Basket", 
            emoji = "🏀", 
            roomId = "room3",
            gradientColors = listOf(EnergyOrange, Crunch)
        ),
        SportCategory(
            id = "tennis", 
            name = "Tennis", 
            emoji = "🎾", 
            roomId = "room4",
            gradientColors = listOf(NeonLime, EnergyOrange)
        ),
        SportCategory(
            id = "voli", 
            name = "Voli", 
            emoji = "🏐", 
            roomId = "room5",
            gradientColors = listOf(VibrantPurple, HotPink)
        )
    )

    // Mock data for chat rooms - Group/Server names (Extended list for scroll testing)
    val allChatRooms = listOf(
        ChatRoomItem(
            id = "room1",
            name = "Badminton Weekend Warriors",
            lastMessage = "Let's play this weekend!",
            lastMessageTime = "Just Now",
            sportEmoji = "🏸",
            sportType = "badminton",
            unreadCount = 0,
            isOnline = true
        ),
        ChatRoomItem(
            id = "room2",
            name = "Futsal Champions League",
            lastMessage = "👍 Ready at the field!",
            lastMessageTime = "3:24 pm",
            sportEmoji = "⚽",
            sportType = "futsal",
            unreadCount = 2,
            isOnline = true
        ),
        ChatRoomItem(
            id = "room3",
            name = "Basketball Pro Squad",
            lastMessage = "I'll be a bit late, wait for me",
            lastMessageTime = "Yesterday",
            sportEmoji = "🏀",
            sportType = "basket",
            unreadCount = 0,
            isOnline = false
        ),
        ChatRoomItem(
            id = "room4",
            name = "Tennis Masters Club",
            lastMessage = "Court is booked for tomorrow!",
            lastMessageTime = "Yesterday",
            sportEmoji = "🎾",
            sportType = "tennis",
            unreadCount = 1,
            isOnline = false
        ),
        ChatRoomItem(
            id = "room5",
            name = "Volleyball Beach Party",
            lastMessage = "Ready for beach volley this week?",
            lastMessageTime = "2 days ago",
            sportEmoji = "🏐",
            sportType = "voli",
            unreadCount = 0,
            isOnline = true
        ),
        ChatRoomItem(
            id = "room6",
            name = "Swimming Dolphins Club",
            lastMessage = "Pool session at 6 AM tomorrow!",
            lastMessageTime = "2 days ago",
            sportEmoji = "🏊",
            sportType = "swimming",
            unreadCount = 5,
            isOnline = true
        ),
        ChatRoomItem(
            id = "room7",
            name = "Running Marathon Crew",
            lastMessage = "5K run this Sunday, who's in?",
            lastMessageTime = "3 days ago",
            sportEmoji = "🏃",
            sportType = "running",
            unreadCount = 0,
            isOnline = false
        ),
        ChatRoomItem(
            id = "room8",
            name = "Cycling Adventure Team",
            lastMessage = "New route discovered! Check the map",
            lastMessageTime = "3 days ago",
            sportEmoji = "🚴",
            sportType = "cycling",
            unreadCount = 3,
            isOnline = true
        ),
        ChatRoomItem(
            id = "room9",
            name = "Golf Legends Club",
            lastMessage = "Tee time at 7 AM Saturday",
            lastMessageTime = "4 days ago",
            sportEmoji = "⛳",
            sportType = "golf",
            unreadCount = 0,
            isOnline = false
        ),
        ChatRoomItem(
            id = "room10",
            name = "Boxing Fighters Arena",
            lastMessage = "Sparring session tonight!",
            lastMessageTime = "4 days ago",
            sportEmoji = "🥊",
            sportType = "boxing",
            unreadCount = 1,
            isOnline = true
        ),
        ChatRoomItem(
            id = "room11",
            name = "Yoga & Meditation Group",
            lastMessage = "Morning yoga at the park 🧘",
            lastMessageTime = "5 days ago",
            sportEmoji = "🧘",
            sportType = "yoga",
            unreadCount = 0,
            isOnline = false
        ),
        ChatRoomItem(
            id = "room12",
            name = "Table Tennis Masters",
            lastMessage = "Tournament next week!",
            lastMessageTime = "5 days ago",
            sportEmoji = "🏓",
            sportType = "tabletennis",
            unreadCount = 2,
            isOnline = true
        ),
        ChatRoomItem(
            id = "room13",
            name = "Cricket All Stars",
            lastMessage = "Match highlights uploaded",
            lastMessageTime = "1 week ago",
            sportEmoji = "🏏",
            sportType = "cricket",
            unreadCount = 0,
            isOnline = false
        ),
        ChatRoomItem(
            id = "room14",
            name = "Skateboarding Crew",
            lastMessage = "New skate park opened downtown!",
            lastMessageTime = "1 week ago",
            sportEmoji = "🛹",
            sportType = "skateboard",
            unreadCount = 4,
            isOnline = true
        ),
        ChatRoomItem(
            id = "room15",
            name = "Archery Precision Club",
            lastMessage = "Practice makes perfect 🎯",
            lastMessageTime = "1 week ago",
            sportEmoji = "🏹",
            sportType = "archery",
            unreadCount = 0,
            isOnline = false
        )
    )
    
    // Filter chat rooms based on search query
    val filteredChatRooms = remember(searchQuery, allChatRooms) {
        if (searchQuery.isBlank()) {
            allChatRooms
        } else {
            allChatRooms.filter { room ->
                room.name.contains(searchQuery, ignoreCase = true) ||
                room.lastMessage.contains(searchQuery, ignoreCase = true) ||
                room.sportType.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    // ==================== COLLAPSING HEADER STATE ====================
    val listState = rememberLazyListState()
    
    // Calculate scroll offset for smooth collapsing effect
    val scrollOffset by remember {
        derivedStateOf {
            val firstVisibleItem = listState.firstVisibleItemIndex
            val firstVisibleOffset = listState.firstVisibleItemScrollOffset
            
            when {
                firstVisibleItem > 0 -> 400f // Max scroll
                else -> firstVisibleOffset.toFloat()
            }
        }
    }
    
    // Calculate collapse progress (0 = fully expanded, 1 = fully collapsed)
    val collapseProgress = (scrollOffset / 300f).coerceIn(0f, 1f)
    
    // Expanded header height = 320dp, Collapsed = 120dp (search bar + mini title)
    val expandedHeight = 320f
    val collapsedHeight = 120f
    val headerHeight = (expandedHeight - (expandedHeight - collapsedHeight) * collapseProgress).dp
    
    // Content alpha - fades out as header collapses
    val expandedContentAlpha = (1f - collapseProgress * 1.5f).coerceIn(0f, 1f)
    
    // Collapsed title alpha - fades in when header is collapsed
    val collapsedTitleAlpha = ((collapseProgress - 0.5f) * 2f).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CascadingWhite)
    ) {
        // ==================== CHAT LIST ====================
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = headerHeight),
            contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
        ) {
            if (filteredChatRooms.isEmpty()) {
                item {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = if (searchQuery.isNotEmpty()) "🔍" else "💬",
                                fontSize = 64.sp
                            )
                            Text(
                                text = if (searchQuery.isNotEmpty()) "Not found" else "No chats yet",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Lead
                            )
                            Text(
                                text = if (searchQuery.isNotEmpty()) 
                                    "Try different keywords" 
                                else 
                                    "Start sparring with new friends!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(filteredChatRooms) { chatRoom ->
                    ModernChatItem(
                        chatRoom = chatRoom,
                        onClick = {
                            navController.navigate(Screen.ChatRoom.createRoute(chatRoom.id))
                        }
                    )
                }
            }
        }
        
        // ==================== STICKY COLLAPSING HEADER ====================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                    ambientColor = VibrantPurple.copy(alpha = 0.3f),
                    spotColor = SportyCyan.copy(alpha = 0.3f)
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            VibrantPurple,
                            ElectricBlue,
                            SportyCyan
                        )
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                
                // Functional Search Bar (Always visible)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp
                            ),
                            cursorBrush = SolidColor(Crunch),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Search sparring partner...",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.White.copy(alpha = 0.7f)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                        
                        if (searchQuery.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Clear search",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { searchQuery = "" }
                            )
                        }
                    }
                }
                
                // Collapsed title (shows when header is collapsed)
                if (collapsedTitleAlpha > 0.1f) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .alpha(collapsedTitleAlpha),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Game On! ",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "Find Your Team",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color(0xFFFFD700)
                        )
                    }
                }
                
                // Expandable content (hides when scrolling up)
                if (expandedContentAlpha > 0.05f) {
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Sporty Title
                    Column(
                        modifier = Modifier.alpha(expandedContentAlpha)
                    ) {
                        Text(
                            text = "Game On!",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 36.sp,
                                letterSpacing = (-1).sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "Find Your Team",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                letterSpacing = (-0.5).sp
                            ),
                            color = Color(0xFFFFD700) // Bright Yellow
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Horizontal Scrollable Sport Categories - Clickable to Room
                    LazyRow(
                        modifier = Modifier.alpha(expandedContentAlpha),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(start = 4.dp, end = 24.dp)
                    ) {
                        items(sportCategories) { category ->
                            SportCategoryItem(
                                category = category,
                                onClick = {
                                    // Navigate directly to chat room
                                    navController.navigate(Screen.ChatRoom.createRoute(category.roomId))
                                }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// ==================== SPORT CATEGORY ITEM ====================

@Composable
private fun SportCategoryItem(
    category: SportCategory,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 2.dp) // Extra padding to prevent clipping
            .clickable(onClick = onClick)
    ) {
        // Sport Category Avatar with Vibrant Gradient
        Box(
            modifier = Modifier
                .size(68.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = category.gradientColors.first().copy(alpha = 0.4f),
                    spotColor = category.gradientColors.last().copy(alpha = 0.4f)
                )
                .background(
                    brush = Brush.linearGradient(colors = category.gradientColors),
                    shape = CircleShape
                )
                .border(
                    width = 3.dp,
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.emoji,
                fontSize = 30.sp
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            color = Color.White,
            maxLines = 1
        )
    }
}

// ==================== MODERN CHAT ITEM ====================

@Composable
private fun ModernChatItem(
    chatRoom: ChatRoomItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar with online indicator
        Box {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        ambientColor = VibrantPurple.copy(alpha = 0.2f)
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                ChineseSilver,
                                Dreamland.copy(alpha = 0.5f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(VibrantPurple.copy(alpha = 0.3f), SportyCyan.copy(alpha = 0.3f))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chatRoom.sportEmoji,
                    fontSize = 28.sp
                )
            }
            
            // Online indicator
            if (chatRoom.isOnline) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-1).dp, y = (-1).dp)
                        .background(OnlineGreen, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }
        
        // Chat info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = chatRoom.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = Lead,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = chatRoom.lastMessage,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp
                ),
                color = WarmHaze,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        // Time and unread badge
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = chatRoom.lastMessageTime,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 12.sp
                ),
                color = WarmHaze
            )
            
            if (chatRoom.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(EnergyOrange, HotPink)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (chatRoom.unreadCount > 9) "9+" else chatRoom.unreadCount.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = Color.White
                    )
                }
            }
        }
    }
    
    // Divider
    HorizontalDivider(
        modifier = Modifier.padding(start = 92.dp, end = 20.dp),
        color = Dreamland.copy(alpha = 0.3f),
        thickness = 1.dp
    )
}

// ==================== PREVIEW ====================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ChatListScreenPreview() {
    MaterialTheme {
        ChatListScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF8B5CF6)
@Composable
private fun SportCategoryPreview() {
    MaterialTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            SportCategoryItem(
                category = SportCategory("add", "Add", "+", "", listOf(WarmHaze, Dreamland), true),
                onClick = {}
            )
            SportCategoryItem(
                category = SportCategory("badminton", "Badminton", "🏸", "room1", listOf(SportyCyan, ElectricBlue)),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ModernChatItemPreview() {
    MaterialTheme {
        Column {
            ModernChatItem(
                chatRoom = ChatRoomItem(
                    id = "1",
                    name = "Badminton Weekend Warriors",
                    lastMessage = "Let's play badminton!",
                    lastMessageTime = "Just Now",
                    sportEmoji = "🏸",
                    unreadCount = 2,
                    isOnline = true
                ),
                onClick = {}
            )
            ModernChatItem(
                chatRoom = ChatRoomItem(
                    id = "2",
                    name = "Futsal Champions League",
                    lastMessage = "See you tomorrow!",
                    lastMessageTime = "Yesterday",
                    sportEmoji = "⚽",
                    isOnline = false
                ),
                onClick = {}
            )
        }
    }
}
