package com.github.radlance.kanbanboards.api.service

import com.google.firebase.database.DatabaseReference

interface Reference {

    val id: String

    fun removeValue()
}

internal class BaseReference(private val reference: DatabaseReference) : Reference {

    override val id: String = reference.key!!

    override fun removeValue() {
        reference.removeValue()
    }
}