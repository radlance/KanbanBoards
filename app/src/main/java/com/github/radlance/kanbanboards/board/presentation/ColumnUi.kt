package com.github.radlance.kanbanboards.board.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.radlance.kanbanboards.R

interface ColumnUi {

    val label: String

    @Composable
    fun StartContent(onClick: (ColumnUi) -> Unit)

    @Composable
    fun EndContent(onClick: (ColumnUi) -> Unit)

    abstract class Abstract(override val label: String) : ColumnUi {

        @Composable
        override fun EndContent(onClick: (ColumnUi) -> Unit) {
            IconButton(onClick = { endClick(onClick) }) {
                Icon(
                    imageVector = Icons.Default.KeyboardDoubleArrowRight,
                    contentDescription = stringResource(R.string.keyboard_double_arrow_right_icon)
                )
            }
        }

        @Composable
        override fun StartContent(onClick: (ColumnUi) -> Unit) {
            IconButton(onClick = { startClick(onClick) }) {
                Icon(
                    imageVector = Icons.Default.KeyboardDoubleArrowLeft,
                    contentDescription = stringResource(R.string.keyboard_double_arrow_left_icon)
                )
            }
        }

        protected open fun startClick(action: (ColumnUi) -> Unit) = Unit

        protected open fun endClick(action: (ColumnUi) -> Unit) = Unit
    }

    object Todo : Abstract(label = "To Do") {

        @Composable
        override fun StartContent(onClick: (ColumnUi) -> Unit) = Unit

        override fun endClick(action: (ColumnUi) -> Unit) = action(InProgress)
    }

    object InProgress : Abstract(label = "In Progress") {

        override fun startClick(action: (ColumnUi) -> Unit) = action(Todo)

        override fun endClick(action: (ColumnUi) -> Unit) = action(Done)
    }

    object Done : Abstract(label = "Done") {

        @Composable
        override fun EndContent(onClick: (ColumnUi) -> Unit) = Unit

        override fun endClick(action: (ColumnUi) -> Unit) = action(InProgress)
    }
}