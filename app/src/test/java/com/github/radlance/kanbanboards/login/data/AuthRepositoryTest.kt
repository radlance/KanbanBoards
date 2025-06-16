package com.github.radlance.kanbanboards.login.data

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.data.RemoteDataSource
import com.github.radlance.kanbanboards.login.domain.AuthRepository
import com.github.radlance.kanbanboards.login.domain.AuthResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthRepositoryTest : BaseTest() {

    private lateinit var remoteDataSource: TestRemoteDataSource
    private lateinit var handleAuthResult: TestHandleAuthResult
    private lateinit var dataStoreManager: TestDataStoreManager

    private lateinit var repository: AuthRepository

    @Before
    fun setup() {

        remoteDataSource = TestRemoteDataSource()
        handleAuthResult = TestHandleAuthResult()
        dataStoreManager = TestDataStoreManager()

        repository = BaseAuthRepository(
            remoteDataSource = remoteDataSource,
            handleAuthResult = handleAuthResult,
            dataStoreManager = dataStoreManager
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(emptyList<String>(), remoteDataSource.signInCalledList)
        assertEquals(0, handleAuthResult.handleCalledCount)
        assertEquals(0, dataStoreManager.authorizedCalledCount)
        assertEquals(emptyList<Boolean>(), dataStoreManager.saveAuthorizedCalledList)
    }

    @Test
    fun test_sign_in_success(): Unit = runBlocking {
        val result = repository.signIn(userIdToken = "1234567890")
        assertEquals(AuthResult.Success, result)

        assertEquals(1, remoteDataSource.signInCalledList.size)
        assertEquals("1234567890", remoteDataSource.signInCalledList[0])
        assertEquals(1, handleAuthResult.handleCalledCount)
        assertEquals(1, dataStoreManager.saveAuthorizedCalledList.size)
        assertEquals(true, dataStoreManager.saveAuthorizedCalledList[0])
    }

    @Test
    fun test_sign_in_error() = runBlocking {
        remoteDataSource.signInException = IllegalStateException("something went wrong")
        val result = repository.signIn(userIdToken = "1234567890")
        assertEquals(AuthResult.Error(message = "something went wrong"), result)

        assertEquals(1, remoteDataSource.signInCalledList.size)
        assertEquals("1234567890", remoteDataSource.signInCalledList[0])
        assertEquals(1, handleAuthResult.handleCalledCount)
        assertEquals(0, dataStoreManager.saveAuthorizedCalledList.size)
    }

    private class TestRemoteDataSource : RemoteDataSource {

        val signInCalledList = mutableListOf<String>()
        var signInException: Exception? = null

        override suspend fun signIn(userTokenId: String) {
            signInCalledList.add(userTokenId)
            signInException?.let { throw it }
        }
    }

    private class TestHandleAuthResult : HandleAuthResult {

        var handleCalledCount = 0

        override suspend fun handle(action: suspend () -> Unit): AuthResult {
            handleCalledCount++
            return try {
                action.invoke()
                AuthResult.Success
            } catch (e: Exception) {
                AuthResult.Error(e.message ?: "")
            }
        }
    }
}