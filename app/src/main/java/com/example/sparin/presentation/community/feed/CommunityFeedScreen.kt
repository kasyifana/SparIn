package com.example.sparin.presentation.community.feed

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.sparin.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * CommunityFeedScreen - Sport-Tech Premium Design
 * 
 * Professional feed/posts screen for community
 * Features:
 * - Clean vertical feed with thin-bordered floating cards
 * - Cropped photo thumbnails (tap for fullscreen)
 * - Minimal outline icons for actions
 * - Premium Create Post FAB
 */

// ==================== MAIN SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityFeedScreen(
    navController: NavHostController,
    communityId: String,
    communityName: String,
    communityEmoji: String,
    viewModel: CommunityFeedViewModel = koinViewModel()
) {
    var postText by remember { mutableStateOf("") }
    var showImageViewer by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(communityId) {
        viewModel.loadCommunityFeed(communityId)
    }
    
    val feedState = viewModel.feedState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CascadingWhite)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Premium Header
            FeedHeader(
                communityName = communityName,
                communityEmoji = communityEmoji,
                onBackClick = { navController.popBackStack() }
            )
            
            // Feed Content
            when {
                feedState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Crunch,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                feedState.posts.isEmpty() -> {
                    EmptyFeedState(modifier = Modifier.weight(1f))
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(feedState.posts) { post ->
                            PostCard(
                                post = post,
                                onLikeClick = { viewModel.toggleLike(post.id) },
                                onCommentClick = { /* Navigate to comments */ },
                                onImageClick = { url -> showImageViewer = url }
                            )
                        }
                        
                        item { 
                            Spacer(modifier = Modifier.height(80.dp)) 
                        }
                    }
                }
            }
            
            // Input Bar
            PostInputBar(
                text = postText,
                onTextChange = { postText = it },
                onSendClick = {
                    if (postText.isNotBlank()) {
                        viewModel.createPost(postText, null)
                        postText = ""
                    }
                }
            )
        }
        
        // Floating Create Post Button
        FloatingPostButton(
            onClick = { /* Show create post modal */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 80.dp)
        )
    }
    
    // Fullscreen Image Viewer
    showImageViewer?.let { url ->
        ImageViewerDialog(
            imageUrl = url,
            onDismiss = { showImageViewer = null }
        )
    }
}

