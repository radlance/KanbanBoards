package com.github.radlance.kanbanboars.profile.edit.data

import com.github.radlance.kanbanboards.api.service.MyUser
import com.github.radlance.kanbanboards.api.service.Service
import com.github.radlance.kanbanboards.board.core.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.core.data.HandleError
import com.github.radlance.kanbanboards.core.data.UserProfileEntity
import javax.inject.Inject

internal interface HandleEditProfileRemoteDataSource {

    suspend fun handleDelete(deleteAction: () -> Unit)

    suspend fun handleEdit(email: String, name: String, editAction: suspend () -> Unit)

    class Base @Inject constructor(
        private val handle: HandleError,
        private val service: Service,
        private val boardRemoteDataSource: BoardRemoteDataSource,
        private val myUser: MyUser
    ) : HandleEditProfileRemoteDataSource {

        override suspend fun handleDelete(deleteAction: () -> Unit) {
            try {
                val uid = myUser.id

                service.delete(
                    path = "users",
                    itemId = uid
                )

                val boardsQuery = service.getListByQueryAwait(
                    path = "boards",
                    queryKey = "owner",
                    queryValue = uid
                )

                boardsQuery.forEach { boardSnapshot ->
                    val boardId = boardSnapshot.id

                    boardRemoteDataSource.deleteBoard(boardId)
                }

                val membersQuery = service.getListByQueryAwait(
                    path = "boards-members",
                    queryKey = "memberId",
                    queryValue = uid
                )

                membersQuery.forEach { boardMemberSnapshot ->
                    service.delete(
                        path = "boards-members",
                        itemId = boardMemberSnapshot.id
                    )

                    boardMemberSnapshot.ref.removeValue()
                }

                val ticketsQuery = service.getListByQueryAwait(
                    path = "tickets",
                    queryKey = "assignee",
                    queryValue = uid
                )

                ticketsQuery.forEach { ticketSnapshot ->
                    service.update(
                        path = "tickets",
                        subPath1 = ticketSnapshot.id,
                        subPath2 = "assignee",
                        obj = ""
                    )
                }

                deleteAction.invoke()
                myUser.signOut()
            } catch (e: Exception) {
                handle.handle(e)
            }
        }

        override suspend fun handleEdit(
            email: String,
            name: String,
            editAction: suspend () -> Unit
        ) {
            try {
                editAction.invoke()
                service.update(
                    path = "users",
                    subPath = myUser.id,
                    obj = UserProfileEntity(email = email, name = name)
                )
            } catch (e: Exception) {
                handle.handle(e)
            }
        }
    }
}