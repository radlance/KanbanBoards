package com.github.radlance.kanbanboards.board.core.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.radlance.kanbanboards.R
import kotlinx.serialization.Serializable

interface ColumnUi {

    val label: String

    @Composable
    fun StartContent(onClick: (ColumnUi) -> Unit)

    @Composable
    fun EndContent(onClick: (ColumnUi) -> Unit)

    fun<T: Any> map(mapper: Mapper<T>): T

    interface Mapper<T: Any> {

        fun mapTodo(): T

        fun mapInProgress(): T

        fun mapDone(): T
    }

    @Serializable
    abstract class Abstract(override val label: String) : ColumnUi {

        @Composable
        private fun Modifier.iconModifier(onClick: () -> Unit): Modifier =
            border(width = 1.dp, shape = CircleShape, color = MaterialTheme.colorScheme.secondary)
                .padding(5.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onClick
                )

        @Composable
        override fun EndContent(onClick: (ColumnUi) -> Unit) {
            Icon(
                imageVector = Icons.Rounded.KeyboardDoubleArrowRight,
                contentDescription = stringResource(R.string.keyboard_double_arrow_right_icon),
                modifier = Modifier.iconModifier { endClick(onClick) }
            )
        }

        @Composable
        override fun StartContent(onClick: (ColumnUi) -> Unit) {
            Icon(
                imageVector = Icons.Default.KeyboardDoubleArrowLeft,
                contentDescription = stringResource(R.string.keyboard_double_arrow_left_icon),
                modifier = Modifier.iconModifier { startClick(onClick) }
            )
        }

        protected open fun startClick(action: (ColumnUi) -> Unit) = Unit

        protected open fun endClick(action: (ColumnUi) -> Unit) = Unit
    }

    @Serializable
    object Todo : Abstract(label = "To Do") {

        @Composable
        override fun StartContent(onClick: (ColumnUi) -> Unit) = Unit

        override fun endClick(action: (ColumnUi) -> Unit) = action(InProgress)

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapTodo()
    }

    @Serializable
    object InProgress : Abstract(label = "In Progress") {

        override fun startClick(action: (ColumnUi) -> Unit) = action(Todo)

        override fun endClick(action: (ColumnUi) -> Unit) = action(Done)

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapInProgress()
    }

    @Serializable
    object Done : Abstract(label = "Done") {

        @Composable
        override fun EndContent(onClick: (ColumnUi) -> Unit) = Unit

        override fun startClick(action: (ColumnUi) -> Unit) = action(InProgress)

        override fun <T : Any> map(mapper: Mapper<T>): T = mapper.mapDone()
    }
}