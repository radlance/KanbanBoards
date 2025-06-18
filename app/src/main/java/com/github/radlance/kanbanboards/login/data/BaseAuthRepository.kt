package com.github.radlance.kanbanboards.login.data

import com.github.radlance.kanbanboards.common.data.DataStoreManager
import com.github.radlance.kanbanboards.login.domain.AuthRepository
import com.github.radlance.kanbanboards.login.domain.AuthResult
import javax.inject.Inject

class BaseAuthRepository @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val handleAuthResult: HandleAuthResult,
    private val dataStoreManager: DataStoreManager
) : AuthRepository {

    override suspend fun signIn(userIdToken: String): AuthResult = handleAuthResult.handle {
        remoteDataSource.signIn(userIdToken)
        dataStoreManager.saveAuthorized(authorized = true)
    }
}