package com.github.radlance.login.data

import com.github.radlance.api.service.Auth
import javax.inject.Inject

internal interface AuthRemoteDataSource {

    suspend fun signInWithToken(userTokenId: String)

    suspend fun signInWithEmail(email: String, password: String)

    suspend fun signUp(name: String, email: String, password: String)
}

internal class BaseAuthRemoteDataSource @Inject constructor(
    private val handle: HandleAuthRemoteDataSource,
    private val auth: Auth,
) : AuthRemoteDataSource {

    override suspend fun signInWithToken(userTokenId: String) = handle.handle {
        auth.signIn(userTokenId)
    }

    override suspend fun signInWithEmail(email: String, password: String) = handle.handle {
        auth.signIn(email, password)
    }

    override suspend fun signUp(name: String, email: String, password: String) = handle.handle {
        auth.signUp(name, email, password)
    }
}