package com.example.sparin.presentation.community.feed

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class CommunityFeedViewModel : ViewModel() {

    var feedState by mutableStateOf(FeedState())
        private set

    fun loadCommunityFeed(communityName: String) {
        // Sample data - nanti bisa diganti dengan API call
        feedState = FeedState(
            posts = listOf(
                Post(
                    id = "1",
                    authorId = "user1",
                    authorName = "Andi Pratama",
                    authorEmoji = "🏸",
                    content = "Hari ini latihan badminton seru banget! Ada yang mau ikut besok pagi?",
                    imageUrl = null,
                    timestamp = System.currentTimeMillis() - 3600000,
                    likes = 12,
                    isLikedByCurrentUser = true,
                    comments = listOf(
                        Comment(
                            id = "c1",
                            authorId = "user2",
                            authorName = "Budi",
                            authorEmoji = "⚽",
                            content = "Wah seru! Jam berapa?",
                            timestamp = System.currentTimeMillis() - 3000000,
                            likes = 3
                        ),
                        Comment(
                            id = "c2",
                            authorId = "user1",
                            authorName = "Andi Pratama",
                            authorEmoji = "🏸",
                            content = "Jam 7 pagi di GOR!",
                            timestamp = System.currentTimeMillis() - 2700000,
                            likes = 1
                        )
                    )
                ),
                Post(
                    id = "2",
                    authorId = "user3",
                    authorName = "Siti Nurhaliza",
                    authorEmoji = "🎾",
                    content = "Tips untuk pemula yang baru mulai main badminton?",
                    imageUrl = null,
                    timestamp = System.currentTimeMillis() - 7200000,
                    likes = 8,
                    isLikedByCurrentUser = false,
                    comments = listOf(
                        Comment(
                            id = "c3",
                            authorId = "user4",
                            authorName = "Rudi",
                            authorEmoji = "🏀",
                            content = "Latihan footwork dulu, itu yang paling penting!",
                            timestamp = System.currentTimeMillis() - 6000000,
                            likes = 5
                        )
                    )
                ),
                Post(
                    id = "3",
                    authorId = "user5",
                    authorName = "Joko Widodo",
                    authorEmoji = "⚽",
                    content = "Turnamen bulan depan siapa yang ikut? Yuk daftar bareng!",
                    imageUrl = null,
                    timestamp = System.currentTimeMillis() - 10800000,
                    likes = 15,
                    isLikedByCurrentUser = false,
                    comments = emptyList()
                )
            )
        )
    }

    fun createPost(content: String, imageUrl: String?) {
        val newPost = Post(
            id = "new_${System.currentTimeMillis()}",
            authorId = "current_user",
            authorName = "You",
            authorEmoji = "😊",
            content = content,
            imageUrl = imageUrl,
            timestamp = System.currentTimeMillis(),
            likes = 0,
            isLikedByCurrentUser = false,
            comments = emptyList()
        )
        
        feedState = feedState.copy(
            posts = listOf(newPost) + feedState.posts
        )
    }

    fun addComment(postId: String, commentContent: String) {
        val newComment = Comment(
            id = "comment_${System.currentTimeMillis()}",
            authorId = "current_user",
            authorName = "You",
            authorEmoji = "😊",
            content = commentContent,
            timestamp = System.currentTimeMillis(),
            likes = 0
        )

        feedState = feedState.copy(
            posts = feedState.posts.map { post ->
                if (post.id == postId) {
                    post.copy(comments = post.comments + newComment)
                } else {
                    post
                }
            }
        )
    }

    fun toggleLike(postId: String) {
        feedState = feedState.copy(
            posts = feedState.posts.map { post ->
                if (post.id == postId) {
                    post.copy(
                        isLikedByCurrentUser = !post.isLikedByCurrentUser,
                        likes = if (post.isLikedByCurrentUser) post.likes - 1 else post.likes + 1
                    )
                } else {
                    post
                }
            }
        )
    }
}
