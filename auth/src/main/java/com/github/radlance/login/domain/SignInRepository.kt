package com.github.radlance.login.domain

import com.github.radlance.common.domain.UnitResult

interface SignUpRepository {

    suspend fun signUp(name: String, email: String, password: String): UnitResult
}

interface SignInRepository {

    suspend fun signInWithToken(userIdToken: String): UnitResult

    suspend fun signInWithEmail(email: String, password: String): UnitResult
}

internal interface AuthRepository : SignInRepository, SignUpRepository