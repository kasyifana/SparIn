package com.example.sparin.presentation.community.feed

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sparin.presentation.navigation.Screen
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// ==================== DATA CLASSES ====================

data class CommunityPost(
    val id: String,
    val authorId: String = "", // User ID to check ownership
    val authorName: String,
    val authorEmoji: String,
    val content: String,
    val timeAgo: String,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean = false,
    val images: List<String> = emptyList(),
    val commentsList: List<PostComment> = emptyList()
)

data class PostComment(
    val id: String,
    val authorName: String,
    val authorEmoji: String,
    val content: String,
    val timeAgo: String
)

data class CommunityInfo(
    val creatorName: String,
    val creatorEmoji: String,
    val createdDate: String,
    val location: String,
    val totalMembers: Int,
    val description: String
)

data class CommunityEvent(
    val id: String,
    val title: String,
    val date: String,
    val time: String = "14:00 - 17:00",
    val location: String,
    val attendees: Int,
    val emoji: String,
    val description: String = "",
    val organizer: String = "Community Admin",
    val requirements: List<String> = emptyList(),
    val category: String = "Sport",
    val highlights: List<String> = emptyList()
)

// ==================== SAMPLE DATA ====================

// Current user ID - In production, get this from AuthRepository
private const val CURRENT_USER_ID = "current_user"

private val samplePosts = listOf(
    CommunityPost(
        id = "1",
        authorId = "user_raka",
        authorName = "Raka Pratama",
        authorEmoji = "🏸",
        content = "Siapa yang mau main badminton bareng weekend ini? Drop location kalian di comment ya! 🏸🔥",
        timeAgo = "2 hours ago",
        likes = 24,
        comments = 8,
        isLiked = true,
        commentsList = listOf(
            PostComment("c1", "Dinda", "💪", "Gue ikut bro! Area mana?", "1 hour ago"),
            PostComment("c2", "Budi", "🏸", "Count me in!", "30 min ago")
        )
    ),
    CommunityPost(
        id = "2",
        authorId = "user_dinda",
        authorName = "Dinda Sports",
        authorEmoji = "💪",
        content = "Hari ini berhasil beat personal record! 💪 Keep pushing everyone! Never give up on your fitness journey! #NeverGiveUp #SportLife",
        timeAgo = "5 hours ago",
        likes = 56,
        comments = 12,
        commentsList = listOf(
            PostComment("c3", "Andi", "🎯", "Keren banget!", "4 hours ago")
        )
    ),
    CommunityPost(
        id = "3",
        authorId = "user_andy",
        authorName = "Coach Andy",
        authorEmoji = "🎯",
        content = "Tips untuk pemula:\n\n1. Warm up sebelum main\n2. Fokus pada footwork\n3. Jangan lupa stretching setelahnya\n\nShare ke teman kalian yang baru mulai olahraga! 🙌",
        timeAgo = "Yesterday",
        likes = 128,
        comments = 34
    ),
    CommunityPost(
        id = "4",
        authorId = "user_maya",
        authorName = "Maya Runner",
        authorEmoji = "🏃‍♀️",
        content = "Morning run done! 5km completed before sunrise ☀️ Who else loves early morning exercise?",
        timeAgo = "2 days ago",
        likes = 89,
        comments = 21
    )
)

// Sample community info
private val sampleCommunityInfo = CommunityInfo(
    creatorName = "Raka Pratama",
    creatorEmoji = "🏸",
    createdDate = "15 November 2024",
    location = "Jakarta, Indonesia",
    totalMembers = 3200,
    description = "Komunitas badminton terbesar di Jakarta! Join us untuk main bareng, sharing tips, dan mencari teman sparring."
)

