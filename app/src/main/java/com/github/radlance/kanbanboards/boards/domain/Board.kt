package com.github.radlance.kanbanboards.boards.domain

interface Board {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface StateMapper<T : Any> {

        fun mapNyOwnBoardTitle(): T

        fun mapNoBoardsOfMyOwnHint(): T

        fun mapOtherBoardsTitle(): T

        fun mapHowToBeAddedToBoardHint(): T
    }

    interface StorageMapper<T : Any> {

        fun mapMyBoard(id: String, name: String): T

        fun mapOtherBoard(id: String, name: String, owner: String): T
    }

    interface Mapper<T : Any> : StateMapper<T>, StorageMapper<T>

    abstract class Storage(private val name: String) : Board {

        fun <T : Any> map(storageMapper: StorageMapper<T>): T = mapStorage(storageMapper)

        override fun <T : Any> map(mapper: Mapper<T>): T = mapStorage(mapper)

        protected abstract fun <T : Any> mapStorage(mapper: StorageMapper<T>): T

        fun compareName(name: String): Boolean = this.name == name
    }

    data class My(private val id: String, private val name: String) : Storage(name) {

        override fun <T : Any> mapStorage(mapper: StorageMapper<T>): T = mapper.mapMyBoard(id, name)
    }

    data class Other(
        private val id: String,
        private val name: String,
        private val owner: String
    ) : Storage(name) {

        override fun <T : Any> mapStorage(mapper: StorageMapper<T>): T = mapper.mapOtherBoard(
            id = id, name = name, owner = owner
        )
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