package com.github.radlance.kanbanboars.profile.edit.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.radlance.kanbanboards.core.presentation.BaseColumn
import com.github.radlance.kanbanboards.core.presentation.ErrorMessage

interface ProfileEditUiState {

    @Composable
    fun Show(
        editProfileAction: EditProfileAction,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
    )

    data class Base(private val name: String, private val email: String) : ProfileEditUiState {

        @Composable
        override fun Show(
            editProfileAction: EditProfileAction,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) {
            val scrollState = rememberScrollState()

            BaseColumn(
                scrollState = scrollState,
                modifier = modifier.fillMaxSize()
            ) {
                EditProfileContent(
                    columnScope = this@BaseColumn,
                    scrollState = scrollState,
                    currentName = name,
                    editProfileAction = editProfileAction,
                    navigateUp = navigateUp
                )
            }
        }
    }

    data class Error(private val message: String) : ProfileEditUiState {

        @Composable
        override fun Show(
            editProfileAction: EditProfileAction,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorMessage(message)
            }
        }
    }

    object Loading : ProfileEditUiState {

        @Composable
        override fun Show(
            editProfileAction: EditProfileAction,
            navigateUp: () -> Unit,
            modifier: Modifier
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}