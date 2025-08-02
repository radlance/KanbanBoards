package com.github.radlance.auth.data

import com.github.radlance.api.service.NewMyUser
import com.github.radlance.api.service.Service
import com.github.radlance.core.data.HandleError
import com.github.radlance.core.data.UserProfileEntity
import javax.inject.Inject

internal interface HandleAuthRemoteDataSource {

    suspend fun handle(action: suspend () -> NewMyUser)

    class Base @Inject constructor(
        private val service: Service,
        private val handle: HandleError,
    ) : HandleAuthRemoteDataSource {
        override suspend fun handle(action: suspend () -> NewMyUser) {
            try {
                val user = action.invoke()

                service.update(
                    path = "users",
                    subPath = user.id,
                    obj = UserProfileEntity(user.email, user.displayName)
                )
            } catch (e: Exception) {
                handle.handle(e)
            }
        }
    }
}