package com.example.sparin.presentation.community.feed

import com.google.firebase.Timestamp

/**
 * Data class for Post in community feed
 */
data class Post(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String = "",
    val content: String = "",
    val imageUrls: List<String> = emptyList(),
    val likes: Int = 0,
    val likedBy: List<String> = emptyList(),
    val commentCount: Int = 0,
    val comments: List<Comment> = emptyList(),
    val timestamp: Timestamp = Timestamp.now(),
    val isLikedByCurrentUser: Boolean = false
)

/**
 * Data class for Comment on a post
 */
data class Comment(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val likes: Int = 0
)
