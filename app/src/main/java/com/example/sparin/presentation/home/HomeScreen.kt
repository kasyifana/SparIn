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
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Premium Home Screen for SparIN
 * Gen-Z aesthetic: pastel gradients, soft-neumorphism, frosted cards, floating shadows
 */
@Composable
fun HomeScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CascadingWhite, SoftLavender.copy(alpha = 0.2f))
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
            // SECTION 1: Greeting Header
            GreetingHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 2: Recommended Rooms
            RecommendedRoomsSection()

            Spacer(modifier = Modifier.height(28.dp))

            // SECTION 3: Last Opponents
            LastOpponentsSection()

            Spacer(modifier = Modifier.height(28.dp))

            // SECTION 4: Upcoming Matches
            UpcomingMatchesSection()

            Spacer(modifier = Modifier.height(28.dp))

            // SECTION 5: Campaign Banner
            CampaignBanner()

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
            .blur(80.dp)
    ) {
        drawCircle(
            color = ChineseSilver.copy(alpha = 0.25f),
            radius = 200f,
            center = Offset(
                x = size.width * 0.1f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 30f,
                y = size.height * 0.05f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 20f
            )
        )

        drawCircle(
            color = PeachGlow.copy(alpha = 0.15f),
            radius = 150f,
            center = Offset(
                x = size.width * 0.9f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 25f,
                y = size.height * 0.15f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 30f
            )
        )

        drawCircle(
            color = MintBreeze.copy(alpha = 0.15f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.2f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f,
                y = size.height * 0.45f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 25f
            )
        )

        drawCircle(
            color = SkyMist.copy(alpha = 0.12f),
            radius = 140f,
            center = Offset(
                x = size.width * 0.85f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 20f,
                y = size.height * 0.7f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )
    }
}

// ==================== SECTION 1: GREETING HEADER ====================

@Composable
private fun GreetingHeader() {
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
                            ChineseSilver.copy(alpha = 0.2f),
                            Dreamland.copy(alpha = 0.1f),
                            ChineseSilver.copy(alpha = 0.15f)
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
                    text = "Hi, Avit 👋",
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
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(32.dp),
                            tint = WarmHaze
                        )
                    }
                }

                // Online indicator
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

// ==================== SECTION 2: RECOMMENDED ROOMS ====================

@Composable
private fun RecommendedRoomsSection() {
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
            TextButton(onClick = { }) {
                Text(
                    text = "See All",
                    color = Crunch,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal scrollable cards
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RecommendedRoomCard(
                sport = "Badminton",
                emoji = "🏸",
                roomTitle = "Morning Smash Session",
                distance = "1.2 km away",
                skillLevel = "Intermediate",
                time = "07:00 AM",
                accentColor = PeachGlow
            )
            RecommendedRoomCard(
                sport = "Futsal",
                emoji = "⚽",
                roomTitle = "Weekend Warriors",
                distance = "2.5 km away",
                skillLevel = "Semi-pro",
                time = "04:00 PM",
                accentColor = MintBreeze
            )
            RecommendedRoomCard(
                sport = "Basketball",
                emoji = "🏀",
                roomTitle = "3v3 Streetball",
                distance = "0.8 km away",
                skillLevel = "Beginner",
                time = "06:30 PM",
                accentColor = SkyMist
            )
            RecommendedRoomCard(
                sport = "Tennis",
                emoji = "🎾",
                roomTitle = "Singles Match",
                distance = "3.1 km away",
                skillLevel = "Expert",
                time = "09:00 AM",
                accentColor = RoseDust
            )
        }
    }
}

@Composable
private fun RecommendedRoomCard(
    sport: String,
    emoji: String,
    roomTitle: String,
    distance: String,
    skillLevel: String,
    time: String,
    accentColor: Color
) {
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
                    text = roomTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Distance
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
                        text = distance,
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmHaze
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Skill Level Chip
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ChineseSilver.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = skillLevel,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Lead,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Time Pill with Crunch accent
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(16.dp)
                            .background(
                                color = Crunch,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
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
                        color = Lead
                    )
                }
            }
        }
    }
}

// ==================== SECTION 3: LAST OPPONENTS ====================

