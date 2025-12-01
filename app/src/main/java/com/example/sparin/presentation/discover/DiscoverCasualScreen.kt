 package com.example.sparin.presentation.discover

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Discover Casual Mode Screen
 * Fun, friendly, inviting design with pastel colors
 * Gen-Z aesthetic with glassmorphism and smooth animations
 * 
 * Color palette: baby blue #74C9FF, pastel purple #A78BFA, 
 * pastel green #7EE5B2, off-white #F8F9FC
 */

// New Gen-Z Color Palette
val BabyBlue = Color(0xFF74C9FF)
val PastelPurple = Color(0xFFA78BFA)
val PastelGreen = Color(0xFF7EE5B2)
val OffWhite = Color(0xFFF8F9FC)
val GlassWhite = Color(0xFFFFFFFF)
val SoftShadow = Color(0x1A000000)

val casualSportCategories = listOf(
    DiscoverSportCategory("All", "🌟"),
    DiscoverSportCategory("Badminton", "🏸"),
    DiscoverSportCategory("Futsal", "⚽"),
    DiscoverSportCategory("Basket", "🏀"),
    DiscoverSportCategory("Voli", "🏐"),
    DiscoverSportCategory("Hiking", "🥾"),
    DiscoverSportCategory("Cycling", "🚴"),
    DiscoverSportCategory("Swimming", "🏊"),
    DiscoverSportCategory("Tennis", "🎾")
)

// Casual room data with enhanced fields
data class CasualRoomItem(
    val id: String,
    val title: String,
    val sport: String,
    val emoji: String,
    val location: String,
    val schedule: String,
    val date: String = "",
    val description: String = "",
    val hostName: String = "Host",
    val currentPlayers: Int,
    val maxPlayers: Int,
    val tags: List<String>,
    val accentColor: Color,
    val level: String = "Beginner Friendly"
)

val sampleCasualRooms = listOf(
    CasualRoomItem(
        id = "1",
        title = "Badminton Fun Match",
        sport = "Badminton",
        emoji = "🏸",
        location = "GOR Soemantri, Jakarta",
        schedule = "Today · 19:00",
        date = "Dec 1, 2025",
        description = "Looking for friendly players to have some fun! All skill levels welcome. Let's just enjoy the game together 🎉",
        hostName = "Anya",
        currentPlayers = 3,
        maxPlayers = 6,
        tags = listOf("Chill Only", "Friendly Match"),
        accentColor = BabyBlue,
        level = "Beginner Friendly"
    ),
    CasualRoomItem(
        id = "2",
        title = "Futsal Friendly Game",
        sport = "Futsal",
        emoji = "⚽",
        location = "Lapangan BSD",
        schedule = "Tomorrow · 16:00",
        date = "Dec 2, 2025",
        description = "Weekly futsal session for casual players. No pressure, just fun! Bring your good vibes ✨",
        hostName = "Budi",
        currentPlayers = 6,
        maxPlayers = 10,
        tags = listOf("Relaxed", "Beginner OK"),
        accentColor = PastelGreen,
        level = "All Levels"
    ),
    CasualRoomItem(
        id = "3",
        title = "Basketball Pickup",
        sport = "Basket",
        emoji = "🏀",
        location = "Senayan Park",
        schedule = "Today · 17:00",
        date = "Dec 1, 2025",
        description = "Casual 3v3 pickup game. Come hang out and shoot some hoops! 🏀",
        hostName = "Kevin",
        currentPlayers = 4,
        maxPlayers = 6,
        tags = listOf("Fun Only", "Chill"),
        accentColor = PastelPurple,
        level = "Beginner Friendly"
    ),
    CasualRoomItem(
        id = "4",
        title = "Weekend Hiking Trip",
        sport = "Hiking",
        emoji = "🥾",
        location = "Gunung Pancar",
        schedule = "Sat · 06:00",
        date = "Dec 7, 2025",
        description = "Easy morning hike with beautiful views. Perfect for beginners! Let's explore nature together 🌿",
        hostName = "Sarah",
        currentPlayers = 8,
        maxPlayers = 15,
        tags = listOf("Relaxed", "Nature Vibes"),
        accentColor = PastelGreen,
        level = "Easy Trail"
    ),
    CasualRoomItem(
        id = "5",
        title = "Morning Cycling",
        sport = "Cycling",
        emoji = "🚴",
        location = "BSD Loop",
        schedule = "Sun · 05:30",
        date = "Dec 8, 2025",
        description = "Chill morning ride around BSD. Coffee stop included! ☕🚴",
        hostName = "Dimas",
        currentPlayers = 5,
        maxPlayers = 12,
        tags = listOf("Chill", "Beginner OK"),
        accentColor = BabyBlue,
        level = "All Levels"
    )
)

