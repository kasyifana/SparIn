package com.example.sparin.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * Modern Gen Z-friendly Onboarding Screen for SparIN
 * Features: Soft neumorphism, floating 3D shapes, pastel gradients
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onNavigateToSignIn: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CascadingWhite, SoftLavender.copy(alpha = 0.3f))
                )
            )
    ) {
        // Animated floating blobs background
        FloatingBlobsBackground()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Skip button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onNavigateToSignIn,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = WarmHaze
                    )
                ) {
                    Text(
                        text = "Skip",
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Main pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> OnboardingPage1()
                    1 -> OnboardingPage2()
                    2 -> OnboardingPage3()
                }
            }

            // Bottom section with indicators and CTA
            BottomSection(
                currentPage = pagerState.currentPage,
                totalPages = 3,
                onNextClick = {
                    if (pagerState.currentPage < 2) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onNavigateToSignIn()
                    }
                },
                isLastPage = pagerState.currentPage == 2
            )
        }
    }
}

@Composable
private fun FloatingBlobsBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "blob_animation")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(60.dp)
    ) {
        // Floating blob 1 - Soft lavender
        drawCircle(
            color = ChineseSilver.copy(alpha = 0.4f),
            radius = 200f,
            center = Offset(
                x = size.width * 0.2f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 50f,
                y = size.height * 0.15f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )

        // Floating blob 2 - Peach
        drawCircle(
            color = PeachGlow.copy(alpha = 0.3f),
            radius = 150f,
            center = Offset(
                x = size.width * 0.8f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 40f,
                y = size.height * 0.3f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 50f
            )
        )

        // Floating blob 3 - Mint
        drawCircle(
            color = MintBreeze.copy(alpha = 0.35f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.5f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 60f,
                y = size.height * 0.7f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 40f
            )
        )

        // Floating blob 4 - Sky
        drawCircle(
            color = SkyMist.copy(alpha = 0.3f),
            radius = 120f,
            center = Offset(
                x = size.width * 0.15f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 35f,
                y = size.height * 0.55f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 45f
            )
        )
    }
}

// ==================== PAGE 1: Find Your Perfect Sport Partner ====================

@Composable
private fun OnboardingPage1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // 3D Hero Illustration Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingSportElements()
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "Find Your Perfect\nSport Partner",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp
            ),
            color = Lead,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tagline
        Text(
            text = "Connect with athletes who match your skill level and passion for sports",
            style = MaterialTheme.typography.bodyLarge,
            color = WarmHaze,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Feature cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeatureChip(
                icon = Icons.Rounded.Psychology,
                text = "AI Matching",
                modifier = Modifier.weight(1f)
            )
            FeatureChip(
                icon = Icons.Rounded.Groups,
                text = "Community",
                modifier = Modifier.weight(1f)
            )
            FeatureChip(
                icon = Icons.Rounded.Speed,
                text = "Quick Find",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FloatingSportElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "sport_elements")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    val float3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float3"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Main central element - Sport ball
        NeumorphicCircle(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.Center)
                .offset(y = float1.dp),
            color = ChineseSilver.copy(alpha = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Rounded.SportsSoccer,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Crunch
            )
        }

        // Floating racket element
        NeumorphicCircle(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-20).dp, y = (40 + float2).dp),
            color = PeachGlow.copy(alpha = 0.5f)
        ) {
            Icon(
                imageVector = Icons.Rounded.SportsTennis,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = SunsetOrange
            )
        }

        // Floating timer element
        NeumorphicCircle(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.BottomStart)
                .offset(x = 30.dp, y = (-30 + float3).dp),
            color = MintBreeze.copy(alpha = 0.5f)
        ) {
            Icon(
                imageVector = Icons.Rounded.Timer,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = WarmHaze
            )
        }

        // Floating person element
        NeumorphicCircle(
            modifier = Modifier
                .size(65.dp)
                .align(Alignment.CenterStart)
                .offset(x = 10.dp, y = (-50 + float1).dp),
            color = SkyMist.copy(alpha = 0.5f)
        ) {
            Icon(
                imageVector = Icons.Rounded.DirectionsRun,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = Lead
            )
        }

        // Small decorative circles
        FloatingDot(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 60.dp, y = (20 + float2).dp),
            color = Crunch.copy(alpha = 0.6f),
            size = 16.dp
        )

        FloatingDot(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-50).dp, y = (-60 + float1).dp),
            color = ChineseSilver,
            size = 20.dp
        )

        FloatingDot(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-10).dp, y = (30 + float3).dp),
            color = MintBreeze,
            size = 12.dp
        )
    }
}

