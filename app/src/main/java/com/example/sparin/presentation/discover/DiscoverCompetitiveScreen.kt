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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
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
 * Discover Competitive Mode Screen
 * Bold, esports-inspired design with intense red + dark tones
 * Futuristic competitive atmosphere with neon accents
 * 
 * Color palette: deep red (#EF4444), crimson (#DC2626), 
 * charcoal black (#0E0E0E), dark grey (#1A1A1A)
 */

// Competitive color palette - Esports inspired
val DeepRed = Color(0xFFEF4444)
val Crimson = Color(0xFFDC2626)
val CharcoalBlack = Color(0xFF0E0E0E)
val DarkGrey = Color(0xFF1A1A1A)
val NeonRed = Color(0xFFFF3B3B)
val GlowRed = Color(0xFFFF5555)
val MetallicDark = Color(0xFF2A2A2A)
val SteelGrey = Color(0xFF3A3A3A)

// Competitive sport categories
val competitiveSportCategories = listOf(
    DiscoverSportCategory("All", "🔥"),
    DiscoverSportCategory("Badminton", "🏸"),
    DiscoverSportCategory("Futsal", "⚽"),
    DiscoverSportCategory("Basket", "🏀"),
    DiscoverSportCategory("Muaythai", "🥋"),
    DiscoverSportCategory("Boxing", "🥊"),
    DiscoverSportCategory("Tennis", "🎾"),
    DiscoverSportCategory("Voli", "🏐")
)

// Enhanced Competitive room data
data class CompetitiveRoomItem(
    val id: String,
    val title: String,
    val sport: String,
    val emoji: String,
    val location: String,
    val distance: String,
    val schedule: String,
    val date: String = "",
    val description: String = "",
    val hostName: String = "Host",
    val hostRating: Float = 4.5f,
    val currentPlayers: Int,
    val maxPlayers: Int,
    val skillLevel: String,
    val tags: List<String>,
    val difficulty: Int, // 1-5 flames
    val entryFee: String? = null,
    val prize: String? = null,
    val requirements: List<String> = emptyList()
)

val sampleCompetitiveRooms = listOf(
    CompetitiveRoomItem(
        id = "1",
        title = "Badminton Ranked Match",
        sport = "Badminton",
        emoji = "🏸",
        location = "Elite Sports Center",
        distance = "2.3 km",
        schedule = "Today · 19:00",
        date = "Dec 1, 2025",
        description = "High-intensity ranked match for serious players. No casual play allowed. Bring your A-game or don't come at all.",
        hostName = "ProPlayer99",
        hostRating = 4.8f,
        currentPlayers = 3,
        maxPlayers = 4,
        skillLevel = "Intermediate+",
        tags = listOf("Ranked", "High Intensity"),
        difficulty = 4,
        entryFee = "50K",
        requirements = listOf("Own racket", "2+ years exp")
    ),
    CompetitiveRoomItem(
        id = "2",
        title = "Futsal Tournament Qualifier",
        sport = "Futsal",
        emoji = "⚽",
        location = "Champion Arena",
        distance = "3.1 km",
        schedule = "Tomorrow · 20:00",
        date = "Dec 2, 2025",
        description = "Official tournament qualifier match. Top performers advance to regional finals. Strict rules apply.",
        hostName = "TournamentOrg",
        hostRating = 5.0f,
        currentPlayers = 8,
        maxPlayers = 10,
        skillLevel = "Advanced",
        tags = listOf("Tournament", "Advanced Only"),
        difficulty = 5,
        entryFee = "100K",
        prize = "2M Prize Pool",
        requirements = listOf("Team jersey", "ID verification")
    ),
    CompetitiveRoomItem(
        id = "3",
        title = "Basketball Pro League",
        sport = "Basket",
        emoji = "🏀",
        location = "Senayan Pro Court",
        distance = "1.8 km",
        schedule = "Today · 18:00",
        date = "Dec 1, 2025",
        description = "Elite basketball league match. Pro-level competition only. Rating 4.5+ required to join.",
        hostName = "EliteHoops",
        hostRating = 4.9f,
        currentPlayers = 8,
        maxPlayers = 10,
        skillLevel = "Pro",
        tags = listOf("Ranked", "Elite Only"),
        difficulty = 5,
        prize = "Winner Trophy",
        requirements = listOf("4.5+ rating", "Pro experience")
    ),
    CompetitiveRoomItem(
        id = "4",
        title = "Muaythai Sparring Session",
        sport = "Muaythai",
        emoji = "🥋",
        location = "Fight Club Elite",
        distance = "4.2 km",
        schedule = "Sat · 17:00",
        date = "Dec 7, 2025",
        description = "Intense sparring session for experienced fighters. Full protective gear mandatory. No beginners.",
        hostName = "FightMaster",
        hostRating = 4.7f,
        currentPlayers = 5,
        maxPlayers = 8,
        skillLevel = "Intermediate+",
        tags = listOf("Serious Only", "High Intensity"),
        difficulty = 4,
        entryFee = "75K",
        requirements = listOf("Full gear", "Medical clearance")
    ),
    CompetitiveRoomItem(
        id = "5",
        title = "Boxing Championship Prep",
        sport = "Boxing",
        emoji = "🥊",
        location = "Knockout Gym",
        distance = "2.5 km",
        schedule = "Sun · 16:00",
        date = "Dec 8, 2025",
        description = "Championship preparation sparring. Looking for serious boxers to train with before the big event.",
        hostName = "ChampionBoxer",
        hostRating = 4.9f,
        currentPlayers = 3,
        maxPlayers = 6,
        skillLevel = "Advanced",
        tags = listOf("Championship", "Pro Level"),
        difficulty = 5,
        requirements = listOf("Boxing license", "2+ years competitive")
    )
)

@Composable
fun DiscoverCompetitiveScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("All") }
    var joinedRooms by remember { mutableStateOf(setOf<String>()) }
    var selectedRoom by remember { mutableStateOf<CompetitiveRoomItem?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showCategoryPicker by remember { mutableStateOf(false) }
    var showAddRoomModal by remember { mutableStateOf(false) }
    var competitiveRooms by remember { mutableStateOf(sampleCompetitiveRooms) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CharcoalBlack)
    ) {
        // Dark gradient background with red accents
        CompetitiveBackgroundEffects()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Mode Indicator Banner with pulse animation
            CompetitiveModeIndicator()

            Spacer(modifier = Modifier.height(20.dp))

            // Header with search
            CompetitiveHeader(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onCategoryClick = { showCategoryPicker = true },
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sport Category Navbar (Sharp edges)
            CompetitiveSportTabs(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Competitive Match Cards
            CompetitiveMatchCards(
                rooms = competitiveRooms,
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

        // Futuristic Create Room FAB
        CompetitiveCreateRoomFAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp),
            onClick = { showAddRoomModal = true }
        )

        // Detail Modal
        selectedRoom?.let { room ->
            CompetitiveRoomDetailModal(
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
                onEnterRoom = { 
                    // Navigate to chat room with competitive mode
                    navController.navigate(
                        Screen.ChatRoom.createRoute(
                            roomId = room.id,
                            mode = "competitive",
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
            CompetitiveCategoryPickerModal(
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
            CompetitiveAddRoomModal(
                onDismiss = { showAddRoomModal = false },
                onAddRoom = { newRoom ->
                    competitiveRooms = listOf(newRoom) + competitiveRooms
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

// ==================== BACKGROUND EFFECTS ====================

@Composable
private fun CompetitiveBackgroundEffects() {
    val infiniteTransition = rememberInfiniteTransition(label = "competitive_bg")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )

    // Dark gradient overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CharcoalBlack,
                        DarkGrey.copy(alpha = 0.8f),
                        CharcoalBlack,
                        DeepRed.copy(alpha = 0.05f),
                        CharcoalBlack
                    )
                )
            )
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(140.dp)
    ) {
        // Red glow spots - more intense
        drawCircle(
            color = DeepRed.copy(alpha = glowAlpha),
            radius = 300f,
            center = Offset(
                x = size.width * 0.15f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 80f,
                y = size.height * 0.1f
            )
        )

        drawCircle(
            color = Crimson.copy(alpha = glowAlpha * 0.9f),
            radius = 250f,
            center = Offset(
                x = size.width * 0.85f,
                y = size.height * 0.5f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 60f
            )
        )

        drawCircle(
            color = NeonRed.copy(alpha = glowAlpha * 0.7f),
            radius = 220f,
            center = Offset(
                x = size.width * 0.4f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 50f,
                y = size.height * 0.8f
            )
        )

        // Additional accent glow
        drawCircle(
            color = DeepRed.copy(alpha = glowAlpha * 0.5f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.7f,
                y = size.height * 0.25f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 40f
            )
        )
    }
}

// ==================== MODE INDICATOR ====================

@Composable
private fun CompetitiveModeIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "mode_indicator")

    // Pulse animation
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // Subtle vibration effect
    val vibration by infiniteTransition.animateFloat(
        initialValue = -0.5f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vibrate"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Glow behind
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            DeepRed.copy(alpha = glowAlpha * 0.3f),
                            Crimson.copy(alpha = glowAlpha * 0.4f),
                            DeepRed.copy(alpha = glowAlpha * 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .blur(20.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .graphicsLayer {
                    scaleX = pulseScale
                    scaleY = pulseScale
                    translationX = vibration
                }
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = DeepRed.copy(alpha = 0.6f),
                    spotColor = NeonRed.copy(alpha = 0.6f)
                ),
            shape = RoundedCornerShape(14.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                DarkGrey,
                                MetallicDark,
                                DarkGrey
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                DeepRed.copy(alpha = 0.8f),
                                NeonRed,
                                DeepRed.copy(alpha = 0.8f)
                            )
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("🔥", fontSize = 22.sp)
                    Text(
                        text = "COMPETITIVE MODE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            letterSpacing = 3.sp
                        ),
                        color = NeonRed
                    )
                    Text("⚡", fontSize = 22.sp)
                }
            }
        }
    }
}

// ==================== HEADER ====================

@Composable
private fun CompetitiveHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Back Button Row
        Surface(
            onClick = onBackClick,
            modifier = Modifier
                .size(44.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    ambientColor = DeepRed.copy(alpha = 0.3f)
                ),
            shape = CircleShape,
            color = DarkGrey
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = DeepRed.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(22.dp),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Title with fire emoji
        Text(
            text = "Competitive\nRooms 🔥",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 36.sp,
                lineHeight = 42.sp,
                letterSpacing = (-1).sp
            ),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = "Find serious opponents. Prove your skills.",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                letterSpacing = 0.5.sp
            ),
            color = Color.White.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Search Bar (Dark themed with neon accent)
        CompetitiveSearchBar(
            searchQuery = searchQuery,
            onSearchChange = onSearchChange,
            onCategoryClick = onCategoryClick
        )
    }
}

