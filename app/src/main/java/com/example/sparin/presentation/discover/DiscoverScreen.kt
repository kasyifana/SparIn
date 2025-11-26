package com.example.sparin.presentation.discover

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Premium Discover / Match Page for SparIN
 * Gen-Z aesthetic: soft-neumorphism, frosted glass cards, pastel gradients
 * Ultra-rounded, airy spacing, floating 3D elements
 */

// Sport categories for discover
data class DiscoverSportCategory(
    val name: String,
    val emoji: String
)

val discoverCategories = listOf(
    DiscoverSportCategory("All", "🌟"),
    DiscoverSportCategory("Badminton", "🏸"),
    DiscoverSportCategory("Basket", "🏀"),
    DiscoverSportCategory("Futsal", "⚽"),
    DiscoverSportCategory("Voli", "🏐"),
    DiscoverSportCategory("Tenis", "🎾"),
    DiscoverSportCategory("Tenis Meja", "🏓"),
    DiscoverSportCategory("Padel", "🎾"),
    DiscoverSportCategory("Sepak Bola", "⚽"),
    DiscoverSportCategory("Mini Soccer", "⚽"),
    DiscoverSportCategory("Lari", "🏃"),
    DiscoverSportCategory("Cycling", "🚴"),
    DiscoverSportCategory("Gym", "💪"),
    DiscoverSportCategory("Boxing", "🥊"),
    DiscoverSportCategory("Muaythai", "🥋"),
    DiscoverSportCategory("Billiard", "🎱"),
    DiscoverSportCategory("Catur", "♟️"),
    DiscoverSportCategory("Hiking", "🥾"),
    DiscoverSportCategory("Swimming", "🏊")
)

// Room data model
data class RoomItem(
    val id: String,
    val title: String,
    val sport: String,
    val emoji: String,
    val location: String,
    val skillLevel: SkillLevel,
    val schedule: String,
    val currentPlayers: Int,
    val maxPlayers: Int,
    val price: String? = null,
    val mode: RoomMode,
    val accentColor: Color
)

enum class SkillLevel(val label: String, val color: Color) {
    BEGINNER("Beginner", MintBreeze),
    INTERMEDIATE("Intermediate", PeachGlow),
    ADVANCED("Advanced", RoseDust)
}

enum class RoomMode {
    CASUAL, COMPETITIVE
}

// Sample rooms data
val sampleRooms = listOf(
    RoomItem(
        id = "1",
        title = "Badminton Fun Match",
        sport = "Badminton",
        emoji = "🏸",
        location = "GOR Soemantri, Jakarta",
        skillLevel = SkillLevel.BEGINNER,
        schedule = "Today · 19:00",
        currentPlayers = 3,
        maxPlayers = 6,
        price = null,
        mode = RoomMode.CASUAL,
        accentColor = PeachGlow
    ),
    RoomItem(
        id = "2",
        title = "Futsal Night League",
        sport = "Futsal",
        emoji = "⚽",
        location = "Champion Futsal, BSD",
        skillLevel = SkillLevel.INTERMEDIATE,
        schedule = "Tomorrow · 20:00",
        currentPlayers = 8,
        maxPlayers = 10,
        price = "Rp 25k/orang",
        mode = RoomMode.COMPETITIVE,
        accentColor = MintBreeze
    ),
    RoomItem(
        id = "3",
        title = "Basketball 3v3 Pickup",
        sport = "Basket",
        emoji = "🏀",
        location = "Senayan Park Court",
        skillLevel = SkillLevel.INTERMEDIATE,
        schedule = "Today · 17:00",
        currentPlayers = 4,
        maxPlayers = 6,
        price = null,
        mode = RoomMode.CASUAL,
        accentColor = SkyMist
    ),
    RoomItem(
        id = "4",
        title = "Tennis Pro Sparring",
        sport = "Tenis",
        emoji = "🎾",
        location = "Elite Tennis Club, Kemang",
        skillLevel = SkillLevel.ADVANCED,
        schedule = "Sat · 08:00",
        currentPlayers = 2,
        maxPlayers = 4,
        price = "Rp 50k/orang",
        mode = RoomMode.COMPETITIVE,
        accentColor = RoseDust
    ),
    RoomItem(
        id = "5",
        title = "Voli Pantai Weekend",
        sport = "Voli",
        emoji = "🏐",
        location = "Ancol Beach Volleyball",
        skillLevel = SkillLevel.BEGINNER,
        schedule = "Sun · 07:00",
        currentPlayers = 6,
        maxPlayers = 12,
        price = "Rp 15k/orang",
        mode = RoomMode.CASUAL,
        accentColor = PeachGlow
    ),
    RoomItem(
        id = "6",
        title = "Boxing Training Session",
        sport = "Boxing",
        emoji = "🥊",
        location = "Fight Club Gym, SCBD",
        skillLevel = SkillLevel.INTERMEDIATE,
        schedule = "Today · 18:30",
        currentPlayers = 5,
        maxPlayers = 8,
        price = "Rp 75k/orang",
        mode = RoomMode.COMPETITIVE,
        accentColor = ChineseSilver
    ),
    RoomItem(
        id = "7",
        title = "Morning Run Jakarta",
        sport = "Lari",
        emoji = "🏃",
        location = "Sudirman-Thamrin Route",
        skillLevel = SkillLevel.BEGINNER,
        schedule = "Tomorrow · 05:30",
        currentPlayers = 12,
        maxPlayers = 30,
        price = null,
        mode = RoomMode.CASUAL,
        accentColor = MintBreeze
    ),
    RoomItem(
        id = "8",
        title = "Cycling Weekend Trip",
        sport = "Cycling",
        emoji = "🚴",
        location = "BSD - Serpong Loop",
        skillLevel = SkillLevel.INTERMEDIATE,
        schedule = "Sat · 06:00",
        currentPlayers = 8,
        maxPlayers = 15,
        price = null,
        mode = RoomMode.CASUAL,
        accentColor = SkyMist
    )
)

