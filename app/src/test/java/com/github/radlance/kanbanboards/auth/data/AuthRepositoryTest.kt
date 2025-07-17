package com.github.radlance.kanbanboards.auth.data

import com.github.radlance.kanbanboards.auth.domain.AuthRepository
import com.github.radlance.kanbanboards.common.domain.UnitResult
import com.github.radlance.kanbanboards.common.BaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthRepositoryTest : BaseTest() {

    private lateinit var remoteDataSource: TestAuthRemoteDataSource
    private lateinit var handleAuthResult: TestHandleAuthResult

    private lateinit var repository: AuthRepository

    @Before
    fun setup() {

        remoteDataSource = TestAuthRemoteDataSource()
        handleAuthResult = TestHandleAuthResult()

        repository = BaseAuthRepository(
            remoteDataSource = remoteDataSource,
            handleAuthResult = handleAuthResult
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(emptyList<String>(), remoteDataSource.signInWithTokenCalledList)
        assertEquals(0, handleAuthResult.handleCalledCount)

    }

    @Test
    fun test_sign_in_with_token_success(): Unit = runBlocking {
        val result = repository.signInWithToken(userIdToken = "1234567890")
        assertEquals(UnitResult.Success, result)

        assertEquals(1, remoteDataSource.signInWithTokenCalledList.size)
        assertEquals("1234567890", remoteDataSource.signInWithTokenCalledList[0])
        assertEquals(1, handleAuthResult.handleCalledCount)

    }

    @Test
    fun test_sign_in_with_token_error() = runBlocking {
        remoteDataSource.signInWithTokenException = IllegalStateException("something went wrong")
        val result = repository.signInWithToken(userIdToken = "1234567890")
        assertEquals(UnitResult.Error(message = "something went wrong"), result)

        assertEquals(1, remoteDataSource.signInWithTokenCalledList.size)
        assertEquals("1234567890", remoteDataSource.signInWithTokenCalledList[0])
        assertEquals(1, handleAuthResult.handleCalledCount)
    }

    @Test
    fun test_sign_in_with_email_success() = runBlocking {
        val result = repository.signInWithEmail(email = "test@email.com", password = "123456")
        assertEquals(UnitResult.Success, result)

        assertEquals(1, remoteDataSource.signInWithEmailCalledList.size)
        assertEquals(Pair("test@email.com", "123456"), remoteDataSource.signInWithEmailCalledList[0])
        assertEquals(1, handleAuthResult.handleCalledCount)
    }

    @Test
    fun test_sign_in_with_email_error() = runBlocking {
        remoteDataSource.signInWithEmailException = IllegalStateException("test error")
        val result = repository.signInWithEmail(email = "test@email.com", password = "123456")
        assertEquals(UnitResult.Error(message = "test error"), result)

        assertEquals(1, remoteDataSource.signInWithEmailCalledList.size)
        assertEquals(Pair("test@email.com", "123456"), remoteDataSource.signInWithEmailCalledList[0])
        assertEquals(1, handleAuthResult.handleCalledCount)
    }

    @Test
    fun test_sign_up_success() = runBlocking {
        val result = repository.signUp(
            name = "test name",
            email = "test@email.com",
            password = "123456"
        )

        assertEquals(UnitResult.Success, result)
        assertEquals(1, remoteDataSource.signUpCalledList.size)
        assertEquals(
            Triple("test name", "test@email.com", "123456"),
            remoteDataSource.signUpCalledList[0]
        )
        assertEquals(1, handleAuthResult.handleCalledCount)
    }

    @Test
    fun test_sign_up_error() = runBlocking {
        remoteDataSource.signUpException = IllegalStateException("some error")
        val result = repository.signUp(
            name = "test name",
            email = "test@email.com",
            password = "123456"
        )
        assertEquals(UnitResult.Error(message = "some error"), result)
        assertEquals(1, remoteDataSource.signUpCalledList.size)
        assertEquals(
            Triple("test name", "test@email.com", "123456"),
            remoteDataSource.signUpCalledList[0]
        )
        assertEquals(1, handleAuthResult.handleCalledCount)
    }

    private class TestAuthRemoteDataSource : AuthRemoteDataSource {
        val signInWithTokenCalledList = mutableListOf<String>()
        var signInWithTokenException: Exception? = null

        val signInWithEmailCalledList = mutableListOf<Pair<String, String>>()
        var signInWithEmailException: Exception? = null

        val signUpCalledList = mutableListOf<Triple<String, String, String>>()
        var signUpException: Exception? = null

        override suspend fun signInWithToken(userTokenId: String) {
            signInWithTokenCalledList.add(userTokenId)
            signInWithTokenException?.let { throw it }
        }

        override suspend fun signInWithEmail(email: String, password: String) {
            signInWithEmailCalledList.add(Pair(email, password))
            signInWithEmailException?.let { throw it }
        }

        override suspend fun signUp(name: String, email: String, password: String) {
            signUpCalledList.add(Triple(name, email, password))
            signUpException?.let { throw it }
        }
    }

    private class TestHandleAuthResult : HandleAuthResult {

        var handleCalledCount = 0

        override suspend fun handle(action: suspend () -> Unit): UnitResult {
            handleCalledCount++
            return try {
                action.invoke()
                UnitResult.Success
            } catch (e: Exception) {
                UnitResult.Error(e.message ?: "")
            }
        }
    }
}