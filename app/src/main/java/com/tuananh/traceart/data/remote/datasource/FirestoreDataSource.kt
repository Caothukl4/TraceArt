package com.tuananh.traceart.data.remote.datasource

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.SetOptions
import com.tuananh.traceart.data.remote.model.BookDto
import com.tuananh.traceart.data.remote.model.LimitDataDto
import com.tuananh.traceart.data.remote.model.PageDto
import com.tuananh.traceart.data.remote.model.VoiceInfoDto
import com.tuananh.traceart.domain.model.Sample
import com.tuananh.traceart.domain.params.BookParams
import com.tuananh.traceart.domain.params.LimitParams
import com.tuananh.traceart.domain.params.PageParams
import com.tuananh.traceart.utils.toFirestoreMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

const val TAG = "BookRemoteDataSource"

object Collections {
    const val Books = "Books"
    const val Pages = "Pages"
    const val Voices = "Voices"
    const val Samples = "Samples"
    const val Limits = "Limits"
}

@Singleton
class FirestoreDataSource @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()
    suspend fun fetchListBook(): List<BookDto> = withContext(Dispatchers.IO) {
        firestore.collection(Collections.Books).orderBy("order", Direction.DESCENDING).get()
            .await().documents.mapNotNull { doc ->
                val book = doc.toObject(BookDto::class.java)
                book?.id = doc.id
                book
            }
    }

    suspend fun fetchListBookByAnonymousId(anonymousId: String, marked: Boolean): List<BookDto> =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "fetchListBook: ")
            var query = firestore.collection(Collections.Books)
                .where(Filter.equalTo("anonymousId", anonymousId))
                .where(Filter.equalTo("deleted", false))
            if (marked) {
                query = query.where(Filter.equalTo("marked", true))
            }
            query = query.orderBy("createAt", Direction.DESCENDING)
            query.get().await().documents.mapNotNull { doc ->
                val book = doc.toObject(BookDto::class.java)
                book?.id = doc.id
                book
            }
        }

    suspend fun fetchListBookByUserId(userId: String): List<BookDto> =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "fetchListBook: ")
            firestore.collection(Collections.Books)
                .where(Filter.equalTo("createBy", userId)).get()
                .await().documents.mapNotNull { doc ->
                    val book = doc.toObject(BookDto::class.java)
                    book?.id = doc.id
                    book
                }
        }

    suspend fun getBookById(bookId: String): BookDto? = withContext(Dispatchers.IO) {
        Log.d(TAG, "fetchListBook: ")
        val docSnapshot = firestore.collection(Collections.Books).document(bookId).get().await()
        docSnapshot.toObject(BookDto::class.java)?.copy(id = docSnapshot.id)
    }

    suspend fun fetchListPage(bookId: String): List<PageDto> = withContext(Dispatchers.IO) {
        Log.d(TAG, "fetchListBook: ")
        firestore.collection(Collections.Books).document(bookId)
            .collection(Collections.Pages).where(Filter.equalTo("deleted", false))
            .orderBy("order", Direction.ASCENDING).get()
            .await().documents.mapNotNull { doc ->
                val page = doc.toObject(PageDto::class.java)
                page?.id = doc.id
                page
            }
    }

    fun listenListPage(bookId: String): Flow<List<PageDto>> = callbackFlow {
        val listenerRegistration = FirebaseFirestore.getInstance()
            .collection(Collections.Books)
            .document(bookId)
            .collection(Collections.Pages)
            .whereEqualTo("deleted", false)
            .orderBy("order", Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "listenListPage error: ", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val pages = snapshot.documents.mapNotNull { doc ->
                        val page = doc.toObject(PageDto::class.java)
                        page?.id = doc.id
                        page
                    }
                    trySend(pages)
                } else {
                    trySend(emptyList())
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    private suspend fun getMaxOrderBook(): Long = withContext(Dispatchers.IO) {
        val snapshot = firestore.collection(Collections.Books)
            .orderBy("order", Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            val maxOrder = snapshot.documents.firstOrNull()?.getLong("order") ?: 0
            maxOrder
        } else {
            0
        }
    }

    private suspend fun getMaxOrderPage(bookId: String): Long = withContext(Dispatchers.IO) {
        val snapshot = firestore.collection(Collections.Books)
            .document(bookId)
            .collection(Collections.Pages)
            .orderBy("order", Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            val maxOrder = snapshot.documents.firstOrNull()?.getLong("order") ?: 0
            maxOrder
        } else {
            0
        }
    }

    suspend fun createBook(params: BookParams): BookDto = withContext(Dispatchers.IO) {
        val maxOrder = getMaxOrderBook()

        val bookData = params.toFirestoreMap().apply {
            this["marked"] = false
            this["order"] = maxOrder + 1000
        }
        val result = addDoc(firestore.collection("Books"), bookData)
        params.toBookDto(result.id)
    }

    suspend fun updateBook(bookId: String, params: BookParams) = withContext(Dispatchers.IO) {

        val bookData = params.toFirestoreMap()
        updateDoc(firestore.collection(Collections.Books).document(bookId), bookData)
    }

    suspend fun addPage(bookId: String, params: PageParams) = withContext(Dispatchers.IO) {
        val maxOrder = getMaxOrderPage(bookId)
        val pageMap = params.toFirestoreMap().apply {
            this["marked"] = false
            this["order"] = maxOrder + 1000
        }
        val result = addDoc(
            firestore.collection(Collections.Books).document(bookId).collection(Collections.Pages),
            pageMap
        )
        params.toPageDto(result.id)
    }

    suspend fun updatePage(bookId: String, pageId: String, params: PageParams) =
        withContext(Dispatchers.IO) {
            val pageMap = params.toFirestoreMap()
            updateDoc(
                firestore.collection(Collections.Books).document(bookId)
                    .collection(Collections.Pages).document(pageId),
                pageMap,
                true
            )
        }

    suspend fun updatePageAudioData(
        bookId: String,
        pageId: String,
        sha1Key: String,
        voice: String,
        audio: String
    ) =
        withContext(Dispatchers.IO) {
            updateDoc(
                firestore.collection(Collections.Books).document(bookId)
                    .collection(Collections.Pages).document(pageId),
                mapOf("audioData.$sha1Key.$voice" to audio)
            )
        }

    suspend fun getListVoices(): List<VoiceInfoDto> = withContext(Dispatchers.IO) {
        val listVoice =
            firestore.collection(Collections.Voices).where(Filter.equalTo("deleted", false))
                .orderBy("order", Direction.DESCENDING).get()
                .await().mapNotNull { doc ->
                    val voiceInfo = doc.toObject(VoiceInfoDto::class.java)
                    voiceInfo
                }
        listVoice
    }

    suspend fun getMapSamples(): Map<String, Sample> = withContext(Dispatchers.IO) {
        val resultMap = mutableMapOf<String, Sample>()
        firestore.collection(Collections.Samples).get().await().mapNotNull { doc ->
            val sampleObj = doc.toObject(Sample::class.java)
            resultMap[doc.id] = sampleObj
        }
        resultMap
    }

    suspend fun getLimitData(deviceId: String): LimitDataDto? = withContext(Dispatchers.IO) {
        val dataSnapshot = firestore.collection(Collections.Limits).document(deviceId).get().await()
        dataSnapshot.toObject(LimitDataDto::class.java)
    }

    suspend fun updateLimitData(deviceId: String, limitParams: LimitParams) =
        withContext(Dispatchers.IO) {
            val limitParamsOk = limitParams.copy(
                limitOrc = limitParams.limitOrc?.copy(updateAt = FieldValue.serverTimestamp()),
                limitTts = limitParams.limitTts?.copy(updateAt = FieldValue.serverTimestamp())
            )
            firestore.collection(Collections.Limits).document(deviceId)
                .set(limitParamsOk.toFirestoreMap(), SetOptions.merge()).await()
        }
}

suspend fun addDoc(
    collection: CollectionReference,
    params: Map<String, Any?>
): DocumentReference = withContext(Dispatchers.IO) {
    val data = params.toMutableMap().apply {
        put("createAt", params["createAt"] ?: FieldValue.serverTimestamp())
        put("updateAt", params["updateAt"] ?: FieldValue.serverTimestamp())
        put("deleted", false)
    }

    val result = collection.add(data).await()
    Log.d("Firestore", "addDoc: ${collection.path} - ${result.id}")
    result
}

suspend fun updateDoc(
    docRef: DocumentReference,
    params: Map<String, Any?>,
    merge: Boolean? = null,
): Unit = withContext(Dispatchers.IO) {
    val paramsOk = params.toMutableMap().apply {
        put("updateAt", params["updateAt"] ?: FieldValue.serverTimestamp())
    }
    if (merge != null && merge) {
        docRef.set(paramsOk, SetOptions.merge()).await()
    } else {
        docRef.update(paramsOk).await()

    }
}