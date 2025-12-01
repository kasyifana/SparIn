package com.example.sparin.presentation.community.feed

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

// ==================== GEN-Z COLOR PALETTE ====================
private val GenZBlue = Color(0xFF8CCFFF)
private val GenZTeal = Color(0xFF35C8C3)
private val GenZCyan = Color(0xFF57D3FF)
private val GenZLavender = Color(0xFFB8B5FF)

// ==================== DATA CLASS ====================
data class UpcomingEvent(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val attendees: Int,
    val maxAttendees: Int,
    val emoji: String,
    val description: String,
    val isJoined: Boolean = false
)

// ==================== SAMPLE DATA ====================
private val allUpcomingEvents = listOf(
    UpcomingEvent(
        id = "1",
        title = "Weekend Tournament",
        date = "Dec 7, 2025",
        time = "09:00 - 17:00",
        location = "GBK Arena, Jakarta",
        attendees = 48,
        maxAttendees = 64,
        emoji = "🏆",
        description = "Turnamen badminton antar komunitas. Hadiah menarik menanti!",
        isJoined = true
    ),
    UpcomingEvent(
        id = "2",
        title = "Beginner Workshop",
        date = "Dec 14, 2025",
        time = "10:00 - 12:00",
        location = "Sport Center BSD",
        attendees = 25,
        maxAttendees = 30,
        emoji = "📚",
        description = "Workshop teknik dasar untuk pemula. Gratis untuk member!"
    ),
    UpcomingEvent(
        id = "3",
        title = "Fun Match Night",
        date = "Dec 20, 2025",
        time = "19:00 - 22:00",
        location = "Mall Arena PIK",
        attendees = 32,
        maxAttendees = 40,
        emoji = "🎉",
        description = "Main bareng santai sambil networking dengan member lain."
    ),
    UpcomingEvent(
        id = "4",
        title = "Year End Championship",
        date = "Dec 28, 2025",
        time = "08:00 - 18:00",
        location = "Senayan Sports Hall",
        attendees = 120,
        maxAttendees = 128,
        emoji = "🥇",
        description = "Championship akhir tahun dengan kategori single dan double."
    ),
    UpcomingEvent(
        id = "5",
        title = "New Year Friendly Match",
        date = "Jan 2, 2026",
        time = "09:00 - 14:00",
        location = "GBK Arena, Jakarta",
        attendees = 56,
        maxAttendees = 80,
        emoji = "🎊",
        description = "Sambut tahun baru dengan friendly match antar komunitas!"
    ),
    UpcomingEvent(
        id = "6",
        title = "Pro Tips Session",
        date = "Jan 10, 2026",
        time = "15:00 - 17:00",
        location = "Online via Zoom",
        attendees = 89,
        maxAttendees = 100,
        emoji = "💡",
        description = "Sesi sharing tips dan trik dari pemain profesional."
    )
)

/**
 * All Upcoming Events Screen
 * Shows all upcoming events in the community
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllUpcomingEventsScreen(
    navController: NavHostController,
    communityId: String,
    communityName: String
) {
    var events by remember { mutableStateOf(allUpcomingEvents) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CascadingWhite,
                        GenZBlue.copy(alpha = 0.06f),
                        SoftLavender.copy(alpha = 0.1f)
                    )
                )
            )
    ) {
        // Background blobs
        EventsBackgroundBlobs()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header
            item {
                EventsHeader(
                    communityName = communityName,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Stats Card
            item {
                EventsStatsCard(
                    totalEvents = events.size,
                    joinedEvents = events.count { it.isJoined }
                )
            }

            // Section Title
            item {
                Text(
                    text = "All Events",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }

            // Events List
            items(events, key = { it.id }) { event ->
                EventListCard(
                    event = event,
                    onJoinClick = {
                        events = events.map { 
                            if (it.id == event.id) it.copy(
                                isJoined = !it.isJoined,
                                attendees = if (it.isJoined) it.attendees - 1 else it.attendees + 1
                            ) else it 
                        }
                    },
                    onClick = { /* Navigate to event detail */ }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// ==================== BACKGROUND ====================

