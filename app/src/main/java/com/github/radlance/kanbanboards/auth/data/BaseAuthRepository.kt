package com.github.radlance.kanbanboards.auth.data

import com.github.radlance.kanbanboards.auth.domain.AuthRepository
import com.github.radlance.kanbanboards.auth.domain.AuthResult
import javax.inject.Inject

class BaseAuthRepository @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val handleAuthResult: HandleAuthResult
) : AuthRepository {

    override suspend fun signInWithToken(userIdToken: String): AuthResult =
        handleAuthResult.handle { remoteDataSource.signInWithToken(userIdToken) }

    override suspend fun signInWithEmail(email: String, password: String): AuthResult =
        handleAuthResult.handle { remoteDataSource.signInWithEmail(email, password) }

    override suspend fun signUp(name: String, email: String, password: String): AuthResult =
        handleAuthResult.handle { remoteDataSource.signUp(name, email, password) }
}