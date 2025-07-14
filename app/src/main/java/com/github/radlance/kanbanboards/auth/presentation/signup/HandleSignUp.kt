package com.github.radlance.kanbanboards.auth.presentation.signup

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.auth.presentation.common.BaseHandle
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface HandleSignUp : BaseHandle {

    fun fieldsState(): MutableStateFlow<SignUpFieldsUiState>

    class Base @Inject constructor(
        savedStateHandle: SavedStateHandle
    ) : HandleSignUp, BaseHandle.Abstract(savedStateHandle, KEY_AUTH) {

        override fun fieldsState(): MutableStateFlow<SignUpFieldsUiState> {
            return savedStateHandle.getMutableStateFlow(KEY_FIELDS, SignUpFieldsUiState())
        }

        private companion object {
            const val KEY_AUTH = "auth"
            const val KEY_FIELDS = "sign up fields"
        }
    }
}