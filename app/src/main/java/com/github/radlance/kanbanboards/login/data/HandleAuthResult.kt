package com.github.radlance.kanbanboards.login.data

import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.login.domain.AuthResult
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.domain.DomainException
import javax.inject.Inject

interface HandleAuthResult {

    suspend fun handle(action: suspend () -> Unit): AuthResult


    class Base @Inject constructor(
        private val managerResource: ManageResource
    ) : HandleAuthResult {

        override suspend fun handle(action: suspend () -> Unit): AuthResult {
            return try {
                action.invoke()
                AuthResult.Success
            } catch (e: DomainException.NoInternetException) {
                AuthResult.Error(managerResource.string(R.string.no_internet_connection))
            } catch (e: DomainException.ServerUnavailableException) {
                AuthResult.Error(e.message)
            }
        }
    }
}