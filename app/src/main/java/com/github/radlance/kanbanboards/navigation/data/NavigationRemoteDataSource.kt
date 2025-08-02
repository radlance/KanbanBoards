package com.github.radlance.kanbanboards.navigation.data

import com.github.radlance.api.service.MyUser
import javax.inject.Inject

interface NavigationRemoteDataSource {

    fun userExists(): Boolean

    class Base @Inject constructor(private val myUser: MyUser) :
        NavigationRemoteDataSource {

        override fun userExists(): Boolean = myUser.exists
    }
}