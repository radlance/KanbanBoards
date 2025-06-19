package com.github.radlance.kanbanboards.boards.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.radlance.kanbanboards.R

interface BoardUi {

    @Composable
    fun Show()

    data class My(private val id: String, private val name: String) : BoardUi {

        @Composable
        override fun Show() {
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    data class Other(
        private val id: String,
        private val name: String,
        private val owner: String
    ) : BoardUi {

        @Composable
        override fun Show() {
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }

    object MyOwnBoardsTitle : BoardUi {

        @Composable
        override fun Show() {
            Text(
                text = stringResource(R.string.boards_you_own),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 15.dp)
            )
        }
    }

    object NoBoardsOfMyOwnHint : BoardUi {

        @Composable
        override fun Show() {
            Text(
                text = stringResource(R.string.my_boards_hint),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    object OtherBoardsTitle : BoardUi {

        @Composable
        override fun Show() {
            Text(
                text = stringResource(R.string.boards_you_are_added_to),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(vertical = 15.dp)
            )
        }
    }

    object HowToBeAddedToBoardHint : BoardUi {

        @Composable
        override fun Show() {
            Text(
                text = stringResource(R.string.other_boards_hint),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}