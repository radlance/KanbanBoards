package com.github.radlance.board.create.presentation

import com.github.radlance.board.create.domain.CreateBoardResult
import com.github.radlance.core.domain.SearchUsersResult
import javax.inject.Inject

interface CreateBoardMapperFacade {

    fun mapCreateBoardResult(createBoardResult: CreateBoardResult): CreateBoardUiState

    fun mapSearchUsersResult(searchUsersResult: SearchUsersResult): SearchUsersUiState

    class Base @Inject constructor(
        private val createBoardMapper: CreateBoardResult.Mapper<CreateBoardUiState>,
        private val searchUsersMapper: SearchUsersResult.Mapper<SearchUsersUiState>
    ) : CreateBoardMapperFacade {
        override fun mapCreateBoardResult(createBoardResult: CreateBoardResult): CreateBoardUiState {
            return createBoardResult.map(createBoardMapper)
        }

        override fun mapSearchUsersResult(searchUsersResult: SearchUsersResult): SearchUsersUiState {
            return searchUsersResult.map(searchUsersMapper)
        }
    }
}