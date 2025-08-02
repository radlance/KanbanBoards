package com.github.radlance.board.create.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.radlance.core.presentation.ErrorMessage

interface SearchUsersUiState {

    @Composable
    fun Show(
        searchFieldValue: String,
        usersActions: UsersActions
    )

    fun users(): List<CreateUserUi>

    abstract class Abstract : SearchUsersUiState {

        override fun users(): List<CreateUserUi> = emptyList()
    }

    data class Success(private val users: List<CreateUserUi>) : Abstract() {

        override fun users(): List<CreateUserUi> = users.filter { it.checked }

        @Composable
        override fun Show(searchFieldValue: String, usersActions: UsersActions) {
            val filteredUsers = if (searchFieldValue.isEmpty()) {
                users.filter { it.checked }
            } else users.filter { it.email.contains(searchFieldValue, ignoreCase = true) }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = filteredUsers, key = { it.email }) { user ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.animateItem()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.email,
                                style = MaterialTheme.typography.titleMedium
                            )

                            if (user.name.isNotEmpty()) {
                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(Modifier.height(4.dp))
                            }
                        }
                        Checkbox(
                            checked = user.checked,
                            onCheckedChange = {
                                usersActions.switch(userId = user.id, users = users)
                            }
                        )
                    }
                }
            }
        }
    }

    data class Error(private val message: String) : Abstract() {

        @Composable
        override fun Show(searchFieldValue: String, usersActions: UsersActions) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorMessage(message)
            }
        }
    }


    object Loading : Abstract() {
        @Composable
        override fun Show(searchFieldValue: String, usersActions: UsersActions) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}