package com.github.radlance.kanbanboards.invitation.presentation

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.invitation.domain.Invitation
import com.github.radlance.kanbanboards.invitation.domain.InvitationRepository
import com.github.radlance.kanbanboards.invitation.domain.InvitationResult
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class InvitationViewModelTest : BaseTest() {

    private lateinit var repository: TestInvitationRepository
    private lateinit var viewModel: InvitationViewModel

    @Before
    fun setup() {
        repository = TestInvitationRepository()

        viewModel = InvitationViewModel(
            invitationRepository = repository,
            mapper = InvitationMapper(),
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(InvitationUiState.Error("initial state"), viewModel.invitationsUiState.value)
        assertEquals(0, viewModel.invitationCount.value.count)
        assertEquals(2, repository.invitationsCalledCount)
    }

    @Test
    fun test_collect_invitations() {
        repository.makeExpectedInvitationResult(InvitationResult.Error("another one error"))
        assertEquals(
            InvitationUiState.Error("another one error"),
            viewModel.invitationsUiState.value
        )
        assertEquals(0, viewModel.invitationCount.value.count)
        assertEquals(2, repository.invitationsCalledCount)
        repository.makeExpectedInvitationResult(
            InvitationResult.Success(
                invitations = listOf(
                    Invitation(
                        id = "id1",
                        boardName = "boardName1",
                        boardId = "boardId1",
                        ownerEmail = "owner1@gmail.com",
                        sendDate = ZonedDateTime.of(
                            LocalDateTime.of(2025, 1, 1, 1, 1), ZoneId.of("UTC")
                        )
                    ),
                    Invitation(
                        id = "id2",
                        boardName = "boardName2",
                        boardId = "boardId2",
                        ownerEmail = "owner2@gmail.com",
                        sendDate = ZonedDateTime.of(
                            LocalDateTime.of(2024, 1, 1, 1, 1), ZoneId.of("UTC")
                        )
                    ),
                )
            )
        )
        assertEquals(
            InvitationUiState.Success(
                listOf(
                    Invitation(
                        id = "id1",
                        boardName = "boardName1",
                        boardId = "boardId1",
                        ownerEmail = "owner1@gmail.com",
                        sendDate = ZonedDateTime.of(
                            LocalDateTime.of(2025, 1, 1, 1, 1), ZoneId.of("UTC")
                        )
                    ),
                    Invitation(
                        id = "id2",
                        boardName = "boardName2",
                        boardId = "boardId2",
                        ownerEmail = "owner2@gmail.com",
                        sendDate = ZonedDateTime.of(
                            LocalDateTime.of(2024, 1, 1, 1, 1), ZoneId.of("UTC")
                        )
                    ),
                )
            ),
            viewModel.invitationsUiState.value
        )
        assertEquals(2, viewModel.invitationCount.value.count)
    }

    @Test
    fun test_accept_invite() {
        viewModel.accept(boardId = "test board id", invitationId = "test invitation id")
        assertEquals(1, repository.acceptInviteCalledList.size)
        assertEquals(
            Pair("test board id", "test invitation id"),
            repository.acceptInviteCalledList[0]
        )
    }

    @Test
    fun test_decline_invite() {
        viewModel.decline(invitationId = "invitation id")
        assertEquals(1, repository.declineInviteCalledList.size)
        assertEquals(
            "invitation id",
            repository.declineInviteCalledList[0]
        )
    }

    private class TestInvitationRepository : InvitationRepository {

        private var invitationResult = MutableStateFlow<InvitationResult>(
            InvitationResult.Error("initial state")
        )

        fun makeExpectedInvitationResult(invitationResult: InvitationResult) {
            this.invitationResult.value = invitationResult
        }

        var invitationsCalledCount = 0

        val acceptInviteCalledList = mutableListOf<Pair<String, String>>()
        val declineInviteCalledList = mutableListOf<String>()

        override fun invitations(): Flow<InvitationResult> {
            invitationsCalledCount++
            return invitationResult
        }

        override fun accept(boardId: String, invitationId: String) {
            acceptInviteCalledList.add(Pair(boardId, invitationId))
        }

        override fun decline(invitationId: String) {
            declineInviteCalledList.add(invitationId)
        }
    }
}