// ==================== PAGE 2: Discover Rooms & Join Activities ====================

@Composable
private fun OnboardingPage2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // 3D UI Mockups Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingRoomCards()
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "Discover Rooms &\nJoin Activities",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp
            ),
            color = Lead,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tagline
        Text(
            text = "Explore sports rooms, join activities, and meet your community",
            style = MaterialTheme.typography.bodyLarge,
            color = WarmHaze,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sport category pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            SportCategoryPill("🏀", "Basketball")
            SportCategoryPill("⚽", "Football")
            SportCategoryPill("🎾", "Tennis")
            SportCategoryPill("🏸", "Badminton")
        }
    }
}

@Composable
private fun FloatingRoomCards() {
    val infiniteTransition = rememberInfiniteTransition(label = "room_cards")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Back card
        RoomPreviewCard(
            modifier = Modifier
                .width(220.dp)
                .align(Alignment.CenterStart)
                .offset(x = 0.dp, y = (30 + float2).dp)
                .graphicsLayer {
                    rotationZ = -8f
                },
            title = "Morning Jog",
            sport = "Running",
            participants = 5,
            time = "6:00 AM",
            color = MintBreeze.copy(alpha = 0.7f)
        )

        // Front card
        RoomPreviewCard(
            modifier = Modifier
                .width(240.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 0.dp, y = float1.dp)
                .graphicsLayer {
                    rotationZ = 5f
                },
            title = "Weekend Basketball",
            sport = "Basketball",
            participants = 8,
            time = "3:00 PM",
            color = PeachGlow.copy(alpha = 0.7f)
        )

        // Calendar floating element
        NeumorphicCircle(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-10).dp, y = (10 + float1).dp),
            color = SkyMist.copy(alpha = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Rounded.CalendarMonth,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = Lead
            )
        }

        // Location pin
        NeumorphicCircle(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.BottomStart)
                .offset(x = 20.dp, y = (-20 + float2).dp),
            color = RoseDust.copy(alpha = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Crunch
            )
        }
    }
}

@Composable
private fun RoomPreviewCard(
    modifier: Modifier = Modifier,
    title: String,
    sport: String,
    participants: Int,
    time: String,
    color: Color
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.3f),
                spotColor = NeumorphDark.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = color
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NeumorphLight.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = sport,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmHaze,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Lead
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Participant avatars
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stacked avatars
                Box {
                    repeat(minOf(participants, 4)) { index ->
                        Surface(
                            modifier = Modifier
                                .size(28.dp)
                                .offset(x = (index * 18).dp),
                            shape = CircleShape,
                            color = listOf(
                                Crunch,
                                ChineseSilver,
                                MintBreeze,
                                SkyMist
                            )[index % 4],
                            border = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                NeumorphLight
                            )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Lead.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width((minOf(participants, 4) * 18 + 16).dp))

                Text(
                    text = "+$participants joined",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze
                )
            }
        }
    }
}

@Composable
private fun SportCategoryPill(emoji: String, name: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = emoji, fontSize = 14.sp)
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                color = WarmHaze,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ==================== PAGE 3: Track Your Sport Stats ====================

@Composable
private fun OnboardingPage3() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Stats visualization area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingStatsCards()
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "Track Your\nSport Stats",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp
            ),
            color = Lead,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tagline
        Text(
            text = "Monitor your progress, celebrate wins, and level up your game",
            style = MaterialTheme.typography.bodyLarge,
            color = WarmHaze,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Gamification badges
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            GamificationBadge(Icons.Rounded.EmojiEvents, "Champion", Crunch)
            GamificationBadge(Icons.Rounded.LocalFireDepartment, "Streak", SunsetOrange)
            GamificationBadge(Icons.Rounded.Star, "MVP", ChineseSilver)
        }
    }
}

