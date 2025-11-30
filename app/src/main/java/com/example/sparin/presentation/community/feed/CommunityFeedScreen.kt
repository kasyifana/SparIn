package com.example.sparin.presentation.community.feed

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.sparin.ui.theme.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Community Feed Screen - untuk membuat dan melihat postingan
 */
import org.koin.androidx.compose.koinViewModel

/**
 * Community Feed Screen - untuk membuat dan melihat postingan
 */
@Composable
fun CommunityFeedScreen(
    navController: NavHostController,
    communityId: String,
    communityName: String,
    communityEmoji: String,
    viewModel: CommunityFeedViewModel = koinViewModel()
) {
    // Decode parameters
    val decodedName = remember {
        try {
            URLDecoder.decode(communityName, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            communityName
        }
    }
    
    val decodedEmoji = remember {
        try {
            URLDecoder.decode(communityEmoji, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            communityEmoji
        }
    }
    
    val feedState = viewModel.feedState
    var showCreatePost by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(communityId) {
        viewModel.loadCommunityFeed(communityId)
    }

    // Show error message if any
    LaunchedEffect(viewModel.feedState.error) {
        viewModel.feedState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black,
                                Color.DarkGray
                            )
                        )
                    )
            )

            // Background blobs
            FeedBackgroundBlobs()

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                FeedHeader(
                    communityName = decodedName,
                    communityEmoji = decodedEmoji,
                    onBackClick = { navController.navigateUp() }
                )

                // Posts List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(feedState.posts) { post ->
                        PostCard(
                            post = post,
                            onLikeClick = { viewModel.toggleLike(post.id) },
                            onCommentSubmit = { comment ->
                                viewModel.addComment(post.id, comment)
                            }
                        )
                    }
                    
                    // Bottom spacer for FAB
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
            
            // Floating Create Post Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                // Glow effect
                Box(
                    modifier = Modifier
                        .size(70.dp)
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
                
                Surface(
                    modifier = Modifier
                        .size(64.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = CircleShape,
                            ambientColor = Crunch.copy(alpha = 0.4f),
                            spotColor = Crunch.copy(alpha = 0.4f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { showCreatePost = true }
                        ),
                    shape = CircleShape,
                    color = Crunch
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Create post",
                            modifier = Modifier.size(28.dp),
                            tint = Lead
                        )
                    }
                }
            }

            // Create Post Dialog
            if (showCreatePost) {
                CreatePostDialog(
                    onDismiss = { showCreatePost = false },
                    onPostCreated = { content, imageUrl ->
                        viewModel.createPost(content, imageUrl)
                        showCreatePost = false
                    }
                )
            }
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
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )
    
    val offset2 by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(120.dp)
    ) {
        // Peach glow top left
        drawCircle(
            color = PeachGlow.copy(alpha = 0.18f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.15f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 40f,
                y = size.height * 0.12f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )

        // Mint breeze bottom right
        drawCircle(
            color = MintBreeze.copy(alpha = 0.15f),
            radius = 150f,
            center = Offset(
                x = size.width * 0.85f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 35f,
                y = size.height * 0.75f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 40f
            )
        )
        
        // Sky mist center
        drawCircle(
            color = SkyMist.copy(alpha = 0.12f),
            radius = 130f,
            center = Offset(
                x = size.width * 0.5f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 30f,
                y = size.height * 0.45f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 25f
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
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                ambientColor = NeumorphDark.copy(alpha = 0.08f)
            ),
        color = NeumorphLight.copy(alpha = 0.98f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button with neumorphic effect
            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = NeumorphDark.copy(alpha = 0.15f),
                        spotColor = NeumorphDark.copy(alpha = 0.15f)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBackClick
                    ),
                shape = CircleShape,
                color = NeumorphLight
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(22.dp),
                        tint = Lead
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Community Icon with gradient
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Crunch.copy(alpha = 0.2f)
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Crunch.copy(alpha = 0.25f),
                                MintBreeze.copy(alpha = 0.25f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = communityEmoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Community Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = communityName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    ),
                    color = Lead,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(MintBreeze, Crunch)
                                ),
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = "Community Feed",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = WarmHaze
                    )
                }
            }

            // Info Button
            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = NeumorphDark.copy(alpha = 0.15f)
                    ),
                shape = CircleShape,
                color = NeumorphLight
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = "Info",
                        modifier = Modifier.size(21.dp),
                        tint = WarmHaze
                    )
                }
            }
        }
    }
}

// ==================== POST CARD ====================

