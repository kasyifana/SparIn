package com.example.sparin.presentation.personalization

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Modern Personalization Screen for SparIN
 * Matches onboarding & sign-in: soft-neumorphism, floating 3D shapes, Gen-Z aesthetic
 */

// Data class for sport items
data class SportItem(
    val name: String,
    val icon: ImageVector,
    val emoji: String
)

// All available sports
val sportsList = listOf(
    SportItem("Badminton", Icons.Rounded.SportsTennis, "🏸"),
    SportItem("Futsal", Icons.Rounded.SportsSoccer, "⚽"),
    SportItem("Basket", Icons.Rounded.SportsBasketball, "🏀"),
    SportItem("Voli", Icons.Rounded.SportsVolleyball, "🏐"),
    SportItem("Tenis Meja", Icons.Rounded.TableRestaurant, "🏓"),
    SportItem("Tenis", Icons.Rounded.SportsTennis, "🎾"),
    SportItem("Padel", Icons.Rounded.SportsTennis, "🎾"),
    SportItem("Golf", Icons.Rounded.GolfCourse, "⛳"),
    SportItem("Sepak Bola", Icons.Rounded.SportsSoccer, "⚽"),
    SportItem("Mini Soccer", Icons.Rounded.SportsSoccer, "⚽"),
    SportItem("Jogging", Icons.AutoMirrored.Rounded.DirectionsRun, "🏃"),
    SportItem("Lari", Icons.AutoMirrored.Rounded.DirectionsRun, "🏃‍♂️"),
    SportItem("Sepeda", Icons.Rounded.DirectionsBike, "🚴"),
    SportItem("Renang", Icons.Rounded.Pool, "🏊"),
    SportItem("Gym", Icons.Rounded.FitnessCenter, "💪"),
    SportItem("Boxing", Icons.Rounded.SportsMma, "🥊"),
    SportItem("Muaythai", Icons.Rounded.SportsMma, "🥋"),
    SportItem("Taekwondo", Icons.Rounded.SportsMartialArts, "🥋"),
    SportItem("Billiard", Icons.Rounded.Circle, "🎱"),
    SportItem("Catur", Icons.Rounded.Casino, "♟️"),
    SportItem("Hiking", Icons.Rounded.Terrain, "🥾"),
    SportItem("Bowling", Icons.Rounded.FiberManualRecord, "🎳")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizationScreen(
    onNavigateToHome: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableFloatStateOf(20f) }
    var selectedGender by remember { mutableStateOf("") }
    var selectedSports by remember { mutableStateOf(setOf<String>()) }
    var skillLevel by remember { mutableFloatStateOf(0f) }
    var location by remember { mutableStateOf("") }
    var playFrequency by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // 3D Hero Illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingHeroElements()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "Personalize Your\nSport Profile",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    lineHeight = 36.sp
                ),
                color = Lead,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Biar rekomendasi & matchmaking makin cocok sama kamu.",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmHaze,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ==================== SECTION 1: Basic Info ====================
            SectionCard(title = "Basic Info") {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Name Input
                    NeumorphicTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full Name",
                        leadingIcon = Icons.Rounded.Person
                    )

                    // Age Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Age",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Crunch.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${age.roundToInt()} years",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Lead
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = age,
                            onValueChange = { age = it },
                            valueRange = 13f..60f,
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = Crunch,
                                activeTrackColor = Crunch,
                                inactiveTrackColor = ChineseSilver.copy(alpha = 0.5f)
                            )
                        )
                    }

                    // Gender Selector
                    Column {
                        Text(
                            text = "Gender",
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmHaze
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GenderPill(
                                text = "Male",
                                emoji = "👨",
                                isSelected = selectedGender == "Male",
                                onClick = { selectedGender = "Male" },
                                modifier = Modifier.weight(1f)
                            )
                            GenderPill(
                                text = "Female",
                                emoji = "👩",
                                isSelected = selectedGender == "Female",
                                onClick = { selectedGender = "Female" },
                                modifier = Modifier.weight(1f)
                            )
                            GenderPill(
                                text = "Other",
                                emoji = "🧑",
                                isSelected = selectedGender == "Other",
                                onClick = { selectedGender = "Other" },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==================== SECTION 2: Sport Interests ====================
            SectionCard(title = "Sport Interests") {
                Column {
                    Text(
                        text = "Select sports you love (multiple allowed)",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmHaze.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sports Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        userScrollEnabled = false
                    ) {
                        items(sportsList) { sport ->
                            SportTile(
                                sport = sport,
                                isSelected = selectedSports.contains(sport.name),
                                onClick = {
                                    selectedSports = if (selectedSports.contains(sport.name)) {
                                        selectedSports - sport.name
                                    } else {
                                        selectedSports + sport.name
                                    }
                                }
                            )
                        }
                    }

                    if (selectedSports.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "${selectedSports.size} sports selected",
                            style = MaterialTheme.typography.labelMedium,
                            color = Crunch,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==================== SECTION 3: Skill Level ====================
            SectionCard(title = "Skill Level") {
                Column {
                    val skillLabels = listOf("Beginner", "Intermediate", "Semi-pro", "Expert")
                    val currentSkillLabel = skillLabels[(skillLevel * 3).roundToInt().coerceIn(0, 3)]

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Your Level",
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmHaze
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (currentSkillLabel) {
                                "Beginner" -> MintBreeze
                                "Intermediate" -> SkyMist
                                "Semi-pro" -> PeachGlow
                                else -> Crunch.copy(alpha = 0.3f)
                            }
                        ) {
                            Text(
                                text = currentSkillLabel,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Lead
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Custom skill slider
                    Column {
                        Slider(
                            value = skillLevel,
                            onValueChange = { skillLevel = it },
                            valueRange = 0f..1f,
                            steps = 2,
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = Crunch,
                                activeTrackColor = Crunch,
                                inactiveTrackColor = ChineseSilver.copy(alpha = 0.5f)
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            skillLabels.forEach { label ->
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = WarmHaze.copy(alpha = 0.6f),
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==================== SECTION 4: Location ====================
            SectionCard(title = "Location") {
                NeumorphicTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = "Search your city",
                    leadingIcon = Icons.Rounded.LocationOn
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==================== SECTION 5: Play Frequency ====================
            SectionCard(title = "Play Frequency") {
                Column {
                    Text(
                        text = "How often do you play?",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmHaze.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FrequencyChip(
                            text = "1–2x/week",
                            emoji = "🌱",
                            isSelected = playFrequency == "1-2",
                            onClick = { playFrequency = "1-2" },
                            modifier = Modifier.weight(1f)
                        )
                        FrequencyChip(
                            text = "3–5x/week",
                            emoji = "🔥",
                            isSelected = playFrequency == "3-5",
                            onClick = { playFrequency = "3-5" },
                            modifier = Modifier.weight(1f)
                        )
                        FrequencyChip(
                            text = "Daily",
                            emoji = "⚡",
                            isSelected = playFrequency == "daily",
                            onClick = { playFrequency = "daily" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ==================== Continue Button ====================
            Button(
                onClick = onNavigateToHome,
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
                        text = "Complete Setup",
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

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ==================== BACKGROUND COMPONENTS ====================

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
        drawCircle(
            color = ChineseSilver.copy(alpha = 0.35f),
            radius = 160f,
            center = Offset(
                x = size.width * 0.1f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 40f,
                y = size.height * 0.08f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 25f
            )
        )

        drawCircle(
            color = PeachGlow.copy(alpha = 0.25f),
            radius = 120f,
            center = Offset(
                x = size.width * 0.9f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 30f,
                y = size.height * 0.15f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 35f
            )
        )

        drawCircle(
            color = MintBreeze.copy(alpha = 0.25f),
            radius = 140f,
            center = Offset(
                x = size.width * 0.2f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 45f,
                y = size.height * 0.5f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 30f
            )
        )

        drawCircle(
            color = SkyMist.copy(alpha = 0.2f),
            radius = 100f,
            center = Offset(
                x = size.width * 0.85f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 25f,
                y = size.height * 0.7f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f
            )
        )

        drawCircle(
            color = RoseDust.copy(alpha = 0.2f),
            radius = 90f,
            center = Offset(
                x = size.width * 0.5f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 35f,
                y = size.height * 0.9f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 20f
            )
        )
    }
}

@Composable
private fun FloatingHeroElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_elements")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    val float3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float3"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Central element
        NeumorphicCircle(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center)
                .offset(y = float1.dp),
            color = ChineseSilver.copy(alpha = 0.7f)
        ) {
            Icon(
                imageVector = Icons.Rounded.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = Crunch
            )
        }

        // Floating elements
        NeumorphicCircle(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-40).dp, y = (10 + float2).dp),
            color = PeachGlow.copy(alpha = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Rounded.SportsBasketball,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = SunsetOrange
            )
        }

        NeumorphicCircle(
            modifier = Modifier
                .size(45.dp)
                .align(Alignment.BottomStart)
                .offset(x = 50.dp, y = (-10 + float3).dp),
            color = MintBreeze.copy(alpha = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Rounded.FitnessCenter,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = WarmHaze
            )
        }

        NeumorphicCircle(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart)
                .offset(x = 60.dp, y = (20 + float1).dp),
            color = SkyMist.copy(alpha = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Rounded.EmojiEvents,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Crunch
            )
        }

        // Decorative dots
        FloatingDot(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-20).dp, y = float2.dp),
            color = Crunch.copy(alpha = 0.6f),
            size = 10.dp
        )

        FloatingDot(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-60).dp, y = (-20 + float1).dp),
            color = ChineseSilver,
            size = 14.dp
        )
    }
}

// ==================== SECTION CARD ====================

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.12f),
                spotColor = NeumorphDark.copy(alpha = 0.12f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = NeumorphLight.copy(alpha = 0.95f)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Lead
            )

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

// ==================== INPUT COMPONENTS ====================

@Composable
private fun NeumorphicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.1f),
                spotColor = NeumorphDark.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = CascadingWhite
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeumorphLight,
                            ChineseSilver.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = label,
                        color = WarmHaze.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = Dreamland
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Lead,
                    unfocusedTextColor = Lead,
                    cursorColor = Crunch,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Crunch,
                    unfocusedLabelColor = WarmHaze.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
private fun GenderPill(
    text: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Crunch.copy(alpha = 0.2f) else CascadingWhite,
        animationSpec = tween(200),
        label = "gender_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else ChineseSilver.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "gender_border"
    )

    Surface(
        modifier = modifier
            .height(48.dp)
            .shadow(
                elevation = if (isSelected) 6.dp else 2.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = if (isSelected) Crunch.copy(alpha = 0.2f) else NeumorphDark.copy(alpha = 0.1f)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                ),
                color = if (isSelected) Lead else WarmHaze
            )
        }
    }
}

// ==================== SPORT TILE ====================

@Composable
private fun SportTile(
    sport: SportItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Crunch.copy(alpha = 0.15f) else CascadingWhite,
        animationSpec = tween(200),
        label = "sport_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else ChineseSilver.copy(alpha = 0.4f),
        animationSpec = tween(200),
        label = "sport_border"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 3.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "sport_elevation"
    )

    Surface(
        modifier = Modifier
            .aspectRatio(1f)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(16.dp),
                ambientColor = if (isSelected) Crunch.copy(alpha = 0.3f) else NeumorphDark.copy(alpha = 0.1f),
                spotColor = if (isSelected) Crunch.copy(alpha = 0.3f) else NeumorphDark.copy(alpha = 0.1f)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = sport.emoji,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = sport.name,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 10.sp
                ),
                color = if (isSelected) Lead else WarmHaze,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

// ==================== FREQUENCY CHIP ====================

@Composable
private fun FrequencyChip(
    text: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Crunch.copy(alpha = 0.2f) else CascadingWhite,
        animationSpec = tween(200),
        label = "freq_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else ChineseSilver.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "freq_border"
    )

    Surface(
        modifier = modifier
            .height(56.dp)
            .shadow(
                elevation = if (isSelected) 6.dp else 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = if (isSelected) Crunch.copy(alpha = 0.2f) else NeumorphDark.copy(alpha = 0.08f)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = 10.sp
                ),
                color = if (isSelected) Lead else WarmHaze,
                textAlign = TextAlign.Center
            )
        }
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
                elevation = 10.dp,
                shape = CircleShape,
                ambientColor = NeumorphDark.copy(alpha = 0.2f),
                spotColor = NeumorphDark.copy(alpha = 0.2f)
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
            .shadow(4.dp, CircleShape)
            .background(color, CircleShape)
    )
}
