package com.example.sparin.presentation.community.feed

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sparin.ui.theme.*
import org.koin.androidx.compose.koinViewModel

/**
 * CommunityFeedScreen - Community Feed/Chat Screen
 * Displays posts and comments for a specific community
 * Features: Post list, add comment, like posts
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityFeedScreen(
    navController: NavHostController,
    communityId: String,
    communityName: String,
    communityEmoji: String,
    viewModel: CommunityFeedViewModel = koinViewModel()
) {
    var commentText by remember { mutableStateOf("") }
    
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
            // Header
            CommunityFeedHeader(
                communityName = communityName,
                communityEmoji = communityEmoji,
                onBackClick = { navController.popBackStack() }
            )

            // Feed Content
            if (feedState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Crunch)
                }
            } else if (feedState.posts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "No posts yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = Lead
                        )
                        Text(
                            text = "Be the first to post!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmHaze
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(feedState.posts) { post ->
                        CommunityPostCard(
                            post = post,
                            onLikeClick = { viewModel.toggleLike(post.id) },
                            onCommentClick = { /* Handle comment click */ }
                        )
                    }
                }
            }

            // Input Area
            CommunityFeedInputBar(
                commentText = commentText,
                onCommentChange = { commentText = it },
                onSendClick = {
                    if (commentText.isNotBlank()) {
                        viewModel.createPost(commentText, null)
                        commentText = ""
                    }
                }
            )
        }
    }
}

@Composable
private fun CommunityFeedHeader(
    communityName: String,
    communityEmoji: String,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        color = ChineseSilver.copy(alpha = 0.5f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            ChineseSilver.copy(alpha = 0.6f),
                            ChineseSilver.copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = Lead,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
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
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Lead
                        )
                    }
                }

                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    }
}

@Composable
private fun CommunityPostCard(
    post: Post,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(12.dp),
        color = NeumorphLight.copy(alpha = 0.95f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Author info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = post.authorEmoji,
                    fontSize = 24.sp
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Lead
                    )
                    Text(
                        text = "Just now",
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmHaze
                    )
                }
            }

            // Post content
            if (post.content.isNotEmpty()) {
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Lead,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Post image if exists
            if (!post.imageUrl.isNullOrEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Dreamland.copy(alpha = 0.1f)
                ) {
                    // Image placeholder - in real app, use AsyncImage with Coil
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Crunch, PeachGlow)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🖼️",
                            fontSize = 32.sp
                        )
                    }
                }
            }

            // Engagement stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "❤️ ${post.likes}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Crunch
                    )
                    Text(
                        text = "💬 ${post.commentCount}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SkyMist
                    )
                }
            }

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(onClick = onLikeClick) {
                    Text(
                        text = if (post.isLikedByCurrentUser) "❤️ Liked" else "🤍 Like",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (post.isLikedByCurrentUser) Crunch else WarmHaze
                    )
                }
                TextButton(onClick = onCommentClick) {
                    Text(
                        text = "💬 Comment",
                        style = MaterialTheme.typography.labelSmall,
                        color = SkyMist
                    )
                }
            }
        }
    }
}

@Composable
private fun CommunityFeedInputBar(
    commentText: String,
    onCommentChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp),
        color = NeumorphLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = commentText,
                onValueChange = onCommentChange,
                placeholder = {
                    Text(
                        "Share your thoughts...",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmHaze
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                textStyle = MaterialTheme.typography.bodySmall,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Dreamland.copy(alpha = 0.1f),
                    unfocusedContainerColor = Dreamland.copy(alpha = 0.05f),
                    focusedIndicatorColor = Crunch,
                    unfocusedIndicatorColor = Transparent,
                    focusedTextColor = Lead,
                    unfocusedTextColor = Lead
                ),
                shape = RoundedCornerShape(12.dp)
            )

            IconButton(
                onClick = onSendClick,
                modifier = Modifier.size(40.dp),
                enabled = commentText.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "Send",
                    tint = if (commentText.isNotBlank()) Crunch else WarmHaze,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private val Transparent = androidx.compose.ui.graphics.Color.Transparent
