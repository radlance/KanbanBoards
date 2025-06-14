package com.github.radlance.kanbanboards.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.radlance.kanbanboards.auth.presentation.SignInScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navHostController, startDestination = SignIn, modifier = modifier) {
        composable<SignIn> {
            SignInScreen(
                onSuccessSignIn = {
                    navHostController.navigate(Home) {
                        popUpTo<SignIn> { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "home screen")
            }
        }
    }
}