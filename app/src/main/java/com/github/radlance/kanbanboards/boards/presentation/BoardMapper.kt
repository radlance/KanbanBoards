package com.github.radlance.kanbanboards.boards.presentation

import com.github.radlance.kanbanboards.boards.domain.Board
import javax.inject.Inject

class BoardMapper @Inject constructor() : Board.Mapper<BoardUi> {

    override fun mapMyBoard(id: String, name: String): BoardUi = BoardUi.My(id, name)

    override fun mapOtherBoard(id: String, name: String, owner: String): BoardUi =
        BoardUi.Other(id, name, owner)

    override fun mapNyOwnBoardTitle(): BoardUi = BoardUi.MyOwnBoardsTitle

    override fun mapNoBoardsOfMyOwnHint(): BoardUi = BoardUi.NoBoardsOfMyOwnHint

    override fun mapOtherBoardsTitle(): BoardUi = BoardUi.OtherBoardsTitle

    override fun mapHowToBeAddedToBoardHint(): BoardUi = BoardUi.HowToBeAddedToBoardHint
}