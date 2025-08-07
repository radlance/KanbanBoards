package com.github.radlance.kanbanboards.profile.edit.presentation

import com.github.radlance.kanbanboards.api.service.ProfileProvider
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResult
import com.github.radlance.kanbanboards.core.BaseTest
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboars.profile.edit.domain.EditProfileRepository
import com.github.radlance.kanbanboars.profile.edit.presentation.BaseEditProfileMapperFacade
import com.github.radlance.kanbanboars.profile.edit.presentation.DeleteProfileMapper
import com.github.radlance.kanbanboars.profile.edit.presentation.DeleteProfileUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.EditProfileMapper
import com.github.radlance.kanbanboars.profile.edit.presentation.EditProfileUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.EditProfileViewModel
import com.github.radlance.kanbanboars.profile.edit.presentation.HandleEditProfile
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileCredentialMapper
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileCredentialUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileEditMapper
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileEditUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileProviderMapper
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileProviderUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EditProfileViewModelTest : BaseTest() {

    private lateinit var repository: TestEditProfileRepository
    private lateinit var handleEditProfile: TestHandleEditProfile
    private lateinit var manageResource: TestManageResource

    private lateinit var viewModel: EditProfileViewModel

    @Before
    fun setup() {
        repository = TestEditProfileRepository()
        handleEditProfile = TestHandleEditProfile()
        manageResource = TestManageResource()

        viewModel = EditProfileViewModel(
            repository = repository,
            facade = BaseEditProfileMapperFacade(
                profileEditMapper = ProfileEditMapper(),
                editProfileMapper = EditProfileMapper(),
                profileProviderMapper = ProfileProviderMapper(),
                profileCredentialMapper = ProfileCredentialMapper(
                    manageResource
                ),
                deleteProfileMapper = DeleteProfileMapper()
            ),
            handleEditProfile = handleEditProfile,
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(
            ProfileEditUiState.Error(message = "initial"),
            viewModel.profileUiState.value
        )
        assertEquals(
            ProfileProviderUi.Email,
            viewModel.profileProviderUi.value
        )
        assertEquals(1, handleEditProfile.saveProfileProviderUiCalledList.size)
        assertEquals(
            ProfileProviderUi.Email,
            handleEditProfile.saveProfileProviderUiCalledList[0]
        )
        assertEquals(1, handleEditProfile.profileCredentialUiStateCalledCount)
        assertEquals(1, handleEditProfile.deleteProfileUiStateCalledCount)
        assertEquals(1, handleEditProfile.editProfileUiStateCalledCount)
    }

    @Test
    fun test_collect_profile() {
        repository.makeExpectedLoadProfileResult(LoadProfileResult.Error("some error"))
        assertEquals(viewModel.profileUiState.value, ProfileEditUiState.Error("some error"))
        assertEquals(1, repository.profileCalledCount)
        repository.makeExpectedLoadProfileResult(
            LoadProfileResult.Success(
                name = "name",
                email = "test@gmail.com"
            )
        )
        assertEquals(
            viewModel.profileUiState.value, ProfileEditUiState.Base(
                name = "name",
                email = "test@gmail.com"
            )
        )
        assertEquals(1, repository.profileCalledCount)
    }

    @Test
    fun test_create_credential() {
        viewModel.createCredential(CredentialResult.Error)
        assertEquals(
            ProfileCredentialUiState.Error(
                manageResource
            ),
            viewModel.profileCredentialUiState.value
        )
        assertEquals(1, handleEditProfile.saveProfileCredentialUiStateCalledList.size)
        assertEquals(
            ProfileCredentialUiState.Error(
                manageResource
            ),
            handleEditProfile.saveProfileCredentialUiStateCalledList[0]
        )

        viewModel.createCredential(CredentialResult.Success(idToken = "test token"))
        assertEquals(
            ProfileCredentialUiState.Success(
                idToken = "test token"
            ),
            viewModel.profileCredentialUiState.value
        )
        assertEquals(2, handleEditProfile.saveProfileCredentialUiStateCalledList.size)
        assertEquals(
            ProfileCredentialUiState.Success(
                idToken = "test token"
            ),
            handleEditProfile.saveProfileCredentialUiStateCalledList[1]
        )
        assertEquals(1, handleEditProfile.profileCredentialUiStateCalledCount)
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
        assertEquals(2, handleEditProfile.saveDeleteProfileUiStateCalledList.size)
        assertEquals(
            DeleteProfileUiState.Loading,
            handleEditProfile.saveDeleteProfileUiStateCalledList[0]
        )
        assertEquals(
            DeleteProfileUiState.Error("delete error"),
            handleEditProfile.saveDeleteProfileUiStateCalledList[1]
        )

        repository.makeExpectedDeleteProfileResult(UnitResult.Success)
        viewModel.deleteProfile(userTokenId = "another token")
        assertEquals(
            DeleteProfileUiState.Success,
            viewModel.deleteProfileUiState.value
        )
        assertEquals(2, repository.deleteProfileWithGoogleCalledList.size)
        assertEquals("another token", repository.deleteProfileWithGoogleCalledList[1])
        assertEquals(4, handleEditProfile.saveDeleteProfileUiStateCalledList.size)
        assertEquals(
            DeleteProfileUiState.Loading,
            handleEditProfile.saveDeleteProfileUiStateCalledList[2]
        )
        assertEquals(
            DeleteProfileUiState.Success,
            handleEditProfile.saveDeleteProfileUiStateCalledList[3]
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
        assertEquals(2, handleEditProfile.saveDeleteProfileUiStateCalledList.size)
        assertEquals(
            DeleteProfileUiState.Loading,
            handleEditProfile.saveDeleteProfileUiStateCalledList[0]
        )
        assertEquals(
            DeleteProfileUiState.Error("delete error"),
            handleEditProfile.saveDeleteProfileUiStateCalledList[1]
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
        assertEquals(4, handleEditProfile.saveDeleteProfileUiStateCalledList.size)
        assertEquals(
            DeleteProfileUiState.Loading,
            handleEditProfile.saveDeleteProfileUiStateCalledList[2]
        )
        assertEquals(
            DeleteProfileUiState.Success,
            handleEditProfile.saveDeleteProfileUiStateCalledList[3]
        )
    }

    @Test
    fun test_collect_edit_profile() {
        repository.makeExpectedEditProfileResult(UnitResult.Error("some message"))
        viewModel.editProfile(newName = "test name")
        assertEquals(viewModel.editProfileUiState.value, EditProfileUiState.Error("some message"))
        assertEquals(1, repository.editProfileCalledList.size)
        assertEquals("test name", repository.editProfileCalledList[0])
        assertEquals(1, handleEditProfile.editProfileUiStateCalledCount)
        assertEquals(2, handleEditProfile.saveEditProfileUiStateCalledList.size)
        assertEquals(
            EditProfileUiState.Loading,
            handleEditProfile.saveEditProfileUiStateCalledList[0]
        )
        assertEquals(
            EditProfileUiState.Error("some message"),
            handleEditProfile.saveEditProfileUiStateCalledList[1]
        )

        repository.makeExpectedEditProfileResult(UnitResult.Success)
        viewModel.editProfile(newName = "another name")
        assertEquals(EditProfileUiState.Success, viewModel.editProfileUiState.value)
        assertEquals(2, repository.editProfileCalledList.size)
        assertEquals("another name", repository.editProfileCalledList[1])
        assertEquals(1, handleEditProfile.editProfileUiStateCalledCount)
        assertEquals(4, handleEditProfile.saveEditProfileUiStateCalledList.size)
        assertEquals(
            EditProfileUiState.Loading,
            handleEditProfile.saveEditProfileUiStateCalledList[2]
        )
        assertEquals(
            EditProfileUiState.Success,
            handleEditProfile.saveEditProfileUiStateCalledList[3]
        )
    }

    private class TestEditProfileRepository : EditProfileRepository {

        var profileProviderCalledCount = 0
        private var profileProvider: ProfileProvider = ProfileProvider.Email

        val deleteProfileWithGoogleCalledList = mutableListOf<String>()
        private var deleteProfileResult: UnitResult = UnitResult.Error("initial state")
        fun makeExpectedDeleteProfileResult(deleteProfileResult: UnitResult) {
            this.deleteProfileResult = deleteProfileResult
        }

        val deleteProfileWithEmailCalledList = mutableListOf<Pair<String, String>>()

        var profileCalledCount = 0
        private val loadProfileResult = MutableStateFlow<LoadProfileResult>(
            LoadProfileResult.Error("initial")
        )

        fun makeExpectedLoadProfileResult(loadProfileResult: LoadProfileResult) {
            this.loadProfileResult.value = loadProfileResult
        }

        val editProfileCalledList = mutableListOf<String>()
        private var editProfileResult: UnitResult = UnitResult.Error("initial")
        fun makeExpectedEditProfileResult(editProfileResult: UnitResult) {
            this.editProfileResult = editProfileResult
        }

        override fun profileProvider(): ProfileProvider {
            profileProviderCalledCount++
            return profileProvider
        }

        override suspend fun deleteProfile(userTokenId: String): UnitResult {
            deleteProfileWithGoogleCalledList.add(userTokenId)
            return deleteProfileResult
        }

        override suspend fun deleteProfile(email: String, password: String): UnitResult {
            deleteProfileWithEmailCalledList.add(Pair(email, password))
            return deleteProfileResult
        }

        override fun editProfile(name: String): UnitResult {
            editProfileCalledList.add(name)
            return editProfileResult
        }

        override fun profile(): Flow<LoadProfileResult> {
            profileCalledCount++
            return loadProfileResult
        }
    }

    private class TestHandleEditProfile : HandleEditProfile {
        var profileProviderUiCalledCount = 0
        private val profileProviderUiMutable =
            MutableStateFlow<ProfileProviderUi>(
                ProfileProviderUi.Initial
            )
        val saveProfileProviderUiCalledList =
            mutableListOf<ProfileProviderUi>()

        var profileCredentialUiStateCalledCount = 0
        private val profileCredentialUiStateMutable =
            MutableStateFlow<ProfileCredentialUiState>(
                ProfileCredentialUiState.Initial
            )
        val saveProfileCredentialUiStateCalledList =
            mutableListOf<ProfileCredentialUiState>()

        var deleteProfileUiStateCalledCount = 0
        private val deleteProfileUiStateMutable =
            MutableStateFlow<DeleteProfileUiState>(
                DeleteProfileUiState.Initial
            )
        val saveDeleteProfileUiStateCalledList =
            mutableListOf<DeleteProfileUiState>()

        var editProfileUiStateCalledCount = 0
        private val editProfileUiStateMutable =
            MutableStateFlow<EditProfileUiState>(
                EditProfileUiState.Initial
            )
        val saveEditProfileUiStateCalledList =
            mutableListOf<EditProfileUiState>()

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


        override val editProfileUiState: StateFlow<EditProfileUiState>
            get() {
                editProfileUiStateCalledCount++
                return editProfileUiStateMutable
            }

        override fun saveEditProfileUiState(editProfileUiState: EditProfileUiState) {
            saveEditProfileUiStateCalledList.add(editProfileUiState)
            editProfileUiStateMutable.value = editProfileUiState
        }
    }
}