package com.example.sparin.presentation.community.feed

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

// ==================== DATA CLASSES ====================

data class CommunityPost(
    val id: String,
    val authorName: String,
    val authorEmoji: String,
    val content: String,
    val timeAgo: String,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean = false,
    val images: List<String> = emptyList()
)

data class CommunityEvent(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val attendees: Int,
    val emoji: String
)

// ==================== SAMPLE DATA ====================

private val samplePosts = listOf(
    CommunityPost(
        id = "1",
        authorName = "Raka Pratama",
        authorEmoji = "🏸",
        content = "Siapa yang mau main badminton bareng weekend ini? Drop location kalian di comment ya! 🏸🔥",
        timeAgo = "2 hours ago",
        likes = 24,
        comments = 8,
        isLiked = true
    ),
    CommunityPost(
        id = "2",
        authorName = "Dinda Sports",
        authorEmoji = "💪",
        content = "Hari ini berhasil beat personal record! 💪 Keep pushing everyone! Never give up on your fitness journey! #NeverGiveUp #SportLife",
        timeAgo = "5 hours ago",
        likes = 56,
        comments = 12
    ),
    CommunityPost(
        id = "3",
        authorName = "Coach Andy",
        authorEmoji = "🎯",
        content = "Tips untuk pemula:\n\n1. Warm up sebelum main\n2. Fokus pada footwork\n3. Jangan lupa stretching setelahnya\n\nShare ke teman kalian yang baru mulai olahraga! 🙌",
        timeAgo = "Yesterday",
        likes = 128,
        comments = 34
    ),
    CommunityPost(
        id = "4",
        authorName = "Maya Runner",
        authorEmoji = "🏃‍♀️",
        content = "Morning run done! 5km completed before sunrise ☀️ Who else loves early morning exercise?",
        timeAgo = "2 days ago",
        likes = 89,
        comments = 21
    )
)

private val upcomingEvents = listOf(
    CommunityEvent(
        id = "1",
        title = "Weekend Tournament",
        date = "Dec 7, 2025",
        location = "GBK Arena",
        attendees = 48,
        emoji = "🏆"
    ),
    CommunityEvent(
        id = "2",
        title = "Beginner Workshop",
        date = "Dec 14, 2025",
        location = "Sport Center BSD",
        attendees = 25,
        emoji = "📚"
    ),
    CommunityEvent(
        id = "3",
        title = "Fun Match Night",
        date = "Dec 20, 2025",
        location = "Mall Arena",
        attendees = 32,
        emoji = "🎉"
    )
)

// ==================== GEN-Z COLOR PALETTE ====================

private val GenZBlue = Color(0xFF8CCFFF)
private val GenZTeal = Color(0xFF35C8C3)
private val GenZCyan = Color(0xFF57D3FF)
private val GenZLavender = Color(0xFFB8B5FF)
private val GenZGradientStart = Color(0xFF35C8C3)
private val GenZGradientEnd = Color(0xFF57D3FF)

/**
 * Community Feed Screen - Shows posts and activities within a community
 * Gen-Z aesthetic with glassmorphism, soft shadows, and vibrant colors
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityFeedScreen(
    navController: NavHostController,
    communityId: String,
    communityName: String,
    communityEmoji: String
) {
    var showCreatePost by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CascadingWhite,
                        GenZBlue.copy(alpha = 0.06f),
                        SoftLavender.copy(alpha = 0.12f)
                    )
                )
            )
    ) {
        // Background blobs
        FeedBackgroundBlobs()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header with back button
            item {
                FeedHeader(
                    communityName = communityName,
                    communityEmoji = communityEmoji,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Community Banner
            item {
                CommunityBanner(
                    name = communityName,
                    emoji = communityEmoji,
                    memberCount = "3.2k",
                    description = "Welcome to $communityName! Connect, share, and play together. 🎯"
                )
            }

            // Quick Actions
            item {
                Spacer(modifier = Modifier.height(20.dp))
                QuickActions(
                    onCreatePost = { showCreatePost = true },
                    onFindMatch = { /* Navigate to match finder */ },
                    onViewMembers = { /* Navigate to members */ }
                )
            }

            // Upcoming Events
            item {
                Spacer(modifier = Modifier.height(24.dp))
                UpcomingEventsSection(events = upcomingEvents)
            }

            // Posts Section Header
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Posts",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Lead
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GenZTeal.copy(alpha = 0.12f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Sort,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = GenZTeal
                            )
                            Text(
                                text = "Latest",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = GenZTeal
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Posts
            items(samplePosts) { post ->
                PostCard(
                    post = post,
                    onLikeClick = { /* Handle like */ },
                    onCommentClick = { /* Handle comment */ },
                    onShareClick = { /* Handle share */ }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showCreatePost = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = GenZTeal.copy(alpha = 0.3f)
                ),
            containerColor = GenZTeal,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Create Post"
            )
        }
    }
}

// ==================== BACKGROUND ====================