@Composable
private fun CompetitiveSearchBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onCategoryClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "search_glow")
    
    val borderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border"
    )

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
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = DeepRed.copy(alpha = 0.3f)
                ),
            shape = RoundedCornerShape(16.dp),
            color = DarkGrey
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.5.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                DeepRed.copy(alpha = borderAlpha),
                                Crimson.copy(alpha = borderAlpha * 0.8f),
                                DeepRed.copy(alpha = borderAlpha)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(22.dp),
                    tint = DeepRed
                )

                TextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "Search matches...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.4f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = DeepRed
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
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
                            tint = Color.White.copy(alpha = 0.6f)
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
                    elevation = 12.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = DeepRed.copy(alpha = 0.3f)
                ),
            shape = RoundedCornerShape(14.dp),
            color = DarkGrey
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.5.dp,
                        color = DeepRed.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.FilterList,
                    contentDescription = "Filter Category",
                    modifier = Modifier.size(24.dp),
                    tint = DeepRed
                )
            }
        }
    }
}

// ==================== SPORT TABS (SHARP EDGES) ====================

@Composable
private fun CompetitiveSportTabs(
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
                text = "CATEGORIES",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 2.sp
                ),
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Red accent line
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                DeepRed.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            competitiveSportCategories.forEach { category ->
                CompetitiveSportChip(
                    category = category,
                    isSelected = selectedCategory == category.name,
                    onClick = { onCategorySelected(category.name) }
                )
            }
        }
    }
}