@Composable
fun DiscoverCasualScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("All") }
    var joinedRooms by remember { mutableStateOf(setOf<String>()) }
    var selectedRoom by remember { mutableStateOf<CasualRoomItem?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showCategoryPicker by remember { mutableStateOf(false) }
    var showAddRoomModal by remember { mutableStateOf(false) }
    var casualRooms by remember { mutableStateOf(sampleCasualRooms) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {
        // Pastel background blobs
        CasualBackgroundBlobs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header with gradient
            CasualHeader(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                selectedCategory = selectedCategory,
                onCategoryClick = { showCategoryPicker = true },
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sport Category Tabs
            CasualSportTabs(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Room Cards
            CasualMatchCards(
                rooms = casualRooms,
                selectedCategory = selectedCategory,
                searchQuery = searchQuery,
                joinedRooms = joinedRooms,
                onToggleJoin = { roomId -> 
                    // Toggle join/unjoin
                    joinedRooms = if (joinedRooms.contains(roomId)) {
                        joinedRooms - roomId
                    } else {
                        joinedRooms + roomId
                    }
                },
                onCardTap = { room -> selectedRoom = room }
            )

            Spacer(modifier = Modifier.height(120.dp))
        }

        // Floating Create Room FAB
        CasualCreateRoomFAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp),
            onClick = { showAddRoomModal = true }
        )

        // Detail Modal
        selectedRoom?.let { room ->
            RoomDetailModal(
                room = room,
                isJoined = joinedRooms.contains(room.id),
                onJoin = { 
                    // Toggle join/unjoin in modal too
                    joinedRooms = if (joinedRooms.contains(room.id)) {
                        joinedRooms - room.id
                    } else {
                        joinedRooms + room.id
                    }
                },
                onDismiss = { selectedRoom = null },
                onEnterChat = { 
                    // Navigate to chat room with casual mode
                    navController.navigate(
                        Screen.ChatRoom.createRoute(
                            roomId = room.id,
                            mode = "casual",
                            roomTitle = room.title,
                            sport = room.sport
                        )
                    )
                    selectedRoom = null
                }
            )
        }
        
        // Category Picker Modal
        if (showCategoryPicker) {
            CategoryPickerModal(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    showCategoryPicker = false
                },
                onDismiss = { showCategoryPicker = false }
            )
        }
        
        // Add Room Modal
        if (showAddRoomModal) {
            CasualAddRoomModal(
                onDismiss = { showAddRoomModal = false },
                onAddRoom = { newRoom ->
                    casualRooms = listOf(newRoom) + casualRooms
                    showAddRoomModal = false
                    // Auto-select the sport category if not "All"
                    if (selectedCategory != "All" && selectedCategory != newRoom.sport) {
                        selectedCategory = newRoom.sport
                    }
                }
            )
        }
    }
}

// ==================== BACKGROUND BLOBS ====================

@Composable
private fun CasualBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "casual_blobs")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(120.dp)
    ) {
        // Baby blue blob (top-left)
        drawCircle(
            color = BabyBlue.copy(alpha = 0.25f),
            radius = 280f,
            center = Offset(
                x = size.width * 0.1f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 60f,
                y = size.height * 0.08f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 40f
            )
        )

        // Pastel purple blob (top-right)
        drawCircle(
            color = PastelPurple.copy(alpha = 0.22f),
            radius = 220f,
            center = Offset(
                x = size.width * 0.9f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 50f,
                y = size.height * 0.15f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 45f
            )
        )

        // Pastel green blob (middle)
        drawCircle(
            color = PastelGreen.copy(alpha = 0.2f),
            radius = 200f,
            center = Offset(
                x = size.width * 0.5f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 45f,
                y = size.height * 0.45f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 45f
            )
        )

        // Baby blue blob (bottom)
        drawCircle(
            color = BabyBlue.copy(alpha = 0.18f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.25f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 40f,
                y = size.height * 0.75f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f
            )
        )

        // Pastel purple blob (bottom-right)
        drawCircle(
            color = PastelPurple.copy(alpha = 0.15f),
            radius = 160f,
            center = Offset(
                x = size.width * 0.8f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f,
                y = size.height * 0.85f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 30f
            )
        )
    }
}

