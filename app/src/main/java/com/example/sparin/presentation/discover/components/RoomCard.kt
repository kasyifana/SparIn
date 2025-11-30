package com.example.sparin.presentation.discover.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*

/**
 * Room Card Component
 * Premium frosted-glass card for displaying room information
 */

data class RoomData(
    val id: String,
    val title: String,
    val sport: String,
    val emoji: String,
    val location: String,
    val skillLevel: RoomSkillLevel,
    val schedule: String,
    val currentPlayers: Int,
    val maxPlayers: Int,
    val price: String? = null,
    val accentColor: Color = PeachGlow
)

enum class RoomSkillLevel(val label: String, val color: Color) {
    BEGINNER("Beginner", MintBreeze),
    INTERMEDIATE("Intermediate", PeachGlow),
    ADVANCED("Advanced", RoseDust)
}

@Composable
fun RoomCard(
    room: RoomData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enableFloating: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "room_card_${room.id}")

    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (enableFloating) 3f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_float"
    )

    Surface(
        modifier = modifier
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
                .clickable(onClick = onClick)
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
                    SportIconBox(
                        emoji = room.emoji,
                        accentColor = room.accentColor
                    )

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
                            RoomChipTag(
                                text = room.skillLevel.label,
                                backgroundColor = room.skillLevel.color.copy(alpha = 0.4f),
                                textColor = Lead
                            )

                            // Schedule chip
                            RoomChipTag(
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
                        PlayerSlotIndicator(
                            currentPlayers = room.currentPlayers,
                            maxPlayers = room.maxPlayers
                        )

                        // Price tag (if exists)
                        room.price?.let { price ->
                            PriceTag(price = price)
                        }
                    }

                    // View Room CTA Button
                    ViewRoomButton(onClick = onClick)
                }
            }
        }
    }
}

@Composable
private fun SportIconBox(
    emoji: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(68.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = accentColor.copy(alpha = 0.4f),
                spotColor = accentColor.copy(alpha = 0.4f)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.6f),
                        accentColor.copy(alpha = 0.3f)
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
        Text(text = emoji, fontSize = 32.sp)
    }
}

@Composable
private fun RoomChipTag(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    icon: ImageVector? = null
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

@Composable
private fun PlayerSlotIndicator(
    currentPlayers: Int,
    maxPlayers: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
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
            text = "$currentPlayers/$maxPlayers",
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
}

@Composable
private fun PriceTag(
    price: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
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

@Composable
private fun ViewRoomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Crunch.copy(alpha = 0.35f),
                spotColor = Crunch.copy(alpha = 0.35f)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
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

/**
 * Compact Room Card - Smaller version for horizontal lists
 */
@Composable
fun CompactRoomCard(
    room: RoomData,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(280.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.12f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sport emoji
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = room.accentColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = room.emoji, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = room.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Lead,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${room.currentPlayers}/${room.maxPlayers} players",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze
                )
                Text(
                    text = room.schedule,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Crunch
                )
            }
        }
    }
}