// Sample members for member list
private val sampleMembers = listOf(
    Triple("Raka Pratama", "🏸", "Admin"),
    Triple("Dinda Sports", "💪", "Moderator"),
    Triple("Coach Andy", "🎯", "Member"),
    Triple("Maya Runner", "🏃‍♀️", "Member"),
    Triple("Budi Santoso", "⚡", "Member"),
    Triple("Sari Dewi", "🌟", "Member"),
    Triple("Andi Wijaya", "🏆", "Member"),
    Triple("Putri Ayu", "✨", "Member")
)

private val upcomingEvents = listOf(
    CommunityEvent(
        id = "1",
        title = "Weekend Tournament",
        date = "Dec 7, 2025",
        time = "08:00 - 17:00",
        location = "GBK Arena",
        attendees = 48,
        emoji = "🏆",
        description = "Join our annual weekend tournament! Compete with players from all skill levels and win exciting prizes.",
        organizer = "Raka Pratama",
        category = "Competition",
        requirements = listOf(
            "Bring your own racket",
            "Wear appropriate sports attire",
            "Register before Dec 5"
        ),
        highlights = listOf(
            "Prize pool Rp 5.000.000",
            "Free lunch provided",
            "Professional referees",
            "Live streaming"
        )
    ),
    CommunityEvent(
        id = "2",
        title = "Beginner Workshop",
        date = "Dec 14, 2025",
        time = "10:00 - 12:00",
        location = "Sport Center BSD",
        attendees = 25,
        emoji = "📚",
        description = "Learn the basics of badminton from our experienced coaches. Perfect for beginners who want to start their journey.",
        organizer = "Coach Andy",
        category = "Workshop",
        requirements = listOf(
            "No prior experience needed",
            "Rackets provided",
            "Comfortable clothing"
        ),
        highlights = listOf(
            "Certified coach instruction",
            "Basic technique training",
            "Free refreshments",
            "Certificate of completion"
        )
    ),
    CommunityEvent(
        id = "3",
        title = "Fun Match Night",
        date = "Dec 20, 2025",
        time = "19:00 - 22:00",
        location = "Mall Arena",
        attendees = 32,
        emoji = "🎉",
        description = "Casual night of fun matches! Meet new friends and enjoy a relaxed evening of badminton with music and snacks.",
        organizer = "Dinda Sports",
        category = "Social",
        requirements = listOf(
            "Bring positive vibes!",
            "All skill levels welcome"
        ),
        highlights = listOf(
            "DJ music",
            "Snacks and drinks included",
            "Random partner matching",
            "Mini games and prizes"
        )
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
    var showInfoDialog by remember { mutableStateOf(false) }
    var showMembersDialog by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }
    
    // Event detail popup state
    var selectedEvent by remember { mutableStateOf<CommunityEvent?>(null) }
    var showEventDetail by remember { mutableStateOf(false) }
    
    // Selected image URI from gallery
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }
    
    // Dynamic posts list - new posts will be added here
    var posts by remember { mutableStateOf(samplePosts) }
    
    // Sort order for posts: true = Latest first, false = Oldest first
    var isLatestFirst by remember { mutableStateOf(true) }
    
    // Sorted posts based on sort order
    val sortedPosts = remember(posts, isLatestFirst) {
        if (isLatestFirst) posts else posts.reversed()
    }

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
            // Header with back button and 3-dot menu
            item {
                FeedHeader(
                    communityName = communityName,
                    communityEmoji = communityEmoji,
                    showMoreMenu = showMoreMenu,
                    onBackClick = { navController.popBackStack() },
                    onMoreClick = { showMoreMenu = true },
                    onDismissMenu = { showMoreMenu = false },
                    onInfoClick = {
                        showMoreMenu = false
                        showInfoDialog = true
                    },
                    onMembersClick = {
                        showMoreMenu = false
                        showMembersDialog = true
                    }
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

            // CREATE POST SECTION - Inline post creator
            item {
                Spacer(modifier = Modifier.height(20.dp))
                CreatePostCard(
                    selectedImageUri = selectedImageUri,
                    onPostCreated = { content, imageUri ->
                        // Create new post and add to top of list
                        val newPost = CommunityPost(
                            id = UUID.randomUUID().toString(),
                            authorId = CURRENT_USER_ID, // Mark as current user's post
                            authorName = "You",
                            authorEmoji = "😊",
                            content = content,
                            timeAgo = "Just now",
                            likes = 0,
                            comments = 0,
                            isLiked = false,
                            images = if (imageUri != null) listOf(imageUri.toString()) else emptyList()
                        )
                        posts = listOf(newPost) + posts
                        selectedImageUri = null
                    },
                    onPickImage = {
                        imagePickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onClearImage = {
                        selectedImageUri = null
                    }
                )
            }

            // Upcoming Events
            item {
                Spacer(modifier = Modifier.height(24.dp))
                UpcomingEventsSection(
                    events = upcomingEvents,
                    onSeeAllClick = {
                        val encodedName = URLEncoder.encode(communityName, StandardCharsets.UTF_8.toString())
                        navController.navigate(Screen.AllUpcomingEvents.createRoute(communityId, encodedName))
                    },
                    onEventClick = { event ->
                        selectedEvent = event
                        showEventDetail = true
                    }
                )
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

                    // Clickable sort toggle
                    Surface(
                        modifier = Modifier.clickable { isLatestFirst = !isLatestFirst },
                        shape = RoundedCornerShape(12.dp),
                        color = GenZTeal.copy(alpha = 0.12f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isLatestFirst) Icons.Rounded.ArrowDownward else Icons.Rounded.ArrowUpward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = GenZTeal
                            )
                            Text(
                                text = if (isLatestFirst) "Latest" else "Oldest",
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

            // Posts with dynamic like/comment
            items(sortedPosts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    isOwnPost = post.authorId == CURRENT_USER_ID,
                    onLikeClick = { updatedPost ->
                        posts = posts.map { if (it.id == updatedPost.id) updatedPost else it }
                    },
                    onCommentAdded = { postId, newComment ->
                        posts = posts.map { 
                            if (it.id == postId) {
                                it.copy(
                                    comments = it.comments + 1,
                                    commentsList = it.commentsList + newComment
                                )
                            } else it 
                        }
                    },
                    onShareClick = { /* Handle share */ },
                    onDeleteClick = {
                        // Delete post from list
                        posts = posts.filter { it.id != post.id }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
    
    // Info Dialog
    if (showInfoDialog) {
        CommunityInfoDialog(
            communityName = communityName,
            communityEmoji = communityEmoji,
            info = sampleCommunityInfo,
            onDismiss = { showInfoDialog = false }
        )
    }
    
    // Members Dialog
    if (showMembersDialog) {
        MembersDialog(
            communityName = communityName,
            members = sampleMembers,
            onDismiss = { showMembersDialog = false }
        )
    }
    
    // Event Detail Dialog
    if (showEventDetail && selectedEvent != null) {
        FeedEventDetailDialog(
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
    showMoreMenu: Boolean,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onInfoClick: () -> Unit,
    onMembersClick: () -> Unit
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
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            ) {
                Text(
                    text = communityEmoji,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = communityName,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 26.sp,
                        letterSpacing = (-0.4).sp,
                        textAlign = TextAlign.Center
                    ),
                    color = Lead,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }

            // More options with dropdown
            Box {
                IconButton(
                    onClick = onMoreClick,
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
                
                // Dropdown Menu
                MaterialTheme(
                    shapes = MaterialTheme.shapes.copy(
                        extraSmall = RoundedCornerShape(16.dp)
                    )
                ) {
                    DropdownMenu(
                        expanded = showMoreMenu,
                        onDismissRequest = onDismissMenu,
                        offset = DpOffset(x = 0.dp, y = 8.dp),
                        containerColor = NeumorphLight,
                        shadowElevation = 8.dp,
                        modifier = Modifier.clip(RoundedCornerShape(16.dp))
                    ) {
                    // Info option
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(36.dp),
                                    shape = CircleShape,
                                    color = GenZTeal.copy(alpha = 0.15f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Rounded.Info,
                                            contentDescription = null,
                                            tint = GenZTeal,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Info",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Lead
                                    )
                                    Text(
                                        text = "Community details",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = WarmHaze
                                    )
                                }
                            }
                        },
                        onClick = onInfoClick,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        color = ChineseSilver.copy(alpha = 0.3f)
                    )
                    
                    // Members option
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(36.dp),
                                    shape = CircleShape,
                                    color = GenZBlue.copy(alpha = 0.15f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Rounded.People,
                                            contentDescription = null,
                                            tint = GenZBlue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Members",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Lead
                                    )
                                    Text(
                                        text = "View all members",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = WarmHaze
                                    )
                                }
                            }
                        },
                        onClick = onMembersClick,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    }
                }
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
        targetValue = 8f,
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
            .padding(top = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GenZGradientStart,
                            GenZGradientEnd
                        )
                    )
                )
        ) {
            // Decorative circle - top right
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-10).dp, y = (10 + float1).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.12f),
                        shape = CircleShape
                    )
                    .blur(10.dp)
            )
            
            // Decorative circle - bottom right
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-20).dp, y = (-20).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.08f),
                        shape = CircleShape
                    )
                    .blur(8.dp)
            )

            // Content - with proper padding to avoid emoji overlap
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left content - text info (with fixed width to prevent overlap)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top section - member count and description
                    Column(
                        modifier = Modifier.padding(end = 70.dp) // More space for emoji
                    ) {
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

                        // Welcome message with community name
                        Text(
                            text = "Welcome to $name",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Bottom section - Member avatars only (removed +more)
                    Row(
                        modifier = Modifier.padding(end = 70.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy((-8).dp)
                    ) {
                        repeat(4) { index ->
                            Surface(
                                modifier = Modifier
                                    .size(28.dp)
                                    .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                                shape = CircleShape,
                                color = listOf(PeachGlow, MintBreeze, SkyMist, RoseDust)[index]
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = listOf("😊", "🏃", "💪", "🎯")[index],
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Right side - Emoji (fixed position, absolute right)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 44.sp,
                        modifier = Modifier.offset(y = float1.dp)
                    )
                }
            }
        }
    }
}



