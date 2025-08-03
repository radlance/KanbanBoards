package com.github.radlance.kanbanboards.core.core

import android.content.Context
import javax.inject.Inject

interface ManageResource {

    fun string(id: Int): String
}

internal class BaseManageResource @Inject constructor(
    private val context: Context
) : ManageResource {

    override fun string(id: Int): String = context.getString(id)
}