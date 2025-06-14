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
object Home : Destination