// ==================== UPCOMING EVENTS ====================

@Composable
private fun UpcomingEventsSection(
    events: List<CommunityEvent>,
    onSeeAllClick: () -> Unit,
    onEventClick: (CommunityEvent) -> Unit
) {
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

            TextButton(onClick = onSeeAllClick) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "See all",
                        color = GenZTeal,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = GenZTeal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events) { event ->
                EventCard(
                    event = event,
                    onClick = { onEventClick(event) }
                )
            }
        }
    }
}

@Composable
private fun EventCard(
    event: CommunityEvent,
    onClick: () -> Unit
) {
    // Poster-style event card - informational only, tap for details
    Surface(
        modifier = Modifier
            .width(180.dp)
            .height(220.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GenZTeal.copy(alpha = 0.9f),
                            GenZCyan.copy(alpha = 0.85f)
                        )
                    )
                )
        ) {
            // Decorative circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset(x = 100.dp, y = (-20).dp)
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .blur(20.dp)
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top section - Emoji and title
                Column {
                    // Large emoji
                    Text(
                        text = event.emoji,
                        fontSize = 42.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Bottom section - Date and location
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Date
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = event.date,
                            style = MaterialTheme.typography.labelSmall,
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
                            modifier = Modifier.size(14.dp),
                            tint = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = event.location,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    // Tap to view hint
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.TouchApp,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Text(
                                text = "Tap for details",
                                style = MaterialTheme.typography.labelSmall.copy(
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

// ==================== POST CARD ====================

@Composable
private fun PostCard(
    post: CommunityPost,
    isOwnPost: Boolean,
    onLikeClick: (CommunityPost) -> Unit,
    onCommentAdded: (String, PostComment) -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var likeCount by remember { mutableIntStateOf(post.likes) }
    var showComments by remember { mutableStateOf(false) }
    var newCommentText by remember { mutableStateOf("") }
    var showPostMenu by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showImagePopup by remember { mutableStateOf(false) }
    var selectedImageForPopup by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(22.dp),
        color = NeumorphLight
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

                // Only show 3-dot menu for user's own posts
                if (isOwnPost) {
                    Box {
                        IconButton(
                            onClick = { showPostMenu = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MoreHoriz,
                                contentDescription = "More",
                                tint = WarmHaze
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showPostMenu,
                            onDismissRequest = { showPostMenu = false },
                            modifier = Modifier.background(NeumorphLight)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = null,
                                            tint = Color(0xFFFF6B6B),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Delete Post",
                                            color = Color(0xFFFF6B6B)
                                        )
                                    }
                                },
                                onClick = {
                                    showPostMenu = false
                                    showDeleteConfirm = true
                                }
                            )
                        }
                    }
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
            
            // Display post images - clickable for fullscreen
            if (post.images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable {
                            val imageUri = post.images.firstOrNull()
                            if (imageUri != null) {
                                selectedImageForPopup = imageUri
                                showImagePopup = true
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = GenZBlue.copy(alpha = 0.05f)
                ) {
                    val imageUri = post.images.firstOrNull()
                    if (imageUri != null && imageUri.startsWith("content://")) {
                        // Real image from gallery
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(Uri.parse(imageUri))
                                .crossfade(true)
                                .build(),
                            contentDescription = "Post image - tap to view full",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Placeholder for demo images
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    modifier = Modifier.size(60.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    color = GenZBlue.copy(alpha = 0.15f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Rounded.Image,
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp),
                                            tint = GenZBlue
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "📷 Photo attached",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GenZBlue
                                )
                            }
                        }
                    }
                }
            }

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
                        onLikeClick(post.copy(isLiked = isLiked, likes = likeCount))
                    }
                )

                // Comment
                PostActionButton(
                    icon = Icons.Rounded.ChatBubbleOutline,
                    label = (post.comments + post.commentsList.size - (post.commentsList.size.coerceAtMost(post.comments))).toString(),
                    color = if (showComments) GenZTeal else WarmHaze,
                    onClick = { showComments = !showComments }
                )

                // Share
                PostActionButton(
                    icon = Icons.Rounded.Share,
                    label = "Share",
                    color = WarmHaze,
                    onClick = onShareClick
                )
            }
            
            // Expandable Comments Section
            AnimatedVisibility(
                visible = showComments,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    HorizontalDivider(
                        color = ChineseSilver.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Existing comments
                    if (post.commentsList.isNotEmpty()) {
                        post.commentsList.forEach { comment ->
                            CommentItem(comment = comment)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    } else {
                        Text(
                            text = "No comments yet. Be the first to comment!",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmHaze,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Add comment input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = GenZTeal.copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "😊", fontSize = 16.sp)
                            }
                        }
                        
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = ChineseSilver.copy(alpha = 0.15f)
                        ) {
                            BasicTextField(
                                value = newCommentText,
                                onValueChange = { newCommentText = it },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                textStyle = TextStyle(
                                    fontSize = 14.sp,
                                    color = Lead
                                ),
                                decorationBox = { innerTextField ->
                                    Box(
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (newCommentText.isEmpty()) {
                                            Text(
                                                text = "Write a comment...",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = WarmHaze
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }
                        
                        IconButton(
                            onClick = {
                                if (newCommentText.isNotBlank()) {
                                    val newComment = PostComment(
                                        id = UUID.randomUUID().toString(),
                                        authorName = "You",
                                        authorEmoji = "😊",
                                        content = newCommentText,
                                        timeAgo = "Just now"
                                    )
                                    onCommentAdded(post.id, newComment)
                                    newCommentText = ""
                                }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    if (newCommentText.isNotBlank()) GenZTeal else ChineseSilver.copy(alpha = 0.3f),
                                    CircleShape
                                ),
                            enabled = newCommentText.isNotBlank()
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Send,
                                contentDescription = "Send",
                                modifier = Modifier.size(18.dp),
                                tint = if (newCommentText.isNotBlank()) Color.White else WarmHaze
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Delete Confirmation Dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            containerColor = NeumorphLight,
            title = {
                Text(
                    text = "Delete Post?",
                    fontWeight = FontWeight.Bold,
                    color = Lead
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this post? This action cannot be undone.",
                    color = WarmHaze
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteClick()
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(
                        text = "Cancel",
                        color = WarmHaze
                    )
                }
            }
        )
    }
    
    // Fullscreen Image Viewer
    if (showImagePopup && selectedImageForPopup != null) {
        Dialog(
            onDismissRequest = { 
                showImagePopup = false 
                selectedImageForPopup = null
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { 
                        showImagePopup = false 
                        selectedImageForPopup = null
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageForPopup!!.startsWith("content://")) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(Uri.parse(selectedImageForPopup))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Full size image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    // Demo placeholder for non-URI images
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Image,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = GenZBlue
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "📷 Demo Image",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                    }
                }
                
                // Close button - top right corner
                IconButton(
                    onClick = { 
                        showImagePopup = false 
                        selectedImageForPopup = null
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        modifier = Modifier.size(28.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable


private fun CommentItem(comment: PostComment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = GenZLavender.copy(alpha = 0.2f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = comment.authorEmoji, fontSize = 14.sp)
            }
        }
        
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ChineseSilver.copy(alpha = 0.12f),
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = comment.authorName,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Lead
                    )
                    Text(
                        text = "•",
                        color = WarmHaze,
                        fontSize = 8.sp
                    )
                    Text(
                        text = comment.timeAgo,
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmHaze
                    )
                }
                Text(
                    text = comment.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = Lead
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

// ==================== CREATE POST CARD ====================

@Composable
private fun CreatePostCard(
    selectedImageUri: Uri?,
    onPostCreated: (String, Uri?) -> Unit,
    onPickImage: () -> Unit,
    onClearImage: () -> Unit
) {
    var postContent by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    val hasImage = selectedImageUri != null

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = GenZTeal.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = NeumorphLight.copy(alpha = 0.98f)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // User Avatar
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                        color = GenZTeal.copy(alpha = 0.15f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = GenZTeal
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "Create Post",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Lead
                        )
                        Text(
                            text = "Share something with the community",
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmHaze
                        )
                    }
                }

                // Expand/Collapse icon
                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = GenZTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Input field
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = if (isExpanded) 120.dp else 60.dp),
                shape = RoundedCornerShape(16.dp),
                color = ChineseSilver.copy(alpha = 0.1f)
            ) {
                BasicTextField(
                    value = postContent,
                    onValueChange = { postContent = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        color = Lead,
                        lineHeight = 22.sp
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (postContent.isEmpty()) {
                                Text(
                                    text = "What's on your mind? Share your sports moments...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = WarmHaze.copy(alpha = 0.7f)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            // Image preview if added - Now showing real image from gallery
            AnimatedVisibility(
                visible = hasImage,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = GenZBlue.copy(alpha = 0.08f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        // Display selected image from gallery
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(selectedImageUri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Selected image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Remove image button
                        IconButton(
                            onClick = onClearImage,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(32.dp)
                                .shadow(6.dp, CircleShape)
                                .background(Color.White.copy(alpha = 0.95f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Remove image",
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFFFF6B6B)
                            )
                        }
                        
                        // Image info overlay at bottom
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.6f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = GenZTeal
                                )
                                Text(
                                    text = "Photo ready to post",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add photo from gallery button
                Surface(
                    modifier = Modifier
                        .clickable { onPickImage() },
                    shape = RoundedCornerShape(14.dp),
                    color = if (hasImage) GenZTeal.copy(alpha = 0.15f) else ChineseSilver.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PhotoLibrary,
                            contentDescription = "Pick from gallery",
                            modifier = Modifier.size(20.dp),
                            tint = if (hasImage) GenZTeal else WarmHaze
                        )
                        Text(
                            text = if (hasImage) "Change Photo" else "Gallery",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = if (hasImage) GenZTeal else WarmHaze
                        )
                    }
                }

                // Post button
                Button(
                    onClick = {
                        if (postContent.isNotBlank() || hasImage) {
                            onPostCreated(postContent, selectedImageUri)
                            postContent = ""
                            isExpanded = false
                        }
                    },
                    enabled = postContent.isNotBlank() || hasImage,
                    modifier = Modifier.height(42.dp),
                    shape = RoundedCornerShape(21.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GenZTeal,
                        disabledContainerColor = ChineseSilver.copy(alpha = 0.3f)
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Send,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Post",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}

// ==================== COMMUNITY INFO DIALOG ====================

@Composable
private fun CommunityInfoDialog(
    communityName: String,
    communityEmoji: String,
    info: CommunityInfo,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = GenZTeal.copy(alpha = 0.2f)
                ),
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Header with emoji
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(GenZGradientStart, GenZGradientEnd)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(text = communityEmoji, fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = communityName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                lineHeight = 18.sp
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Info Section Title
                Text(
                    text = "Community Info",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Lead
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Creator Info
                InfoRow(
                    icon = Icons.Rounded.Person,
                    label = "Created by",
                    value = "${info.creatorEmoji} ${info.creatorName}",
                    iconColor = GenZTeal
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Created Date
                InfoRow(
                    icon = Icons.Rounded.CalendarToday,
                    label = "Created on",
                    value = info.createdDate,
                    iconColor = GenZBlue
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Location
                InfoRow(
                    icon = Icons.Rounded.LocationOn,
                    label = "Location",
                    value = info.location,
                    iconColor = GenZLavender
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Total Members
                InfoRow(
                    icon = Icons.Rounded.People,
                    label = "Total Members",
                    value = "${info.totalMembers} members",
                    iconColor = GenZCyan
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "About",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = info.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Close button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GenZTeal
                    )
                ) {
                    Text(
                        text = "Close",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    iconColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(12.dp),
            color = iconColor.copy(alpha = 0.12f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = iconColor
                )
            }
        }

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = WarmHaze
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Lead
            )
        }
    }
}

// ==================== MEMBERS DIALOG ====================

@Composable
private fun MembersDialog(
    communityName: String,
    members: List<Triple<String, String, String>>,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    // Filter members based on search query
    val filteredMembers = remember(searchQuery, members) {
        if (searchQuery.isBlank()) {
            members
        } else {
            members.filter { (name, _, role) ->
                name.contains(searchQuery, ignoreCase = true) ||
                role.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 500.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = GenZBlue.copy(alpha = 0.2f)
                ),
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Members",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Lead
                        )
                        Text(
                            text = "${members.size} members in",
                            style = MaterialTheme.typography.labelMedium,
                            color = WarmHaze
                        )
                        Text(
                            text = communityName,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GenZTeal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .background(ChineseSilver.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = WarmHaze,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Functional Search bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = ChineseSilver.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = if (searchQuery.isNotEmpty()) GenZTeal else WarmHaze
                        )
                        
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                color = Lead
                            ),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box(
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Search members...",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = WarmHaze.copy(alpha = 0.6f)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                        
                        // Clear search button
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Clear search",
                                    modifier = Modifier.size(16.dp),
                                    tint = WarmHaze
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                // Search result info
                if (searchQuery.isNotEmpty()) {
                    Text(
                        text = "${filteredMembers.size} result${if (filteredMembers.size != 1) "s" else ""} found",
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmHaze,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Members List
                if (filteredMembers.isEmpty()) {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = WarmHaze.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No members found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze
                            )
                            Text(
                                text = "Try a different search",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmHaze.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredMembers) { (name, emoji, role) ->
                            MemberItem(
                                name = name,
                                emoji = emoji,
                                role = role
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberItem(
    name: String,
    emoji: String,
    role: String
) {
    val roleColor = when (role) {
        "Admin" -> GenZTeal
        "Moderator" -> GenZBlue
        else -> WarmHaze
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ChineseSilver.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = GenZLavender.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = emoji, fontSize = 20.sp)
                }
            }

            // Name and role
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Lead
                )
                Text(
                    text = role,
                    style = MaterialTheme.typography.labelSmall,
                    color = roleColor
                )
            }

            // Role badge for Admin/Moderator
            if (role != "Member") {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = roleColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = role,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = roleColor
                    )
                }
            }
        }
    }
}

// ==================== EVENT DETAIL DIALOG ====================

@Composable
private fun FeedEventDetailDialog(
    event: CommunityEvent,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 24.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Hero Header with Gradient
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
                            .size(150.dp)
                            .offset(x = (-30).dp, y = (-30).dp)
                            .background(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .offset(x = 280.dp, y = 120.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                    )
                    
                    // Close button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close",
                                modifier = Modifier.padding(8.dp),
                                tint = Color.White
                            )
                        }
                    }
                    
                    // Large emoji
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = event.emoji,
                            fontSize = 72.sp
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Category badge
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = event.category,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
                
                // Scrollable Content
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title
                    item {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Lead,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // Date & Time
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = GenZTeal.copy(alpha = 0.08f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Date
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.CalendarToday,
                                        contentDescription = null,
                                        tint = GenZTeal,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = event.date,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = Lead
                                    )
                                }
                                
                                // Divider
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(40.dp)
                                        .background(GenZTeal.copy(alpha = 0.3f))
                                )
                                
                                // Time
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Schedule,
                                        contentDescription = null,
                                        tint = GenZTeal,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = event.time,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = Lead
                                    )
                                }
                            }
                        }
                    }
                    
                    // Location
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = GenZLavender.copy(alpha = 0.15f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Rounded.LocationOn,
                                        contentDescription = null,
                                        tint = GenZLavender,
                                        modifier = Modifier.size(22.dp)
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
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Lead
                                )
                            }
                        }
                    }
                    
                    // Organizer
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = GenZBlue.copy(alpha = 0.15f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = null,
                                        tint = GenZBlue,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "Organized by",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = WarmHaze
                                )
                                Text(
                                    text = event.organizer,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Lead
                                )
                            }
                        }
                    }
                    

                    
                    // Description
                    if (event.description.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "About Event",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Lead
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmHaze,
                                lineHeight = 22.sp
                            )
                        }
                    }
                    
                    // Highlights
                    if (event.highlights.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "✨ Highlights",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Lead
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                event.highlights.forEach { highlight ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Surface(
                                            modifier = Modifier.size(6.dp),
                                            shape = CircleShape,
                                            color = GenZTeal
                                        ) {}
                                        Text(
                                            text = highlight,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Lead
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Requirements
                    if (event.requirements.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "📋 Requirements",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Lead
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = ChineseSilver.copy(alpha = 0.08f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    event.requirements.forEach { req ->
                                        Row(
                                            verticalAlignment = Alignment.Top,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.CheckCircle,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = GenZTeal
                                            )
                                            Text(
                                                text = req,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Lead
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Bottom spacing
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                
                // Bottom Action - Close only
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GenZTeal
                            )
                        ) {
                            Text(
                                text = "Close",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
