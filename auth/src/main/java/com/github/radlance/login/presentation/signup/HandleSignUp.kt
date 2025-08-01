package com.github.radlance.login.presentation.signup

import com.github.radlance.login.presentation.common.AbstractBaseHandle
import com.github.radlance.login.presentation.common.BaseHandle
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface HandleSignUp : BaseHandle {

    val fieldsState: MutableStateFlow<SignUpFieldsUiState>
}

internal class BaseHandleSignUp @Inject constructor() : HandleSignUp, AbstractBaseHandle() {

    private val fieldsStateMutable = MutableStateFlow(SignUpFieldsUiState())

    override val fieldsState = fieldsStateMutable
}