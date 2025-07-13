package com.github.radlance.kanbanboards.auth.data

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.domain.DomainException
import com.github.radlance.kanbanboards.auth.domain.AuthResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandleAuthResultTest : BaseTest() {

    private lateinit var manageResource: TestManageResource
    private lateinit var dataStoreManager: TestDataStoreManager

    private lateinit var handleAuthResult: HandleAuthResult

    @Before
    fun setup() {
        manageResource = TestManageResource()
        dataStoreManager = TestDataStoreManager()

        handleAuthResult = HandleAuthResult.Base(
            managerResource = manageResource,
            dataStoreManager = dataStoreManager
        )
    }

    @Test
    fun test_handle_success(): Unit = runBlocking {
        val result = handleAuthResult.handle {}
        assertEquals(AuthResult.Success, result)
        assertEquals(0, manageResource.stringCalledCount)
        assertEquals(1, dataStoreManager.saveAuthorizedCalledList.size)
        assertEquals(true, dataStoreManager.saveAuthorizedCalledList[0])
    }

    @Test
    fun test_handle_no_connection(): Unit = runBlocking {
        manageResource.makeExpectedString("no internet connection")
        val result = handleAuthResult.handle { throw DomainException.NoInternetException() }
        assertEquals(AuthResult.Error("no internet connection"), result)
        assertEquals(1, manageResource.stringCalledCount)
        assertEquals(0, dataStoreManager.saveAuthorizedCalledList.size)
    }

    @Test
    fun test_handle_server_error(): Unit = runBlocking {
        val result = handleAuthResult.handle {
            throw DomainException.ServerUnavailableException(message = "server unavailable")
        }
        assertEquals(AuthResult.Error("server unavailable"), result)
        assertEquals(0, manageResource.stringCalledCount)
    }
}