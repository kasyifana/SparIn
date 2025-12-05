package com.example.sparinprofile.presentation.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.sparinprofile.presentation.profile.AIInsights
import com.example.sparinprofile.ui.theme.*

@Composable
fun AIInsightCard(
    insights: AIInsights
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aiBlobs")
    
    val blob1Rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob1"
    )
    
    val blob2Rotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob2"
    )
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Section Header
        Text(
            text = "AI Insights 🤖",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkColors.TextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = DarkColors.SurfaceDefault
        ) {
            Box {
                // Animated background blobs
                Box(modifier = Modifier.fillMaxSize()) {
                    // Blob 1
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 20.dp, y = (-20).dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        VibrantPurple.copy(alpha = 0.15f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )
                    
                    // Blob 2
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.BottomStart)
                            .offset(x = (-30).dp, y = 30.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        SportyCyan.copy(alpha = 0.12f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )
                    
                    // Blob 3
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Crunch.copy(alpha = 0.1f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )
                }
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Performance Trend
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Crunch.copy(alpha = 0.2f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "📈", fontSize = 20.sp)
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = insights.performanceTrend,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkColors.TextPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Recommendations
                    Text(
                        text = "Recommendations",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkColors.TextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    insights.recommendations.forEach { recommendation ->
                        RecommendationItem(recommendation)
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Suggested Rooms
                    Text(
                        text = "Suggested Rooms",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkColors.TextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    insights.suggestedRooms.forEach { room ->
                        SuggestedRoomItem(room)
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "💡",
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = DarkColors.TextSecondary,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun SuggestedRoomItem(room: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { /* Navigate to room */ },
        shape = RoundedCornerShape(16.dp),
        color = DarkColors.SurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            DarkColors.BorderLight
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "🎯", fontSize = 20.sp)
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = room,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkColors.TextPrimary
                )
            }
            
            // Join button
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Crunch
            ) {
                Text(
                    text = "Join",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202022)
@Composable
fun AIInsightCardPreview() {
    AIInsightCard(
        insights = AIInsights(
            performanceTrend = "📈 Up 15% this week",
            recommendations = listOf(
                "Practice your backhand shots",
                "Join more competitive matches",
                "Focus on stamina training"
            ),
            suggestedRooms = listOf(
                "Advanced Badminton - 7PM",
                "Competitive Futsal - Weekend"
            )
        )
    )
}
