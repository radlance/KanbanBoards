package com.github.radlance.kanbanboards.ticket.edit.data

import com.github.radlance.kanbanboards.R
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.domain.UnitResult
import javax.inject.Inject

interface HandleUnitResult {

    suspend fun handle(action: suspend () -> Unit): UnitResult

    class Base @Inject constructor(
        private val manageResource: ManageResource
    ) : HandleUnitResult {

        override suspend fun handle(action: suspend () -> Unit): UnitResult {
            return try {
                action.invoke()
                UnitResult.Success
            } catch (e: Exception) {
                UnitResult.Error(e.message ?: manageResource.string(R.string.error))
            }
        }
    }
}