@Composable
private fun PostCard(
    post: Post,
    onLikeClick: () -> Unit,
    onCommentSubmit: (String) -> Unit
) {
    var showComments by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.12f),
                spotColor = NeumorphDark.copy(alpha = 0.12f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = NeumorphLight
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Author Info with better spacing
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with gradient and shadow
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = CircleShape,
                            ambientColor = PeachGlow.copy(alpha = 0.3f)
                        )
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    PeachGlow.copy(alpha = 0.5f),
                                    MintBreeze.copy(alpha = 0.5f),
                                    SkyMist.copy(alpha = 0.4f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = post.authorEmoji, fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = Lead
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = formatTimestamp(post.timestamp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 13.sp
                        ),
                        color = WarmHaze.copy(alpha = 0.8f)
                    )
                }

                // More Button with neumorphic style
                Surface(
                    modifier = Modifier
                        .size(36.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            ambientColor = NeumorphDark.copy(alpha = 0.08f)
                        ),
                    shape = CircleShape,
                    color = Dreamland.copy(alpha = 0.4f)
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "More",
                            tint = WarmHaze,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Post Content with better typography
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                ),
                color = Lead
            )

            // Post Image with better styling
            post.imageUrl?.let { imageUrl ->
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = NeumorphDark.copy(alpha = 0.1f)
                        ),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.Transparent
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Post image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(18.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons with improved design
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Like Button with animation
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(
                            elevation = if (post.isLikedByCurrentUser) 8.dp else 6.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = if (post.isLikedByCurrentUser)
                                Crunch.copy(alpha = 0.25f)
                            else
                                NeumorphDark.copy(alpha = 0.1f),
                            spotColor = if (post.isLikedByCurrentUser)
                                Crunch.copy(alpha = 0.25f)
                            else
                                NeumorphDark.copy(alpha = 0.1f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onLikeClick
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = if (post.isLikedByCurrentUser)
                        Crunch.copy(alpha = 0.3f)
                    else
                        Dreamland.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (post.isLikedByCurrentUser)
                                Icons.Rounded.Favorite
                            else
                                Icons.Rounded.FavoriteBorder,
                            contentDescription = "Like",
                            modifier = Modifier.size(20.dp),
                            tint = if (post.isLikedByCurrentUser) Crunch else WarmHaze
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = post.likes.toString(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (post.isLikedByCurrentUser) Crunch else Lead
                        )
                    }
                }

                // Comment Button
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = NeumorphDark.copy(alpha = 0.1f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { showComments = !showComments }
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = Dreamland.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ChatBubbleOutline,
                            contentDescription = "Comment",
                            modifier = Modifier.size(20.dp),
                            tint = WarmHaze
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = post.comments.size.toString(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Lead
                        )
                    }
                }

                // Share Button
                Surface(
                    modifier = Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = NeumorphDark.copy(alpha = 0.1f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { }
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = Dreamland.copy(alpha = 0.4f)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(20.dp),
                            tint = WarmHaze
                        )
                    }
                }
            }

            // Comments Section with improved styling
            AnimatedVisibility(
                visible = showComments,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                ) {
                    HorizontalDivider(
                        color = Dreamland.copy(alpha = 0.6f),
                        thickness = 1.5.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Comments List
                    if (post.comments.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            post.comments.forEach { comment ->
                                CommentItem(comment = comment)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Add Comment Input with premium design
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    ambientColor = NeumorphDark.copy(alpha = 0.08f)
                                ),
                            shape = RoundedCornerShape(20.dp),
                            color = Dreamland.copy(alpha = 0.3f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                BasicTextField(
                                    value = commentText,
                                    onValueChange = { commentText = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                                        color = Lead,
                                        fontSize = 14.sp
                                    ),
                                    decorationBox = { innerTextField ->
                                        if (commentText.isEmpty()) {
                                            Text(
                                                text = "Tulis komentar...",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 14.sp
                                                ),
                                                color = WarmHaze.copy(alpha = 0.6f)
                                            )
                                        }
                                        innerTextField()
                                    }
                                )
                            }
                        }

                        // Send Comment Button with glow
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                Crunch.copy(alpha = 0.25f),
                                                Crunch.copy(alpha = 0f)
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                            )
                            
                            Surface(
                                modifier = Modifier
                                    .size(42.dp)
                                    .align(Alignment.Center)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = CircleShape,
                                        ambientColor = Crunch.copy(alpha = 0.3f),
                                        spotColor = Crunch.copy(alpha = 0.3f)
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            if (commentText.isNotBlank()) {
                                                onCommentSubmit(commentText)
                                                commentText = ""
                                            }
                                        }
                                    ),
                                shape = CircleShape,
                                color = Crunch
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.Send,
                                        contentDescription = "Send",
                                        modifier = Modifier.size(19.dp),
                                        tint = Lead
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== COMMENT ITEM ====================

