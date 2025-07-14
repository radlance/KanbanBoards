package com.github.radlance.kanbanboards.auth.domain

interface SignUpRepository {

    suspend fun signUp(name: String, email: String, password: String): AuthResult
}

interface SignInRepository {

    suspend fun signInWithToken(userIdToken: String): AuthResult

    suspend fun signInWithEmail(email: String, password: String): AuthResult
}

interface AuthRepository : SignInRepository, SignUpRepository