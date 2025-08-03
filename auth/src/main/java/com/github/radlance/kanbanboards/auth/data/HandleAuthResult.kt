package com.github.radlance.kanbanboards.auth.data

import com.github.radlance.kanbanboards.core.core.ManageResource
import com.github.radlance.kanbanboards.core.data.DataStoreManager
import com.github.radlance.kanbanboards.core.domain.DomainException
import com.github.radlance.kanbanboards.core.domain.UnitResult
import javax.inject.Inject

internal interface HandleAuthResult {

    suspend fun handle(action: suspend () -> Unit): UnitResult

    class Base @Inject constructor(
        private val managerResource: ManageResource,
        private val dataStoreManager: DataStoreManager
    ) : HandleAuthResult {

        override suspend fun handle(action: suspend () -> Unit): UnitResult {
            return try {
                action.invoke()
                dataStoreManager.saveAuthorized(authorized = true)
                UnitResult.Success
            } catch (e: DomainException.NoInternetException) {
                UnitResult.Error(
                    managerResource.string(
                        com.github.radlance.core.R.string.no_internet_connection
                    )
                )
            } catch (e: DomainException.ServerUnavailableException) {
                UnitResult.Error(e.message)
            }
        }
    }
}