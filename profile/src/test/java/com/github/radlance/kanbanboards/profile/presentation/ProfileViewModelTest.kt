package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.api.service.ProfileProvider
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResult
import com.github.radlance.kanbanboards.core.BaseTest
import com.github.radlance.kanbanboards.core.data.UserProfileEntity
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest : BaseTest() {

    private lateinit var repository: TestProfileRepository
    private lateinit var handleProfile: TestHandleProfile
    private lateinit var manageResource: TestManageResource

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        repository = TestProfileRepository()
        handleProfile = TestHandleProfile()
        manageResource = TestManageResource()

        viewModel = ProfileViewModel(
            profileRepository = repository,
            facade = BaseProfileMapperFacade(
                loadProfileMapper = LoadProfileResultMapper(),
                profileProviderMapper = ProfileProviderMapper(),
                profileCredentialMapper = ProfileCredentialMapper(manageResource),
                deleteProfileMapper = DeleteProfileMapper()
            ),
            handleProfile = handleProfile,
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(
            ProfileUiState.Base(name = "test name", email = "test@gmail.com"),
            viewModel.profileUiState.value
        )
        assertEquals(1, handleProfile.profileUiStateCalledCount)
        assertEquals(2, handleProfile.saveProfileUiStateCalledList.size)
        assertEquals(
            ProfileUiState.Loading,
            handleProfile.saveProfileUiStateCalledList[0]
        )
        assertEquals(
            ProfileUiState.Base(name = "test name", email = "test@gmail.com"),
            handleProfile.saveProfileUiStateCalledList[1]
        )
        assertEquals(ProfileProviderUi.Email, viewModel.profileProviderUi.value)
        assertEquals(1, handleProfile.saveProfileProviderUiCalledList.size)
        assertEquals(ProfileProviderUi.Email, handleProfile.saveProfileProviderUiCalledList[0])
        assertEquals(1, handleProfile.profileCredentialUiStateCalledCount)
        assertEquals(1, handleProfile.deleteProfileUiStateCalledCount)
    }

    @Test
    fun test_sign_out() {
        viewModel.signOut()
        assertEquals(1, repository.signOutCalledCount)
    }

    @Test
    fun test_create_credential() {
        viewModel.createCredential(CredentialResult.Error)
        assertEquals(
            ProfileCredentialUiState.Error(manageResource),
            viewModel.profileCredentialUiState.value
        )
        assertEquals(1, handleProfile.saveProfileCredentialUiStateCalledList.size)
        assertEquals(
            ProfileCredentialUiState.Error(manageResource),
            handleProfile.saveProfileCredentialUiStateCalledList[0]
        )

        viewModel.createCredential(CredentialResult.Success(idToken = "test token"))
        assertEquals(
            ProfileCredentialUiState.Success(idToken = "test token"),
            viewModel.profileCredentialUiState.value
        )
        assertEquals(2, handleProfile.saveProfileCredentialUiStateCalledList.size)
        assertEquals(
            ProfileCredentialUiState.Success(idToken = "test token"),
            handleProfile.saveProfileCredentialUiStateCalledList[1]
        )
        assertEquals(1, handleProfile.profileCredentialUiStateCalledCount)
    }

    @Test
    fun test_collect_delete_token_profile_ui_state() {
        repository.makeExpectedDeleteProfileResult(UnitResult.Error(message = "delete error"))
        viewModel.deleteProfile(userTokenId = "test token")
        assertEquals(
            DeleteProfileUiState.Error("delete error"),
            viewModel.deleteProfileUiState.value
        )
        assertEquals(1, repository.deleteProfileWithGoogleCalledList.size)
        assertEquals(
            "test token",
            repository.deleteProfileWithGoogleCalledList[0]
        )
        assertEquals(2, handleProfile.saveDeleteProfileUiStateCalledList.size)
        assertEquals(
            DeleteProfileUiState.Loading,
            handleProfile.saveDeleteProfileUiStateCalledList[0]
        )
        assertEquals(
            DeleteProfileUiState.Error("delete error"),
            handleProfile.saveDeleteProfileUiStateCalledList[1]
        )

        repository.makeExpectedDeleteProfileResult(UnitResult.Success)
        viewModel.deleteProfile(userTokenId = "another token")
        assertEquals(
            DeleteProfileUiState.Success,
            viewModel.deleteProfileUiState.value
        )
        assertEquals(2, repository.deleteProfileWithGoogleCalledList.size)
        assertEquals("another token", repository.deleteProfileWithGoogleCalledList[1])
        assertEquals(4, handleProfile.saveDeleteProfileUiStateCalledList.size)
        assertEquals(
            DeleteProfileUiState.Loading,
            handleProfile.saveDeleteProfileUiStateCalledList[2]
        )
        assertEquals(
            DeleteProfileUiState.Success,
            handleProfile.saveDeleteProfileUiStateCalledList[3]
        )
    }

    @Test
    fun test_collect_delete_email_profile_ui_state() {
        repository.makeExpectedDeleteProfileResult(UnitResult.Error(message = "delete error"))
        viewModel.deleteProfile(email = "test@email.com", password = "123456")
        assertEquals(
            DeleteProfileUiState.Error("delete error"),
            viewModel.deleteProfileUiState.value
        )
        assertEquals(1, repository.deleteProfileWithEmailCalledList.size)
        assertEquals(
            Pair("test@email.com", "123456"),
            repository.deleteProfileWithEmailCalledList[0]
        )
        assertEquals(2, handleProfile.saveDeleteProfileUiStateCalledList.size)
        assertEquals(
            DeleteProfileUiState.Loading,
            handleProfile.saveDeleteProfileUiStateCalledList[0]
        )
        assertEquals(
            DeleteProfileUiState.Error("delete error"),
            handleProfile.saveDeleteProfileUiStateCalledList[1]
        )

        repository.makeExpectedDeleteProfileResult(UnitResult.Success)
        viewModel.deleteProfile(email = "anotherEmail@gmai.com", password = "987654")
        assertEquals(
            DeleteProfileUiState.Success,
            viewModel.deleteProfileUiState.value
        )
        assertEquals(2, repository.deleteProfileWithEmailCalledList.size)
        assertEquals(
            Pair("anotherEmail@gmai.com", "987654"),
            repository.deleteProfileWithEmailCalledList[1]
        )
        assertEquals(4, handleProfile.saveDeleteProfileUiStateCalledList.size)
        assertEquals(
            DeleteProfileUiState.Loading,
            handleProfile.saveDeleteProfileUiStateCalledList[2]
        )
        assertEquals(
            DeleteProfileUiState.Success,
            handleProfile.saveDeleteProfileUiStateCalledList[3]
        )
    }

    private class TestProfileRepository : ProfileRepository {
        var profileCalledCount = 0
        var signOutCalledCount = 0

        private var userProfileEntity: UserProfileEntity = UserProfileEntity(
            email = "test@gmail.com",
            name = "test name"
        )

        var profileProviderCalledCount = 0
        private var profileProvider: ProfileProvider = ProfileProvider.Email

        val deleteProfileWithGoogleCalledList = mutableListOf<String>()

        private var deleteProfileResult: UnitResult = UnitResult.Error("initial state")

        fun makeExpectedDeleteProfileResult(deleteProfileResult: UnitResult) {
            this.deleteProfileResult = deleteProfileResult
        }

        val deleteProfileWithEmailCalledList = mutableListOf<Pair<String, String>>()

        override fun profile(): LoadProfileResult {
            profileCalledCount++
            val userProfileEntity = userProfileEntity
            return LoadProfileResult.Base(userProfileEntity.name!!, userProfileEntity.email)
        }

        override suspend fun signOut() {
            signOutCalledCount++
        }

        override fun profileProvider(): ProfileProvider {
            profileProviderCalledCount++
            return profileProvider
        }

        override suspend fun deleteProfileWithGoogle(userTokenId: String): UnitResult {
            deleteProfileWithGoogleCalledList.add(userTokenId)
            return deleteProfileResult
        }

        override suspend fun deleteProfileWithEmail(email: String, password: String): UnitResult {
            deleteProfileWithEmailCalledList.add(Pair(email, password))
            return deleteProfileResult
        }
    }

    private class TestHandleProfile : HandleProfile {

        private val profileUiStateMutable = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)
        var profileUiStateCalledCount = 0
        val saveProfileUiStateCalledList = mutableListOf<ProfileUiState>()

        var profileProviderUiCalledCount = 0
        private val profileProviderUiMutable = MutableStateFlow<ProfileProviderUi>(
            ProfileProviderUi.Initial
        )
        val saveProfileProviderUiCalledList = mutableListOf<ProfileProviderUi>()

        var profileCredentialUiStateCalledCount = 0
        private val profileCredentialUiStateMutable = MutableStateFlow<ProfileCredentialUiState>(
            ProfileCredentialUiState.Initial
        )
        val saveProfileCredentialUiStateCalledList = mutableListOf<ProfileCredentialUiState>()

        var deleteProfileUiStateCalledCount = 0
        private val deleteProfileUiStateMutable = MutableStateFlow<DeleteProfileUiState>(
            DeleteProfileUiState.Initial
        )
        val saveDeleteProfileUiStateCalledList = mutableListOf<DeleteProfileUiState>()

        override val profileUiState: StateFlow<ProfileUiState>
            get() {
                profileUiStateCalledCount++
                return profileUiStateMutable
            }

        override fun saveProfileUiState(profileUiState: ProfileUiState) {
            this.profileUiStateMutable.value = profileUiState
            saveProfileUiStateCalledList.add(profileUiState)
        }

        override val profileProviderUi: StateFlow<ProfileProviderUi>
            get() {
                profileProviderUiCalledCount++
                return profileProviderUiMutable
            }

        override fun saveProfileProviderUi(profileProviderUi: ProfileProviderUi) {
            saveProfileProviderUiCalledList.add(profileProviderUi)
            profileProviderUiMutable.value = profileProviderUi
        }

        override val profileCredentialUiState: StateFlow<ProfileCredentialUiState>
            get() {
                profileCredentialUiStateCalledCount++
                return profileCredentialUiStateMutable
            }

        override fun saveProfileCredentialUiState(profileCredentialUiState: ProfileCredentialUiState) {
            saveProfileCredentialUiStateCalledList.add(profileCredentialUiState)
            profileCredentialUiStateMutable.value = profileCredentialUiState
        }

        override val deleteProfileUiState: StateFlow<DeleteProfileUiState>
            get() {
                deleteProfileUiStateCalledCount++
                return deleteProfileUiStateMutable
            }

        override fun saveDeleteProfileUiState(deleteProfileUiState: DeleteProfileUiState) {
            saveDeleteProfileUiStateCalledList.add(deleteProfileUiState)
            deleteProfileUiStateMutable.value = deleteProfileUiState
        }
    }
}