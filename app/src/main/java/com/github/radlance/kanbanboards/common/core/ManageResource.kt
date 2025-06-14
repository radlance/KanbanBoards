package com.github.radlance.kanbanboards.common.core

import android.content.Context
import javax.inject.Inject

interface ManageResource {

    fun string(id: Int): String

    class Base @Inject constructor(private val context: Context) : ManageResource {

        override fun string(id: Int): String = context.getString(id)
    }
}