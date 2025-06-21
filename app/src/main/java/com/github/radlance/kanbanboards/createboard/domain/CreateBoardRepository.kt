package com.github.radlance.kanbanboards.createboard.domain

interface CreateBoardRepository {

    suspend fun checkBoard(name: String): Boolean

    suspend fun createBoard(name: String): CreateBoardResult
}