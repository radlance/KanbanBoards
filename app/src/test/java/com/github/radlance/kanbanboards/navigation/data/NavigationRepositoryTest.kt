//package com.github.radlance.kanbanboards.navigation.data
//
//import com.github.radlance.kanbanboards.common.BaseTest
//import com.github.radlance.kanbanboards.navigation.domain.NavigationRepository
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertFalse
//import org.junit.Assert.assertTrue
//import org.junit.Before
//import org.junit.Test
//
//class NavigationRepositoryTest : BaseTest() {
//
//    private lateinit var dataStoreManager: TestDataStoreManager
//    private lateinit var remoteDataSource: TestNavigationRemoteDataSource
//
//    private lateinit var repository: NavigationRepository
//
//    @Before
//    fun setup() {
//        dataStoreManager = TestDataStoreManager()
//        remoteDataSource = TestNavigationRemoteDataSource()
//
//        repository = BaseNavigationRepository(
//            dataStoreManager = dataStoreManager,
//            remoteDataSource = remoteDataSource
//        )
//    }
//
//    @Test
//    fun test_first_call() = runBlocking {
//        assertEquals(0, dataStoreManager.authorizedCalledCount)
//        val authorizedStatus = repository.authorizedStatus()
//        assertFalse(authorizedStatus.first())
//        assertEquals(1, dataStoreManager.authorizedCalledCount)
//        assertEquals(1, remoteDataSource.userExistsCalledCount)
//    }
//
//    @Test
//    fun test_authorized_user_exists() = runBlocking {
//        remoteDataSource.userExists = true
//        val authorizedStatus = repository.authorizedStatus()
//
//        dataStoreManager.saveAuthorized(authorized = true)
//        assertEquals(1, dataStoreManager.authorizedCalledCount)
//        assertEquals(1, remoteDataSource.userExistsCalledCount)
//        assertTrue(authorizedStatus.first())
//    }
//
//    @Test
//    fun test_authorized_user_not_exists() = runBlocking {
//        remoteDataSource.userExists = false
//        val authorizedStatus = repository.authorizedStatus()
//
//        dataStoreManager.saveAuthorized(authorized = true)
//        assertEquals(1, dataStoreManager.authorizedCalledCount)
//        assertEquals(1, remoteDataSource.userExistsCalledCount)
//        assertFalse(authorizedStatus.first())
//    }
//
//    private class TestNavigationRemoteDataSource : NavigationRemoteDataSource {
//
//        var userExistsCalledCount = 0
//        var userExists = false
//
//        override fun userExists(): Boolean {
//            userExistsCalledCount++
//            return userExists
//        }
//    }
//}