@Composable
private fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar with better gradient
        Box(
            modifier = Modifier
                .size(36.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    ambientColor = PeachGlow.copy(alpha = 0.2f)
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            PeachGlow.copy(alpha = 0.4f),
                            MintBreeze.copy(alpha = 0.4f),
                            SkyMist.copy(alpha = 0.3f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = comment.authorEmoji, fontSize = 16.sp)
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Dreamland.copy(alpha = 0.3f),
                modifier = Modifier.shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = NeumorphDark.copy(alpha = 0.06f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = comment.authorName,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = Lead
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = comment.content,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        ),
                        color = Lead.copy(alpha = 0.9f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.padding(start = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTimestamp(comment.timestamp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 12.sp
                    ),
                    color = WarmHaze.copy(alpha = 0.7f)
                )
                Text(
                    text = "•",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmHaze.copy(alpha = 0.5f)
                )
                Text(
                    text = "Balas",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    ),
                    color = WarmHaze,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { }
                    )
                )
            }
        }
    }
}

// ==================== CREATE POST DIALOG ====================

@Composable
private fun CreatePostDialog(
    onDismiss: () -> Unit,
    onPostCreated: (String, String?) -> Unit
) {
    var postText by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NeumorphLight,
        shape = RoundedCornerShape(28.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Crunch.copy(alpha = 0.3f),
                                    PeachGlow.copy(alpha = 0.3f)
                                )
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Create,
                        contentDescription = null,
                        tint = Crunch,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Text(
                    text = "Buat Postingan",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = Lead
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Text Input with better design
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = NeumorphDark.copy(alpha = 0.1f)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    color = Dreamland.copy(alpha = 0.35f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        BasicTextField(
                            value = postText,
                            onValueChange = { postText = it },
                            modifier = Modifier.fillMaxSize(),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = Lead,
                                fontSize = 15.sp,
                                lineHeight = 22.sp
                            ),
                            decorationBox = { innerTextField ->
                                if (postText.isEmpty()) {
                                    Text(
                                        text = "Apa yang ingin kamu bagikan?",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = 15.sp
                                        ),
                                        color = WarmHaze.copy(alpha = 0.6f)
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                // Image Preview with better styling
                AnimatedVisibility(
                    visible = selectedImageUri != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    selectedImageUri?.let { uri ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .shadow(
                                    elevation = 6.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    ambientColor = NeumorphDark.copy(alpha = 0.1f)
                                ),
                            shape = RoundedCornerShape(20.dp),
                            color = Color.Transparent
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Selected image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(20.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(10.dp)
                                        .size(36.dp)
                                        .shadow(
                                            elevation = 6.dp,
                                            shape = CircleShape
                                        )
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = { selectedImageUri = null }
                                        ),
                                    shape = CircleShape,
                                    color = Lead.copy(alpha = 0.9f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = "Remove",
                                            modifier = Modifier.size(20.dp),
                                            tint = CascadingWhite
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Add Image Button with premium design
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = NeumorphDark.copy(alpha = 0.1f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { imagePickerLauncher.launch("image/*") }
                        ),
                    shape = RoundedCornerShape(18.dp),
                    color = Dreamland.copy(alpha = 0.35f)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            PeachGlow.copy(alpha = 0.3f),
                                            MintBreeze.copy(alpha = 0.3f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Image,
                                contentDescription = "Add image",
                                tint = WarmHaze,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Text(
                            text = "Tambah Gambar",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            ),
                            color = Lead
                        )
                    }
                }
            }
        },
        confirmButton = {
            // Posting Button with glow effect
            Box {
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 50.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Crunch.copy(alpha = 0.3f),
                                    Crunch.copy(alpha = 0f)
                                )
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                )
                
                Surface(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = Crunch.copy(alpha = 0.35f),
                            spotColor = Crunch.copy(alpha = 0.35f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (postText.isNotBlank()) {
                                    onPostCreated(postText, selectedImageUri?.toString())
                                }
                            }
                        ),
                    shape = RoundedCornerShape(18.dp),
                    color = Crunch
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Posting",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = Lead
                        )
                    }
                }
            }
        },
        dismissButton = {
            Surface(
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(18.dp),
                        ambientColor = NeumorphDark.copy(alpha = 0.1f)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss
                    ),
                shape = RoundedCornerShape(18.dp),
                color = Dreamland
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Batal",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        ),
                        color = WarmHaze
                    )
                }
            }
        }
    )
}

// ==================== HELPER FUNCTIONS ====================

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60000 -> "Baru saja"
        diff < 3600000 -> "${diff / 60000} menit yang lalu"
        diff < 86400000 -> "${diff / 3600000} jam yang lalu"
        diff < 604800000 -> "${diff / 86400000} hari yang lalu"
        else -> SimpleDateFormat("dd MMM yyyy", Locale.Builder().setLanguage("id").setRegion("ID").build()).format(Date(timestamp))
    }
}
