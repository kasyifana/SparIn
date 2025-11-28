package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.presentation.profile.UserProfile
import com.example.sparin.ui.theme.*

/**
 * ProfileHeader Component
 * Displays user profile image, name, and bio with premium styling
 */
@Composable
fun ProfileHeader(
    userProfile: UserProfile,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "profile_header_anim")

    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.12f),
                spotColor = NeumorphDark.copy(alpha = 0.12f)
            ),
        shape = RoundedCornerShape(28.dp),
        color = ChineseSilver.copy(alpha = 0.4f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            ChineseSilver.copy(alpha = 0.5f),
                            SoftLavender.copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            // Floating accent blobs
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-20).dp, y = (20 + floatAnim).dp)
                    .background(
                        color = Crunch.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .blur(20.dp)
            )

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = 30.dp, y = (-15 - floatAnim).dp)
                    .background(
                        color = MintBreeze.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .blur(15.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image Container
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    // Glow ring
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Crunch.copy(alpha = 0.3f),
                                        Crunch.copy(alpha = 0f)
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    // Profile Image
                    Surface(
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(
                                elevation = 16.dp,
                                shape = CircleShape,
                                ambientColor = NeumorphDark.copy(alpha = 0.2f)
                            )
                            .border(
                                width = 3.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        NeumorphLight,
                                        ChineseSilver.copy(alpha = 0.5f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        shape = CircleShape,
                        color = NeumorphLight
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.size(50.dp),
                                tint = WarmHaze
                            )
                        }
                    }

                    // Status indicator
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-5).dp, y = (-5).dp)
                            .background(
                                color = MintBreeze,
                                shape = CircleShape
                            )
                            .border(3.dp, NeumorphLight, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name
                Text(
                    text = userProfile.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    ),
                    color = Lead,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Bio
                Text(
                    text = userProfile.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // User Info Pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoPill(text = "${userProfile.age} yo")
                    InfoPill(text = userProfile.city)
                    InfoPill(text = userProfile.gender)
                }
            }
        }
    }
}

@Composable
private fun InfoPill(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = NeumorphLight.copy(alpha = 0.7f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Dreamland.copy(alpha = 0.3f)
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = WarmHaze,
            fontWeight = FontWeight.Medium
        )
    }
}
