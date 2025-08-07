package com.github.radlance.kanbanboards.profile.edit.presentation

import com.github.radlance.kanbanboards.core.BaseTest
import com.github.radlance.kanbanboards.core.core.ManageResource
import com.github.radlance.kanbanboars.profile.edit.presentation.BaseHandleEditProfile
import com.github.radlance.kanbanboars.profile.edit.presentation.DeleteProfileUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.EditProfileUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.HandleEditProfile
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileCredentialUiState
import com.github.radlance.kanbanboars.profile.edit.presentation.ProfileProviderUi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HandleEditProfileTest : BaseTest() {

    private lateinit var manageResource: ManageResource
    private lateinit var handle: HandleEditProfile

    @Before
    fun setup() {
        manageResource = TestManageResource()
        handle = BaseHandleEditProfile()
    }

    @Test
    fun test_profile_provider_ui() {
        assertEquals(ProfileProviderUi.Initial, handle.profileProviderUi.value)
        handle.saveProfileProviderUi(ProfileProviderUi.Email)
        assertEquals(ProfileProviderUi.Email, handle.profileProviderUi.value)
        handle.saveProfileProviderUi(ProfileProviderUi.Google)
        assertEquals(ProfileProviderUi.Google, handle.profileProviderUi.value)
    }

    @Test
    fun test_profile_credential_ui_state() {
        assertEquals(ProfileCredentialUiState.Initial, handle.profileCredentialUiState.value)
        handle.saveProfileCredentialUiState(ProfileCredentialUiState.Error(manageResource))
        assertEquals(
            ProfileCredentialUiState.Error(manageResource),
            handle.profileCredentialUiState.value
        )
        handle.saveProfileCredentialUiState(ProfileCredentialUiState.Success(idToken = "testIdToken"))
        assertEquals(
            ProfileCredentialUiState.Success(idToken = "testIdToken"),
            handle.profileCredentialUiState.value
        )
    }

    @Test
    fun test_delete_profile_ui_state() {
        assertEquals(DeleteProfileUiState.Initial, handle.deleteProfileUiState.value)
        handle.saveDeleteProfileUiState(DeleteProfileUiState.Loading)
        assertEquals(DeleteProfileUiState.Loading, handle.deleteProfileUiState.value)
        handle.saveDeleteProfileUiState(DeleteProfileUiState.Error("delete error"))
        assertEquals(DeleteProfileUiState.Error("delete error"), handle.deleteProfileUiState.value)
        handle.saveDeleteProfileUiState(DeleteProfileUiState.Success)
        assertEquals(DeleteProfileUiState.Success, handle.deleteProfileUiState.value)
    }

    @Test
    fun test_edit_profile_ui_state() {
        assertEquals(EditProfileUiState.Initial, handle.editProfileUiState.value)
        handle.saveEditProfileUiState(EditProfileUiState.Loading)
        assertEquals(EditProfileUiState.Loading, handle.editProfileUiState.value)
        handle.saveEditProfileUiState(EditProfileUiState.Error("edit error"))
        assertEquals(EditProfileUiState.Error("edit error"), handle.editProfileUiState.value)
        handle.saveEditProfileUiState(EditProfileUiState.Success)
        assertEquals(EditProfileUiState.Success, handle.editProfileUiState.value)
    }
}