// ==================== MODE INDICATOR ====================

@Composable
private fun CasualModeIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "mode_glow")
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Soft glow behind - centered
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            PastelGreen.copy(alpha = glowAlpha * 0.4f),
                            BabyBlue.copy(alpha = glowAlpha * 0.4f),
                            PastelPurple.copy(alpha = glowAlpha * 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .blur(16.dp)
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {
            Text("🌈 Casual Mode 🌈", color = Color.Transparent)
        }

        // Main pill - clean without white background
        Row(
            modifier = Modifier
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            PastelGreen.copy(alpha = 0.6f),
                            BabyBlue.copy(alpha = 0.6f),
                            PastelPurple.copy(alpha = 0.6f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("🌈", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Casual Mode",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                color = Lead
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("🌈", fontSize = 16.sp)
        }
    }
}

// ==================== HEADER ====================

@Composable
private fun CasualHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "header_float")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BabyBlue.copy(alpha = 0.15f),
                        PastelPurple.copy(alpha = 0.1f),
                        OffWhite
                    )
                )
            )
            .padding(top = 48.dp)
    ) {
        // Back Button - Top Left
        Surface(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp)
                .size(44.dp)
                .shadow(8.dp, CircleShape),
            shape = CircleShape,
            color = GlassWhite.copy(alpha = 0.95f)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(22.dp),
                    tint = Lead
                )
            }
        }

        // Floating decorative element - clean without white bg
        Text(
            text = "✨",
            fontSize = 28.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-32).dp, y = (24 + float1).dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(top = 48.dp)
        ) {
            // Title with emoji
            Text(
                text = "Casual Play\nRooms 😊",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp,
                    lineHeight = 40.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = Lead
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Find fun activities & friendly matches near you",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp
                ),
                color = WarmHaze
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Search Bar with Category Filter
            CasualSearchBar(
                searchQuery = searchQuery,
                onSearchChange = onSearchChange,
                selectedCategory = selectedCategory,
                onCategoryClick = onCategoryClick
            )
        }
    }
}

@Composable
private fun CasualSearchBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search TextField
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = BabyBlue.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(26.dp),
            color = GlassWhite.copy(alpha = 0.95f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                BabyBlue.copy(alpha = 0.3f),
                                PastelPurple.copy(alpha = 0.3f)
                            )
                        ),
                        shape = RoundedCornerShape(26.dp)
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(22.dp),
                    tint = BabyBlue
                )

                TextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "Search rooms...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmHaze.copy(alpha = 0.5f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = BabyBlue
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Lead)
                )

                // Clear button
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchChange("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.size(18.dp),
                            tint = WarmHaze
                        )
                    }
                }
            }
        }

        // Category Filter Button - Separate
        Surface(
            onClick = onCategoryClick,
            modifier = Modifier
                .size(52.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = PastelPurple.copy(alpha = 0.2f)
                ),
            shape = RoundedCornerShape(16.dp),
            color = GlassWhite.copy(alpha = 0.95f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = PastelPurple.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.FilterList,
                    contentDescription = "Filter Category",
                    modifier = Modifier.size(24.dp),
                    tint = PastelPurple
                )
            }
        }
    }
}

// ==================== SPORT TABS ====================

@Composable
private fun CasualSportTabs(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sport Categories",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                ),
                color = Lead
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("🎯", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            casualSportCategories.forEach { category ->
                CasualSportChip(
                    category = category,
                    isSelected = selectedCategory == category.name,
                    onClick = { onCategorySelected(category.name) }
                )
            }
        }
    }
}

@Composable
private fun CasualSportChip(
    category: DiscoverSportCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "chip_scale"
    )

    // Clean chip without white background
    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = if (isSelected) BabyBlue.copy(alpha = 0.2f) else Color.Transparent
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) BabyBlue else WarmHaze.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = category.emoji, fontSize = 18.sp)
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp
                ),
                color = if (isSelected) BabyBlue else WarmHaze
            )
        }
    }
}

// ==================== MATCH CARDS ====================

