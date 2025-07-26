package com.github.radlance.kanbanboards.navigation.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import javax.inject.Inject

interface NavigationRemoteDataSource {

    fun userExists(): Boolean

    class Base @Inject constructor() : NavigationRemoteDataSource {

        override fun userExists(): Boolean = Firebase.auth.currentUser != null
    }
}