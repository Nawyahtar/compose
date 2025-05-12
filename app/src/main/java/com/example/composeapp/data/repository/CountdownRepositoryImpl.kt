package com.example.composeapp.data.repository

import com.example.composeapp.data.remote.CountdownEvent
import com.example.composeapp.domain.repository.CountdownRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CountdownRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : CountdownRepository {
    override suspend fun addEvent(event: CountdownEvent) {
        fireStore.collection("events").add(event).await()
    }

    override fun getAllEvents(): Flow<List<CountdownEvent>> = callbackFlow {
        val listener = fireStore.collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error !== null || snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val events = snapshot.documents.mapNotNull { doc ->
                    doc.toObject<CountdownEvent>()
                }
                trySend(events)
            }
        awaitClose { listener.remove() }
    }
}