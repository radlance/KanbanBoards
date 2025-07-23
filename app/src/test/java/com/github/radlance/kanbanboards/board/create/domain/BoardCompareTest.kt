package com.github.radlance.kanbanboards.board.create.domain

import com.github.radlance.kanbanboards.boards.domain.Board
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BoardCompareTest {

    @Test
    fun test_compare_my_board_name() {
        val myBoard = Board.My(id = "456", name = "my board")
        assertTrue(myBoard.compareName(name = "my board"))
        assertFalse(myBoard.compareName(name = "my board1"))
        assertFalse(myBoard.compareName(name = "my BOARD"))
    }

    @Test
    fun test_compare_other_board_name() {
        val otherBoard = Board.Other(id = "456", name = "other board", owner = "owner")
        assertTrue(otherBoard.compareName(name = "other board"))
        assertFalse(otherBoard.compareName(name = "other 1 board"))
        assertFalse(otherBoard.compareName(name = "OTHER board"))
    }
}