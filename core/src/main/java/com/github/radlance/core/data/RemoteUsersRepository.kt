package com.github.radlance.core.data

import com.github.radlance.core.domain.BoardMembersResult
import com.github.radlance.core.domain.SearchUsersResult
import com.github.radlance.core.domain.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RemoteUsersRepository @Inject constructor(
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