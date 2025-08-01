package com.github.radlance.api.service

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface Service {

    fun post(path: String, obj: Any): Reference

    fun post(path: String, subPath: String, obj: Any): Reference

    fun delete(path: String, itemId: String)

    fun update(path: String, subPath: String, obj: Any)

    fun update(path: String, subPath1: String, subPath2: String, obj: Any)

    fun get(path: String): Flow<Snapshot>

    fun get(path: String, subPath: String): Flow<Snapshot>

    fun getListByQuery(path: String, queryKey: String, queryValue: String): Flow<List<Snapshot>>

    suspend fun getSingleQueryAwait(path: String, queryKey: String, queryValue: String): Snapshot

    suspend fun getListByQueryAwait(
        path: String,
        queryKey: String,
        queryValue: String
    ): List<Snapshot>
}

internal class BaseService @Inject constructor(
    private val database: DatabaseReference
) : Service {

    override fun post(path: String, obj: Any): Reference {
        val reference = database.child(path).push()
        reference.setValue(obj)
        return BaseReference(reference)
    }

    override fun post(path: String, subPath: String, obj: Any): Reference {
        val reference = database.child(path).child(subPath).push()
        reference.setValue(obj)
        return BaseReference(reference)
    }

    override fun delete(path: String, itemId: String) {
        database.child(path).child(itemId).removeValue()
    }

    override fun update(path: String, subPath: String, obj: Any) {
        database.child(path).child(subPath).setValue(obj)
    }

    override fun update(path: String, subPath1: String, subPath2: String, obj: Any) {
        database.child(path).child(subPath1).child(subPath2).setValue(obj)
    }

    override fun get(path: String): Flow<Snapshot> {
        return database
            .child(path)
            .snapshots.map { BaseSnapshot(it) }
    }

    override fun get(path: String, subPath: String): Flow<Snapshot> {
        return database
            .child(path)
            .child(subPath)
            .snapshots.map { BaseSnapshot(it) }
    }

    override fun getListByQuery(
        path: String,
        queryKey: String,
        queryValue: String
    ): Flow<List<Snapshot>> {
        return database
            .child(path)
            .orderByChild(queryKey)
            .equalTo(queryValue)
            .snapshots.map { snapshot -> snapshot.children.map { BaseSnapshot(it) } }
    }

    override suspend fun getSingleQueryAwait(
        path: String,
        queryKey: String,
        queryValue: String
    ): Snapshot {
        val snapshot = database
            .child(path)
            .orderByChild(queryKey)
            .equalTo(queryValue)
            .get().await()

        return BaseSnapshot(snapshot)
    }

    override suspend fun getListByQueryAwait(
        path: String,
        queryKey: String,
        queryValue: String
    ): List<Snapshot> {
        return database
            .child(path)
            .orderByChild(queryKey)
            .equalTo(queryValue)
            .get().await().children.map { BaseSnapshot(it) }
    }
}