package com.github.radlance.kanbanboards.ticket.create.presentation

interface TicketColor {

    fun hex(): String

    object Yellow : TicketColor {

        override fun hex(): String = "#FFFF00"
    }

    object Orange : TicketColor {

        override fun hex(): String = "#FFA500"
    }

    object Purple : TicketColor {

        override fun hex(): String = "#A020F0"
    }

    object Red : TicketColor {

        override fun hex(): String = "#FF0000"
    }

    object Green : TicketColor {

        override fun hex(): String = "#008000"
    }

    object Blue : TicketColor {

        override fun hex(): String = "#0000FF"
    }
}