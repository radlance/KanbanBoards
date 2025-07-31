package com.github.radlance.kanbanboards.invitation.data

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.invitation.domain.Invitation
import com.github.radlance.kanbanboards.invitation.domain.InvitationRepository
import com.github.radlance.kanbanboards.invitation.domain.InvitationResult
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class InvitationRepositoryTest : BaseTest() {

    private lateinit var remoteDataSource: TestInvitationRemoteDataSource
    private lateinit var manageResource: TestManageResource

    private lateinit var repository: InvitationRepository

    @Before
    fun setup() {
        remoteDataSource = TestInvitationRemoteDataSource()
        manageResource = TestManageResource()

        repository = RemoteInvitationRepository(
            remoteDataSource = remoteDataSource,
            manageResource = manageResource
        )
    }

    @Test
    fun test_invitations_success() = runBlocking {
        remoteDataSource.makeExpectedInvitations(
            listOf(
                Invitation(
                    id = "id1",
                    boardName = "boardName1",
                    boardId = "boardId1",
                    ownerEmail = "owner1@gmail.com",
                    sendDate = ZonedDateTime.of(
                        LocalDateTime.of(2025, 1, 1, 1, 1), ZoneId.of("UTC")
                    )
                )
            )
        )

        assertEquals(
            InvitationResult.Success(
                listOf(
                    Invitation(
                        id = "id1",
                        boardName = "boardName1",
                        boardId = "boardId1",
                        ownerEmail = "owner1@gmail.com",
                        sendDate = ZonedDateTime.of(
                            LocalDateTime.of(2025, 1, 1, 1, 1), ZoneId.of("UTC")
                        )
                    )
                )
            ),
            repository.invitations().first()
        )
        assertEquals(1, remoteDataSource.invitationsCalledCount)
    }

    @Test
    fun test_invitations_error_with_message() = runBlocking {
        remoteDataSource.makeExpectedInvitationsException(IllegalStateException("server error"))
        assertEquals(InvitationResult.Error("server error"), repository.invitations().first())
        assertEquals(1, remoteDataSource.invitationsCalledCount)
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_invitations_error_without_message() = runBlocking {
        manageResource.makeExpectedString(expected = "test error")
        remoteDataSource.makeExpectedInvitationsException(IllegalStateException())
        assertEquals(InvitationResult.Error("test error"), repository.invitations().first())
        assertEquals(1, remoteDataSource.invitationsCalledCount)
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_accept_invite() = runBlocking {
        repository.acceptInvite(boardId = "test board id", invitationId = "test invitation id")
        assertEquals(1, remoteDataSource.acceptInviteCalledList.size)
        assertEquals(
            Pair("test board id", "test invitation id"),
            remoteDataSource.acceptInviteCalledList[0]
        )
    }

    @Test
    fun test_decline_invite() = runBlocking {
        repository.declineInvite(boardId = "board id", invitationId = "invitation id")
        assertEquals(1, remoteDataSource.declineInviteCalledList.size)
        assertEquals(
            Pair("board id", "invitation id"),
            remoteDataSource.declineInviteCalledList[0]
        )
    }

    private class TestInvitationRemoteDataSource : InvitationRemoteDataSource {

        private val invitations = MutableStateFlow<List<Invitation>>(emptyList())
        private var invitationsException: Exception? = null
        var invitationsCalledCount = 0

        val acceptInviteCalledList = mutableListOf<Pair<String, String>>()
        val declineInviteCalledList = mutableListOf<Pair<String, String>>()

        fun makeExpectedInvitations(invitations: List<Invitation>) {
            this.invitations.value = invitations
        }

        fun makeExpectedInvitationsException(exception: Exception) {
            invitationsException = exception
        }

        override fun invitations(): Flow<List<Invitation>> = flow {
            invitationsCalledCount++
            invitationsException?.let { throw it }
            emitAll(invitations)
        }

        override suspend fun acceptInvite(boardId: String, invitationId: String) {
            acceptInviteCalledList.add(Pair(boardId, invitationId))
        }

        override suspend fun declineInvite(boardId: String, invitationId: String) {
            declineInviteCalledList.add(Pair(boardId, invitationId))
        }
    }
}