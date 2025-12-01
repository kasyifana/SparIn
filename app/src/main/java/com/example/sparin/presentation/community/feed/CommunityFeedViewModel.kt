package com.example.sparin.presentation.community.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparin.data.repository.AuthRepository
import com.example.sparin.data.repository.FeedRepository
import com.example.sparin.domain.util.Resource
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Community Feed Screen
 * Manages posts, likes, comments within a community
 */
class CommunityFeedViewModel(
    private val feedRepository: FeedRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // State for posts
    private val _postsState = MutableStateFlow<PostsState>(PostsState.Loading)
    val postsState: StateFlow<PostsState> = _postsState.asStateFlow()

    // State for creating post
    private val _createPostState = MutableStateFlow<CreatePostState>(CreatePostState.Idle)
    val createPostState: StateFlow<CreatePostState> = _createPostState.asStateFlow()

    // Current community ID
    private var currentCommunityId: String = ""

    // Current user ID
    private val currentUserId: String?
        get() = authRepository.getCurrentUserId()

    /**
     * Load posts for a community
     */
    fun loadPosts(communityId: String) {
        currentCommunityId = communityId
        viewModelScope.launch {
            _postsState.value = PostsState.Loading
            
            val userId = currentUserId ?: ""
            
            when (val result = feedRepository.getPosts(communityId, userId)) {
                is Resource.Success -> {
                    _postsState.value = PostsState.Success(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _postsState.value = PostsState.Error(result.message ?: "Failed to load posts")
                }
                is Resource.Loading -> {
                    _postsState.value = PostsState.Loading
                }
            }
        }
    }

    /**
     * Refresh posts
     */
    fun refreshPosts() {
        if (currentCommunityId.isNotEmpty()) {
            loadPosts(currentCommunityId)
        }
    }

    /**
     * Create a new post
     */
    fun createPost(content: String, imageUrls: List<String> = emptyList()) {
        viewModelScope.launch {
            _createPostState.value = CreatePostState.Loading

            val userId = currentUserId
            if (userId == null) {
                _createPostState.value = CreatePostState.Error("Not logged in")
                return@launch
            }

            // Get user info
            val userName = authRepository.getCurrentUserName() ?: "Anonymous"
            val userPhoto = authRepository.getCurrentUserPhotoUrl() ?: ""

            val post = Post(
                authorId = userId,
                authorName = userName,
                authorPhotoUrl = userPhoto,
                content = content,
                imageUrls = imageUrls,
                timestamp = Timestamp.now()
            )

            when (val result = feedRepository.createPost(currentCommunityId, post)) {
                is Resource.Success -> {
                    _createPostState.value = CreatePostState.Success
                    refreshPosts()
                }
                is Resource.Error -> {
                    _createPostState.value = CreatePostState.Error(result.message ?: "Failed to create post")
                }
                is Resource.Loading -> {
                    _createPostState.value = CreatePostState.Loading
                }
            }
        }
    }

    /**
     * Reset create post state
     */
    fun resetCreatePostState() {
        _createPostState.value = CreatePostState.Idle
    }

    /**
     * Toggle like on a post
     */
    fun toggleLike(postId: String) {
        viewModelScope.launch {
            val userId = currentUserId ?: return@launch
            
            when (feedRepository.toggleLike(currentCommunityId, postId, userId)) {
                is Resource.Success -> {
                    // Optimistic update - refresh to get accurate state
                    refreshPosts()
                }
                is Resource.Error -> {
                    // Handle error silently or show a snackbar
                }
                is Resource.Loading -> { }
            }
        }
    }

    /**
     * Add a comment to a post
     */
    fun addComment(postId: String, content: String) {
        viewModelScope.launch {
            val userId = currentUserId ?: return@launch
            val userName = authRepository.getCurrentUserName() ?: "Anonymous"
            val userPhoto = authRepository.getCurrentUserPhotoUrl() ?: ""

            val comment = Comment(
                authorId = userId,
                authorName = userName,
                authorPhotoUrl = userPhoto,
                content = content,
                timestamp = Timestamp.now()
            )

            when (feedRepository.addComment(currentCommunityId, postId, comment)) {
                is Resource.Success -> {
                    refreshPosts()
                }
                is Resource.Error -> {
                    // Handle error
                }
                is Resource.Loading -> { }
            }
        }
    }
}

/**
 * State for posts loading
 */
sealed class PostsState {
    object Loading : PostsState()
    data class Success(val posts: List<Post>) : PostsState()
    data class Error(val message: String) : PostsState()
}

/**
 * State for creating a post
 */
sealed class CreatePostState {
    object Idle : CreatePostState()
    object Loading : CreatePostState()
    object Success : CreatePostState()
    data class Error(val message: String) : CreatePostState()
}
