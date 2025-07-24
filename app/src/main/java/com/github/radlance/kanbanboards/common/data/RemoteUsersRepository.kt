package com.github.radlance.kanbanboards.common.data

import com.github.radlance.kanbanboards.common.domain.SearchUsersResult
import com.github.radlance.kanbanboards.common.domain.UsersRepository
import com.github.radlance.kanbanboards.ticket.create.domain.BoardMembersResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteUsersRepository @Inject constructor(
    private val usersRemoteDataSource: UsersRemoteDataSource
) : UsersRepository {

    override fun users(): Flow<SearchUsersResult> {
        return usersRemoteDataSource.users().map {
            SearchUsersResult.Success(it)
        }.catch { e ->
            SearchUsersResult.Error(message = e.message!!)
        }
    }

    override fun boardMembers(boardId: String, ownerId: String): Flow<BoardMembersResult> {
        return usersRemoteDataSource.boardMembers(boardId, ownerId).map {
            BoardMembersResult.Success(it)
        }.catch { e -> BoardMembersResult.Error(e.message!!) }
    }
}