package com.github.radlance.kanbanboards.profile.edit.data

import com.github.radlance.kanbanboards.api.service.ProfileProvider
import com.github.radlance.kanbanboards.core.BaseTest
import com.github.radlance.kanbanboards.core.data.UserProfileEntity
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboars.profile.edit.data.EditProfileRemoteDataSource
import com.github.radlance.kanbanboars.profile.edit.data.RemoteEditProfileRepository
import com.github.radlance.kanbanboars.profile.edit.domain.EditProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EditProfileRepositoryTest : BaseTest() {

    private lateinit var remoteDataSource: TestEditProfileRemoteDataSource
    private lateinit var repository: EditProfileRepository

    @Before
    fun setup() {
        remoteDataSource = TestEditProfileRemoteDataSource()
        repository = RemoteEditProfileRepository(
            remoteDataSource = remoteDataSource,
            handleUnitResult = TestHandleUnitResult()
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
    fun test_profile_provider() {
        remoteDataSource.makeExpectedProfileProvider(ProfileProvider.Email)
        assertEquals(
            ProfileProvider.Email,
            repository.profileProvider()
        )
        assertEquals(1, remoteDataSource.profileProviderCalledCount)
        remoteDataSource.makeExpectedProfileProvider(ProfileProvider.Google)
        assertEquals(
            ProfileProvider.Google,
            repository.profileProvider()
        )
        assertEquals(2, remoteDataSource.profileProviderCalledCount)
    }

    @Test
    fun test_delete_profile_with_google_success() = runBlocking {
        assertEquals(
            UnitResult.Success,
            repository.deleteProfile(userTokenId = "test token")
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
            repository.deleteProfile(userTokenId = "token")
        )
        assertEquals(1, remoteDataSource.deleteProfileWithGoogleCalledList.size)
        assertEquals("token", remoteDataSource.deleteProfileWithGoogleCalledList[0])
    }

    @Test
    fun test_delete_profile_with_google_error_without_message() = runBlocking {
        remoteDataSource.makeExpectedDeleteProfileWithGoogleException(IllegalStateException())
        assertEquals(
            UnitResult.Error("Error"),
            repository.deleteProfile(userTokenId = "token")
        )
        assertEquals(1, remoteDataSource.deleteProfileWithGoogleCalledList.size)
        assertEquals("token", remoteDataSource.deleteProfileWithGoogleCalledList[0])
    }

    @Test
    fun test_delete_profile_with_email_success() = runBlocking {
        assertEquals(
            UnitResult.Success,
            repository.deleteProfile(email = "user1@gmail.com", password = "123456")
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
            repository.deleteProfile(email = "user2@gmail.com", password = "654321")
        )
        assertEquals(1, remoteDataSource.deleteProfileWithEmailCalledList.size)
        assertEquals(
            Pair("user2@gmail.com", "654321"),
            remoteDataSource.deleteProfileWithEmailCalledList[0]
        )
    }

    @Test
    fun test_delete_profile_with_email_error_without_message() = runBlocking {
        remoteDataSource.makeExpectedDeleteProfileWithEmailException(IllegalStateException())
        assertEquals(
            UnitResult.Error("Error"),
            repository.deleteProfile(email = "user3@gmail.com", password = "987654")
        )
        assertEquals(1, remoteDataSource.deleteProfileWithEmailCalledList.size)
        assertEquals(
            Pair("user3@gmail.com", "987654"),
            remoteDataSource.deleteProfileWithEmailCalledList[0]
        )
    }

    private class TestEditProfileRemoteDataSource : EditProfileRemoteDataSource {
        var profileProviderCalledCount = 0
        private var profileProvider: ProfileProvider =
            ProfileProvider.Email

        fun makeExpectedProfileProvider(profileProvider: ProfileProvider) {
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

        var profileCalledCount = 0

        private val userProfileEntity = MutableStateFlow<UserProfileEntity?>(null)
        fun setUserData(name: String?, email: String) {
            userProfileEntity.value = UserProfileEntity(email, name)
        }

        private var profileException: Exception? = null
        fun makeExpectedProfileException(exception: Exception) {
            profileException = exception
        }

        val editProfileNameCalledList = mutableListOf<String>()

        override fun profileProvider(): ProfileProvider {
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

        override fun profile(): Flow<UserProfileEntity> = flow {
            profileCalledCount++
            profileException?.let { throw it }
            emitAll(userProfileEntity.mapNotNull { it })
        }


        override fun editProfileName(name: String) {
            editProfileNameCalledList.add(name)
        }
    }
}