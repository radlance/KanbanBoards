package com.github.radlance.kanbanboards.auth.presentation.signup

import com.github.radlance.kanbanboards.auth.presentation.common.BaseHandle
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface HandleSignUp : BaseHandle {

    val fieldsState: MutableStateFlow<SignUpFieldsUiState>

    class Base @Inject constructor() : HandleSignUp, BaseHandle.Abstract() {

        private val fieldsStateMutable = MutableStateFlow(SignUpFieldsUiState())

        override val fieldsState = fieldsStateMutable
    }
}