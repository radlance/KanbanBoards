package com.github.radlance.kanbanboards.board.core.domain

interface Column {

    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {

        fun mapTodo(): T

        fun mapInProgress(): T

        fun mapDone(): T
    }

    object Todo : Column {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapTodo()
    }

    object InProgress : Column {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapInProgress()
    }

    object Done : Column {

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapDone()
    }

}