package com.github.radlance.kanbanboards.navigation.data

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.navigation.domain.NavigationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NavigationRepositoryTest : BaseTest() {

    private lateinit var dataStoreManager: TestDataStoreManager
    private lateinit var repository: NavigationRepository

    @Before
    fun setup() {
        dataStoreManager = TestDataStoreManager()
        repository = LocalNavigationRepository(dataStoreManager = dataStoreManager)
    }

    @Test
    fun test_collect_authorized_state() = runBlocking {
        assertEquals(0, dataStoreManager.authorizedCalledCount)
        val authorizedStatus = repository.authorizedStatus()
        assertFalse(authorizedStatus.first())
        assertEquals(1, dataStoreManager.authorizedCalledCount)

        dataStoreManager.saveAuthorized(true)
        assertEquals(1, dataStoreManager.authorizedCalledCount)
        assertTrue(authorizedStatus.first())

        dataStoreManager.saveAuthorized(false)
        assertEquals(1, dataStoreManager.authorizedCalledCount)
        assertFalse(authorizedStatus.first())
    }
}