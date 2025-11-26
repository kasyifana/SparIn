package com.example.sparin.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Generic Firestore Service untuk CRUD operations
 */
class FirestoreService(
    private val firestore: FirebaseFirestore
) {
    
    /**
     * Create document
     */
    suspend fun <T> createDocument(collection: String, document: T): String {
        val docRef = firestore.collection(collection).document()
        docRef.set(document as Any).await()
        return docRef.id
    }
    
    /**
     * Create document with custom ID
     */
    suspend fun <T> createDocumentWithId(collection: String, documentId: String, document: T) {
        firestore.collection(collection)
            .document(documentId)
            .set(document as Any)
            .await()
    }
    
    /**
     * Get document by ID
     */
    suspend fun <T> getDocument(collection: String, documentId: String, clazz: Class<T>): T? {
        val snapshot = firestore.collection(collection)
            .document(documentId)
            .get()
            .await()
        return snapshot.toObject(clazz)
    }
    
    /**
     * Update document
     */
    suspend fun updateDocument(collection: String, documentId: String, data: Map<String, Any>) {
        firestore.collection(collection)
            .document(documentId)
            .update(data)
            .await()
    }
    
    /**
     * Delete document
     */
    suspend fun deleteDocument(collection: String, documentId: String) {
        firestore.collection(collection)
            .document(documentId)
            .delete()
            .await()
    }
    
    /**
     * Get all documents dari collection
     */
    suspend fun <T> getCollection(collection: String, clazz: Class<T>): List<T> {
        val snapshot = firestore.collection(collection)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject(clazz) }
    }
    
    /**
     * Query collection dengan filter
     */
    suspend fun <T> queryCollection(
        collection: String,
        field: String,
        value: Any,
        clazz: Class<T>
    ): List<T> {
        val snapshot = firestore.collection(collection)
            .whereEqualTo(field, value)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject(clazz) }
    }
    
    /**
     * Query collection dengan multiple filters
     */
    suspend fun <T> queryCollectionMultiple(
        collection: String,
        filters: Map<String, Any>,
        clazz: Class<T>
    ): List<T> {
        var query: Query = firestore.collection(collection)
        filters.forEach { (field, value) ->
            query = query.whereEqualTo(field, value)
        }
        val snapshot = query.get().await()
        return snapshot.documents.mapNotNull { it.toObject(clazz) }
    }
    
    /**
     * Observe collection real-time
     */
    fun <T> observeCollection(collection: String, clazz: Class<T>): Flow<List<T>> = callbackFlow {
        val listener = firestore.collection(collection)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val items = snapshot?.documents?.mapNotNull { it.toObject(clazz) } ?: emptyList()
                trySend(items)
            }
        
        awaitClose { listener.remove() }
    }
    
    /**
     * Observe document real-time
     */
    fun <T> observeDocument(collection: String, documentId: String, clazz: Class<T>): Flow<T?> = callbackFlow {
        val listener = firestore.collection(collection)
            .document(documentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val item = snapshot?.toObject(clazz)
                trySend(item)
            }
        
        awaitClose { listener.remove() }
    }
    
    /**
     * Batch write operations
     */
    suspend fun batchWrite(operations: (com.google.firebase.firestore.WriteBatch) -> Unit) {
        val batch = firestore.batch()
        operations(batch)
        batch.commit().await()
    }
}