@Composable
private fun CasualMatchCards(
    rooms: List<CasualRoomItem>,
    selectedCategory: String,
    searchQuery: String,
    joinedRooms: Set<String>,
    onToggleJoin: (String) -> Unit,
    onCardTap: (CasualRoomItem) -> Unit
) {
    val filteredRooms = rooms.filter { room ->
        val matchesCategory = selectedCategory == "All" || room.sport == selectedCategory
        val matchesSearch = searchQuery.isEmpty() || 
            room.title.contains(searchQuery, ignoreCase = true) ||
            room.sport.contains(searchQuery, ignoreCase = true) ||
            room.location.contains(searchQuery, ignoreCase = true) ||
            room.hostName.contains(searchQuery, ignoreCase = true) ||
            room.tags.any { it.contains(searchQuery, ignoreCase = true) }
        matchesCategory && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredRooms.size} rooms available",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                ),
                color = Lead
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("🎮", fontSize = 16.sp)
        }

        if (filteredRooms.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔍", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No rooms found",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Lead
                    )
                    Text(
                        text = "Try a different search or category",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze
                    )
                }
            }
        } else {
            filteredRooms.forEach { room ->
                val isJoined = joinedRooms.contains(room.id)
                CasualRoomCard(
                    room = room,
                    isJoined = isJoined,
                    onToggleJoin = { onToggleJoin(room.id) },
                    onCardTap = { onCardTap(room) }
                )
            }
        }
    }
}