@Composable
private fun CompetitiveSportChip(
    category: DiscoverSportCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "chip_glow_${category.name}")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Box {
        // Neon glow for selected
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DeepRed.copy(alpha = glowAlpha * 0.4f),
                                DeepRed.copy(alpha = 0f)
                            )
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .blur(14.dp)
            )
        }

        Surface(
            onClick = onClick,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(
                    elevation = if (isSelected) 16.dp else 6.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = if (isSelected) DeepRed.copy(alpha = 0.5f) else Color.Transparent
                ),
            shape = RoundedCornerShape(14.dp),
            color = if (isSelected) DarkGrey else MetallicDark,
            border = if (isSelected) {
                androidx.compose.foundation.BorderStroke(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(DeepRed, NeonRed, DeepRed)
                    )
                )
            } else {
                androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color.White.copy(alpha = 0.15f)
                )
            }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = category.emoji, fontSize = 16.sp)
                Text(
                    text = category.name.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                        fontSize = 12.sp,
                        letterSpacing = if (isSelected) 1.sp else 0.5.sp
                    ),
                    color = if (isSelected) NeonRed else Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ==================== MATCH CARDS ====================

@Composable
private fun CompetitiveMatchCards(
    rooms: List<CompetitiveRoomItem>,
    selectedCategory: String,
    searchQuery: String,
    joinedRooms: Set<String>,
    onToggleJoin: (String) -> Unit,
    onCardTap: (CompetitiveRoomItem) -> Unit
) {
    val filteredRooms = rooms.filter { room ->
        val matchesCategory = selectedCategory == "All" || room.sport == selectedCategory
        val matchesSearch = searchQuery.isEmpty() || 
            room.title.contains(searchQuery, ignoreCase = true) ||
            room.sport.contains(searchQuery, ignoreCase = true) ||
            room.location.contains(searchQuery, ignoreCase = true) ||
            room.hostName.contains(searchQuery, ignoreCase = true) ||
            room.skillLevel.contains(searchQuery, ignoreCase = true) ||
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
                text = "${filteredRooms.size} ELITE MATCHES",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 1.5.sp
                ),
                color = DeepRed
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("⚔️", fontSize = 16.sp)
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
                        text = "No matches found",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "Try a different search or category",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        } else {
            filteredRooms.forEach { room ->
                val isJoined = joinedRooms.contains(room.id)
                CompetitiveRoomCard(
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
private fun CompetitiveRoomCard(
    room: CompetitiveRoomItem,
    isJoined: Boolean,
    onToggleJoin: () -> Unit,
    onCardTap: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "card_${room.id}")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = if (isJoined) 0.3f else 0.15f,
        targetValue = if (isJoined) 0.6f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val cardScale by animateFloatAsState(
        targetValue = if (isJoined) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_scale"
    )

    val animatedPlayerCount by animateIntAsState(
        targetValue = if (isJoined) room.currentPlayers + 1 else room.currentPlayers,
        animationSpec = tween(500, easing = EaseOutBack),
        label = "player_count"
    )

    // Check if slots almost full
    val slotsAlmostFull = animatedPlayerCount >= room.maxPlayers - 1

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
    ) {
        // Neon red glow (stronger when joined or almost full)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            DeepRed.copy(alpha = if (slotsAlmostFull) glowAlpha * 1.2f else glowAlpha),
                            DeepRed.copy(alpha = 0f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .blur(24.dp)
        )

        // Main card - Dark glassmorphism with rounded corners
        Surface(
            onClick = onCardTap,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isJoined) 24.dp else 16.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = DeepRed.copy(alpha = 0.4f),
                    spotColor = NeonRed.copy(alpha = 0.4f)
                ),
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                DarkGrey,
                                CharcoalBlack.copy(alpha = 0.95f),
                                DarkGrey.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .border(
                        width = if (isJoined) 2.5.dp else 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = if (isJoined) {
                                listOf(NeonRed, DeepRed, GlowRed, DeepRed, NeonRed)
                            } else {
                                listOf(
                                    DeepRed.copy(alpha = 0.5f),
                                    Crimson.copy(alpha = 0.3f),
                                    DeepRed.copy(alpha = 0.5f)
                                )
                            }
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        // Sport emoji - clean without heavy shadow
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .background(
                                    color = DeepRed.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = DeepRed.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = room.emoji, fontSize = 30.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = room.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Skill tier badge
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = DeepRed.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    DeepRed.copy(alpha = 0.5f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = NeonRed
                                    )
                                    Text(
                                        text = room.skillLevel.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            letterSpacing = 0.5.sp
                                        ),
                                        color = NeonRed
                                    )
                                }
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
                                    tint = DeepRed.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "${room.location} · ${room.distance}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp
                                    ),
                                    color = Color.White.copy(alpha = 0.5f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Difficulty flames
                        DifficultyIndicator(difficulty = room.difficulty)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tags row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        room.tags.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = DeepRed.copy(alpha = 0.12f),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 1.dp,
                                    color = DeepRed.copy(alpha = 0.4f)
                                )
                            ) {
                                Text(
                                    text = tag.uppercase(),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = NeonRed
                                )
                            }
                        }
                        
                        // Entry fee if available
                        room.entryFee?.let { fee ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF2A1F1F)
                            ) {
                                Text(
                                    text = "💰 $fee",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = Color(0xFFFFD700)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Schedule row
                    Row(
                        modifier = Modifier
                            .background(
                                CharcoalBlack.copy(alpha = 0.6f),
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                DeepRed.copy(alpha = 0.2f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = DeepRed
                        )
                        Text(
                            text = room.schedule,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = DeepRed
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom row: Player slots + Join button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Player avatar slots with red glow when almost full
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row {
                                repeat(minOf(animatedPlayerCount, 4)) { index ->
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .offset(x = (-8 * index).dp)
                                            .then(
                                                if (slotsAlmostFull) {
                                                    Modifier.shadow(
                                                        8.dp,
                                                        CircleShape,
                                                        ambientColor = DeepRed.copy(alpha = 0.5f)
                                                    )
                                                } else Modifier
                                            )
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        DeepRed.copy(alpha = 0.4f),
                                                        Crimson.copy(alpha = 0.2f)
                                                    )
                                                ),
                                                shape = CircleShape
                                            )
                                            .border(
                                                2.dp,
                                                if (slotsAlmostFull) NeonRed.copy(alpha = 0.8f) 
                                                else DeepRed.copy(alpha = 0.5f),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = listOf("😤", "💪", "🔥", "⚡")[index % 4],
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                                // Empty slots
                                repeat(minOf(room.maxPlayers - animatedPlayerCount, 2)) { index ->
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .offset(x = (-8 * (animatedPlayerCount + index)).dp)
                                            .background(
                                                CharcoalBlack,
                                                CircleShape
                                            )
                                            .border(
                                                1.5.dp,
                                                SteelGrey.copy(alpha = 0.5f),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Add,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = Color.White.copy(alpha = 0.3f)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "$animatedPlayerCount/${room.maxPlayers}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Black
                                ),
                                color = if (slotsAlmostFull) NeonRed else Color.White
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
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color.Transparent,
                                    modifier = Modifier
                                        .border(
                                            2.dp,
                                            DeepRed.copy(alpha = 0.6f),
                                            RoundedCornerShape(14.dp)
                                        )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                brush = Brush.horizontalGradient(
                                                    colors = listOf(
                                                        DarkGrey,
                                                        MetallicDark
                                                    )
                                                )
                                            )
                                            .padding(horizontal = 18.dp, vertical = 10.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = "JOINED",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.Black,
                                                    letterSpacing = 1.sp
                                                ),
                                                color = DeepRed
                                            )
                                            Text("✔️", fontSize = 14.sp)
                                        }
                                    }
                                }
                            } else {
                                // Join button with neon glow
                                CompetitiveJoinButton(onClick = onToggleJoin)
                            }
                        }
                    }
                    
                    // Tap to detail hint at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tap for details ⚡",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White.copy(alpha = 0.35f)
                        )
                    }
                }
            }
        }
    }
}

