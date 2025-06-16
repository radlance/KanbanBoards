package com.github.radlance.kanbanboards.login.data

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.domain.DomainException
import com.github.radlance.kanbanboards.login.domain.AuthResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandleAuthResultTest : BaseTest() {

    private lateinit var manageResource: TestManageResource
    private lateinit var handleAuthResult: HandleAuthResult

    @Before
    fun setup() {
        manageResource = TestManageResource()
        handleAuthResult = HandleAuthResult.Base(managerResource = manageResource)
    }

    @Test
    fun test_handle_success(): Unit = runBlocking {
        val result = handleAuthResult.handle {}
        assertEquals(AuthResult.Success, result)
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_handle_no_connection(): Unit = runBlocking {
        manageResource.makeExpectedAnswer("no internet connection")
        val result = handleAuthResult.handle { throw DomainException.NoInternetException() }
        assertEquals(AuthResult.Error("no internet connection"), result)
        assertEquals(1, manageResource.stringCalledCount)
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