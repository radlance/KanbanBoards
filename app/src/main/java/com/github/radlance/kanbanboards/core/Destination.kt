package com.github.radlance.kanbanboards.core

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

interface Destination

@Keep
@Serializable
object SignIn : Destination

@Keep
@Serializable
object Home : Destination