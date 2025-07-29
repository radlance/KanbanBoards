package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.board.core.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface HandleProfileRemoteDataSource {

    suspend fun handle(authCredential: (FirebaseUser) -> AuthCredential)

    class Base @Inject constructor(
        private val handle: HandleError,
        private val provideDatabase: ProvideDatabase,
        private val boardRemoteDataSource: BoardRemoteDataSource
    ) : HandleProfileRemoteDataSource {

        override suspend fun handle(authCredential: (FirebaseUser) -> AuthCredential) {
            try {
                val currentUser = Firebase.auth.currentUser!!
                val uid = currentUser.uid

                provideDatabase.database()
                    .child("users")
                    .child(uid)
                    .removeValue()
                    .await()

                val boardsQuery = provideDatabase.database()
                    .child("boards")
                    .orderByChild("owner")
                    .equalTo(uid)

                boardsQuery.get().await().children.forEach { boardSnapshot ->
                    val boardId = boardSnapshot.key!!

                    boardRemoteDataSource.deleteBoard(boardId)
                }

                val membersQuery = provideDatabase.database()
                    .child("boards-members")
                    .orderByChild("memberId")
                    .equalTo(uid)

                membersQuery.get().await().children.forEach { boardMemberSnapshot ->
                    provideDatabase.database()
                        .child("boards-members")
                        .child(boardMemberSnapshot.key!!)
                        .removeValue()
                        .await()
                }

                val membersSnapshot = membersQuery.get().await()

                membersSnapshot.children.forEach { memberSnapshot ->
                    memberSnapshot.ref.removeValue().await()
                }
                val ticketsQuery = provideDatabase.database()
                    .child("tickets")
                    .orderByChild("assignee")
                    .equalTo(uid)

                ticketsQuery.get().await().children.forEach { ticketSnapshot ->
                    provideDatabase.database()
                        .child("tickets")
                        .child(ticketSnapshot.key!!)
                        .child("assignee")
                        .setValue("")
                        .await()
                }

                val credential = authCredential(currentUser)
                currentUser.reauthenticate(credential).await()
                currentUser.delete().await()
                Firebase.auth.signOut()
            } catch (e: Exception) {
                handle.handle(e)
            }
        }
    }
}