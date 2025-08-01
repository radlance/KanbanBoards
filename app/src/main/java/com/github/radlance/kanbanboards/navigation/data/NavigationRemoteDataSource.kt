package com.github.radlance.kanbanboards.navigation.data

import javax.inject.Inject

interface NavigationRemoteDataSource {

    fun userExists(): Boolean

    class Base @Inject constructor(private val myUser: com.github.radlance.api.service.MyUser) :
        NavigationRemoteDataSource {

        override fun userExists(): Boolean = myUser.exists
    }
}