// ==================== DIFFICULTY INDICATOR ====================

@Composable
private fun DifficultyIndicator(difficulty: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            repeat(difficulty) {
                Text("🔥", fontSize = 11.sp)
            }
        }
        Text(
            text = "LVL $difficulty",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 8.sp,
                letterSpacing = 0.5.sp
            ),
            color = NeonRed.copy(alpha = 0.7f)
        )
    }
}

// ==================== JOIN BUTTON ====================

@Composable
private fun CompetitiveJoinButton(onClick: () -> Unit = {}) {
    val infiniteTransition = rememberInfiniteTransition(label = "join_button")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box {
        // Glow effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            DeepRed.copy(alpha = glowAlpha * 0.5f),
                            DeepRed.copy(alpha = 0f)
                        )
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .blur(10.dp)
        )

        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(14.dp),
            color = Color.Transparent,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = pulseScale
                    scaleY = pulseScale
                }
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = DeepRed.copy(alpha = 0.6f)
                )
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                DeepRed,
                                Crimson,
                                DeepRed
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                NeonRed,
                                GlowRed,
                                NeonRed
                            )
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "JOIN",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            fontSize = 13.sp
                        ),
                        color = Color.White
                    )
                    Text("🔥", fontSize = 14.sp)
                }
            }
        }
    }
}

