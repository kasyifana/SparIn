package com.example.sparin.presentation.discover

import java.text.SimpleDateFormat
import java.util.*
import org.koin.androidx.compose.koinViewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.navigation.NavHostController
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Discover Competitive Mode Screen
 * High-energy, intense, elite design with red + dark tones
 * Futuristic competitive atmosphere with neon accents
 */

// Competitive color palette
val NeonRed = Color(0xFFFF2D2D)
val DarkGraphite = Color(0xFF1A1A1A)
val Gunmetal = Color(0xFF0F0F0F)
val MetallicRed = Color(0xFFDC143C)
val GlowRed = Color(0xFFFF4444)

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

// Competitive room data
data class CompetitiveRoomItem(
    val id: String,
    val title: String,
    val sport: String,
    val emoji: String,
    val location: String,
    val distance: String,
    val schedule: String,
    val currentPlayers: Int,
    val maxPlayers: Int,
    val skillLevel: String,
    val tags: List<String>,
    val difficulty: Int // 1-5 flames
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
        currentPlayers = 2,
        maxPlayers = 4,
        skillLevel = "Intermediate+",
        tags = listOf("Ranked", "High Skill"),
        difficulty = 4
    ),
    CompetitiveRoomItem(
        id = "2",
        title = "Futsal Tournament Qualifier",
        sport = "Futsal",
        emoji = "⚽",
        location = "Champion Arena",
        distance = "3.1 km",
        schedule = "Tomorrow · 20:00",
        currentPlayers = 8,
        maxPlayers = 10,
        skillLevel = "Advanced",
        tags = listOf("Tournament", "Serious Only"),
        difficulty = 5
    ),
    CompetitiveRoomItem(
        id = "3",
        title = "Basketball Pro League",
        sport = "Basket",
        emoji = "🏀",
        location = "Senayan Pro Court",
        distance = "1.8 km",
        schedule = "Today · 18:00",
        currentPlayers = 6,
        maxPlayers = 10,
        skillLevel = "Pro",
        tags = listOf("Ranked", "Elite Only"),
        difficulty = 5
    ),
    CompetitiveRoomItem(
        id = "4",
        title = "Muaythai Sparring Session",
        sport = "Muaythai",
        emoji = "🥋",
        location = "Fight Club Elite",
        distance = "4.2 km",
        schedule = "Sat · 17:00",
        currentPlayers = 4,
        maxPlayers = 8,
        skillLevel = "Intermediate+",
        tags = listOf("Serious Only", "High Skill"),
        difficulty = 4
    )
)

@Composable
fun DiscoverCompetitiveScreen(
    navController: NavHostController,
    viewModel: DiscoverViewModel = koinViewModel()
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val scrollState = rememberScrollState()
    val roomsState by viewModel.competitiveRoomsState.collectAsState()

    // Refresh on entry
    LaunchedEffect(Unit) {
        viewModel.loadCompetitiveRooms()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0A0A),
                        Gunmetal,
                        DarkGraphite,
                        Color(0xFF0A0A0A)
                    )
                )
            )
    ) {
        // Dark background with red accents
        CompetitiveBackgroundEffects()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))





            // Header
            CompetitiveHeader(onBack = { navController.navigateUp() })

            Spacer(modifier = Modifier.height(20.dp))

            // Sport Category Navbar (Sharp edges)
            CompetitiveSportTabs(
                selectedCategory = selectedCategory,
                onCategorySelected = { 
                    selectedCategory = it
                    viewModel.filterByCategory(it)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Competitive Match Cards
            when (val state = roomsState) {
                is RoomsState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = NeonRed)
                    }
                }
                is RoomsState.Error -> {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("Error: ${state.message}", color = Color.Red)
                    }
                }
                is RoomsState.Success -> {
                    val rooms = state.rooms
                    if (rooms.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text("No competitive rooms found. Create one!", color = Color.Gray)
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "${rooms.size} elite matches",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = NeonRed
                            )

                            rooms.forEach { room ->
                                // Map Room to UI Item
                                val uiItem = CompetitiveRoomItem(
                                    id = room.id,
                                    title = room.name,
                                    sport = room.category,
                                    emoji = getEmojiForCategory(room.category),
                                    location = room.locationName,
                                    distance = "2.5 km", // Dummy distance
                                    schedule = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(room.dateTime)),
                                    currentPlayers = room.currentPlayers,
                                    maxPlayers = room.maxPlayers,
                                    skillLevel = "Intermediate+", // Default
                                    tags = listOf("Ranked", "Serious"), // Default
                                    difficulty = 4 // Default
                                )
                                CompetitiveRoomCard(
                                    room = uiItem,
                                    onClick = { navController.navigate("room_detail/${room.id}") }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Hexagonal Create Room FAB
        CompetitiveCreateRoomFAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp),
            onClick = { navController.navigate("create_room/Competitive") }
        )
    }
}

