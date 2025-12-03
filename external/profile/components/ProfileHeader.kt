package com.example.sparinprofile.presentation.profile.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparinprofile.presentation.profile.UserProfile
import com.example.sparinprofile.ui.theme.*

@Composable
fun ProfileHeader(
    userProfile: UserProfile,
    onEditClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkColors.BgSecondary,
                        DarkColors.BgTertiary
                    )
                )
            )
            .padding(24.dp)
    ) {
        // Floating animated blobs
        AnimatedFloatingBlobs()
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image with glow ring
            Box(
                contentAlignment = Alignment.Center
            ) {
                // Glow ring
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Crunch.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
                
                // Profile image
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = DarkColors.SurfaceVariant,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        // Fallback icon if no image
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(40.dp),
                            tint = DarkColors.TextSecondary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Name
            Text(
                text = userProfile.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkColors.TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Bio
            Text(
                text = userProfile.bio,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = DarkColors.TextSecondary,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // User info pills
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoPill("${userProfile.age} yo")
                InfoPill(userProfile.city)
                InfoPill(userProfile.gender)
            }
        }
        
        // Edit button
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(40.dp)
                .clickable { onEditClick() },
            shape = CircleShape,
            color = Crunch.copy(alpha = 0.9f),
            shadowElevation = 4.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun InfoPill(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkColors.SurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            DarkColors.BorderLight
        )
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = DarkColors.TextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun AnimatedFloatingBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "blobs")
    
    val blob1Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1"
    )
    
    val blob2Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2"
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Blob 1
        Box(
            modifier = Modifier
                .offset(x = blob1Offset.dp, y = blob1Offset.dp)
                .size(60.dp)
                .align(Alignment.TopStart)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            SportyCyan.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        // Blob 2
        Box(
            modifier = Modifier
                .offset(x = blob2Offset.dp, y = (-blob2Offset).dp)
                .size(80.dp)
                .align(Alignment.BottomEnd)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Crunch.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202022)
@Composable
fun ProfileHeaderPreview() {
    ProfileHeader(
        userProfile = UserProfile(
            name = "Alex Thompson",
            bio = "Passionate badminton player | Weekend warrior",
            profileImageUrl = null,
            city = "Jakarta",
            age = 25,
            gender = "Male"
        )
    )
}
