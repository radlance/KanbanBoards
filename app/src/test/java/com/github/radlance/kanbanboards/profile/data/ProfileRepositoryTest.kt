package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class ProfileRepositoryTest : BaseTest() {

    private lateinit var dataStoreManager: TestDataStoreManager
    private lateinit var remoteDataSource: TestRemoteDataSource

    private lateinit var profileRepository: ProfileRepository

    @Before
    fun setup() {
        remoteDataSource = TestRemoteDataSource()
        dataStoreManager = TestDataStoreManager()

        profileRepository = RemoteProfileRepository(
            remoteDataSource = remoteDataSource,
            dataStoreManager = dataStoreManager
        )
    }

    @Test
    fun test_profile() {
        remoteDataSource.setUserData(name = "test", email = "test@gmail.com")
        assertEquals(
            LoadProfileResult.Base(name = "test", email = "test@gmail.com"),
            profileRepository.profile()
        )
        assertEquals(1, remoteDataSource.profileCalledCount)
    }

    @Test
    fun test_sign_out() = runBlocking {
        profileRepository.signOut()
        assertFalse(dataStoreManager.authorized().first())
        assertEquals(1, dataStoreManager.authorizedCalledCount)
        assertEquals(1, dataStoreManager.saveAuthorizedCalledList.size)
        assertFalse(dataStoreManager.saveAuthorizedCalledList[0])
        assertEquals(1, remoteDataSource.signOutCalledCount)
    }
}