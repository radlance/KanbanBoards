package com.github.radlance.board.create.presentation

import com.github.radlance.core.domain.SearchUsersResult
import com.github.radlance.core.domain.User
import javax.inject.Inject

class SearchUsersResultMapper @Inject constructor() : SearchUsersResult.Mapper<SearchUsersUiState> {
    override fun mapSuccess(users: List<User>): SearchUsersUiState =
        SearchUsersUiState.Success(
            users.map { with(it) { CreateUserUi(id, email, name, false) } }
        )

    override fun mapError(message: String): SearchUsersUiState = SearchUsersUiState.Error(message)
}