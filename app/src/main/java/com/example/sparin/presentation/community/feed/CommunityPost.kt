package com.example.sparin.presentation.community.feed

/**
 * Data class untuk Post di komunitas
 */
data class Post(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorEmoji: String = "",
    val content: String = "",
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val likes: Int = 0,
    val likedBy: List<String> = emptyList(), // List of user IDs who liked
    val commentCount: Int = 0,
    // UI-only property (excluded from Firestore if possible, or handled manually)
    var isLikedByCurrentUser: Boolean = false,
    // Comments will be fetched separately or limited list
    val comments: List<Comment> = emptyList()
)

/**
 * Data class untuk Comment
 */
data class Comment(
    val id: String = "",
    val postId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorEmoji: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val likes: Int = 0
)

/**
 * State holder untuk feed
 */
data class FeedState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
