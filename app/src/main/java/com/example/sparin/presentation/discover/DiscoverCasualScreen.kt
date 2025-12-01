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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Discover Casual Mode Screen
 * Fun, friendly, inviting design with pastel colors
 * Gen-Z aesthetic with glassmorphism and smooth animations
 */

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

// Casual room data
data class CasualRoomItem(
    val id: String,
    val title: String,
    val sport: String,
    val emoji: String,
    val location: String,
    val schedule: String,
    val currentPlayers: Int,
    val maxPlayers: Int,
    val tags: List<String>,
    val accentColor: Color
)

val sampleCasualRooms = listOf(
    CasualRoomItem(
        id = "1",
        title = "Badminton Fun Match",
        sport = "Badminton",
        emoji = "🏸",
        location = "GOR Soemantri, Jakarta",
        schedule = "Today · 19:00",
        currentPlayers = 3,
        maxPlayers = 6,
        tags = listOf("Chill", "Fun Only"),
        accentColor = MintBreeze
    ),
    CasualRoomItem(
        id = "2",
        title = "Futsal Friendly Game",
        sport = "Futsal",
        emoji = "⚽",
        location = "Lapangan BSD",
        schedule = "Tomorrow · 16:00",
        currentPlayers = 6,
        maxPlayers = 10,
        tags = listOf("Relaxed", "Beginner OK"),
        accentColor = SkyMist
    ),
    CasualRoomItem(
        id = "3",
        title = "Basketball Pickup",
        sport = "Basket",
        emoji = "🏀",
        location = "Senayan Park",
        schedule = "Today · 17:00",
        currentPlayers = 4,
        maxPlayers = 6,
        tags = listOf("Fun Only", "Chill"),
        accentColor = PeachGlow
    ),
    CasualRoomItem(
        id = "4",
        title = "Weekend Hiking Trip",
        sport = "Hiking",
        emoji = "🥾",
        location = "Gunung Pancar",
        schedule = "Sat · 06:00",
        currentPlayers = 8,
        maxPlayers = 15,
        tags = listOf("Relaxed", "Nature"),
        accentColor = MintBreeze
    ),
    CasualRoomItem(
        id = "5",
        title = "Morning Cycling",
        sport = "Cycling",
        emoji = "🚴",
        location = "BSD Loop",
        schedule = "Sun · 05:30",
        currentPlayers = 5,
        maxPlayers = 12,
        tags = listOf("Chill", "Beginner OK"),
        accentColor = SoftLavender
    )
)

@Composable
fun DiscoverCasualScreen(
    navController: NavHostController,
    viewModel: DiscoverViewModel = koinViewModel()
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val scrollState = rememberScrollState()
    val roomsState by viewModel.casualRoomsState.collectAsState()

    // Refresh on entry
    LaunchedEffect(Unit) {
        viewModel.loadCasualRooms()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CascadingWhite,
                        MintBreeze.copy(alpha = 0.08f),
                        SoftLavender.copy(alpha = 0.08f),
                        CascadingWhite
                    )
                )
            )
    ) {
        // Pastel background blobs
        CasualBackgroundBlobs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header
            CasualHeader(onBack = { navController.navigateUp() })

            Spacer(modifier = Modifier.height(20.dp))

            // Sport Category Tabs
            CasualSportTabs(
                selectedCategory = selectedCategory,
                onCategorySelected = { 
                    selectedCategory = it
                    viewModel.filterByCategory(it)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Match Cards
            when (val state = roomsState) {
                is RoomsState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MintBreeze)
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
                            Text("No casual rooms found. Create one!", color = WarmHaze)
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "${rooms.size} matches available",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 17.sp
                                ),
                                color = Lead
                            )

                            rooms.forEach { room ->
                                // Map Room to UI Item
                                val uiItem = CasualRoomItem(
                                    id = room.id,
                                    title = room.name,
                                    sport = room.category,
                                    emoji = getEmojiForCategory(room.category),
                                    location = room.locationName,
                                    schedule = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(room.dateTime)),
                                    currentPlayers = room.currentPlayers,
                                    maxPlayers = room.maxPlayers,
                                    tags = listOf("Casual", "Fun"), // Default tags for now
                                    accentColor = getColorForCategory(room.category)
                                )
                                CasualRoomCard(
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

        // Floating Create Room FAB
        CasualCreateRoomFAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp),
            onClick = { navController.navigate("create_room/Casual") }
        )
    }
}

// Helper functions moved to DiscoverUtils.kt

// ... (Rest of the file remains mostly the same, but CasualMatchCards function is removed/inlined above)

// ==================== MATCH CARDS (Updated to accept onClick) ====================

