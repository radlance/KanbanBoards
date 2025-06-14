package com.github.radlance.kanbanboards.navigation.core

import androidx.navigation.NavHostController

interface AuthorizedUiState {

    fun navigate(navHostController: NavHostController)

    object Authorized : AuthorizedUiState {
        override fun navigate(navHostController: NavHostController) {
            navHostController.navigate(Home)
        }
    }

    object UnAuthorized : AuthorizedUiState {
        override fun navigate(navHostController: NavHostController) {
            navHostController.navigate(SignIn)
        }
    }
}