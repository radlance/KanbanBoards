package com.github.radlance.login.data

import com.github.radlance.common.domain.UnitResult
import com.github.radlance.login.domain.AuthRepository
import javax.inject.Inject

internal class BaseAuthRepository @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val handleAuthResult: HandleAuthResult
) : AuthRepository {

    override suspend fun signInWithToken(userIdToken: String): UnitResult =
        handleAuthResult.handle { remoteDataSource.signInWithToken(userIdToken) }

    override suspend fun signInWithEmail(email: String, password: String): UnitResult =
        handleAuthResult.handle { remoteDataSource.signInWithEmail(email, password) }

    override suspend fun signUp(name: String, email: String, password: String): UnitResult =
        handleAuthResult.handle { remoteDataSource.signUp(name, email, password) }
}