@Composable
fun DiscoverScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedMode by remember { mutableStateOf(RoomMode.CASUAL) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CascadingWhite,
                        SoftLavender.copy(alpha = 0.15f),
                        CascadingWhite
                    )
                )
            )
    ) {
        // Background floating blobs
        DiscoverBackgroundBlobs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Section 1: Header + Search + FAB
            DiscoverHeader(onCreateRoom = { /* Navigate to create room */ })

            Spacer(modifier = Modifier.height(20.dp))

            // Section 2: Sport Category Navbar
            SportCategoryNavbarSection(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Section 3: Mode Picker (Casual / Competitive)
            ModePickerSection(
                selectedMode = selectedMode,
                onModeSelected = { selectedMode = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section 4: List of Rooms
            RoomListSection(
                selectedCategory = selectedCategory,
                selectedMode = selectedMode
            )

            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for nav bar
        }

        // Floating Create Room FAB
        CreateRoomFAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp),
            onClick = { /* Navigate to create room */ }
        )
    }
}

// ==================== BACKGROUND BLOBS ====================

@Composable
private fun DiscoverBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "discover_blobs")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob_offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(32000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob_offset2"
    )

    val offset3 by infiniteTransition.animateFloat(
        initialValue = 90f,
        targetValue = 450f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob_offset3"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(90.dp)
    ) {
        // Top-left sport blob
        drawCircle(
            color = PeachGlow.copy(alpha = 0.2f),
            radius = 200f,
            center = Offset(
                x = size.width * 0.08f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 40f,
                y = size.height * 0.05f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 25f
            )
        )

        // Top-right blob
        drawCircle(
            color = ChineseSilver.copy(alpha = 0.22f),
            radius = 170f,
            center = Offset(
                x = size.width * 0.88f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 30f,
                y = size.height * 0.12f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 35f
            )
        )

        // Middle-left blob
        drawCircle(
            color = MintBreeze.copy(alpha = 0.15f),
            radius = 160f,
            center = Offset(
                x = size.width * 0.15f + sin(Math.toRadians(offset3.toDouble())).toFloat() * 35f,
                y = size.height * 0.4f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )

        // Middle-right blob
        drawCircle(
            color = SkyMist.copy(alpha = 0.12f),
            radius = 140f,
            center = Offset(
                x = size.width * 0.9f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 25f,
                y = size.height * 0.35f + sin(Math.toRadians(offset3.toDouble())).toFloat() * 40f
            )
        )

        // Lower-center blob
        drawCircle(
            color = RoseDust.copy(alpha = 0.1f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.5f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 35f,
                y = size.height * 0.65f + cos(Math.toRadians(offset3.toDouble())).toFloat() * 30f
            )
        )

        // Bottom blob
        drawCircle(
            color = PeachGlow.copy(alpha = 0.1f),
            radius = 150f,
            center = Offset(
                x = size.width * 0.25f + cos(Math.toRadians(offset3.toDouble())).toFloat() * 30f,
                y = size.height * 0.85f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 25f
            )
        )
    }
}

