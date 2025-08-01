package com.github.radlance.kanbanboards.profile.data

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.github.radlance.kanbanboards.common.domain.UnitResult
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
            dataStoreManager = dataStoreManager,
            manageResource = manageResource
        )
    }

    @Test
    fun test_profile() {
        remoteDataSource.setUserData(name = "test", email = "test@gmail.com")
        assertEquals(
            LoadProfileResult.Base(name = "test", email = "test@gmail.com"),
            repository.profile()
        )
        assertEquals(1, remoteDataSource.profileCalledCount)
        remoteDataSource.setUserData(name = null, email = "test@gmail.com")
        assertEquals(
            LoadProfileResult.Base(name = "", email = "test@gmail.com"),
            repository.profile()
        )
        assertEquals(2, remoteDataSource.profileCalledCount)
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

    @Test
    fun test_profile_provider() {
        remoteDataSource.makeExpectedProfileProvider(com.github.radlance.api.service.ProfileProvider.Email)
        assertEquals(
            com.github.radlance.api.service.ProfileProvider.Email,
            repository.profileProvider()
        )
        assertEquals(1, remoteDataSource.profileProviderCalledCount)
        remoteDataSource.makeExpectedProfileProvider(com.github.radlance.api.service.ProfileProvider.Google)
        assertEquals(
            com.github.radlance.api.service.ProfileProvider.Google,
            repository.profileProvider()
        )
        assertEquals(2, remoteDataSource.profileProviderCalledCount)
    }

    @Test
    fun test_delete_profile_with_google_success() = runBlocking {
        assertEquals(
            UnitResult.Success,
            repository.deleteProfileWithGoogle(userTokenId = "test token")
        )
        assertEquals(1, remoteDataSource.deleteProfileWithGoogleCalledList.size)
        assertEquals("test token", remoteDataSource.deleteProfileWithGoogleCalledList[0])
    }

    @Test
    fun test_delete_profile_with_google_error_with_message() = runBlocking {
        remoteDataSource.makeExpectedDeleteProfileWithGoogleException(
            IllegalStateException("test error")
        )
        assertEquals(
            UnitResult.Error("test error"),
            repository.deleteProfileWithGoogle(userTokenId = "token")
        )
        assertEquals(1, remoteDataSource.deleteProfileWithGoogleCalledList.size)
        assertEquals("token", remoteDataSource.deleteProfileWithGoogleCalledList[0])
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_delete_profile_with_google_error_without_message() = runBlocking {
        manageResource.makeExpectedString(expected = "resource error message")
        remoteDataSource.makeExpectedDeleteProfileWithGoogleException(IllegalStateException())
        assertEquals(
            UnitResult.Error("resource error message"),
            repository.deleteProfileWithGoogle(userTokenId = "token")
        )
        assertEquals(1, remoteDataSource.deleteProfileWithGoogleCalledList.size)
        assertEquals("token", remoteDataSource.deleteProfileWithGoogleCalledList[0])
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_delete_profile_with_email_success() = runBlocking {
        assertEquals(
            UnitResult.Success,
            repository.deleteProfileWithEmail(email = "user1@gmail.com", password = "123456")
        )
        assertEquals(1, remoteDataSource.deleteProfileWithEmailCalledList.size)
        assertEquals(
            Pair("user1@gmail.com", "123456"),
            remoteDataSource.deleteProfileWithEmailCalledList[0]
        )
    }

    @Test
    fun test_delete_profile_with_email_error_with_message() = runBlocking {
        remoteDataSource.makeExpectedDeleteProfileWithEmailException(
            IllegalStateException("test error")
        )
        assertEquals(
            UnitResult.Error("test error"),
            repository.deleteProfileWithEmail(email = "user2@gmail.com", password = "654321")
        )
        assertEquals(1, remoteDataSource.deleteProfileWithEmailCalledList.size)
        assertEquals(
            Pair("user2@gmail.com", "654321"),
            remoteDataSource.deleteProfileWithEmailCalledList[0]
        )
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_delete_profile_with_email_error_without_message() = runBlocking {
        manageResource.makeExpectedString(expected = "resource error message")
        remoteDataSource.makeExpectedDeleteProfileWithEmailException(IllegalStateException())
        assertEquals(
            UnitResult.Error("resource error message"),
            repository.deleteProfileWithEmail(email = "user3@gmail.com", password = "987654")
        )
        assertEquals(1, remoteDataSource.deleteProfileWithEmailCalledList.size)
        assertEquals(
            Pair("user3@gmail.com", "987654"),
            remoteDataSource.deleteProfileWithEmailCalledList[0]
        )
        assertEquals(1, manageResource.stringCalledCount)
    }

    private class TestProfileRemoteDataSource : ProfileRemoteDataSource {

        private var userProfileEntity: UserProfileEntity? = null

        var profileCalledCount = 0
        var signOutCalledCount = 0

        private var profileProvider: com.github.radlance.api.service.ProfileProvider =
            com.github.radlance.api.service.ProfileProvider.Email
        var profileProviderCalledCount = 0

        fun setUserData(name: String?, email: String) {
            userProfileEntity = UserProfileEntity(email, name)
        }

        fun makeExpectedProfileProvider(profileProvider: com.github.radlance.api.service.ProfileProvider) {
            this.profileProvider = profileProvider
        }

        val deleteProfileWithGoogleCalledList = mutableListOf<String>()
        private var deleteProfileWithGoogleException: Exception? = null
        fun makeExpectedDeleteProfileWithGoogleException(exception: Exception) {
            deleteProfileWithGoogleException = exception
        }

        val deleteProfileWithEmailCalledList = mutableListOf<Pair<String, String>>()
        private var deleteProfileWithEmailException: Exception? = null
        fun makeExpectedDeleteProfileWithEmailException(exception: Exception) {
            deleteProfileWithEmailException = exception
        }

        override fun profile(): UserProfileEntity {
            profileCalledCount++
            return userProfileEntity!!
        }

        override fun signOut() {
            signOutCalledCount++
        }

        override fun profileProvider(): com.github.radlance.api.service.ProfileProvider {
            profileProviderCalledCount++
            return profileProvider
        }

        override suspend fun deleteProfileWithGoogle(userTokenId: String) {
            deleteProfileWithGoogleCalledList.add(userTokenId)
            deleteProfileWithGoogleException?.let { throw it }
        }

        override suspend fun deleteProfileWithEmail(email: String, password: String) {
            deleteProfileWithEmailCalledList.add(Pair(email, password))
            deleteProfileWithEmailException?.let { throw it }
        }
    }
}