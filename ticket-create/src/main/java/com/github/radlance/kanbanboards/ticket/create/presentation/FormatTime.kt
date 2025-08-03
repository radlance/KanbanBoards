package com.github.radlance.kanbanboards.ticket.create.presentation

import java.time.LocalDateTime
import javax.inject.Inject

interface FormatTime {

    fun now(): LocalDateTime
}

internal class BaseFormatTime @Inject constructor() : FormatTime {

    override fun now(): LocalDateTime = LocalDateTime.now()
}