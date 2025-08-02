package com.github.radlance.ticket.common.presentation

interface TicketColor {

    fun hex(): String

    abstract class Abstract(private val color: String) : TicketColor {

        override fun hex(): String = color
    }

    object Yellow : Abstract("#FFFF00")

    object Orange : Abstract("#FFA500")

    object Purple : Abstract("#A020F0")

    object Red : Abstract("#FF0000")

    object Green : Abstract("#008000")

    object Blue : Abstract("#0000FF")
}