// ==================== SECTION 1: HEADER + SEARCH ====================

@Composable
private fun DiscoverHeader(onCreateRoom: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "header_float")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    val float3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1900, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float3"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
    ) {
        // Floating 3D sport elements
        Box(
            modifier = Modifier
                .size(35.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-50).dp, y = (15 + float1).dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = PeachGlow.copy(alpha = 0.4f)
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PeachGlow.copy(alpha = 0.7f),
                            PeachGlow.copy(alpha = 0.3f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("🏸", fontSize = 16.sp)
        }

        Box(
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-95).dp, y = (35 + float2).dp)
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape,
                    ambientColor = MintBreeze.copy(alpha = 0.4f)
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MintBreeze.copy(alpha = 0.7f),
                            MintBreeze.copy(alpha = 0.3f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("⚽", fontSize = 12.sp)
        }

        Box(
            modifier = Modifier
                .size(22.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-130).dp, y = (20 + float3).dp)
                .background(
                    color = SkyMist.copy(alpha = 0.5f),
                    shape = CircleShape
                )
                .blur(4.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Title
            Text(
                text = "Discover Matches",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = Lead
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = "Find a room that fits your playstyle",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp
                ),
                color = WarmHaze
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Search Bar
            SearchBar()
        }
    }
}

@Composable
private fun SearchBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.12f),
                spotColor = NeumorphDark.copy(alpha = 0.12f)
            ),
        shape = RoundedCornerShape(26.dp),
        color = NeumorphLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = Dreamland.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                modifier = Modifier.size(22.dp),
                tint = WarmHaze.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Search rooms, sports, locations...",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmHaze.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Filter button
            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(12.dp),
                color = ChineseSilver.copy(alpha = 0.4f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Tune,
                        contentDescription = "Filter",
                        modifier = Modifier.size(18.dp),
                        tint = WarmHaze
                    )
                }
            }
        }
    }
}

// ==================== SECTION 2: SPORT CATEGORY NAVBAR ====================

@Composable
private fun SportCategoryNavbarSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Sport Categories",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            ),
            color = Lead,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            discoverCategories.forEach { category ->
                SportCategoryPill(
                    category = category,
                    isSelected = selectedCategory == category.name,
                    onClick = { onCategorySelected(category.name) }
                )
            }
        }
    }
}

@Composable
private fun SportCategoryPill(
    category: DiscoverSportCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else NeumorphLight,
        animationSpec = tween(250),
        label = "pill_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) SunsetOrange else Dreamland.copy(alpha = 0.35f),
        animationSpec = tween(250),
        label = "pill_border"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 10.dp else 4.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pill_elevation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pill_scale"
    )

    Box {
        // Glow effect for active
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(y = 2.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Crunch.copy(alpha = 0.4f),
                                Crunch.copy(alpha = 0f)
                            )
                        ),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .blur(12.dp)
            )
        }

        Surface(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(22.dp),
                    ambientColor = if (isSelected) Crunch.copy(alpha = 0.35f) else NeumorphDark.copy(alpha = 0.1f),
                    spotColor = if (isSelected) Crunch.copy(alpha = 0.35f) else NeumorphDark.copy(alpha = 0.1f)
                )
                .border(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(22.dp)
                )
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(22.dp),
            color = backgroundColor
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = category.emoji, fontSize = 15.sp)
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    ),
                    color = if (isSelected) Lead else WarmHaze
                )

                // Spark particle for active
                if (isSelected) {
                    Text(
                        text = "✨",
                        fontSize = 10.sp,
                        modifier = Modifier.offset(x = 2.dp, y = (-2).dp)
                    )
                }
            }
        }
    }
}

// ==================== SECTION 3: MODE PICKER ====================