@Composable
private fun CasualRoomCard(
    room: CasualRoomItem,
    isJoined: Boolean,
    onToggleJoin: () -> Unit,
    onCardTap: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "room_card_${room.id}")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = if (isJoined) 0.4f else 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val cardScale by animateFloatAsState(
        targetValue = if (isJoined) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_scale"
    )

    // Animated player count
    val animatedPlayerCount by animateIntAsState(
        targetValue = if (isJoined) room.currentPlayers + 1 else room.currentPlayers,
        animationSpec = tween(500, easing = EaseOutBack),
        label = "player_count"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
    ) {
        // Glow effect (stronger when joined)
        if (isJoined) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                room.accentColor.copy(alpha = glowAlpha),
                                room.accentColor.copy(alpha = 0f)
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .blur(20.dp)
            )
        }

        // Main glass card
        Surface(
            onClick = onCardTap,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isJoined) 16.dp else 10.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = room.accentColor.copy(alpha = 0.2f),
                    spotColor = room.accentColor.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(24.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                GlassWhite.copy(alpha = 0.95f),
                                GlassWhite.copy(alpha = 0.9f),
                                room.accentColor.copy(alpha = 0.08f)
                            )
                        )
                    )
                    .border(
                        width = if (isJoined) 2.dp else 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = if (isJoined) {
                                listOf(
                                    room.accentColor.copy(alpha = 0.6f),
                                    PastelGreen.copy(alpha = 0.6f)
                                )
                            } else {
                                listOf(
                                    room.accentColor.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            }
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        // Sport emoji - clean without heavy background
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    color = room.accentColor.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = room.accentColor.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = room.emoji, fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            // Title
                            Text(
                                text = room.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = Lead,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Level badge
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PastelGreen.copy(alpha = 0.3f)
                            ) {
                                Text(
                                    text = "🌱 ${room.level}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp
                                    ),
                                    color = Lead.copy(alpha = 0.8f)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Location
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = WarmHaze
                                )
                                Text(
                                    text = room.location,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = WarmHaze,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tags row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        room.tags.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = room.accentColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = tag,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp
                                    ),
                                    color = Lead.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Time badge
                    Row(
                        modifier = Modifier
                            .background(
                                color = BabyBlue.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = BabyBlue
                        )
                        Text(
                            text = room.schedule,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = BabyBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom row: Avatars + Join button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Player avatars (slot style)
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar slots
                            Row {
                                repeat(minOf(animatedPlayerCount, 4)) { index ->
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .offset(x = (-8 * index).dp)
                                            .shadow(4.dp, CircleShape)
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        room.accentColor.copy(alpha = 0.4f),
                                                        room.accentColor.copy(alpha = 0.2f)
                                                    )
                                                ),
                                                shape = CircleShape
                                            )
                                            .border(2.dp, GlassWhite, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = listOf("😊", "🙂", "😄", "🤗")[index % 4],
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                                // Empty slots
                                repeat(room.maxPlayers - animatedPlayerCount) { index ->
                                    if (index < 2) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .offset(x = (-8 * (animatedPlayerCount + index)).dp)
                                                .background(
                                                    color = Dreamland.copy(alpha = 0.3f),
                                                    shape = CircleShape
                                                )
                                                .border(
                                                    2.dp,
                                                    Dreamland.copy(alpha = 0.5f),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Add,
                                                contentDescription = null,
                                                modifier = Modifier.size(14.dp),
                                                tint = WarmHaze.copy(alpha = 0.5f)
                                            )
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "$animatedPlayerCount/${room.maxPlayers}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = room.accentColor
                            )
                        }

                        // Join button with animation - toggleable
                        AnimatedContent(
                            targetState = isJoined,
                            transitionSpec = {
                                scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) togetherWith
                                        scaleOut()
                            },
                            label = "join_button"
                        ) { joined ->
                            if (joined) {
                                // Joined state - clickable to unjoin
                                Surface(
                                    onClick = onToggleJoin,
                                    shape = RoundedCornerShape(16.dp),
                                    color = PastelGreen.copy(alpha = 0.3f),
                                    modifier = Modifier
                                        .border(
                                            2.dp,
                                            PastelGreen.copy(alpha = 0.5f),
                                            RoundedCornerShape(16.dp)
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "Joined",
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = PastelGreen
                                        )
                                        Text("✔️", fontSize = 14.sp)
                                    }
                                }
                            } else {
                                // Join button
                                Surface(
                                    onClick = onToggleJoin,
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.Transparent,
                                    modifier = Modifier
                                        .shadow(
                                            elevation = 8.dp,
                                            shape = RoundedCornerShape(16.dp),
                                            ambientColor = room.accentColor.copy(alpha = 0.3f)
                                        )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        room.accentColor,
                                                        room.accentColor.copy(alpha = 0.8f)
                                                    )
                                                ),
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                            .padding(horizontal = 18.dp, vertical = 10.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = "Join Room",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Color.White
                                            )
                                            Text("😊", fontSize = 14.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Tap to detail hint at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tap for details 👆",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = WarmHaze.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

// ==================== CREATE ROOM FAB ====================

@Composable
private fun CasualCreateRoomFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fab_anim")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Multi-color glow
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            BabyBlue.copy(alpha = glowAlpha),
                            PastelPurple.copy(alpha = glowAlpha * 0.7f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
                .blur(20.dp)
        )

        // FAB with pastel gradient
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = BabyBlue.copy(alpha = 0.4f),
                    spotColor = PastelPurple.copy(alpha = 0.4f)
                ),
            containerColor = Color.Transparent,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                BabyBlue,
                                PastelPurple.copy(alpha = 0.9f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create Room",
                    modifier = Modifier.size(28.dp),
                    tint = Color.White
                )
            }
        }
    }
}

// ==================== ROOM DETAIL MODAL ====================

