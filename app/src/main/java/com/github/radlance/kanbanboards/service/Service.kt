package com.github.radlance.kanbanboards.service

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface Snapshot {

    val id: String

    val ref: Reference

    val children: List<Snapshot>

    fun <T> getValue(clazz: Class<T>): T?

    fun child(path: String): Snapshot

    class Base(private val snapshot: DataSnapshot) : Snapshot {

        override val id: String = snapshot.key!!

        override val ref: Reference = Reference.Base(snapshot.ref)

        override val children: List<Snapshot> = snapshot.children.map { Base(it) }

        override fun <T> getValue(clazz: Class<T>): T? = snapshot.getValue(clazz)

        override fun child(path: String): Snapshot = Base(snapshot.child(path))
    }
}

interface Reference {

    val id: String

    fun removeValue()

    class Base(private val reference: DatabaseReference) : Reference {

        override val id: String = reference.key!!

        override fun removeValue() {
            reference.removeValue()
        }
    }
}

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

    class Base @Inject constructor(private val database: DatabaseReference) : Service {

        override fun post(path: String, obj: Any): Reference {
            val reference = database.child(path).push()
            reference.setValue(obj)
            return Reference.Base(reference)
        }

        override fun post(path: String, subPath: String, obj: Any): Reference {
            val reference = database.child(path).child(subPath).push()
            reference.setValue(obj)
            return Reference.Base(reference)
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
                .snapshots.map { Snapshot.Base(it) }
        }

        override fun get(path: String, subPath: String): Flow<Snapshot> {
            return database
                .child(path)
                .child(subPath)
                .snapshots.map { Snapshot.Base(it) }
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
                .snapshots.map { snapshot -> snapshot.children.map { Snapshot.Base(it) } }
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

            return Snapshot.Base(snapshot)
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
                .get().await().children.map { Snapshot.Base(it) }
        }
    }
}