// ==================== FUTURISTIC CREATE FAB ====================

@Composable
private fun CompetitiveCreateRoomFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fab_competitive")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Rotating neon glow ring
        Box(
            modifier = Modifier
                .size(88.dp)
                .graphicsLayer { rotationZ = rotation }
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            DeepRed.copy(alpha = glowAlpha),
                            Color.Transparent,
                            NeonRed.copy(alpha = glowAlpha * 0.8f),
                            Color.Transparent,
                            DeepRed.copy(alpha = glowAlpha)
                        )
                    ),
                    shape = CircleShape
                )
                .blur(20.dp)
        )

        // Rounded futuristic FAB
        Surface(
            onClick = onClick,
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    scaleX = pulseScale
                    scaleY = pulseScale
                }
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = DeepRed.copy(alpha = 0.7f),
                    spotColor = NeonRed.copy(alpha = 0.7f)
                ),
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                CharcoalBlack,
                                DarkGrey,
                                CharcoalBlack
                            )
                        )
                    )
                    .border(
                        width = 2.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                DeepRed,
                                NeonRed,
                                DeepRed
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create Room",
                    modifier = Modifier.size(30.dp),
                    tint = NeonRed
                )
            }
        }
    }
}

// ==================== ROOM DETAIL MODAL ====================

