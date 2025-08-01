package com.github.radlance.kanbanboards.navigation.core

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

interface Destination

@Keep
@Serializable
object Splash : Destination

@Keep
@Serializable
object SignIn : Destination

@Keep
@Serializable
object SignUp : Destination

@Keep
@Serializable
object Boards : Destination

@Keep
@Serializable
object Profile : Destination

@Keep
@Serializable
object CreateBoard : Destination

@Keep
@Serializable
object Board : Destination

@Keep
@Serializable
data class CreateTicket(val boardId: String) : Destination

@Keep
@Serializable
data class TicketInfo(val ticketId: String, val boardId: String) : Destination

@Keep
@Serializable
data class EditTicket(val boardId: String) : Destination

@Keep
@Serializable
object BoardSettings : Destination

@Keep
@Serializable
object Invitation : Destination