@Composable
private fun RoomDetailModal(
    room: CasualRoomItem,
    isJoined: Boolean,
    onJoin: () -> Unit,
    onDismiss: () -> Unit,
    onEnterChat: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Dialog(
        onDismissRequest = {
            visible = false
            onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .blur(if (visible) 0.dp else 10.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { 
                    visible = false
                    onDismiss() 
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                // Bottom sheet content
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { /* Prevent dismiss */ },
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        GlassWhite.copy(alpha = 0.98f),
                                        OffWhite
                                    )
                                ),
                                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                            )
                            .border(
                                width = 1.5.dp,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        room.accentColor.copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                ),
                                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(24.dp)
                        ) {
                            // Handle bar
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .width(40.dp)
                                    .height(4.dp)
                                    .background(
                                        Dreamland.copy(alpha = 0.4f),
                                        RoundedCornerShape(2.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Sport icon + name header
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .shadow(
                                            12.dp,
                                            RoundedCornerShape(20.dp),
                                            ambientColor = room.accentColor.copy(alpha = 0.3f)
                                        )
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    room.accentColor.copy(alpha = 0.3f),
                                                    room.accentColor.copy(alpha = 0.15f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .border(
                                            1.5.dp,
                                            room.accentColor.copy(alpha = 0.4f),
                                            RoundedCornerShape(20.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(room.emoji, fontSize = 36.sp)
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = room.title,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 22.sp
                                        ),
                                        color = Lead
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = room.accentColor.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = room.sport,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = room.accentColor
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Host info
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        BabyBlue.copy(alpha = 0.1f),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(BabyBlue, PastelPurple)
                                            ),
                                            shape = CircleShape
                                        )
                                        .border(2.dp, GlassWhite, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("👤", fontSize = 20.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Hosted by",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = WarmHaze
                                    )
                                    Text(
                                        text = room.hostName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = Lead
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Info cards row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Location card
                                DetailInfoCard(
                                    modifier = Modifier.weight(1f),
                                    icon = "📍",
                                    label = "Location",
                                    value = room.location,
                                    accentColor = PastelGreen
                                )
                                // Time card
                                DetailInfoCard(
                                    modifier = Modifier.weight(1f),
                                    icon = "🕐",
                                    label = "Time",
                                    value = room.schedule,
                                    accentColor = BabyBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Date card
                            DetailInfoCard(
                                modifier = Modifier.fillMaxWidth(),
                                icon = "📅",
                                label = "Date",
                                value = room.date,
                                accentColor = PastelPurple
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Description
                            Text(
                                text = "About this room",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Lead
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = room.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Players/Avatars
                            Text(
                                text = "Players (${room.currentPlayers}/${room.maxPlayers})",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Lead
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy((-8).dp)
                            ) {
                                val avatarEmojis = listOf("😊", "🙂", "😄", "🤗", "😎", "🥳", "😁", "🤩")
                                repeat(room.currentPlayers) { index ->
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .shadow(4.dp, CircleShape)
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        room.accentColor.copy(alpha = 0.4f),
                                                        room.accentColor.copy(alpha = 0.2f)
                                                    )
                                                ),
                                                shape = CircleShape
                                            )
                                            .border(2.dp, GlassWhite, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            avatarEmojis[index % avatarEmojis.size],
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Tags
                            Text(
                                text = "Tags",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Lead
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                room.tags.forEach { tag ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = room.accentColor.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = tag,
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = Lead.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = PastelGreen.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "🌱 ${room.level}",
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = Lead.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // CTA Button
                            if (isJoined) {
                                // Enter Chat button
                                Surface(
                                    onClick = onEnterChat,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .shadow(
                                            12.dp,
                                            RoundedCornerShape(28.dp),
                                            ambientColor = BabyBlue.copy(alpha = 0.3f)
                                        ),
                                    shape = RoundedCornerShape(28.dp),
                                    color = Color.Transparent
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        BabyBlue,
                                                        PastelPurple
                                                    )
                                                ),
                                                shape = RoundedCornerShape(28.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "Enter Chat Room",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Color.White
                                            )
                                            Text("💬", fontSize = 20.sp)
                                        }
                                    }
                                }
                            } else {
                                // Join Room button - using toggle
                                Surface(
                                    onClick = onJoin,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .shadow(
                                            12.dp,
                                            RoundedCornerShape(28.dp),
                                            ambientColor = room.accentColor.copy(alpha = 0.3f)
                                        ),
                                    shape = RoundedCornerShape(28.dp),
                                    color = Color.Transparent
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        room.accentColor,
                                                        room.accentColor.copy(alpha = 0.8f)
                                                    )
                                                ),
                                                shape = RoundedCornerShape(28.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "Join Room",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Color.White
                                            )
                                            Text("😊", fontSize = 20.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailInfoCard(
    modifier: Modifier = Modifier,
    icon: String,
    label: String,
    value: String,
    accentColor: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = accentColor.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(icon, fontSize = 16.sp)
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Lead,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ==================== CATEGORY PICKER MODAL ====================

@Composable
private fun CategoryPickerModal(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    Dialog(
        onDismissRequest = {
            visible = false
            onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    visible = false
                    onDismiss()
                },
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .padding(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { /* Prevent dismiss */ },
                    shape = RoundedCornerShape(28.dp),
                    color = GlassWhite
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .widthIn(max = 320.dp)
                    ) {
                        // Title
                        Text(
                            text = "Filter by Category",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Lead
                        )
                        
                        Text(
                            text = "Choose a sport category 🎯",
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmHaze,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Category grid
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            casualSportCategories.chunked(3).forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    row.forEach { category ->
                                        val isSelected = selectedCategory == category.name
                                        val bgColor = if (isSelected) {
                                            when (category.name) {
                                                "Badminton", "Futsal", "Tennis" -> BabyBlue
                                                "Basket", "Swimming" -> PastelPurple
                                                else -> PastelGreen
                                            }
                                        } else {
                                            OffWhite
                                        }
                                        
                                        Surface(
                                            onClick = { onCategorySelected(category.name) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .border(
                                                    width = if (isSelected) 2.dp else 1.dp,
                                                    color = if (isSelected) bgColor else WarmHaze.copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(16.dp)
                                                ),
                                            shape = RoundedCornerShape(16.dp),
                                            color = if (isSelected) bgColor.copy(alpha = 0.15f) else Color.Transparent
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = category.emoji,
                                                    fontSize = 24.sp
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = category.name,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                        fontSize = 11.sp
                                                    ),
                                                    color = if (isSelected) Lead else WarmHaze,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                    // Fill empty space if less than 3 items in row
                                    repeat(3 - row.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Close button
                        Surface(
                            onClick = {
                                visible = false
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            color = Lead
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Done",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== ADD ROOM MODAL ====================

@Composable
private fun CasualAddRoomModal(
    onDismiss: () -> Unit,
    onAddRoom: (CasualRoomItem) -> Unit
) {
    // Form states
    var title by remember { mutableStateOf("") }
    var selectedSport by remember { mutableStateOf(casualSportCategories[1]) } // Default Badminton
    var location by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var maxPlayers by remember { mutableStateOf("4") }
    var selectedLevel by remember { mutableStateOf("Beginner Friendly") }
    var tag1 by remember { mutableStateOf("") }
    var tag2 by remember { mutableStateOf("") }
    var showSportPicker by remember { mutableStateOf(false) }
    var showLevelPicker by remember { mutableStateOf(false) }

    val levels = listOf("Beginner Friendly", "All Levels", "Intermediate", "Advanced")
    val accentColors = listOf(BabyBlue, PastelPurple, PastelGreen)

    // Validation
    val isFormValid = title.isNotBlank() && location.isNotBlank() && schedule.isNotBlank()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            // Modal Content
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.85f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Prevent dismiss */ },
                shape = RoundedCornerShape(28.dp),
                color = OffWhite
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(BabyBlue, PastelPurple, PastelGreen)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Create Room ✨",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                                Text(
                                    text = "Let's play together!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // Form Content - Scrollable
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Room Title
                        CasualFormField(
                            label = "Room Title",
                            emoji = "📝",
                            value = title,
                            onValueChange = { title = it },
                            placeholder = "e.g., Badminton Fun Match"
                        )

                        // Sport Picker
                        Text(
                            text = "🏆 Sport",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Lead
                        )
                        Surface(
                            onClick = { showSportPicker = !showSportPicker },
                            shape = RoundedCornerShape(16.dp),
                            color = GlassWhite,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                BabyBlue.copy(alpha = 0.3f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(selectedSport.emoji, fontSize = 24.sp)
                                    Text(
                                        text = selectedSport.name,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = Lead
                                    )
                                }
                                Icon(
                                    imageVector = if (showSportPicker) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = BabyBlue
                                )
                            }
                        }

                        // Sport Options
                        AnimatedVisibility(visible = showSportPicker) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = GlassWhite,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                casualSportCategories.filter { it.name != "All" }.forEach { sport ->
                                    Surface(
                                        onClick = {
                                            selectedSport = sport
                                            showSportPicker = false
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (selectedSport == sport) BabyBlue.copy(alpha = 0.2f) else Color.Transparent
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(sport.emoji, fontSize = 20.sp)
                                            Text(
                                                text = sport.name,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (selectedSport == sport) FontWeight.Bold else FontWeight.Normal
                                                ),
                                                color = if (selectedSport == sport) BabyBlue else Lead
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Location
                        CasualFormField(
                            label = "Location",
                            emoji = "📍",
                            value = location,
                            onValueChange = { location = it },
                            placeholder = "e.g., GOR Soemantri, Jakarta"
                        )

                        // Schedule & Date Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                CasualFormField(
                                    label = "Time",
                                    emoji = "⏰",
                                    value = schedule,
                                    onValueChange = { schedule = it },
                                    placeholder = "e.g., Today · 19:00"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                CasualFormField(
                                    label = "Date",
                                    emoji = "📅",
                                    value = date,
                                    onValueChange = { date = it },
                                    placeholder = "e.g., Dec 1, 2025"
                                )
                            }
                        }

                        // Description
                        CasualFormField(
                            label = "Description",
                            emoji = "💬",
                            value = description,
                            onValueChange = { description = it },
                            placeholder = "Tell others about your room...",
                            singleLine = false,
                            minLines = 3
                        )

                        // Max Players
                        CasualFormField(
                            label = "Max Players",
                            emoji = "👥",
                            value = maxPlayers,
                            onValueChange = { if (it.all { c -> c.isDigit() }) maxPlayers = it },
                            placeholder = "e.g., 6"
                        )

                        // Level Picker
                        Text(
                            text = "🌱 Skill Level",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Lead
                        )
                        Surface(
                            onClick = { showLevelPicker = !showLevelPicker },
                            shape = RoundedCornerShape(16.dp),
                            color = GlassWhite,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                PastelGreen.copy(alpha = 0.3f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedLevel,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Lead
                                )
                                Icon(
                                    imageVector = if (showLevelPicker) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = PastelGreen
                                )
                            }
                        }

                        AnimatedVisibility(visible = showLevelPicker) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = GlassWhite,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                levels.forEach { level ->
                                    Surface(
                                        onClick = {
                                            selectedLevel = level
                                            showLevelPicker = false
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (selectedLevel == level) PastelGreen.copy(alpha = 0.2f) else Color.Transparent
                                    ) {
                                        Text(
                                            text = level,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (selectedLevel == level) FontWeight.Bold else FontWeight.Normal
                                            ),
                                            color = if (selectedLevel == level) PastelGreen else Lead
                                        )
                                    }
                                }
                            }
                        }

                        // Tags
                        Text(
                            text = "🏷️ Tags (optional)",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Lead
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                CasualFormFieldSimple(
                                    value = tag1,
                                    onValueChange = { tag1 = it },
                                    placeholder = "e.g., Chill Only"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                CasualFormFieldSimple(
                                    value = tag2,
                                    onValueChange = { tag2 = it },
                                    placeholder = "e.g., Fun Match"
                                )
                            }
                        }
                    }

                    // Bottom Action Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, OffWhite)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Button(
                            onClick = {
                                val tags = listOfNotNull(
                                    tag1.takeIf { it.isNotBlank() },
                                    tag2.takeIf { it.isNotBlank() }
                                ).ifEmpty { listOf("Fun", "Casual") }

                                val newRoom = CasualRoomItem(
                                    id = System.currentTimeMillis().toString(),
                                    title = title,
                                    sport = selectedSport.name,
                                    emoji = selectedSport.emoji,
                                    location = location,
                                    schedule = schedule.ifBlank { "TBD" },
                                    date = date.ifBlank { "TBD" },
                                    description = description.ifBlank { "Join us for a fun game! 🎉" },
                                    hostName = "You",
                                    currentPlayers = 1,
                                    maxPlayers = maxPlayers.toIntOrNull() ?: 4,
                                    tags = tags,
                                    accentColor = accentColors.random(),
                                    level = selectedLevel
                                )
                                onAddRoom(newRoom)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = isFormValid,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BabyBlue,
                                disabledContainerColor = BabyBlue.copy(alpha = 0.4f)
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Text(
                                    text = "Create Room",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                                Text("🎮", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CasualFormField(
    label: String,
    emoji: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "$emoji $label",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Lead
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = WarmHaze.copy(alpha = 0.6f)
                )
            },
            singleLine = singleLine,
            minLines = minLines,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BabyBlue,
                unfocusedBorderColor = BabyBlue.copy(alpha = 0.2f),
                focusedContainerColor = GlassWhite,
                unfocusedContainerColor = GlassWhite,
                cursorColor = BabyBlue
            )
        )
    }
}

@Composable
private fun CasualFormFieldSimple(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = WarmHaze.copy(alpha = 0.6f),
                fontSize = 13.sp
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PastelPurple,
            unfocusedBorderColor = PastelPurple.copy(alpha = 0.2f),
            focusedContainerColor = GlassWhite,
            unfocusedContainerColor = GlassWhite,
            cursorColor = PastelPurple
        )
    )
}
