package com.example.sparin.data.repository

import com.example.sparin.data.remote.FirestoreService
import com.example.sparin.domain.util.Resource
import com.example.sparin.presentation.community.feed.Comment
import com.example.sparin.presentation.community.feed.Post
import com.example.sparin.util.Constants
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/**
 * Repository untuk Feed operations (Posts, Likes, Comments)
 */
class FeedRepository(
    private val firestoreService: FirestoreService,
    private val firestore: FirebaseFirestore // Direct access for transactions/subcollections
) {

    /**
     * Get posts for a community
     */
    suspend fun getPosts(communityId: String, currentUserId: String): Resource<List<Post>> {
        return try {
            val snapshot = firestore.collection(Constants.Collections.COMMUNITIES)
                .document(communityId)
                .collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val posts = snapshot.documents.mapNotNull { doc ->
                val post = doc.toObject(Post::class.java)
                post?.copy(
                    id = doc.id,
                    isLikedByCurrentUser = post.likedBy.contains(currentUserId)
                )
            }
            Resource.Success(posts)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load posts")
        }
    }

    /**
     * Create a new post
     */
    suspend fun createPost(communityId: String, post: Post): Resource<String> {
        return try {
            val docRef = firestore.collection(Constants.Collections.COMMUNITIES)
                .document(communityId)
                .collection("posts")
                .document() // Auto-ID

            val postWithId = post.copy(id = docRef.id)
            docRef.set(postWithId).await()
            
            Resource.Success(docRef.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to create post")
        }
    }

    /**
     * Toggle like on a post
     */
    suspend fun toggleLike(communityId: String, postId: String, userId: String): Resource<Unit> {
        return try {
            val postRef = firestore.collection(Constants.Collections.COMMUNITIES)
                .document(communityId)
                .collection("posts")
                .document(postId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                @Suppress("UNCHECKED_CAST")
                val likedBy = snapshot.get("likedBy") as? List<String> ?: emptyList()
                val currentLikes = snapshot.getLong("likes") ?: 0

                if (likedBy.contains(userId)) {
                    // Unlike
                    transaction.update(postRef, "likedBy", FieldValue.arrayRemove(userId))
                    transaction.update(postRef, "likes", currentLikes - 1)
                } else {
                    // Like
                    transaction.update(postRef, "likedBy", FieldValue.arrayUnion(userId))
                    transaction.update(postRef, "likes", currentLikes + 1)
                }
            }.await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update like")
        }
    }

    /**
     * Add a comment to a post
     */
    suspend fun addComment(communityId: String, postId: String, comment: Comment): Resource<String> {
        return try {
            // 1. Add to comments subcollection (optional, if we want to scale)
            // For now, we are storing comments INSIDE the post document as a list for simplicity (MVP)
            // based on the Post data model which has `val comments: List<Comment>`
            
            // However, Firestore arrayUnion only works for unique elements and has size limits.
            // A better approach for "comments" is usually a subcollection.
            // But since the UI expects `post.comments` list, let's try to update the array in the document.
            
            val postRef = firestore.collection(Constants.Collections.COMMUNITIES)
                .document(communityId)
                .collection("posts")
                .document(postId)
                
            val commentWithId = comment.copy(id = firestore.collection("dummy").document().id) // Generate ID

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val currentComments = snapshot.toObject(Post::class.java)?.comments ?: emptyList()
                val newComments = currentComments + commentWithId
                
                transaction.update(postRef, "comments", newComments)
                transaction.update(postRef, "commentCount", newComments.size)
            }.await()

            Resource.Success(commentWithId.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add comment")
        }
    }
}
