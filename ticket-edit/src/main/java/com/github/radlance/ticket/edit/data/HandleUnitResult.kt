package com.github.radlance.ticket.edit.data

import com.github.radlance.core.core.ManageResource
import com.github.radlance.core.domain.UnitResult
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
                UnitResult.Error(
                    e.message ?: manageResource.string(com.github.radlance.core.R.string.error)
                )
            }
        }
    }
}