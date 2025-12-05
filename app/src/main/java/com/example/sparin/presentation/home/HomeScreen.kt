package com.example.sparin.presentation.home

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
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import kotlin.math.cos
import kotlin.math.sin

// ==================== GEN-Z COLOR PALETTE ====================

private val GenZBlue = Color(0xFF8CCFFF)
private val GenZTeal = Color(0xFF35C8C3)
private val GenZCyan = Color(0xFF57D3FF)
private val GenZLavender = Color(0xFFB8B5FF)
private val GenZGradientStart = Color(0xFF35C8C3)
private val GenZGradientEnd = Color(0xFF57D3FF)

/**
 * Premium Home Screen for SparIN
 * Gen-Z aesthetic: pastel gradients, soft-neumorphism, frosted cards, floating shadows
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val scrollState = rememberScrollState()
    
    // Collect all states from ViewModel
    val userState by viewModel.userState.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()
    val recommendedRoomsState by viewModel.recommendedRoomsState.collectAsState()
    val lastOpponentsState by viewModel.lastOpponentsState.collectAsState()
    val upcomingMatchesState by viewModel.upcomingMatchesState.collectAsState()
    val activeCampaignState by viewModel.activeCampaignState.collectAsState()
    
    // Handle navigation events
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { event ->
            when (event) {
                is NavigationEvent.NavigateToPersonalization -> {
                    navController.navigate(Screen.Personalization.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                    viewModel.onNavigationHandled()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GenZGradientStart.copy(alpha = 0.03f),
                        GenZGradientEnd.copy(alpha = 0.05f),
                        CascadingWhite
                    )
                )
            )
    ) {
        // Subtle background blobs
        HomeBackgroundBlobs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // SECTION 1: Greeting Header with dynamic user data
            GreetingHeader(userState = userState)

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 2: Recommended Rooms
            RecommendedRoomsSection(roomsState = recommendedRoomsState)

            Spacer(modifier = Modifier.height(28.dp))

            // SECTION 3: Last Opponents
            LastOpponentsSection(opponentsState = lastOpponentsState)

            Spacer(modifier = Modifier.height(28.dp))

            // SECTION 4: Upcoming Matches
            UpcomingMatchesSection(matchesState = upcomingMatchesState)

            Spacer(modifier = Modifier.height(28.dp))

            // SECTION 5: Campaign Banner
            CampaignBanner(campaignState = activeCampaignState)

            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for nav bar
        }
    }
}

// ==================== BACKGROUND ====================

@Composable
private fun HomeBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "home_blobs")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(70.dp)
    ) {
        // Teal blob - top right
        drawCircle(
            color = GenZTeal.copy(alpha = 0.15f),
            radius = 220f,
            center = Offset(
                x = size.width * 0.85f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 30f,
                y = size.height * 0.1f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 20f
            )
        )

        // Blue blob - left side
        drawCircle(
            color = GenZBlue.copy(alpha = 0.12f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.15f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 25f,
                y = size.height * 0.3f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 30f
            )
        )

        // Lavender blob - center
        drawCircle(
            color = GenZLavender.copy(alpha = 0.1f),
            radius = 160f,
            center = Offset(
                x = size.width * 0.5f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f,
                y = size.height * 0.5f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 25f
            )
        )

        // Cyan blob - bottom right
        drawCircle(
            color = GenZCyan.copy(alpha = 0.1f),
            radius = 140f,
            center = Offset(
                x = size.width * 0.8f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 20f,
                y = size.height * 0.75f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )

        // Peach blob - bottom left
        drawCircle(
            color = PeachGlow.copy(alpha = 0.12f),
            radius = 150f,
            center = Offset(
                x = size.width * 0.2f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 25f,
                y = size.height * 0.8f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 20f
            )
        )
    }
}

// ==================== SECTION 1: GREETING HEADER ====================

@Composable
private fun GreetingHeader(userState: UserState) {
    val infiniteTransition = rememberInfiniteTransition(label = "header_blob")
    
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    
    // Get display name based on user state
    val displayName = when (userState) {
        is UserState.Success -> userState.user.name.split(" ").firstOrNull() ?: "User"
        is UserState.Loading -> "Loading..."
        is UserState.Error -> "User"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
    ) {
        // Subtle gradient swirl background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            ChineseSilver.copy(alpha = 0f),
                            Dreamland.copy(alpha = 0f),
                            ChineseSilver.copy(alpha = 0f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hi, $displayName 👋",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = Lead
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ready for today's match?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = WarmHaze
                )
            }

            // Profile Avatar with floating blob accent
            Box {
                // Small floating blob accent
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .offset(x = (-8).dp, y = (-5 + floatAnim).dp)
                        .background(
                            color = Crunch.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                        .blur(4.dp)
                )

                // Profile Avatar
                Surface(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            ambientColor = NeumorphDark.copy(alpha = 0.2f)
                        ),
                    shape = CircleShape,
                    color = ChineseSilver
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        // Show loading or user icon
                        if (userState is UserState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Crunch,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(32.dp),
                                tint = WarmHaze
                            )
                        }
                    }
                }

                // Online indicator (only show when user is loaded)
                if (userState is UserState.Success) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .background(
                                color = MintBreeze,
                                shape = CircleShape
                            )
                            .border(2.dp, NeumorphLight, CircleShape)
                    )
                }
            }
        }
    }
}

// ==================== SECTION 2: RECOMMENDED ROOMS ====================

@Composable
private fun RecommendedRoomsSection(roomsState: RoomsState) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recommended Rooms",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Lead
            )
            if (roomsState is RoomsState.Success && roomsState.rooms.isNotEmpty()) {
                TextButton(onClick = { }) {
                    Text(
                        text = "See All",
                        color = Crunch,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on state
        when (roomsState) {
            is RoomsState.Loading -> {
                // Loading state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Crunch,
                        strokeWidth = 3.dp
                    )
                }
            }
            is RoomsState.Success -> {
                if (roomsState.rooms.isEmpty()) {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "🏸",
                                fontSize = 48.sp
                            )
                            Text(
                                text = "No rooms available yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze
                            )
                        }
                    }
                } else {
                    // Success state with data
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        roomsState.rooms.forEach { room ->
                            RecommendedRoomCard(room = room)
                        }
                    }
                }
            }
            is RoomsState.Error -> {
                // Error state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 48.sp
                        )
                        Text(
                            text = roomsState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmHaze,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendedRoomCard(room: com.example.sparin.data.model.Room) {
    // Get sport emoji from category
    val emoji = getSportEmoji(room.category)
    val accentColor = getSportColor(room.category)
    
    // Format time
    val time = formatTime(room.dateTime)
    
    Surface(
        modifier = Modifier
            .width(200.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.15f),
                spotColor = NeumorphDark.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = NeumorphLight.copy(alpha = 0.95f)
    ) {
        Box {
            // Frosted gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.3f),
                                accentColor.copy(alpha = 0.05f)
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Sport Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = accentColor.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Room Title
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead,
                    maxLines = 2,
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
                        tint = WarmHaze
                    )
                    Text(
                        text = room.locationName.ifEmpty { "Location TBD" },
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmHaze,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mode Chip
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ChineseSilver.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = room.mode,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Lead,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Time & Players
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Time
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Crunch
                        )
                        Text(
                            text = time,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Lead,
                            fontSize = 11.sp
                        )
                    }
                    
                    // Players count
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.People,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = accentColor
                        )
                        Text(
                            text = "${room.currentPlayers}/${room.maxPlayers}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = accentColor,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

// Helper functions
private fun getSportEmoji(category: String): String {
    return when (category.lowercase()) {
        "badminton" -> "🏸"
        "futsal", "sepak bola", "mini soccer" -> "⚽"
        "basket", "basketball" -> "🏀"
        "voli", "volleyball" -> "🏐"
        "tennis", "tenis" -> "🎾"
        "tenis meja", "table tennis" -> "🏓"
        "golf" -> "⛳"
        "gym" -> "💪"
        "boxing", "muay thai" -> "🥊"
        "taekwondo" -> "🥋"
        "jogging", "lari", "running" -> "🏃"
        "sepeda", "cycling" -> "🚴"
        "renang", "swimming" -> "🏊"
        "hiking" -> "🥾"
        "billiard" -> "🎱"
        "catur", "chess" -> "♟️"
        "bowling" -> "🎳"
        else -> "🏃"
    }
}

private fun getSportColor(category: String): Color {
    return when (category.lowercase()) {
        "badminton", "tennis", "tenis" -> PeachGlow
        "futsal", "sepak bola", "mini soccer" -> MintBreeze
        "basket", "basketball" -> SkyMist
        "voli", "volleyball" -> RoseDust
        else -> SoftLavender
    }
}

private fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = timestamp - now
    
    // If in the past, show "Past"
    if (diff < 0) return "Past"
    
    // If today
    val calendar = java.util.Calendar.getInstance()
    calendar.timeInMillis = timestamp
    val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
    val minute = calendar.get(java.util.Calendar.MINUTE)
    
    return String.format("%02d:%02d", hour, minute)
}

// ==================== SECTION 3: LAST OPPONENTS ====================

@Composable
private fun LastOpponentsSection(opponentsState: OpponentsState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Last Opponents",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Lead
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (opponentsState) {
            is OpponentsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Crunch,
                        strokeWidth = 3.dp
                    )
                }
            }
            is OpponentsState.Success -> {
                if (opponentsState.opponents.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No match history yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmHaze
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        opponentsState.opponents.forEach { opponent ->
                            OpponentCard(opponent)
                        }
                    }
                }
            }
            is OpponentsState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opponentsState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun OpponentCard(opponent: com.example.sparin.data.model.User) {
    Surface(
        modifier = Modifier
            .size(80.dp)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor = NeumorphDark.copy(alpha = 0.1f),
                spotColor = NeumorphDark.copy(alpha = 0.1f)
            ),
        shape = CircleShape,
        color = NeumorphLight
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Avatar Circle with Initial
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(PeachGlow.copy(alpha = 0.3f), MintBreeze.copy(alpha = 0.3f))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = opponent.name.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead
                )
            }
        }
    }
}

// ==================== SECTION 4: UPCOMING MATCHES ====================

@Composable
private fun UpcomingMatchesSection(matchesState: MatchesState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Upcoming Matches",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Lead
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (matchesState) {
            is MatchesState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Crunch,
                        strokeWidth = 3.dp
                    )
                }
            }
            is MatchesState.Success -> {
                if (matchesState.matches.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "⏰",
                                fontSize = 48.sp
                            )
                            Text(
                                text = "No upcoming matches",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze
                            )
                        }
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        matchesState.matches.take(3).forEach { match ->
                            UpcomingMatchCard(match)
                        }
                    }
                }
            }
            is MatchesState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = matchesState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmHaze,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun UpcomingMatchCard(match: com.example.sparin.data.model.Match) {
    val emoji = getSportEmoji(match.category)
    val accentColor = getSportColor(match.category)
    val time = formatTime(match.dateTime)
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.1f),
                spotColor = NeumorphDark.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight.copy(alpha = 0.95f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sport Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Match Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = match.category,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Crunch
                    )
                    Text(
                     text = time,
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmHaze
                    )
                }
            }

            // Mode Chip
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = accentColor.copy(alpha = 0.2f)
            ) {
                Text(
                    text = match.mode,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = accentColor
                )
            }
        }
    }
}

// ==================== SECTION 5: CAMPAIGN BANNER ====================

@Composable
private fun CampaignBanner(campaignState: CampaignState) {
    // For now, use hardcoded banner - will implement dynamic version later
    val infiniteTransition = rememberInfiniteTransition(label = "banner_anim")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(180.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Crunch.copy(alpha = 0.2f),
                spotColor = Crunch.copy(alpha = 0.2f)
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
                            GenZGradientStart,
                            GenZGradientEnd,
                            GenZBlue
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            // Floating 3D elements
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 15.dp, y = (-15 + float1).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
                    .blur(12.dp)
            )

            Box(
                modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-40).dp, y = (30 + float2).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.12f),
                        shape = CircleShape
                    )
                    .blur(10.dp)
            )

            // Sport icons floating
            Text(
                text = "🏆",
                fontSize = 36.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-25).dp, y = (20 + float1).dp)
            )

            Text(
                text = "⚽",
                fontSize = 32.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = (-20).dp, y = float2.dp)
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Upcoming Events",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Tournaments &\nCampaigns",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 26.sp
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = Color.Black.copy(alpha = 0.15f)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "See More",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GenZTeal
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = GenZTeal
                        )
                    }
                }
            }
        }
    }
}
