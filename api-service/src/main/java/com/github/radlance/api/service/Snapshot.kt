package com.github.radlance.api.service

import com.google.firebase.database.DataSnapshot

interface Snapshot {

    val id: String

    val ref: Reference

    val children: List<Snapshot>

    fun <T> getValue(clazz: Class<T>): T?

    fun child(path: String): Snapshot
}

internal class BaseSnapshot(private val snapshot: DataSnapshot) : Snapshot {

    override val id: String = snapshot.key!!

    override val ref: Reference = BaseReference(snapshot.ref)

    override val children: List<Snapshot> = snapshot.children.map { BaseSnapshot(it) }

    override fun <T> getValue(clazz: Class<T>): T? = snapshot.getValue(clazz)

    override fun child(path: String): Snapshot = BaseSnapshot(snapshot.child(path))
}