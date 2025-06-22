package com.github.radlance.kanbanboards.createboard.domain

interface CreateBoardRepository {

    suspend fun createBoard(name: String): CreateBoardResult
}