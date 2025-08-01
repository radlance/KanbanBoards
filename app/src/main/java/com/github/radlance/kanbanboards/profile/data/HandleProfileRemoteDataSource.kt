package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.board.core.data.BoardRemoteDataSource
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.service.MyUser
import com.github.radlance.kanbanboards.service.Service
import javax.inject.Inject

interface HandleProfileRemoteDataSource {

    suspend fun handle(deleteAction: () -> Unit)

    class Base @Inject constructor(
        private val handle: HandleError,
        private val service: Service,
        private val boardRemoteDataSource: BoardRemoteDataSource,
        private val myUser: MyUser
    ) : HandleProfileRemoteDataSource {

        override suspend fun handle(deleteAction: () -> Unit) {
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
    }
}