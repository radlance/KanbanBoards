package com.github.radlance.kanbanboards.board.create.presentation

import com.github.radlance.common.domain.SearchUsersResult
import com.github.radlance.kanbanboards.board.create.domain.CreateBoardResult
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