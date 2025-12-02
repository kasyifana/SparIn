package com.example.sparin.presentation.community.feed

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    var selectedEvent by remember { mutableStateOf<UpcomingEvent?>(null) }
    var showEventDetail by remember { mutableStateOf(false) }
    
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
                    totalEvents = events.size
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
                    onClick = { 
                        selectedEvent = event
                        showEventDetail = true
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
    
    // Event Detail Dialog
    if (showEventDetail && selectedEvent != null) {
        EventDetailDialog(
            event = selectedEvent!!,
            onDismiss = { 
                showEventDetail = false
                selectedEvent = null
            }
        )
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
    totalEvents: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
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
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Event icon
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Event,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(20.dp))
                
                Column {
                    Text(
                        text = "$totalEvents Events",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "Tap any event for details",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.85f)
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Poster-style event card - informational only
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GenZTeal.copy(alpha = 0.95f),
                            GenZCyan.copy(alpha = 0.9f)
                        )
                    )
                )
        ) {
            // Decorative circles
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .blur(15.dp)
            )
            
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-10).dp, y = 10.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.08f),
                        shape = CircleShape
                    )
                    .blur(10.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side - Emoji
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = event.emoji, fontSize = 36.sp)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Right side - Event info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // Date and Time
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = "${event.date} • ${event.time}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    
                    // Location
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = event.location,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    // Description
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // Tap for details hint
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.TouchApp,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Text(
                                text = "Tap for details",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium
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

// ==================== EVENT DETAIL DIALOG ====================

@Composable
private fun EventDetailDialog(
    event: UpcomingEvent,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "detail_anim")
    
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 24.dp, vertical = 48.dp)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .clip(RoundedCornerShape(32.dp))
                    .clickable(enabled = false, onClick = {}),
                shape = RoundedCornerShape(32.dp),
                color = NeumorphLight
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    // Hero Header with gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        GenZTeal,
                                        GenZCyan
                                    )
                                )
                            )
                    ) {
                        // Decorative circles
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 30.dp, y = (-30).dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.1f),
                                    shape = CircleShape
                                )
                                .blur(20.dp)
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.BottomStart)
                                .offset(x = (-20).dp, y = 20.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.08f),
                                    shape = CircleShape
                                )
                                .blur(15.dp)
                        )
                        
                        // Close button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                        
                        // Event emoji and title
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Large emoji with glow effect
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                // Glow
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .background(
                                            color = Color.White.copy(alpha = 0.2f),
                                            shape = CircleShape
                                        )
                                        .blur(20.dp)
                                )
                                
                                Surface(
                                    modifier = Modifier.size(88.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    color = Color.White.copy(alpha = 0.25f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = event.emoji, fontSize = 48.sp)
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    
                    // Content section
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Quick info cards row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Date card
                            EventInfoCard(
                                icon = Icons.Rounded.CalendarToday,
                                title = "Date",
                                value = event.date,
                                color = GenZTeal,
                                modifier = Modifier.weight(1f)
                            )
                            
                            // Time card
                            EventInfoCard(
                                icon = Icons.Rounded.Schedule,
                                title = "Time",
                                value = event.time,
                                color = GenZBlue,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        // Location card (full width)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    ambientColor = GenZLavender.copy(alpha = 0.1f)
                                ),
                            shape = RoundedCornerShape(20.dp),
                            color = NeumorphLight
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    color = GenZLavender.copy(alpha = 0.15f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Rounded.LocationOn,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = GenZLavender
                                        )
                                    }
                                }
                                
                                Column {
                                    Text(
                                        text = "Location",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = WarmHaze
                                    )
                                    Text(
                                        text = event.location,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = Lead
                                    )
                                }
                                
                                Spacer(modifier = Modifier.weight(1f))
                                
                                // Map icon button
                                Surface(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable { /* Open maps */ },
                                    shape = CircleShape,
                                    color = GenZLavender.copy(alpha = 0.15f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Rounded.Map,
                                            contentDescription = "Open Maps",
                                            modifier = Modifier.size(20.dp),
                                            tint = GenZLavender
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Divider
                        HorizontalDivider(
                            color = ChineseSilver.copy(alpha = 0.2f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        
                        // About section
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "About Event",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Lead
                            )
                            
                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze,
                                lineHeight = 24.sp
                            )
                            
                            // Extended description for demo
                            Text(
                                text = "Join us for an amazing experience! This event is perfect for both beginners and experienced players. Don't miss out on the opportunity to connect with fellow sports enthusiasts and improve your skills.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze,
                                lineHeight = 24.sp
                            )
                        }
                        
                        // Spacer before close button
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Close button
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GenZTeal
                            )
                        ) {
                            Text(
                                text = "Close",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== EVENT INFO CARD ====================

@Composable
private fun EventInfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = color.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(14.dp),
                color = color.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = color
                    )
                }
            }
            
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = WarmHaze
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Lead,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
