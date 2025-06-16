package com.github.radlance.kanbanboards.navigation.core

import androidx.navigation.NavHostController

interface AuthorizedUiState {

    fun navigate(navHostController: NavHostController)

    abstract class Abstract(private val destination: Destination) : AuthorizedUiState {
        override fun navigate(navHostController: NavHostController) {
            navHostController.navigate(destination) {
                popUpTo<Splash> { inclusive = true }
            }
        }
    }

    data object Authorized : Abstract(Home)

    data object Unauthorized : Abstract(SignIn)
}