@Composable
private fun FloatingStatsCards() {
    val infiniteTransition = rememberInfiniteTransition(label = "stats_cards")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    val progressAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Main stat card with circular progress
        Surface(
            modifier = Modifier
                .width(200.dp)
                .align(Alignment.Center)
                .offset(y = float1.dp)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = NeumorphDark.copy(alpha = 0.2f)
                ),
            shape = RoundedCornerShape(28.dp),
            color = NeumorphLight
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Win Rate",
                    style = MaterialTheme.typography.labelMedium,
                    color = WarmHaze
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Circular progress
                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressRing(
                        progress = 0.73f * (0.5f + progressAnimation * 0.5f),
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "73%",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Lead
                        )
                        Text(
                            text = "Great!",
                            style = MaterialTheme.typography.labelSmall,
                            color = Crunch
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatItem("22", "Wins")
                    StatItem("8", "Losses")
                }
            }
        }

        // Small match history card
        Surface(
            modifier = Modifier
                .width(140.dp)
                .align(Alignment.TopStart)
                .offset(x = 10.dp, y = (20 + float2).dp)
                .graphicsLayer { rotationZ = -10f }
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = NeumorphDark.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(20.dp),
            color = PeachGlow.copy(alpha = 0.8f)
        ) {
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Text(
                    text = "Matches",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "30",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead
                )
                Text(
                    text = "This month",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze.copy(alpha = 0.7f)
                )
            }
        }

        // Streak card
        Surface(
            modifier = Modifier
                .width(120.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-10).dp, y = (-40 + float1).dp)
                .graphicsLayer { rotationZ = 8f }
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = NeumorphDark.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(20.dp),
            color = MintBreeze.copy(alpha = 0.8f)
        ) {
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocalFireDepartment,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = SunsetOrange
                    )
                    Text(
                        text = "Streak",
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmHaze
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "7 days",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead
                )
            }
        }

        // Chart icon floating
        NeumorphicCircle(
            modifier = Modifier
                .size(55.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-5).dp, y = (-60 + float2).dp),
            color = SkyMist.copy(alpha = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Rounded.BarChart,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = Lead
            )
        }
    }
}

@Composable
private fun CircularProgressRing(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 12.dp.toPx()
        val radius = (size.minDimension - strokeWidth) / 2

        // Background ring
        drawCircle(
            color = ChineseSilver.copy(alpha = 0.3f),
            radius = radius,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )

        // Progress ring with gradient
        drawArc(
            brush = Brush.sweepGradient(
                colors = listOf(Crunch, SunsetOrange, Crunch)
            ),
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            ),
            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
            size = androidx.compose.ui.geometry.Size(
                size.width - strokeWidth,
                size.height - strokeWidth
            )
        )
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Lead
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = WarmHaze
        )
    }
}

@Composable
private fun GamificationBadge(
    icon: ImageVector,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = color.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(2.dp, color)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = color
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = WarmHaze,
            fontWeight = FontWeight.Medium
        )
    }
}

// ==================== SHARED COMPONENTS ====================

@Composable
private fun NeumorphicCircle(
    modifier: Modifier = Modifier,
    color: Color,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = CircleShape,
                ambientColor = NeumorphDark.copy(alpha = 0.25f),
                spotColor = NeumorphDark.copy(alpha = 0.25f)
            )
            .background(
                color = color,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
private fun FloatingDot(
    modifier: Modifier = Modifier,
    color: Color,
    size: Dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(8.dp, CircleShape)
            .background(color, CircleShape)
    )
}

@Composable
private fun FeatureChip(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = NeumorphLight
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Crunch
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = WarmHaze,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun BottomSection(
    currentPage: Int,
    totalPages: Int,
    onNextClick: () -> Unit,
    isLastPage: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Page indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(totalPages) { index ->
                PageIndicator(isSelected = index == currentPage)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // CTA Button
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Crunch.copy(alpha = 0.4f),
                    spotColor = Crunch.copy(alpha = 0.4f)
                ),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Crunch
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isLastPage) "Get Started" else "Continue",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    tint = Lead
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PageIndicator(isSelected: Boolean) {
    val width by animateDpAsState(
        targetValue = if (isSelected) 32.dp else 8.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "indicator_width"
    )

    Box(
        modifier = Modifier
            .height(8.dp)
            .width(width)
            .clip(RoundedCornerShape(4.dp))
            .background(
                if (isSelected) Crunch else Dreamland.copy(alpha = 0.5f)
            )
    )
}
