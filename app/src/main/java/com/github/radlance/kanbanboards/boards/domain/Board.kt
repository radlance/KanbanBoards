package com.github.radlance.kanbanboards.boards.domain

interface Board {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapMyBoard(id: String, name: String): T

        fun mapOtherBoard(id: String, name: String, owner: String): T

        fun mapNyOwnBoardTitle(): T

        fun mapNoBoardsOfMyOwnHint(): T

        fun mapOtherBoardsTitle(): T

        fun mapHowToBeAddedToBoardHint(): T
    }

    interface Storage : Board {

        fun compareName(name: String): Boolean
    }

    data class My(private val id: String, private val name: String) : Storage {

        override fun compareName(name: String): Boolean = this.name == name

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapMyBoard(id, name)
    }

    data class Other(
        private val id: String,
        private val name: String,
        private val owner: String
    ) : Storage {

        override fun compareName(name: String): Boolean = this.name == name

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapOtherBoard(id, name, owner)
    }

    data object MyOwnBoardsTitle : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapNyOwnBoardTitle()
    }

    data object NoBoardsOfMyOwnHint : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapNoBoardsOfMyOwnHint()
    }

    data object OtherBoardsTitle : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapOtherBoardsTitle()
    }

    data object HowToBeAddedToBoardHint : Board {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapHowToBeAddedToBoardHint()
    }
}