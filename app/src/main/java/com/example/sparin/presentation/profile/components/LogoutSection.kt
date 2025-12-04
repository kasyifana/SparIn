package com.example.sparin.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.sparin.ui.theme.*

/**
 * LogoutSection Component - Premium Gen-Z Design
 * Matches ProfileHeader & AIInsightCard aesthetic
 * Features glassmorphism, floating blobs, and sophisticated animations
 */
@Composable
fun LogoutSection(
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Header (matching other sections)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Crunch
            )
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = Lead
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Main Logout Card (Premium Design)
        LogoutCard(
            onClick = { showLogoutDialog = true }
        )
    }
    
    // Confirmation Dialog
    if (showLogoutDialog) {
        PremiumLogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }
}

@Composable
private fun LogoutCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logout_card_anim")
    
    // Floating blob animations
    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )
    
    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = RoseDust.copy(alpha = 0.2f),
                spotColor = RoseDust.copy(alpha = 0.2f)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
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
                            ChineseSilver.copy(alpha = 0.6f),
                            RoseDust.copy(alpha = 0.4f),
                            SoftLavender.copy(alpha = 0.5f)
                        )
                    )
                )
        ) {
            // Floating accent blobs (matching ProfileHeader style)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-10 + float1).dp)
                    .background(
                        color = RoseDust.copy(alpha = 0.25f),
                        shape = CircleShape
                    )
                    .blur(30.dp)
            )
            
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-20).dp, y = (20 + float2).dp)
                    .background(
                        color = SoftLavender.copy(alpha = 0.25f),
                        shape = CircleShape
                    )
                    .blur(25.dp)
            )
            
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-10).dp, y = float1.dp)
                    .background(
                        color = Crunch.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
                    .blur(20.dp)
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Primary Logout Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon container with gradient
                        Surface(
                            modifier = Modifier.size(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Transparent
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                DarkColors.StatusDanger.copy(alpha = 0.25f),
                                                RoseDust.copy(alpha = 0.3f)
                                            )
                                        )
                                    )
                                    .border(
                                        width = 1.5.dp,
                                        color = DarkColors.StatusDanger.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ExitToApp,
                                    contentDescription = "Logout",
                                    tint = DarkColors.StatusDanger.copy(alpha = 0.9f),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        
                        Column(
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "Logout",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = Lead
                            )
                            Text(
                                text = "See you next game! 👋",
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmHaze,
                                fontSize = 13.sp
                            )
                        }
                    }
                    
                    // Arrow indicator
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = CircleShape,
                        color = DarkColors.StatusDanger.copy(alpha = 0.15f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowForward,
                                contentDescription = null,
                                tint = DarkColors.StatusDanger.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
                
                // Divider with gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Dreamland.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                
                // Quick Actions Row (matching design)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionItem(
                        icon = Icons.Rounded.Settings,
                        label = "Settings",
                        accentColor = MintBreeze,
                        modifier = Modifier.weight(1f),
                        onClick = { /* Navigate to settings */ }
                    )
                    
                    QuickActionItem(
                        icon = Icons.Rounded.Info,
                        label = "About",
                        accentColor = PeachGlow,
                        modifier = Modifier.weight(1f),
                        onClick = { /* Navigate to about */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .height(70.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = accentColor.copy(alpha = 0.15f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = NeumorphLight
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = accentColor.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = accentColor.copy(alpha = 0.9f),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun PremiumLogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dialog_anim")
    
    val float by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    
    val iconPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralInk.copy(alpha = 0.65f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            // Main dialog card
            Surface(
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .shadow(
                        elevation = 28.dp,
                        shape = RoundedCornerShape(32.dp),
                        ambientColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Prevent click through */ },
                shape = RoundedCornerShape(32.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    ChineseSilver.copy(alpha = 0.95f),
                                    SoftLavender.copy(alpha = 0.9f),
                                    CascadingWhite
                                )
                            )
                        )
                ) {
                    // Decorative floating blobs
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 40.dp, y = (-30 + float).dp)
                            .background(
                                color = RoseDust.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                            .blur(50.dp)
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .align(Alignment.BottomStart)
                            .offset(x = (-25).dp, y = (30 - float).dp)
                            .background(
                                color = Crunch.copy(alpha = 0.15f),
                                shape = CircleShape
                            )
                            .blur(45.dp)
                    )
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Emoji Badge Icon
                        Surface(
                            modifier = Modifier
                                .size(90.dp)
                                .scale(iconPulse)
                                .shadow(
                                    elevation = 14.dp,
                                    shape = CircleShape,
                                    ambientColor = RoseDust.copy(alpha = 0.3f)
                                ),
                            shape = CircleShape,
                            color = Color.Transparent
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                RoseDust.copy(alpha = 0.3f),
                                                RoseDust.copy(alpha = 0.08f)
                                            )
                                        )
                                    )
                                    .border(
                                        width = 2.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                NeumorphLight,
                                                RoseDust.copy(alpha = 0.4f)
                                            )
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "👋",
                                    fontSize = 44.sp
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(26.dp))
                        
                        // Title
                        Text(
                            text = "Leaving Already?",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            ),
                            color = Lead,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        // Subtitle
                        Text(
                            text = "We'll miss you! Your game stats and progress will be saved securely.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 22.sp
                            ),
                            color = WarmHaze,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Stay Button
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(54.dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(18.dp),
                                        ambientColor = ChineseSilver.copy(alpha = 0.3f)
                                    ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeumorphLight,
                                    contentColor = Lead
                                ),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Text(
                                    text = "Stay",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            
                            // Logout Button (Icon Only)
                            Button(
                                onClick = onConfirm,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(54.dp)
                                    .shadow(
                                        elevation = 10.dp,
                                        shape = RoundedCornerShape(18.dp),
                                        ambientColor = DarkColors.StatusDanger.copy(alpha = 0.4f),
                                        spotColor = DarkColors.StatusDanger.copy(alpha = 0.4f)
                                    ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkColors.StatusDanger,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ExitToApp,
                                    contentDescription = "Logout",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
