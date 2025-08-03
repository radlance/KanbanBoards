package com.github.radlance.kanbanboards.ticket.edit.data

import com.github.radlance.kanbanboards.core.core.ManageResource
import com.github.radlance.kanbanboards.core.domain.UnitResult
import javax.inject.Inject

internal interface HandleUnitResult {

    fun handle(action: () -> Unit): UnitResult

    class Base @Inject constructor(
        private val manageResource: ManageResource
    ) : HandleUnitResult {

        override fun handle(action: () -> Unit): UnitResult {
            return try {
                action.invoke()
                UnitResult.Success
            } catch (e: Exception) {
                UnitResult.Error(
                    e.message ?: manageResource.string(com.github.radlance.core.R.string.error)
                )
            }
        }
    }
}