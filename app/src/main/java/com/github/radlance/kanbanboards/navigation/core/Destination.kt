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
data class CreateTicket(val boardId: String): Destination

@Keep
@Serializable
object TicketInfo : Destination

@Keep
@Serializable
object EditTicket : Destination