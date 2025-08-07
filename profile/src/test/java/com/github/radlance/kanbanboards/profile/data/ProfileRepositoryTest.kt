package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.core.BaseTest
import com.github.radlance.kanbanboards.core.data.UserProfileEntity
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class ProfileRepositoryTest : BaseTest() {

    private lateinit var dataStoreManager: TestDataStoreManager
    private lateinit var remoteDataSource: TestProfileRemoteDataSource
    private lateinit var manageResource: TestManageResource

    private lateinit var repository: ProfileRepository

    @Before
    fun setup() {
        remoteDataSource = TestProfileRemoteDataSource()
        dataStoreManager = TestDataStoreManager()
        manageResource = TestManageResource()

        repository = RemoteProfileRepository(
            remoteDataSource = remoteDataSource,
            dataStoreManager = dataStoreManager
        )
    }

    @Test
    fun test_profile() = runBlocking {
        remoteDataSource.setUserData(name = "test", email = "test@gmail.com")
        assertEquals(
            LoadProfileResult.Success(name = "test", email = "test@gmail.com"),
            repository.profile().first()
        )
        assertEquals(1, remoteDataSource.profileCalledCount)
        remoteDataSource.setUserData(name = null, email = "test@gmail.com")
        assertEquals(
            LoadProfileResult.Success(name = "", email = "test@gmail.com"),
            repository.profile().first()
        )
        assertEquals(2, remoteDataSource.profileCalledCount)
    }

    @Test
    fun test_profile_error() = runBlocking {
        remoteDataSource.makeExpectedProfileException(IllegalStateException("exception"))
        assertEquals(
            LoadProfileResult.Error("exception"),
            repository.profile().first()
        )
        assertEquals(1, remoteDataSource.profileCalledCount)
    }

    @Test
    fun test_sign_out() = runBlocking {
        repository.signOut()
        assertFalse(dataStoreManager.authorized().first())
        assertEquals(1, dataStoreManager.authorizedCalledCount)
        assertEquals(1, dataStoreManager.saveAuthorizedCalledList.size)
        assertFalse(dataStoreManager.saveAuthorizedCalledList[0])
        assertEquals(1, remoteDataSource.signOutCalledCount)
    }


    private class TestProfileRemoteDataSource : ProfileRemoteDataSource {

        private val userProfileEntity = MutableStateFlow<UserProfileEntity?>(null)
        private var profileException: Exception? = null
        fun makeExpectedProfileException(exception: Exception) {
            profileException = exception
        }

        var profileCalledCount = 0
        var signOutCalledCount = 0

        fun setUserData(name: String?, email: String) {
            userProfileEntity.value = UserProfileEntity(email, name)
        }

        override fun profile(): Flow<UserProfileEntity> = flow {
            profileCalledCount++
            profileException?.let { throw it }
            emitAll(userProfileEntity.mapNotNull { it })
        }

        override fun signOut() {
            signOutCalledCount++
        }
    }
}