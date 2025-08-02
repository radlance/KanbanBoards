package com.github.radlance.login.data

import com.github.radlance.common.core.ManageResource
import com.github.radlance.common.data.DataStoreManager
import com.github.radlance.common.domain.DomainException
import com.github.radlance.common.domain.UnitResult
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
                        com.github.radlance.common.R.string.no_internet_connection
                    )
                )
            } catch (e: DomainException.ServerUnavailableException) {
                UnitResult.Error(e.message)
            }
        }
    }
}