// ... (Rest of the file remains mostly the same, but CompetitiveMatchCards function is removed/inlined above)

// ==================== MATCH CARDS (Updated to accept onClick) ====================

@Composable
private fun CompetitiveRoomCard(room: CompetitiveRoomItem, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "card_${room.id}")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Red neon glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            NeonRed.copy(alpha = glowAlpha * 0.3f),
                            NeonRed.copy(alpha = 0f)
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .blur(20.dp)
        )

        // Main card
        Surface(
            onClick = onClick, // Use passed onClick
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(10.dp),
                    ambientColor = NeonRed.copy(alpha = 0.4f),
                    spotColor = NeonRed.copy(alpha = 0.4f)
                ),
            shape = RoundedCornerShape(10.dp), // Sharp corners
            color = Gunmetal
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1A1A1A),
                                Gunmetal,
                                Color(0xFF0F0F0F)
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                NeonRed.copy(alpha = 0.5f),
                                MetallicRed.copy(alpha = 0.3f),
                                NeonRed.copy(alpha = 0.5f)
                            )
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sport emoji with metallic background
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .shadow(
                                    elevation = 12.dp,
                                    shape = RoundedCornerShape(8.dp),
                                    ambientColor = NeonRed.copy(alpha = 0.4f)
                                )
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            NeonRed.copy(alpha = 0.3f),
                                            MetallicRed.copy(alpha = 0.2f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = NeonRed.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(8.dp)
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
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    letterSpacing = 0.3.sp
                                ),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = NeonRed.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "${room.location} · ${room.distance}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.6f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Difficulty flames
                        DifficultyIndicator(difficulty = room.difficulty)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tags
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        room.tags.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = NeonRed.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 1.dp,
                                    color = NeonRed.copy(alpha = 0.4f)
                                )
                            ) {
                                Text(
                                    text = tag,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.3.sp
                                    ),
                                    color = NeonRed
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Skill level requirement
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = GlowRed
                        )
                        Text(
                            text = "Skill Level Required: ${room.skillLevel}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bottom row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Schedule
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = NeonRed
                            )
                            Text(
                                text = room.schedule,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        // Players count + Join button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.People,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = NeonRed
                                )
                                Text(
                                    text = "${room.currentPlayers}/${room.maxPlayers}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = NeonRed
                                )
                            }

                            // Join button
                            Surface(
                                onClick = { /* Join room */ },
                                shape = RoundedCornerShape(6.dp),
                                color = NeonRed.copy(alpha = 0.9f),
                                modifier = Modifier.shadow(
                                    elevation = 6.dp,
                                    shape = RoundedCornerShape(6.dp),
                                    ambientColor = NeonRed.copy(alpha = 0.3f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Join",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper for Difficulty
@Composable
fun DifficultyIndicator(difficulty: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(5) { index ->
            Text(
                text = "🔥",
                fontSize = 12.sp,
                color = if (index < difficulty) Color.Unspecified else Color.White.copy(alpha = 0.2f)
            )
        }
    }
}

// ==================== BACKGROUND EFFECTS ====================

@Composable
private fun CompetitiveBackgroundEffects() {
    val infiniteTransition = rememberInfiniteTransition(label = "competitive_bg")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(120.dp)
    ) {
        // Red glow spots
        drawCircle(
            color = NeonRed.copy(alpha = glowAlpha),
            radius = 250f,
            center = Offset(
                x = size.width * 0.2f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 60f,
                y = size.height * 0.15f
            )
        )

        drawCircle(
            color = MetallicRed.copy(alpha = glowAlpha * 0.8f),
            radius = 200f,
            center = Offset(
                x = size.width * 0.8f,
                y = size.height * 0.6f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 50f
            )
        )

        drawCircle(
            color = GlowRed.copy(alpha = glowAlpha * 0.6f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.5f,
                y = size.height * 0.85f
            )
        )
    }
}



// ==================== HEADER ====================

@Composable
private fun CompetitiveHeader(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Back Button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .offset(x = (-12).dp) // Align with padding
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        // Title
        Text(
            text = "Competitive Rooms 🔥",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                letterSpacing = (-0.5).sp
            ),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Subtitle
        Text(
            text = "Find real challenges near you.",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 15.sp
            ),
            color = Color.White.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Search Bar (Dark themed)
        CompetitiveSearchBar()
    }
}