// ==================== FEED HEADER ====================

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
                elevation = 2.dp,
                spotColor = MistGray.copy(alpha = 0.1f)
            ),
        color = IceWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = NeutralInk,
                    modifier = Modifier.size(22.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Community info
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Emoji badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = SmokySilver.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = communityEmoji, fontSize = 20.sp)
                }
                
                Column {
                    Text(
                        text = communityName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NeutralInk,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Community Feed",
                        style = MaterialTheme.typography.labelSmall,
                        color = TitaniumGray
                    )
                }
            }
            
            // Options button
            IconButton(onClick = { /* Show options */ }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Options",
                    tint = TitaniumGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ==================== POST CARD ====================

@Composable
private fun PostCard(
    post: Post,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onImageClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = MistGray.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = IceWhite,
        border = BorderStroke(1.dp, ShadowMist.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Author row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Author avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = SmokySilver.copy(alpha = 0.6f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (post.authorEmoji.isNotEmpty()) {
                        Text(text = post.authorEmoji, fontSize = 18.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = TitaniumGray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralInk
                    )
                    Text(
                        text = formatPostTime(post.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = TitaniumGray
                    )
                }
                
                // More options
                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = "More",
                    modifier = Modifier.size(18.dp),
                    tint = TitaniumGray
                )
            }
            
            // Post content
            if (post.content.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = NeutralInk,
                    lineHeight = 22.sp
                )
            }
            
            // Post image (cropped thumbnail)
            if (!post.imageUrl.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable { onImageClick(post.imageUrl) },
                    shape = RoundedCornerShape(12.dp),
                    color = SmokySilver.copy(alpha = 0.3f)
                ) {
                    AsyncImage(
                        model = post.imageUrl,
                        contentDescription = "Post image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(14.dp))
            
            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ShadowMist.copy(alpha = 0.5f))
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Stats and actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stats
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Like count
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (post.isLikedByCurrentUser) 
                                Icons.Rounded.Favorite 
                            else 
                                Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (post.isLikedByCurrentUser) BasketCoral else TitaniumGray
                        )
                        Text(
                            text = "${post.likes}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (post.isLikedByCurrentUser) BasketCoral else TitaniumGray
                        )
                    }
                    
                    // Comment count
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = TitaniumGray
                        )
                        Text(
                            text = "${post.commentCount}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TitaniumGray
                        )
                    }
                }
                
                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Like button
                    Surface(
                        onClick = onLikeClick,
                        shape = RoundedCornerShape(8.dp),
                        color = if (post.isLikedByCurrentUser) 
                            BasketCoral.copy(alpha = 0.1f) 
                        else 
                            SmokySilver.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (post.isLikedByCurrentUser)
                                    Icons.Rounded.Favorite
                                else
                                    Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like",
                                modifier = Modifier.size(14.dp),
                                tint = if (post.isLikedByCurrentUser) BasketCoral else TitaniumGray
                            )
                            Text(
                                text = if (post.isLikedByCurrentUser) "Liked" else "Like",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = if (post.isLikedByCurrentUser) BasketCoral else TitaniumGray
                            )
                        }
                    }
                    
                    // Comment button
                    Surface(
                        onClick = onCommentClick,
                        shape = RoundedCornerShape(8.dp),
                        color = SmokySilver.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = "Comment",
                                modifier = Modifier.size(14.dp),
                                tint = TitaniumGray
                            )
                            Text(
                                text = "Comment",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = TitaniumGray
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== POST INPUT BAR ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = IceWhite,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Text field
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(22.dp),
                color = SmokySilver.copy(alpha = 0.4f),
                border = BorderStroke(1.dp, ShadowMist.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = "Share your thoughts...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TitaniumGray
                        )
                    }
                    androidx.compose.foundation.text.BasicTextField(
                        value = text,
                        onValueChange = onTextChange,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = NeutralInk
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Send button
            val isEnabled = text.isNotBlank()
            Surface(
                onClick = { if (isEnabled) onSendClick() },
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = if (isEnabled) NeutralInk else SmokySilver.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(20.dp),
                        tint = if (isEnabled) IceWhite else TitaniumGray
                    )
                }
            }
        }
    }
}

// ==================== FLOATING POST BUTTON ====================

@Composable
private fun FloatingPostButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Subtle glow
        Box(
            modifier = Modifier
                .size(56.dp)
                .graphicsLayer { alpha = 0.25f }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Crunch, Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
        
        Surface(
            onClick = onClick,
            modifier = Modifier
                .size(52.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape,
                    spotColor = Crunch.copy(alpha = 0.3f)
                ),
            shape = CircleShape,
            color = NeutralInk
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Create Post",
                    modifier = Modifier.size(22.dp),
                    tint = IceWhite
                )
            }
        }
        
        // Gold accent ring
        Box(
            modifier = Modifier
                .size(52.dp)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Crunch, GoldenAmber)
                    ),
                    shape = CircleShape
                )
        )
    }
}

// ==================== EMPTY FEED STATE ====================

@Composable
private fun EmptyFeedState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        color = SmokySilver.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Article,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = TitaniumGray
                )
            }
            
            Text(
                text = "No posts yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = NeutralInk
            )
            
            Text(
                text = "Be the first to share something!",
                style = MaterialTheme.typography.bodyMedium,
                color = TitaniumGray
            )
        }
    }
}

// ==================== IMAGE VIEWER DIALOG ====================

@Composable
private fun ImageViewerDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralInk.copy(alpha = 0.95f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            // Close button
            Surface(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .size(44.dp),
                shape = CircleShape,
                color = IceWhite.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, IceWhite.copy(alpha = 0.25f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = IceWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            // Image
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                color = SmokySilver.copy(alpha = 0.1f)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Full image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

// ==================== HELPER FUNCTIONS ====================

private fun formatPostTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        diff < 604800000 -> "${diff / 86400000}d ago"
        else -> {
            val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}
