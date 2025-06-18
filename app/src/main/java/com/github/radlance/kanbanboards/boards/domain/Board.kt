package com.github.radlance.kanbanboards.boards.domain

interface Board {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapMyBoard(id: String, name: String): T

        fun mapOtherBoard(id: String, name: String, owner: String): T

        fun mapError(message: String): T

        fun mapNyOwnBoardTitle(): T

        fun mapNoBoardsOfMyOwnHint(): T

        fun mapOtherBoardsTitle(): T

        fun mapHowToBeAddedToBoardHint(): T
    }

    data class My(private val id: String, private val name: String) : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapMyBoard(id, name)
    }

    data class Other(
        private val id: String,
        private val name: String,
        private val owner: String
    ) : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapOtherBoard(id, name, owner)
    }

    data class Error(private val message: String) : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapError(message)
    }

    object MyOwnBoardsTitle : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapNyOwnBoardTitle()
    }

    object NoBoardsOfMyOwnHint : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapNoBoardsOfMyOwnHint()
    }

    object OtherBoardsTitle : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapOtherBoardsTitle()
    }

    object HowToBeAddedToBoardHint : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapHowToBeAddedToBoardHint()
    }
}