@Composable
private fun CompetitiveRoomDetailModal(
    room: CompetitiveRoomItem,
    isJoined: Boolean,
    onJoin: () -> Unit,
    onDismiss: () -> Unit,
    onEnterRoom: () -> Unit
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
                .background(Color.Black.copy(alpha = 0.7f))
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
                // Futuristic bottom sheet
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { /* Prevent dismiss */ },
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        DarkGrey,
                                        CharcoalBlack,
                                        CharcoalBlack
                                    )
                                ),
                                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                            )
                            .border(
                                width = 2.dp,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        DeepRed.copy(alpha = 0.8f),
                                        DeepRed.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
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
                                    .width(48.dp)
                                    .height(4.dp)
                                    .background(
                                        DeepRed.copy(alpha = 0.6f),
                                        RoundedCornerShape(2.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Sport icon + category header
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .shadow(
                                            16.dp,
                                            RoundedCornerShape(16.dp),
                                            ambientColor = DeepRed.copy(alpha = 0.5f)
                                        )
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    DeepRed.copy(alpha = 0.3f),
                                                    Crimson.copy(alpha = 0.2f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .border(
                                            2.dp,
                                            DeepRed.copy(alpha = 0.6f),
                                            RoundedCornerShape(16.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(room.emoji, fontSize = 40.sp)
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = room.title,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Black,
                                            fontSize = 22.sp,
                                            letterSpacing = 0.5.sp
                                        ),
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = DeepRed.copy(alpha = 0.2f),
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                DeepRed.copy(alpha = 0.5f)
                                            )
                                        ) {
                                            Text(
                                                text = room.sport.uppercase(),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 1.sp
                                                ),
                                                color = DeepRed
                                            )
                                        }
                                        // Difficulty
                                        repeat(room.difficulty) {
                                            Text("🔥", fontSize = 14.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Host info with rating
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        DarkGrey,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .border(
                                        1.dp,
                                        DeepRed.copy(alpha = 0.3f),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(DeepRed, Crimson)
                                            ),
                                            shape = CircleShape
                                        )
                                        .border(2.dp, NeonRed, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("👤", fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "HOST",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            letterSpacing = 1.sp
                                        ),
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = room.hostName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White
                                    )
                                }
                                // Rating
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = Color(0xFFFFD700)
                                    )
                                    Text(
                                        text = "${room.hostRating}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color(0xFFFFD700)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Info cards grid
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CompetitiveDetailInfoCard(
                                    modifier = Modifier.weight(1f),
                                    icon = "📍",
                                    label = "LOCATION",
                                    value = room.location
                                )
                                CompetitiveDetailInfoCard(
                                    modifier = Modifier.weight(1f),
                                    icon = "🕐",
                                    label = "TIME",
                                    value = room.schedule
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CompetitiveDetailInfoCard(
                                    modifier = Modifier.weight(1f),
                                    icon = "📅",
                                    label = "DATE",
                                    value = room.date
                                )
                                CompetitiveDetailInfoCard(
                                    modifier = Modifier.weight(1f),
                                    icon = "⭐",
                                    label = "SKILL TIER",
                                    value = room.skillLevel
                                )
                            }

                            // Entry fee & Prize if available
                            if (room.entryFee != null || room.prize != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    room.entryFee?.let {
                                        CompetitiveDetailInfoCard(
                                            modifier = Modifier.weight(1f),
                                            icon = "💰",
                                            label = "ENTRY FEE",
                                            value = it
                                        )
                                    }
                                    room.prize?.let {
                                        CompetitiveDetailInfoCard(
                                            modifier = Modifier.weight(1f),
                                            icon = "🏆",
                                            label = "PRIZE",
                                            value = it
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Description
                            Text(
                                text = "ABOUT THIS MATCH",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = room.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.6f),
                                lineHeight = 22.sp
                            )

                            // Requirements
                            if (room.requirements.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "REQUIREMENTS",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                room.requirements.forEach { req ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(DeepRed, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = req,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Players with red rank borders
                            Text(
                                text = "COMPETITORS (${room.currentPlayers}/${room.maxPlayers})",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy((-12).dp)
                            ) {
                                val avatarEmojis = listOf("😤", "💪", "🔥", "⚡", "👊", "🎯", "💥", "⚔️")
                                repeat(room.currentPlayers) { index ->
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .shadow(8.dp, CircleShape, ambientColor = DeepRed.copy(alpha = 0.4f))
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        DeepRed.copy(alpha = 0.4f),
                                                        Crimson.copy(alpha = 0.2f)
                                                    )
                                                ),
                                                shape = CircleShape
                                            )
                                            .border(
                                                2.5.dp,
                                                brush = Brush.linearGradient(
                                                    colors = listOf(DeepRed, NeonRed)
                                                ),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            avatarEmojis[index % avatarEmojis.size],
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Tags
                            Text(
                                text = "MATCH TAGS",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                room.tags.forEach { tag ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = DeepRed.copy(alpha = 0.15f),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            DeepRed.copy(alpha = 0.5f)
                                        )
                                    ) {
                                        Text(
                                            text = tag.uppercase(),
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 0.5.sp
                                            ),
                                            color = NeonRed
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // CTA Button
                            if (isJoined) {
                                // Enter Room button
                                CompetitiveCTAButton(
                                    text = "ENTER ROOM",
                                    emoji = "⚔️",
                                    onClick = onEnterRoom
                                )
                            } else {
                                // Join Match button
                                CompetitiveCTAButton(
                                    text = "JOIN MATCH",
                                    emoji = "🔥",
                                    onClick = onJoin
                                )
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
private fun CompetitiveDetailInfoCard(
    modifier: Modifier = Modifier,
    icon: String,
    label: String,
    value: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = CharcoalBlack,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            DeepRed.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(icon, fontSize = 14.sp)
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 0.5.sp
                    ),
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CompetitiveCTAButton(
    text: String,
    emoji: String,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cta_button")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        // Glow behind
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            DeepRed.copy(alpha = glowAlpha * 0.4f),
                            NeonRed.copy(alpha = glowAlpha * 0.5f),
                            DeepRed.copy(alpha = glowAlpha * 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .blur(16.dp)
        )

        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .shadow(
                    16.dp,
                    RoundedCornerShape(14.dp),
                    ambientColor = DeepRed.copy(alpha = 0.5f)
                ),
            shape = RoundedCornerShape(14.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                DeepRed,
                                Crimson,
                                DeepRed
                            )
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(
                        2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(NeonRed, GlowRed, NeonRed)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        ),
                        color = Color.White
                    )
                    Text(emoji, fontSize = 22.sp)
                }
            }
        }
    }
}

// ==================== CATEGORY PICKER MODAL ====================

@Composable
private fun CompetitiveCategoryPickerModal(
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
                .background(Color.Black.copy(alpha = 0.7f))
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
                    shape = RoundedCornerShape(24.dp),
                    color = DarkGrey
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .widthIn(max = 320.dp)
                    ) {
                        // Title
                        Text(
                            text = "FILTER BY CATEGORY",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            ),
                            color = NeonRed
                        )
                        
                        Text(
                            text = "Choose your battleground 🔥",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Category grid
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            competitiveSportCategories.chunked(3).forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    row.forEach { category ->
                                        val isSelected = selectedCategory == category.name
                                        
                                        Surface(
                                            onClick = { onCategorySelected(category.name) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .border(
                                                    width = if (isSelected) 2.dp else 1.dp,
                                                    color = if (isSelected) NeonRed else SteelGrey,
                                                    shape = RoundedCornerShape(14.dp)
                                                ),
                                            shape = RoundedCornerShape(14.dp),
                                            color = if (isSelected) DeepRed.copy(alpha = 0.2f) else CharcoalBlack
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
                                                    text = category.name.uppercase(),
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                                        fontSize = 10.sp,
                                                        letterSpacing = 0.5.sp
                                                    ),
                                                    color = if (isSelected) NeonRed else Color.White.copy(alpha = 0.6f),
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
                            shape = RoundedCornerShape(14.dp),
                            color = DeepRed
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "DONE",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 2.sp
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
private fun CompetitiveAddRoomModal(
    onDismiss: () -> Unit,
    onAddRoom: (CompetitiveRoomItem) -> Unit
) {
    // Form states
    var title by remember { mutableStateOf("") }
    var selectedSport by remember { mutableStateOf(competitiveSportCategories[1]) } // Default Badminton
    var location by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var maxPlayers by remember { mutableStateOf("4") }
    var selectedSkillLevel by remember { mutableStateOf("Intermediate") }
    var selectedDifficulty by remember { mutableStateOf(3) }
    var entryFee by remember { mutableStateOf("") }
    var prize by remember { mutableStateOf("") }
    var tag1 by remember { mutableStateOf("") }
    var tag2 by remember { mutableStateOf("") }
    var requirement1 by remember { mutableStateOf("") }
    var requirement2 by remember { mutableStateOf("") }
    var showSportPicker by remember { mutableStateOf(false) }
    var showSkillPicker by remember { mutableStateOf(false) }

    val skillLevels = listOf("Beginner", "Intermediate", "Intermediate+", "Advanced", "Pro")

    // Validation
    val isFormValid = title.isNotBlank() && location.isNotBlank() && schedule.isNotBlank()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
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
                    .fillMaxHeight(0.9f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Prevent dismiss */ },
                shape = RoundedCornerShape(20.dp),
                color = CharcoalBlack,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    DeepRed.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header with neon glow
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(DeepRed, Crimson, DeepRed)
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
                                    text = "CREATE MATCH ⚔️",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 2.sp
                                    ),
                                    color = Color.White
                                )
                                Text(
                                    text = "Set up your competitive arena",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.8f)
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
                        CompetitiveFormField(
                            label = "MATCH TITLE",
                            value = title,
                            onValueChange = { title = it },
                            placeholder = "e.g., Badminton Ranked Match"
                        )

                        // Sport Picker
                        Text(
                            text = "🏆 SPORT",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = DeepRed
                        )
                        Surface(
                            onClick = { showSportPicker = !showSportPicker },
                            shape = RoundedCornerShape(12.dp),
                            color = DarkGrey,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                DeepRed.copy(alpha = 0.4f)
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
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(selectedSport.emoji, fontSize = 24.sp)
                                    Text(
                                        text = selectedSport.name.uppercase(),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = Color.White
                                    )
                                }
                                Icon(
                                    imageVector = if (showSportPicker) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = DeepRed
                                )
                            }
                        }

                        // Sport Options
                        AnimatedVisibility(visible = showSportPicker) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = DarkGrey,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        1.dp,
                                        DeepRed.copy(alpha = 0.3f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                competitiveSportCategories.filter { it.name != "All" }.forEach { sport ->
                                    Surface(
                                        onClick = {
                                            selectedSport = sport
                                            showSportPicker = false
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (selectedSport == sport) DeepRed.copy(alpha = 0.3f) else Color.Transparent
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
                                                text = sport.name.uppercase(),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (selectedSport == sport) FontWeight.Bold else FontWeight.Normal,
                                                    letterSpacing = 0.5.sp
                                                ),
                                                color = if (selectedSport == sport) NeonRed else Color.White.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Location & Distance Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(2f)) {
                                CompetitiveFormField(
                                    label = "📍 LOCATION",
                                    value = location,
                                    onValueChange = { location = it },
                                    placeholder = "e.g., Elite Sports Center"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                CompetitiveFormField(
                                    label = "DISTANCE",
                                    value = distance,
                                    onValueChange = { distance = it },
                                    placeholder = "e.g., 2.3 km"
                                )
                            }
                        }

                        // Schedule & Date Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                CompetitiveFormField(
                                    label = "⏰ TIME",
                                    value = schedule,
                                    onValueChange = { schedule = it },
                                    placeholder = "e.g., Today · 19:00"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                CompetitiveFormField(
                                    label = "📅 DATE",
                                    value = date,
                                    onValueChange = { date = it },
                                    placeholder = "e.g., Dec 1, 2025"
                                )
                            }
                        }

                        // Description
                        CompetitiveFormField(
                            label = "💬 DESCRIPTION",
                            value = description,
                            onValueChange = { description = it },
                            placeholder = "Describe your competitive match...",
                            singleLine = false,
                            minLines = 3
                        )

                        // Skill Level Picker
                        Text(
                            text = "⭐ SKILL LEVEL",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = DeepRed
                        )
                        Surface(
                            onClick = { showSkillPicker = !showSkillPicker },
                            shape = RoundedCornerShape(12.dp),
                            color = DarkGrey,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                NeonRed.copy(alpha = 0.4f)
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
                                    text = selectedSkillLevel.uppercase(),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = NeonRed
                                )
                                Icon(
                                    imageVector = if (showSkillPicker) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = NeonRed
                                )
                            }
                        }

                        AnimatedVisibility(visible = showSkillPicker) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = DarkGrey,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        1.dp,
                                        NeonRed.copy(alpha = 0.3f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                skillLevels.forEach { level ->
                                    Surface(
                                        onClick = {
                                            selectedSkillLevel = level
                                            showSkillPicker = false
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (selectedSkillLevel == level) NeonRed.copy(alpha = 0.3f) else Color.Transparent
                                    ) {
                                        Text(
                                            text = level.uppercase(),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (selectedSkillLevel == level) FontWeight.Bold else FontWeight.Normal,
                                                letterSpacing = 0.5.sp
                                            ),
                                            color = if (selectedSkillLevel == level) NeonRed else Color.White.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }

                        // Difficulty (Flames)
                        Text(
                            text = "🔥 DIFFICULTY",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = DeepRed
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            (1..5).forEach { level ->
                                Surface(
                                    onClick = { selectedDifficulty = level },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (level <= selectedDifficulty) DeepRed.copy(alpha = 0.3f) else DarkGrey,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (level <= selectedDifficulty) NeonRed else SteelGrey
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "🔥",
                                            fontSize = if (level <= selectedDifficulty) 20.sp else 16.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Max Players
                        CompetitiveFormField(
                            label = "👥 MAX PLAYERS",
                            value = maxPlayers,
                            onValueChange = { if (it.all { c -> c.isDigit() }) maxPlayers = it },
                            placeholder = "e.g., 4"
                        )

                        // Entry Fee & Prize Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                CompetitiveFormField(
                                    label = "💰 ENTRY FEE",
                                    value = entryFee,
                                    onValueChange = { entryFee = it },
                                    placeholder = "e.g., 50K"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                CompetitiveFormField(
                                    label = "🏆 PRIZE",
                                    value = prize,
                                    onValueChange = { prize = it },
                                    placeholder = "e.g., 500K"
                                )
                            }
                        }

                        // Tags
                        Text(
                            text = "🏷️ TAGS (optional)",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = DeepRed
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                CompetitiveFormFieldSimple(
                                    value = tag1,
                                    onValueChange = { tag1 = it },
                                    placeholder = "e.g., Ranked"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                CompetitiveFormFieldSimple(
                                    value = tag2,
                                    onValueChange = { tag2 = it },
                                    placeholder = "e.g., High Intensity"
                                )
                            }
                        }

                        // Requirements
                        Text(
                            text = "📋 REQUIREMENTS (optional)",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = DeepRed
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                CompetitiveFormFieldSimple(
                                    value = requirement1,
                                    onValueChange = { requirement1 = it },
                                    placeholder = "e.g., Own racket"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                CompetitiveFormFieldSimple(
                                    value = requirement2,
                                    onValueChange = { requirement2 = it },
                                    placeholder = "e.g., 2+ years exp"
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
                                    colors = listOf(Color.Transparent, CharcoalBlack)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Button(
                            onClick = {
                                val tags = listOfNotNull(
                                    tag1.takeIf { it.isNotBlank() },
                                    tag2.takeIf { it.isNotBlank() }
                                ).ifEmpty { listOf("Competitive", "Ranked") }

                                val requirements = listOfNotNull(
                                    requirement1.takeIf { it.isNotBlank() },
                                    requirement2.takeIf { it.isNotBlank() }
                                )

                                val newRoom = CompetitiveRoomItem(
                                    id = System.currentTimeMillis().toString(),
                                    title = title,
                                    sport = selectedSport.name,
                                    emoji = selectedSport.emoji,
                                    location = location,
                                    distance = distance.ifBlank { "N/A" },
                                    schedule = schedule.ifBlank { "TBD" },
                                    date = date.ifBlank { "TBD" },
                                    description = description.ifBlank { "Competitive match for serious players. Bring your A-game! 🔥" },
                                    hostName = "You",
                                    hostRating = 4.5f,
                                    currentPlayers = 1,
                                    maxPlayers = maxPlayers.toIntOrNull() ?: 4,
                                    skillLevel = selectedSkillLevel,
                                    tags = tags,
                                    difficulty = selectedDifficulty,
                                    entryFee = entryFee.takeIf { it.isNotBlank() },
                                    prize = prize.takeIf { it.isNotBlank() },
                                    requirements = requirements
                                )
                                onAddRoom(newRoom)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = isFormValid,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepRed,
                                disabledContainerColor = DeepRed.copy(alpha = 0.4f)
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
                                    text = "CREATE MATCH",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 2.sp
                                    ),
                                    color = Color.White
                                )
                                Text("⚔️", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompetitiveFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = DeepRed
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.White.copy(alpha = 0.3f)
                )
            },
            singleLine = singleLine,
            minLines = minLines,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonRed,
                unfocusedBorderColor = SteelGrey,
                focusedContainerColor = DarkGrey,
                unfocusedContainerColor = DarkGrey,
                cursorColor = NeonRed,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}

@Composable
private fun CompetitiveFormFieldSimple(
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
                color = Color.White.copy(alpha = 0.3f),
                fontSize = 13.sp
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DeepRed,
            unfocusedBorderColor = SteelGrey.copy(alpha = 0.5f),
            focusedContainerColor = DarkGrey,
            unfocusedContainerColor = DarkGrey,
            cursorColor = DeepRed,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}