@Composable
private fun ModePickerSection(
    selectedMode: RoomMode,
    onModeSelected: (RoomMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Match Mode",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            ),
            color = Lead
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Mode toggle capsule
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = NeumorphDark.copy(alpha = 0.12f),
                    spotColor = NeumorphDark.copy(alpha = 0.12f)
                ),
            shape = RoundedCornerShape(28.dp),
            color = NeumorphLight
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = Dreamland.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Casual Mode
                    ModeButton(
                        text = "CASUAL",
                        emoji = "🎮",
                        isSelected = selectedMode == RoomMode.CASUAL,
                        onClick = { onModeSelected(RoomMode.CASUAL) },
                        modifier = Modifier.weight(1f)
                    )

                    // Competitive Mode
                    ModeButton(
                        text = "COMPETITIVE",
                        emoji = "🏆",
                        isSelected = selectedMode == RoomMode.COMPETITIVE,
                        onClick = { onModeSelected(RoomMode.COMPETITIVE) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeButton(
    text: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else Color.Transparent,
        animationSpec = tween(300, easing = EaseInOutCubic),
        label = "mode_bg"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "mode_elevation"
    )

    Box(
        modifier = modifier
    ) {
        // Glow effect
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Crunch.copy(alpha = 0.3f),
                                Crunch.copy(alpha = 0f)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .blur(8.dp)
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = if (isSelected) Crunch.copy(alpha = 0.3f) else Color.Transparent,
                    spotColor = if (isSelected) Crunch.copy(alpha = 0.3f) else Color.Transparent
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            shape = RoundedCornerShape(24.dp),
            color = backgroundColor
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = emoji,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 1.sp,
                        fontSize = 12.sp
                    ),
                    color = if (isSelected) Lead else WarmHaze
                )
            }
        }
    }
}

// ==================== SECTION 4: ROOM LIST ====================

@Composable
private fun RoomListSection(
    selectedCategory: String,
    selectedMode: RoomMode
) {
    val filteredRooms = sampleRooms.filter { room ->
        val categoryMatch = selectedCategory == "All" || room.sport.contains(selectedCategory, ignoreCase = true)
        val modeMatch = room.mode == selectedMode
        categoryMatch && modeMatch
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Available Rooms",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = Lead
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ChineseSilver.copy(alpha = 0.4f)
            ) {
                Text(
                    text = "${filteredRooms.size} rooms",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = WarmHaze
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (filteredRooms.isEmpty()) {
            // Section 5: Empty State
            EmptyStateSection()
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                filteredRooms.forEach { room ->
                    RoomCard(room = room)
                }
            }
        }
    }
}

@Composable
private fun RoomCard(room: RoomItem) {
    val infiniteTransition = rememberInfiniteTransition(label = "room_card_${room.id}")

    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_float"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = floatAnim.dp)
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.14f),
                spotColor = NeumorphDark.copy(alpha = 0.14f)
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
                            NeumorphLight.copy(alpha = 0.98f),
                            NeumorphLight.copy(alpha = 0.95f),
                            room.accentColor.copy(alpha = 0.08f)
                        ),
                        start = Offset.Zero,
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Dreamland.copy(alpha = 0.2f),
                            room.accentColor.copy(alpha = 0.2f),
                            Dreamland.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            // Background accent blob
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-30).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                room.accentColor.copy(alpha = 0.25f),
                                room.accentColor.copy(alpha = 0f)
                            )
                        ),
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // 3D Sport Icon
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = room.accentColor.copy(alpha = 0.4f),
                                spotColor = room.accentColor.copy(alpha = 0.4f)
                            )
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        room.accentColor.copy(alpha = 0.6f),
                                        room.accentColor.copy(alpha = 0.3f)
                                    )
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = NeumorphLight.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = room.emoji, fontSize = 32.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Room Details
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        // Room Title
                        Text(
                            text = room.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            ),
                            color = Lead,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

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
                                tint = WarmHaze.copy(alpha = 0.7f)
                            )
                            Text(
                                text = room.location,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp
                                ),
                                color = WarmHaze,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Chips row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            // Skill level chip
                            ChipTag(
                                text = room.skillLevel.label,
                                backgroundColor = room.skillLevel.color.copy(alpha = 0.4f),
                                textColor = Lead
                            )

                            // Schedule chip
                            ChipTag(
                                text = room.schedule,
                                icon = Icons.Rounded.Schedule,
                                backgroundColor = ChineseSilver.copy(alpha = 0.5f),
                                textColor = WarmHaze
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom row: slots, price, CTA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Slot count
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Groups,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = WarmHaze
                            )
                            Text(
                                text = "${room.currentPlayers}/${room.maxPlayers}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Lead
                            )
                            Text(
                                text = "players",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmHaze
                            )
                        }

                        // Price tag (if exists)
                        room.price?.let { price ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = PeachGlow.copy(alpha = 0.3f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "💰",
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = price,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 11.sp
                                        ),
                                        color = Lead
                                    )
                                }
                            }
                        }
                    }

                    // View Room CTA Button
                    Surface(
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(14.dp),
                                ambientColor = Crunch.copy(alpha = 0.35f),
                                spotColor = Crunch.copy(alpha = 0.35f)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { /* Navigate to room detail */ },
                        shape = RoundedCornerShape(14.dp),
                        color = Color.Transparent
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Crunch,
                                            SunsetOrange
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
                                    text = "View Room",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    ),
                                    color = Lead
                                )
                                Icon(
                                    imageVector = Icons.Rounded.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Lead
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChipTag(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = textColor
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp
                ),
                color = textColor
            )
        }
    }
}