@Composable
private fun LastOpponentsSection() {
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

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OpponentAvatar(name = "Reza", sport = "🏸", glowColor = PeachGlow)
            OpponentAvatar(name = "Dina", sport = "⚽", glowColor = MintBreeze)
            OpponentAvatar(name = "Aldo", sport = "🏀", glowColor = SkyMist)
            OpponentAvatar(name = "Maya", sport = "🎾", glowColor = RoseDust)
            OpponentAvatar(name = "Budi", sport = "🏐", glowColor = ChineseSilver)
            OpponentAvatar(name = "Sari", sport = "🏓", glowColor = PeachGlow)
        }
    }
}

@Composable
private fun OpponentAvatar(
    name: String,
    sport: String,
    glowColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            // Glow ring
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.4f),
                                glowColor.copy(alpha = 0f)
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Avatar
            Surface(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = NeumorphDark.copy(alpha = 0.15f)
                    ),
                shape = CircleShape,
                color = NeumorphLight
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = WarmHaze
                    )
                }
            }

            // Sport badge
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .background(
                        color = NeumorphLight,
                        shape = CircleShape
                    )
                    .border(1.dp, glowColor.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = sport, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            color = Lead,
            fontWeight = FontWeight.Medium
        )
    }
}

// ==================== SECTION 4: UPCOMING MATCHES ====================

@Composable
private fun UpcomingMatchesSection() {
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
                text = "Upcoming Matches",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Lead
            )
            TextButton(onClick = { }) {
                Text(
                    text = "View All",
                    color = Crunch,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UpcomingMatchCard(
                sport = "🏸",
                sportName = "Badminton",
                opponent = "vs. Reza Pratama",
                time = "Today, 07:00 AM",
                location = "GOR Senayan",
                status = "Confirmed",
                statusColor = MintBreeze
            )
            UpcomingMatchCard(
                sport = "⚽",
                sportName = "Futsal",
                opponent = "vs. Team Alpha",
                time = "Tomorrow, 04:00 PM",
                location = "Futsal Arena BSD",
                status = "Pending",
                statusColor = PeachGlow
            )
            UpcomingMatchCard(
                sport = "🏀",
                sportName = "Basketball",
                opponent = "vs. Aldo & Friends",
                time = "Sat, 06:30 PM",
                location = "Lapangan Basket UI",
                status = "Confirmed",
                statusColor = MintBreeze
            )
        }
    }
}

@Composable
private fun UpcomingMatchCard(
    sport: String,
    sportName: String,
    opponent: String,
    time: String,
    location: String,
    status: String,
    statusColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.1f),
                spotColor = NeumorphDark.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight.copy(alpha = 0.98f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            ChineseSilver.copy(alpha = 0.3f),
                            statusColor.copy(alpha = 0.2f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sport Icon Container
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    statusColor.copy(alpha = 0.3f),
                                    statusColor.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = sport, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Match Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = opponent,
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
                            tint = WarmHaze
                        )
                        Text(
                            text = time,
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmHaze
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

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
                            text = location,
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmHaze
                        )
                    }
                }

                // Status Chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusColor.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        statusColor.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = status,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Lead
                    )
                }
            }
        }
    }
}

// ==================== SECTION 5: CAMPAIGN BANNER ====================

@Composable
private fun CampaignBanner() {
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
            .height(160.dp)
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
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            ChineseSilver.copy(alpha = 0.9f),
                            ChineseSilver.copy(alpha = 0.7f),
                            Crunch.copy(alpha = 0.6f)
                        )
                    )
                )
        ) {
            // Floating 3D elements
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-20).dp, y = (15 + float1).dp)
                    .background(
                        color = PeachGlow.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .blur(8.dp)
            )

            Box(
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-50).dp, y = (10 + float2).dp)
                    .background(
                        color = MintBreeze.copy(alpha = 0.6f),
                        shape = CircleShape
                    )
                    .blur(6.dp)
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-80).dp, y = (-20 + float1).dp)
                    .background(
                        color = Crunch.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .blur(5.dp)
            )

            // Sport icons floating
            Text(
                text = "🏆",
                fontSize = 32.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-30).dp, y = (25 + float2).dp)
            )

            Text(
                text = "⚽",
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = (-15).dp, y = float1.dp)
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
                    color = WarmHaze
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Tournaments &\nCampaigns",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 26.sp
                    ),
                    color = Lead
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    modifier = Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = Crunch.copy(alpha = 0.3f)
                        )
                        .clickable { },
                    shape = RoundedCornerShape(12.dp),
                    color = Crunch
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "See More",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Lead
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Lead
                        )
                    }
                }
            }
        }
    }
}
