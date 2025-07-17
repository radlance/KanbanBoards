package com.github.radlance.kanbanboards.auth.domain

import com.github.radlance.kanbanboards.common.domain.UnitResult

interface SignUpRepository {

    suspend fun signUp(name: String, email: String, password: String): UnitResult
}

interface SignInRepository {

    suspend fun signInWithToken(userIdToken: String): UnitResult

    suspend fun signInWithEmail(email: String, password: String): UnitResult
}

interface AuthRepository : SignInRepository, SignUpRepository