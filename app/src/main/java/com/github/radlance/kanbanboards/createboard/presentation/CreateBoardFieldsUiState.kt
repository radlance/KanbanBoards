package com.github.radlance.kanbanboards.createboard.presentation

data class CreateBoardFieldsUiState(
    val nameFieldUiState: CreateBoardUiState = CreateBoardUiState.CanNotCreate,
    val searchFieldErrorMessage: String = ""
)