@Composable
private fun EventsBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "events_blobs")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(70.dp)
    ) {
        drawCircle(
            color = GenZTeal.copy(alpha = 0.12f),
            radius = 150f,
            center = Offset(
                x = size.width * 0.85f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 25f,
                y = size.height * 0.12f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 20f
            )
        )

        drawCircle(
            color = GenZBlue.copy(alpha = 0.15f),
            radius = 130f,
            center = Offset(
                x = size.width * 0.15f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 20f,
                y = size.height * 0.4f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 25f
            )
        )

        drawCircle(
            color = GenZLavender.copy(alpha = 0.1f),
            radius = 120f,
            center = Offset(
                x = size.width * 0.6f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f,
                y = size.height * 0.7f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 20f
            )
        )
    }
}

// ==================== HEADER ====================

@Composable
private fun EventsHeader(
    communityName: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
    ) {
        // Gradient header background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GenZTeal.copy(alpha = 0.08f),
                            GenZBlue.copy(alpha = 0.06f),
                            GenZCyan.copy(alpha = 0.08f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = NeumorphDark.copy(alpha = 0.1f)
                    )
                    .background(NeumorphLight, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Lead
                )
            }

            // Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Upcoming Events",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead
                )
                Text(
                    text = communityName,
                    style = MaterialTheme.typography.labelMedium,
                    color = WarmHaze
                )
            }

            // Placeholder for alignment
            Spacer(modifier = Modifier.size(40.dp))
        }
    }
}

// ==================== STATS CARD ====================

@Composable
private fun EventsStatsCard(
    totalEvents: Int,
    joinedEvents: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = GenZTeal.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(GenZTeal, GenZCyan)
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Total Events
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Event,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = totalEvents.toString(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "Total Events",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(80.dp)
                        .background(Color.White.copy(alpha = 0.3f))
                )

                // Joined Events
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = joinedEvents.toString(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "Joined",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

// ==================== EVENT LIST CARD ====================

@Composable
private fun EventListCard(
    event: UpcomingEvent,
    onJoinClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.08f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight.copy(alpha = 0.98f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top row - Emoji, Title, Joined badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Emoji container
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = GenZTeal.copy(alpha = 0.12f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = event.emoji, fontSize = 26.sp)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title and date
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Lead,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = GenZTeal
                        )
                        Text(
                            text = event.date,
                            style = MaterialTheme.typography.labelMedium,
                            color = WarmHaze
                        )
                        Text(text = "•", color = WarmHaze, fontSize = 10.sp)
                        Text(
                            text = event.time,
                            style = MaterialTheme.typography.labelMedium,
                            color = WarmHaze
                        )
                    }
                }

                // Joined badge
                if (event.isJoined) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GenZTeal.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = GenZTeal
                            )
                            Text(
                                text = "Joined",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = GenZTeal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodySmall,
                color = WarmHaze,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Location row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = GenZBlue
                )
                Text(
                    text = event.location,
                    style = MaterialTheme.typography.labelMedium,
                    color = Lead,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom row - Attendees progress and Join button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Attendees progress
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.People,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = GenZLavender
                        )
                        Text(
                            text = "${event.attendees}/${event.maxAttendees} attendees",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Lead
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    // Progress bar
                    LinearProgressIndicator(
                        progress = { event.attendees.toFloat() / event.maxAttendees.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(6.dp),
                        color = GenZTeal,
                        trackColor = ChineseSilver.copy(alpha = 0.2f),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }

                // Join/Leave button
                Button(
                    onClick = onJoinClick,
                    modifier = Modifier.height(38.dp),
                    shape = RoundedCornerShape(19.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (event.isJoined) ChineseSilver.copy(alpha = 0.2f) else GenZTeal
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = if (event.isJoined) Icons.Rounded.Close else Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (event.isJoined) WarmHaze else Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (event.isJoined) "Leave" else "Join",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (event.isJoined) WarmHaze else Color.White
                    )
                }
            }
        }
    }
}