@Composable
private fun FeedBackgroundBlobs() {
    val infiniteTransition = rememberInfiniteTransition(label = "feed_blobs")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
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
            color = GenZTeal.copy(alpha = 0.15f),
            radius = 160f,
            center = Offset(
                x = size.width * 0.9f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 30f,
                y = size.height * 0.1f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 25f
            )
        )

        drawCircle(
            color = GenZBlue.copy(alpha = 0.18f),
            radius = 140f,
            center = Offset(
                x = size.width * 0.1f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 25f,
                y = size.height * 0.35f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 30f
            )
        )

        drawCircle(
            color = GenZLavender.copy(alpha = 0.12f),
            radius = 130f,
            center = Offset(
                x = size.width * 0.7f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f,
                y = size.height * 0.65f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 25f
            )
        )
    }
}

// ==================== HEADER ====================

@Composable
private fun FeedHeader(
    communityName: String,
    communityEmoji: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
    ) {
        // Gradient background
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = communityEmoji,
                    fontSize = 24.sp
                )
                Text(
                    text = communityName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // More options
            IconButton(
                onClick = { /* Show options */ },
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
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "More",
                    tint = Lead
                )
            }
        }
    }
}

// ==================== COMMUNITY BANNER ====================

@Composable
private fun CommunityBanner(
    name: String,
    emoji: String,
    memberCount: String,
    description: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "banner_anim")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = GenZTeal.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GenZGradientStart,
                            GenZGradientEnd
                        )
                    )
                )
        ) {
            // Decorative elements
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-15).dp, y = (15 + float1).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
                    .blur(8.dp)
            )

            Text(
                text = emoji,
                fontSize = 48.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = (-30).dp, y = float1.dp)
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.People,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = "$memberCount members",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Member avatars
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy((-8).dp)
                ) {
                    repeat(5) { index ->
                        Surface(
                            modifier = Modifier
                                .size(28.dp)
                                .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                            shape = CircleShape,
                            color = listOf(PeachGlow, MintBreeze, SkyMist, RoseDust, SoftLavender)[index]
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = listOf("😊", "🏃", "💪", "🎯", "⚡")[index],
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "and more...",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

// ==================== QUICK ACTIONS ====================

@Composable
private fun QuickActions(
    onCreatePost: () -> Unit,
    onFindMatch: () -> Unit,
    onViewMembers: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionButton(
            icon = Icons.Rounded.Edit,
            label = "Post",
            color = GenZTeal,
            onClick = onCreatePost,
            modifier = Modifier.weight(1f)
        )

        QuickActionButton(
            icon = Icons.Rounded.Groups,
            label = "Match",
            color = GenZBlue,
            onClick = onFindMatch,
            modifier = Modifier.weight(1f)
        )

        QuickActionButton(
            icon = Icons.Rounded.People,
            label = "Members",
            color = GenZLavender,
            onClick = onViewMembers,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(70.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = color.copy(alpha = 0.2f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = NeumorphLight.copy(alpha = 0.98f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = color
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Lead
            )
        }
    }
}

// ==================== UPCOMING EVENTS ====================

@Composable
private fun UpcomingEventsSection(events: List<CommunityEvent>) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upcoming Events",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Lead
            )

            TextButton(onClick = { /* View all */ }) {
                Text(
                    text = "See all",
                    color = GenZTeal,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events) { event ->
                EventCard(event = event)
            }
        }
    }
}

@Composable
private fun EventCard(event: CommunityEvent) {
    Surface(
        modifier = Modifier
            .width(200.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight.copy(alpha = 0.98f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Emoji and title row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = GenZTeal.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = event.emoji, fontSize = 20.sp)
                    }
                }

                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = GenZTeal
                )
                Text(
                    text = event.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = GenZBlue
                )
                Text(
                    text = event.location,
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Attendees
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MintBreeze.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.People,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = GenZTeal
                    )
                    Text(
                        text = "${event.attendees} going",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = GenZTeal
                    )
                }
            }
        }
    }
}

// ==================== POST CARD ====================

@Composable
private fun PostCard(
    post: CommunityPost,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit
) {
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var likeCount by remember { mutableIntStateOf(post.likes) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(22.dp),
        color = NeumorphLight.copy(alpha = 0.98f)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            // Author row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Avatar
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                        color = GenZTeal.copy(alpha = 0.15f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = post.authorEmoji, fontSize = 22.sp)
                        }
                    }

                    Column {
                        Text(
                            text = post.authorName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Lead
                        )

                        Text(
                            text = post.timeAgo,
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmHaze
                        )
                    }
                }

                IconButton(
                    onClick = { /* More options */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreHoriz,
                        contentDescription = "More",
                        tint = WarmHaze
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Content
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Lead,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Divider
            HorizontalDivider(
                color = ChineseSilver.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Like
                PostActionButton(
                    icon = if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    label = likeCount.toString(),
                    color = if (isLiked) Color(0xFFFF6B6B) else WarmHaze,
                    onClick = {
                        isLiked = !isLiked
                        likeCount = if (isLiked) likeCount + 1 else likeCount - 1
                        onLikeClick()
                    }
                )

                // Comment
                PostActionButton(
                    icon = Icons.Rounded.ChatBubbleOutline,
                    label = post.comments.toString(),
                    color = WarmHaze,
                    onClick = onCommentClick
                )

                // Share
                PostActionButton(
                    icon = Icons.Rounded.Share,
                    label = "Share",
                    color = WarmHaze,
                    onClick = onShareClick
                )
            }
        }
    }
}

@Composable
private fun PostActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = color
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}