@Composable
private fun CasualRoomCard(room: CasualRoomItem, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "room_card_${room.id}")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            room.accentColor.copy(alpha = glowAlpha),
                            room.accentColor.copy(alpha = 0f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .blur(16.dp)
        )

        // Main card
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = room.accentColor.copy(alpha = 0.2f),
                    spotColor = room.accentColor.copy(alpha = 0.2f)
                ),
            shape = RoundedCornerShape(28.dp),
            color = Color.Transparent
        ) {
            // ... (Content same as before)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                room.accentColor.copy(alpha = 0.15f),
                                NeumorphLight.copy(alpha = 0.95f),
                                room.accentColor.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                room.accentColor.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sport emoji
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = CircleShape,
                                    ambientColor = room.accentColor.copy(alpha = 0.3f)
                                )
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            room.accentColor.copy(alpha = 0.3f),
                                            room.accentColor.copy(alpha = 0.1f)
                                        )
                                    ),
                                    shape = CircleShape
                                )
                            ,
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = room.emoji, fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
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

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
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

                    // Tags
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
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Lead.copy(alpha = 0.8f)
                                )
                            }
                        }
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
                                tint = WarmHaze
                            )
                            Text(
                                text = room.schedule,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = WarmHaze
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
                                    tint = room.accentColor
                                )
                                Text(
                                    text = "${room.currentPlayers}/${room.maxPlayers}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = room.accentColor
                                )
                            }

                            // Join button
                            Surface(
                                onClick = onClick, // Use the passed onClick
                                shape = RoundedCornerShape(16.dp),
                                color = room.accentColor.copy(alpha = 0.9f),
                                modifier = Modifier.shadow(
                                    elevation = 6.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    ambientColor = room.accentColor.copy(alpha = 0.3f)
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

// ==================== BACKGROUND BLOBS ====================

@Composable
private fun CasualBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "casual_blobs")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(35000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(100.dp)
    ) {
        // Mint blob
        drawCircle(
            color = MintBreeze.copy(alpha = 0.25f),
            radius = 220f,
            center = Offset(
                x = size.width * 0.15f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 50f,
                y = size.height * 0.1f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f
            )
        )

        // Lavender blob
        drawCircle(
            color = SoftLavender.copy(alpha = 0.22f),
            radius = 190f,
            center = Offset(
                x = size.width * 0.85f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 40f,
                y = size.height * 0.2f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 45f
            )
        )

        // Sky blob
        drawCircle(
            color = SkyMist.copy(alpha = 0.2f),
            radius = 170f,
            center = Offset(
                x = size.width * 0.5f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 40f,
                y = size.height * 0.5f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 40f
            )
        )

        // Peach blob
        drawCircle(
            color = PeachGlow.copy(alpha = 0.18f),
            radius = 160f,
            center = Offset(
                x = size.width * 0.3f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 35f,
                y = size.height * 0.75f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )
    }
}

// ==================== HEADER ====================

@Composable
private fun CasualHeader(onBack: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "header_float")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp) // Adjusted padding for back button
    ) {
        // Back Button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 12.dp)
                .zIndex(1f)
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = Lead
            )
        }
        // Floating decorative elements
        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-40).dp, y = (20 + float1).dp)
                .shadow(
                    elevation = 8.dp,
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
            Text("🎉", fontSize = 20.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 56.dp, bottom = 16.dp)
        ) {
            // Title
            Text(
                text = "Find Your Next\nFun Match 🎉",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 38.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = Lead
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Casual activities around you",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp
                ),
                color = WarmHaze
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Search Bar
            CasualSearchBar()
        }
    }
}

@Composable
private fun CasualSearchBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(27.dp),
                ambientColor = MintBreeze.copy(alpha = 0.15f),
                spotColor = MintBreeze.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(27.dp),
        color = NeumorphLight.copy(alpha = 0.95f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MintBreeze.copy(alpha = 0.05f),
                            SoftLavender.copy(alpha = 0.05f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MintBreeze.copy(alpha = 0.3f),
                            SoftLavender.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(27.dp)
                )
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                modifier = Modifier.size(22.dp),
                tint = MintBreeze
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Search fun activities...",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmHaze.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Sparkle icon
            Text("✨", fontSize = 18.sp)
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
        Text(
            text = "Sport Categories",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp
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
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MintBreeze.copy(alpha = 0.3f)
        } else {
            NeumorphLight.copy(alpha = 0.8f)
        },
        animationSpec = tween(300),
        label = "chip_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MintBreeze else Dreamland.copy(alpha = 0.3f),
        animationSpec = tween(300),
        label = "chip_border"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "chip_scale"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isSelected) 10.dp else 4.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = if (isSelected) MintBreeze.copy(alpha = 0.3f) else NeumorphDark.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
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
                color = if (isSelected) Lead else WarmHaze
            )

            if (isSelected) {
                Text("✨", fontSize = 12.sp)
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
        // Glow
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MintBreeze.copy(alpha = glowAlpha),
                            MintBreeze.copy(alpha = 0f)
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
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = MintBreeze.copy(alpha = 0.4f),
                    spotColor = MintBreeze.copy(alpha = 0.4f)
                ),
            containerColor = MintBreeze,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Create Room",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
