package com.github.radlance.kanbanboards.navigation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.radlance.kanbanboards.boards.presentation.BoardsScreen
import com.github.radlance.kanbanboards.login.presentation.SignInScreen
import com.github.radlance.kanbanboards.profile.presentation.ProfileScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {

    val authorized by navigationViewModel.authorized.collectAsStateWithLifecycle()

    NavHost(
        navController = navHostController,
        startDestination = Splash,
        modifier = modifier
    ) {
        composable<Splash> {
            SplashScreen(
                onDelayFinished = { authorized.navigate(navHostController) }
            )
        }

        composable<SignIn> {
            SignInScreen(
                onSuccessSignIn = {
                    navHostController.navigate(Boards) {
                        popUpTo<SignIn> { inclusive = true }
                    }
                }
            )
        }

        composable<Boards> {
            BoardsScreen(
                navigateToProfile = { navHostController.navigate(Profile) },
                navigateToBoardCreation = {}
            )
        }

        composable<Profile> {
            ProfileScreen(
                navigateToLoginScreen = {
                    navHostController.navigate(SignIn) {
                        popUpTo<SignIn>()
                    }
                },
                navigateUp = navHostController::navigateUp
            )
        }
    }
}