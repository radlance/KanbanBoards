package com.github.radlance.kanbanboards.service

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

internal interface ProvideDatabase {

    fun database(): DatabaseReference

    class Base : ProvideDatabase {

        init {
            Firebase.database(DATABASE_URL).setPersistenceEnabled(false)
        }

        override fun database(): DatabaseReference {
            return Firebase.database(DATABASE_URL).reference.root
        }

        companion object {
            private const val DATABASE_URL =
                "https://kanban-boards-f9ea6-default-rtdb.europe-west1.firebasedatabase.app/"
        }
    }
}