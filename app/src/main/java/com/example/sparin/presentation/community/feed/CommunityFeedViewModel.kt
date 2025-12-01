package com.example.sparin.presentation.community.feed

import androidx.compose.runtime.*
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.repository.FeedRepository
import kotlinx.coroutines.launch

class CommunityFeedViewModel(
    private val feedRepository: FeedRepository,
    private val authRepository: com.example.sparin.data.repository.AuthRepository
) : ViewModel() {

    var feedState by mutableStateOf(FeedState())
        private set

    private var currentCommunityId: String = ""

    fun loadCommunityFeed(communityId: String) {
        currentCommunityId = communityId
        feedState = feedState.copy(isLoading = true)
        
        viewModelScope.launch {
            val currentUserId = authRepository.getCurrentUserId() ?: return@launch
            
            when (val result = feedRepository.getPosts(communityId, currentUserId)) {
                is com.example.sparin.domain.util.Resource.Success -> {
                    feedState = feedState.copy(
                        posts = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is com.example.sparin.domain.util.Resource.Error -> {
                    feedState = feedState.copy(isLoading = false)
                    // Handle error (maybe add error message to state)
                }
                else -> {
                    feedState = feedState.copy(isLoading = false)
                }
            }
        }
    }

    fun createPost(content: String, imageUrl: String?) {
        if (currentCommunityId.isEmpty()) return
        
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser() ?: return@launch
            
            val newPost = Post(
                authorId = currentUser.uid,
                authorName = currentUser.name ?: "User",
                authorEmoji = "👤", // TODO: Get user emoji/avatar
                content = content,
                imageUrl = imageUrl,
                timestamp = System.currentTimeMillis()
            )
            
            // Optimistic update
            feedState = feedState.copy(
                posts = listOf(newPost.copy(id = "temp_${System.currentTimeMillis()}")) + feedState.posts
            )
            
            Log.d("CommunityFeedVM", "Creating post for community: $currentCommunityId")
            
            when (val result = feedRepository.createPost(currentCommunityId, newPost)) {
                is com.example.sparin.domain.util.Resource.Success -> {
                    Log.d("CommunityFeedVM", "Post created successfully: ${result.data}")
                    // Reload feed to ensure we have the latest data and correct ordering
                    loadCommunityFeed(currentCommunityId)
                }
                is com.example.sparin.domain.util.Resource.Error -> {
                    Log.e("CommunityFeedVM", "Failed to create post: ${result.message}")
                    // Revert optimistic update on error
                    feedState = feedState.copy(
                        posts = feedState.posts.filter { !it.id.startsWith("temp_") },
                        error = result.message // Assuming FeedState has an error field, if not I'll check
                    )
                }
                else -> {}
            }
        }
    }

    fun addComment(postId: String, commentContent: String) {
        if (currentCommunityId.isEmpty()) return

        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser() ?: return@launch

            val newComment = Comment(
                postId = postId,
                authorId = currentUser.uid,
                authorName = currentUser.name ?: "User",
                authorEmoji = "👤",
                content = commentContent,
                timestamp = System.currentTimeMillis()
            )

            // Optimistic update (add to local list of the specific post)
            feedState = feedState.copy(
                posts = feedState.posts.map { post ->
                    if (post.id == postId) {
                        post.copy(
                            comments = post.comments + newComment,
                            commentCount = post.commentCount + 1
                        )
                    } else {
                        post
                    }
                }
            )
            
            feedRepository.addComment(currentCommunityId, postId, newComment)
            // Error handling for comment failure could be added here (revert)
        }
    }

    fun toggleLike(postId: String) {
        if (currentCommunityId.isEmpty()) return
        
        viewModelScope.launch {
            val currentUserId = authRepository.getCurrentUserId() ?: return@launch

            // Optimistic update
            feedState = feedState.copy(
                posts = feedState.posts.map { post ->
                    if (post.id == postId) {
                        val isLiked = !post.isLikedByCurrentUser
                        post.copy(
                            isLikedByCurrentUser = isLiked,
                            likes = if (isLiked) post.likes + 1 else post.likes - 1
                        )
                    } else {
                        post
                    }
                }
            )
            
            feedRepository.toggleLike(currentCommunityId, postId, currentUserId)
        }
    }
}
