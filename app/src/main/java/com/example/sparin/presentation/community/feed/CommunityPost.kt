package com.example.sparin.presentation.community.feed

/**
 * Data class untuk Post di komunitas
 */
data class Post(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorEmoji: String,
    val content: String,
    val imageUrl: String? = null,
    val timestamp: Long,
    val likes: Int = 0,
    val isLikedByCurrentUser: Boolean = false,
    val comments: List<Comment> = emptyList()
)

/**
 * Data class untuk Comment
 */
data class Comment(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorEmoji: String,
    val content: String,
    val timestamp: Long,
    val likes: Int = 0
)

/**
 * State holder untuk feed
 */
data class FeedState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false
)