@Composable
private fun CompetitiveSearchBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = NeonRed.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(8.dp),
        color = DarkGraphite
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = NeonRed.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                modifier = Modifier.size(22.dp),
                tint = NeonRed.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Search competitive matches...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Rounded.FilterList,
                contentDescription = "Filter",
                modifier = Modifier.size(20.dp),
                tint = NeonRed
            )
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
        Text(
            text = "Sport Categories",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 0.5.sp
            ),
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

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
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
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
                                NeonRed.copy(alpha = glowAlpha * 0.5f),
                                NeonRed.copy(alpha = 0f)
                            )
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .blur(12.dp)
            )
        }

        Surface(
            onClick = onClick,
            modifier = Modifier
                .shadow(
                    elevation = if (isSelected) 12.dp else 4.dp,
                    shape = RoundedCornerShape(6.dp),
                    ambientColor = if (isSelected) NeonRed.copy(alpha = 0.5f) else Color.Transparent
                ),
            shape = RoundedCornerShape(6.dp), // Sharp corners
            color = if (isSelected) {
                DarkGraphite
            } else {
                Color(0xFF1F1F1F)
            },
            border = androidx.compose.foundation.BorderStroke(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) NeonRed else Color.White.copy(alpha = 0.2f)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = category.emoji, fontSize = 16.sp)
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        fontSize = 13.sp,
                        letterSpacing = if (isSelected) 0.5.sp else 0.sp
                    ),
                    color = if (isSelected) NeonRed else Color.White.copy(alpha = 0.7f)
                )

                if (isSelected) {
                    Text("⚡", fontSize = 10.sp)
                }
            }
        }
    }
}

// Duplicates removed

// ==================== HEXAGONAL CREATE FAB ====================

@Composable
private fun CompetitiveCreateRoomFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fab_competitive")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Rotating glow
        Box(
            modifier = Modifier
                .size(80.dp)
                .graphicsLayer { rotationZ = rotation }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            NeonRed.copy(alpha = glowAlpha),
                            NeonRed.copy(alpha = 0f)
                        )
                    ),
                    shape = CircleShape
                )
                .blur(16.dp)
        )

        // FAB
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .size(64.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = NeonRed.copy(alpha = 0.6f),
                    spotColor = NeonRed.copy(alpha = 0.6f)
                ),
            containerColor = DarkGraphite,
            contentColor = NeonRed,
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DarkGraphite,
                                Gunmetal
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = NeonRed,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create Room",
                    modifier = Modifier.size(28.dp),
                    tint = NeonRed
                )
            }
        }
    }
}
