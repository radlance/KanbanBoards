package com.github.radlance.kanbanboards.navigation.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.github.radlance.kanbanboards.auth.presentation.signin.SignInScreen
import com.github.radlance.kanbanboards.auth.presentation.signup.SignUpScreen
import com.github.radlance.kanbanboards.board.core.presentation.BoardScreen
import com.github.radlance.kanbanboards.board.core.presentation.BoardViewModel
import com.github.radlance.kanbanboards.board.create.presentation.CreateBoardViewModel
import com.github.radlance.kanbanboards.board.create.presentation.CreateBoardsScreen
import com.github.radlance.kanbanboards.board.settings.presentation.BoardSettingsScreen
import com.github.radlance.kanbanboards.board.settings.presentation.BoardSettingsViewModel
import com.github.radlance.kanbanboards.boards.presentation.BoardsScreen
import com.github.radlance.kanbanboards.invitation.presentation.InvitationScreen
import com.github.radlance.kanbanboards.invitation.presentation.InvitationViewModel
import com.github.radlance.kanbanboards.profile.presentation.ProfileScreen
import com.github.radlance.kanbanboards.ticket.create.presentation.CreateTicketScreen
import com.github.radlance.kanbanboards.ticket.create.presentation.CreateTicketViewModel
import com.github.radlance.kanbanboards.ticket.edit.presentation.EditTicketScreen
import com.github.radlance.kanbanboards.ticket.edit.presentation.EditTicketViewModel
import com.github.radlance.kanbanboards.ticket.info.presentation.TicketInfoScreen
import com.github.radlance.kanbanboards.ticket.info.presentation.TicketInfoViewModel

@Composable
fun NavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    navigationViewModel: NavigationViewModel = hiltViewModel(),
    boardViewModel: BoardViewModel = hiltViewModel(),
    createTicketViewModel: CreateTicketViewModel = hiltViewModel(),
    createBoardViewModel: CreateBoardViewModel = hiltViewModel(),
    ticketInfoViewModel: TicketInfoViewModel = hiltViewModel(),
    editTicketViewModel: EditTicketViewModel = hiltViewModel(),
    boardSettingsViewModel: BoardSettingsViewModel = hiltViewModel(),
    invitationViewModel: InvitationViewModel = hiltViewModel()
) {
    val authorized by navigationViewModel.authorized.collectAsStateWithLifecycle()

    NavHost(
        navController = navHostController,
        startDestination = Splash,
        enterTransition = {
            sharedXTransitionIn(initial = { (it * INITIAL_OFFSET).toInt() })
        },
        exitTransition = {
            sharedXTransitionOut(target = { -(it * INITIAL_OFFSET).toInt() })
        },
        popEnterTransition = {
            sharedXTransitionIn(initial = { -(it * INITIAL_OFFSET).toInt() })
        },
        popExitTransition = {
            sharedXTransitionOut(target = { -(it * INITIAL_OFFSET).toInt() })
        },
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
                navigateToBoardCreation = {
                    navHostController.navigate(CreateBoard)
                    createBoardViewModel.fetchUsers()
                },
                navigateToBoard = {
                    boardViewModel.fetchBoard(it)
                    navHostController.navigate(Board)
                },
                navigateToInvitations = { navHostController.navigate(Invitation) },
                invitationCountAction = invitationViewModel
            )
        }

        composable<Profile> {
            ProfileScreen(
                navigateToSignInScreen = {
                    navHostController.navigate(SignIn) {
                        popUpTo<Boards> { inclusive = true }
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
                },
                viewModel = createBoardViewModel
            )
        }

        composable<Board> {
            BoardScreen(
                viewModel = boardViewModel,
                navigateUp = navHostController::navigateUp,
                navigateToCreateTicket = { boardId, ownerId ->
                    createTicketViewModel.fetchBoardMembers(boardId, ownerId)
                    navHostController.navigate(CreateTicket(boardId))
                },
                navigateToTicketInfo = { ticketUi, boardId, ownerId ->
                    editTicketViewModel.fetchBoardMembers(boardId, ownerId)
                    ticketInfoViewModel.fetchTicket(ticketUi)
                    navHostController.navigate(TicketInfo(ticketUi.id, boardId))
                },
                navigateToBoardSettings = { boardInfo ->
                    boardSettingsViewModel.fetchBoard(boardInfo)
                    boardSettingsViewModel.fetchBoardSettings(boardId = boardInfo.id)
                    navHostController.navigate(BoardSettings)
                },
                navigateToBoardsScreen = {
                    navHostController.popBackStack(Boards, inclusive = false)
                }
            )
        }

        composable<CreateTicket> {

            val args = it.toRoute<CreateTicket>()

            CreateTicketScreen(
                navigateUp = navHostController::navigateUp,
                boardId = args.boardId,
                viewModel = createTicketViewModel
            )
        }

        composable<TicketInfo> {
            val args = it.toRoute<TicketInfo>()

            TicketInfoScreen(
                navigateUp = navHostController::navigateUp,
                navigateToEditTicket = {
                    editTicketViewModel.fetchTicket(args.ticketId)
                    navHostController.navigate(EditTicket(args.boardId))
                },
                viewModel = ticketInfoViewModel
            )
        }

        composable<EditTicket> {
            val args = it.toRoute<EditTicket>()
            EditTicketScreen(
                navigateUp = navHostController::navigateUp,
                boardId = args.boardId,
                navigateToBoard = {
                    navHostController.popBackStack(route = Board, inclusive = false)
                },
                viewModel = editTicketViewModel
            )
        }

        composable<BoardSettings> {
            BoardSettingsScreen(
                navigateUp = navHostController::navigateUp,
                navigateToBoardsScreen = {
                    navHostController.popBackStack(Boards, inclusive = false)
                },
                viewModel = boardSettingsViewModel
            )
        }

        composable<Invitation> {
            InvitationScreen(
                navigateUp = navHostController::navigateUp,
                viewModel = invitationViewModel
            )
        }
    }
}

private fun sharedXTransitionIn(
    initial: (fullWidth: Int) -> Int,
    durationMillis: Int = NAVIGATION_TIME,
): EnterTransition {
    val outgoingDuration = (durationMillis * OFFSET_LIMIT).toInt()
    val incomingDuration = durationMillis - outgoingDuration

    return slideInHorizontally(
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ), initialOffsetX = initial
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = incomingDuration,
            delayMillis = outgoingDuration,
            easing = LinearOutSlowInEasing
        )
    )
}

fun sharedXTransitionOut(
    target: (fullWidth: Int) -> Int,
    durationMillis: Int = NAVIGATION_TIME,
): ExitTransition {
    val outgoingDuration = (durationMillis * OFFSET_LIMIT).toInt()

    return slideOutHorizontally(
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ), targetOffsetX = target
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = outgoingDuration,
            delayMillis = 0,
            easing = FastOutLinearInEasing
        )
    )
}

private const val NAVIGATION_TIME: Int = 300
private const val INITIAL_OFFSET = 0.175f
private const val OFFSET_LIMIT = 0.4f