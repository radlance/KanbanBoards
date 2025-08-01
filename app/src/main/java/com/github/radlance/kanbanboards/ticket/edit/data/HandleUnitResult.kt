package com.github.radlance.kanbanboards.ticket.edit.data

import com.github.radlance.common.core.ManageResource
import com.github.radlance.common.domain.UnitResult
import com.github.radlance.kanbanboards.R
import javax.inject.Inject

interface HandleUnitResult {

    fun handle(action: () -> Unit): UnitResult

    class Base @Inject constructor(
        private val manageResource: ManageResource
    ) : HandleUnitResult {

        override fun handle(action: () -> Unit): UnitResult {
            return try {
                action.invoke()
                UnitResult.Success
            } catch (e: Exception) {
                UnitResult.Error(e.message ?: manageResource.string(R.string.error))
            }
        }
    }
}