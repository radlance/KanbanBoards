package com.github.radlance.kanbanboards.auth.data

import com.github.radlance.kanbanboards.auth.domain.AuthRepository
import com.github.radlance.kanbanboards.auth.domain.AuthResult
import com.github.radlance.kanbanboards.common.data.RemoteDataSource
import javax.inject.Inject

class BaseAuthRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val handleAuthResult: HandleAuthResult
) : AuthRepository {

    override suspend fun signIn(userIdToken: String): AuthResult = handleAuthResult.handle {
        remoteDataSource.signIn(userIdToken)
    }

}