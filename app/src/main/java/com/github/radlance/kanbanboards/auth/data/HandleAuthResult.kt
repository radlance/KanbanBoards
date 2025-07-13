package com.github.radlance.kanbanboards.auth.data

import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.domain.DomainException
import com.github.radlance.kanbanboards.auth.domain.AuthResult
import com.github.radlance.kanbanboards.common.data.DataStoreManager
import javax.inject.Inject

interface HandleAuthResult {

    suspend fun handle(action: suspend () -> Unit): AuthResult

    class Base @Inject constructor(
        private val managerResource: ManageResource,
        private val dataStoreManager: DataStoreManager
    ) : HandleAuthResult {

        override suspend fun handle(action: suspend () -> Unit): AuthResult {
            return try {
                action.invoke()
                dataStoreManager.saveAuthorized(authorized = true)
                AuthResult.Success
            } catch (e: DomainException.NoInternetException) {
                AuthResult.Error(managerResource.string(R.string.no_internet_connection))
            } catch (e: DomainException.ServerUnavailableException) {
                AuthResult.Error(e.message)
            }
        }
    }
}