// ==================== SECTION 5: EMPTY STATE ====================

@Composable
private fun EmptyStateSection() {
    val infiniteTransition = rememberInfiniteTransition(label = "empty_state")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "empty_float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "empty_float2"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Abstract sports shapes
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background gradient circle
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                ChineseSilver.copy(alpha = 0.3f),
                                ChineseSilver.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Floating sport elements
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .offset(x = (-35).dp, y = (float1 - 20).dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = PeachGlow.copy(alpha = 0.4f)
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                PeachGlow.copy(alpha = 0.6f),
                                PeachGlow.copy(alpha = 0.3f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🏸", fontSize = 24.sp)
            }

            Box(
                modifier = Modifier
                    .size(45.dp)
                    .offset(x = 40.dp, y = (float2 + 10).dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = MintBreeze.copy(alpha = 0.4f)
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MintBreeze.copy(alpha = 0.6f),
                                MintBreeze.copy(alpha = 0.3f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("⚽", fontSize = 22.sp)
            }

            Box(
                modifier = Modifier
                    .size(35.dp)
                    .offset(x = 5.dp, y = (float1 + 35).dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        ambientColor = SkyMist.copy(alpha = 0.4f)
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                SkyMist.copy(alpha = 0.6f),
                                SkyMist.copy(alpha = 0.3f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🏀", fontSize = 16.sp)
            }

            // Sad face indicator
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        ambientColor = NeumorphDark.copy(alpha = 0.2f)
                    )
                    .background(
                        color = NeumorphLight,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Dreamland.copy(alpha = 0.3f),
                                ChineseSilver.copy(alpha = 0.2f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🔍", fontSize = 28.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No matching rooms found",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = Lead
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Try adjusting your category or mode filter\nto discover more rooms",
            style = MaterialTheme.typography.bodyMedium,
            color = WarmHaze,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Create Room suggestion
        Surface(
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Crunch.copy(alpha = 0.3f)
                )
                .clickable { /* Navigate to create room */ },
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Crunch,
                                SunsetOrange
                            )
                        )
                    )
                    .padding(horizontal = 28.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Lead
                    )
                    Text(
                        text = "Create Your Own Room",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Lead
                    )
                }
            }
        }
    }
}

// ==================== FLOATING CREATE ROOM FAB ====================

@Composable
private fun CreateRoomFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fab_glow")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fab_glow_alpha"
    )

    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fab_float"
    )

    Box(
        modifier = modifier.offset(y = floatY.dp)
    ) {
        // Outer glow
        Box(
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.Center)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Crunch.copy(alpha = glowAlpha),
                            Crunch.copy(alpha = glowAlpha * 0.5f),
                            Crunch.copy(alpha = 0f)
                        )
                    ),
                    shape = CircleShape
                )
                .blur(16.dp)
        )

        // FAB Button
        Surface(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.Center)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = Crunch.copy(alpha = 0.5f),
                    spotColor = Crunch.copy(alpha = 0.5f)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            shape = CircleShape,
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Crunch,
                                SunsetOrange
                            ),
                            start = Offset.Zero,
                            end = Offset(100f, 100f)
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                NeumorphLight.copy(alpha = 0.5f),
                                Crunch.copy(alpha = 0.3f)
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
                    tint = Lead
                )
            }
        }
    }
}
