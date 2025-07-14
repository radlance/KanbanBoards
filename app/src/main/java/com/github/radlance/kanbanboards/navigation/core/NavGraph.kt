package com.github.radlance.kanbanboards.navigation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.radlance.kanbanboards.auth.presentation.signin.SignInScreen
import com.github.radlance.kanbanboards.auth.presentation.signup.SignUpScreen
import com.github.radlance.kanbanboards.board.presentation.BoardScreen
import com.github.radlance.kanbanboards.board.presentation.BoardViewModel
import com.github.radlance.kanbanboards.boards.presentation.BoardsScreen
import com.github.radlance.kanbanboards.createboard.presentation.CreateBoardsScreen
import com.github.radlance.kanbanboards.profile.presentation.ProfileScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    navigationViewModel: NavigationViewModel = hiltViewModel(),
    boardViewModel: BoardViewModel = hiltViewModel()
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

        val navigateToBoardsScreen = {
            navHostController.navigate(Boards) {
                popUpTo<SignIn> { inclusive = true }
            }
        }

        composable<SignIn> {
            SignInScreen(
                navigateToBoardsScreen = navigateToBoardsScreen,
                navigateToSignUpScreen = {
                    navHostController.navigate(SignUp) { popUpTo<SignIn>() }
                }
            )
        }

        composable<SignUp> {
            SignUpScreen(
                navigateToBoardsScreen = navigateToBoardsScreen,
                navigateToSignInScreen = {
                    navHostController.navigate(SignIn) { popUpTo<SignIn> { inclusive = true } }
                }
            )
        }

        composable<Boards> {
            BoardsScreen(
                navigateToProfile = { navHostController.navigate(Profile) },
                navigateToBoardCreation = { navHostController.navigate(CreateBoard) },
                navigateToBoard = {
                    boardViewModel.fetchBoard(it)
                    navHostController.navigate(Board)
                }
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

        composable<CreateBoard> {
            CreateBoardsScreen(
                navigateUp = navHostController::navigateUp,
                navigateToBoardScreen = {
                    boardViewModel.fetchBoard(it)
                    navHostController.navigate(Board) { popUpTo<Boards>() }
                }
            )
        }

        composable<Board> {
            BoardScreen(
                viewModel = boardViewModel,
                navigateUp = { navHostController.navigate(Boards) { popUpTo<Boards>() } }
            )
        }
    }
}